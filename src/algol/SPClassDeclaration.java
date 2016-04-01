package algol;

/**
 * Created by Gabriel on 07/03/2016.
 */

        import java.util.ArrayList;
        import static algol.CLConstants.*;

class SPClassDeclaration extends SPAST implements SPTypeDecl {

    private ArrayList<String> mods;

    private String name;

    private ArrayList<SPMember> classBlock;

    private Type superType;

    private Type thisType;

    private ClassContext context;

    private boolean hasExplicitConstructor;

    private ArrayList<SPFieldDeclaration> instanceFieldInitializations;

    private ArrayList<SPFieldDeclaration> staticFieldInitializations;

    public SPClassDeclaration(int line, ArrayList<String> mods, String name,
                             Type superType, ArrayList<SPMember> classBlock) {
        super(line);
        this.mods = mods;
        this.name = name;
        this.superType = superType;
        this.classBlock = classBlock;
        hasExplicitConstructor = false;
        instanceFieldInitializations = new ArrayList<SPFieldDeclaration>();
        staticFieldInitializations = new ArrayList<SPFieldDeclaration>();
    }

    public String name() {
        return name;
    }

    public Type superType() {
        return superType;
    }

    public Type thisType() {
        return thisType;
    }

    public ArrayList<SPFieldDeclaration> instanceFieldInitializations() {
        return instanceFieldInitializations;
    }

    public void declareThisType(Context context) {
        String qualifiedName = SPAST.compilationUnit.packageName() == "" ? name
                : SPAST.compilationUnit.packageName() + "/" + name;
        CLEmitter partial = new CLEmitter(false);
        partial.addClass(mods, qualifiedName, Type.OBJECT.jvmName(), null,
                false); // Object for superClass, just for now
        thisType = Type.typeFor(partial.toClass());
        context.addType(line, thisType);
    }

    public void preAnalyze(Context context) {
        this.context = new ClassContext(this, context);

        superType = superType.resolve(this.context);

        thisType.checkAccess(line, superType);
        if (superType.isFinal()) {
            SPAST.compilationUnit.reportSemanticError(line,
                    "Cannot extend a final type: %s", superType.toString());
        }

        CLEmitter partial = new CLEmitter(false);

        String qualifiedName = SPAST.compilationUnit.packageName() == "" ? name : SPAST.compilationUnit.packageName() + "/" + name;
        partial.addClass(mods, qualifiedName, superType.jvmName(), null, false);

        for (SPMember member : classBlock) {
            member.preAnalyze(this.context, partial);
        }

        if (!hasExplicitConstructor) {
            codegenPartialImplicitConstructor(partial);
        }

        Type id = this.context.lookupType(name);
        if (id != null && !SPAST.compilationUnit.errorHasOccurred()) {
            id.setClassRep(partial.toClass());
        }
    }

    public SPAST analyze(Context context) {
        if(classBlock != null) {
            for (SPMember member : classBlock) {
                ((SPAST) member).analyze(this.context);
            }

            for (SPMember member : classBlock) {
                if (member instanceof SPFieldDeclaration) {
                    SPFieldDeclaration fieldDecl = (SPFieldDeclaration) member;
                    if (fieldDecl.mods().contains("static")) {
                        staticFieldInitializations.add(fieldDecl);
                    } else {
                        instanceFieldInitializations.add(fieldDecl);
                    }
                }
            }
        }

        return this;
    }

    public void codegen(CLEmitter output) {
        String qualifiedName = SPAST.compilationUnit.packageName() == "" ? name
                : SPAST.compilationUnit.packageName() + "/" + name;
        output.addClass(mods, qualifiedName, superType.toString() , null, false);

        if (!hasExplicitConstructor) {
            codegenImplicitConstructor(output);
        }

        if(classBlock != null) {
            for (SPMember member : classBlock) {
                ((SPAST) member).codegen(output);
            }
        }

        if (staticFieldInitializations.size() > 0) {
            codegenClassInit(output);
        }
    }

    private void codegenPartialImplicitConstructor(CLEmitter partial) {
        ArrayList<String> mods = new ArrayList<String>();
        mods.add("public");
        partial.addMethod(mods, "<init>", "()V", null, false);
        partial.addNoArgInstruction(ALOAD_0);
        partial.addMemberAccessInstruction(INVOKESPECIAL, superType.jvmName(),
                "<init>", "()V");

        partial.addNoArgInstruction(RETURN);
    }

    private void codegenImplicitConstructor(CLEmitter output) {
        // Invoke super constructor
        ArrayList<String> mods = new ArrayList<String>();
        mods.add("public");
        output.addMethod(mods, "<init>", "()V", null, false);
        output.addNoArgInstruction(ALOAD_0);
        output.addMemberAccessInstruction(INVOKESPECIAL, superType.jvmName(),
                "<init>", "()V");

        for (SPFieldDeclaration instanceField : instanceFieldInitializations) {
            instanceField.codegenInitializations(output);
        }

        output.addNoArgInstruction(RETURN);
    }

    private void codegenClassInit(CLEmitter output) {
        ArrayList<String> mods = new ArrayList<String>();
        mods.add("public");
        mods.add("static");
        output.addMethod(mods, "<clinit>", "()V", null, false);

        for (SPFieldDeclaration staticField : staticFieldInitializations) {
            staticField.codegenInitializations(output);
        }

        output.addNoArgInstruction(RETURN);
    }

}

