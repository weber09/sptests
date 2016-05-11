package algol;

/**
 * Created by Gabriel on 08/03/2016.
 */
import java.util.ArrayList;
import static algol.CLConstants.*;

class SPMethodDeclaration
        extends SPAST implements SPMember {

    /** Method modifiers. */
    protected ArrayList<String> mods;

    /** Method name. */
    protected String name;

    /** Return type. */
    protected Type returnType;

    /** The formal parameters. */
    protected ArrayList<SPFormalParameter> params;

    /** Method body. */
    protected SPBlock body;

    /** Built in analyze(). */
    protected MethodContext context;

    /** Computed by preAnalyze(). */
    protected String descriptor;

    /** Is method abstract. */
    protected boolean isAbstract;

    /** Is method static. */
    protected boolean isStatic;

    /** Is method private. */
    protected boolean isPrivate;

    public SPMethodDeclaration(int line, ArrayList<String> mods,
                              String name, Type returnType,
                              ArrayList<SPFormalParameter> params, SPBlock body)

    {
        super(line);
        this.mods = mods;
        this.name = name;
        this.returnType = returnType;
        this.params = params;
        this.body = body;
        this.isAbstract = mods.contains("abstract");
        this.isStatic = mods.contains("static");
        this.isPrivate = mods.contains("private");
    }

    public void preAnalyze(Context context, CLEmitter partial) {
        // Resolve types of the formal parameters
        for (SPFormalParameter param : params) {
            param.setType(param.type().resolve(context));
        }

        // Resolve return type
        returnType = returnType.resolve(context);

        // Compute descriptor
        descriptor = "(";
        for (SPFormalParameter param : params) {
            descriptor += param.type().toDescriptor();
        }
        descriptor += ")" + returnType.toDescriptor();

        partialCodegen(context, partial);
    }
    public SPAST analyze(Context context) {
        MethodContext methodContext =
                new MethodContext(context, isStatic, returnType);
        this.context = methodContext;

        if (!isStatic) {
            // Offset 0 is used to address "this".
            this.context.nextOffset();
        }

        // Declare the parameters. a formal parameter
        // is  always initialized, via a function call.
        for (SPFormalParameter param : params) {
            LocalVariableDefn defn = new LocalVariableDefn(param.type(),
                    this.context.nextOffset());
           defn.initialize();
           this.context.addEntry(param.line(), param.name(), defn);
        }
        if (body != null) {
            body = body.analyze(this.context);
            if (returnType!=Type.VOID && ! methodContext.methodHasReturn()){
                SPAST.compilationUnit.reportSemanticError(line(),
                        "Non-void method must have a return statement");
            }
        }
        return this;
    }

    public void partialCodegen(Context context, CLEmitter partial) {
        partial.addMethod(mods, name, descriptor, null, false);

        if (returnType == Type.VOID) {
            partial.addNoArgInstruction(RETURN);
        } else if (returnType == Type.INT
                || returnType == Type.BOOLEAN || returnType == Type.CHAR) {
            partial.addNoArgInstruction(ICONST_0);
            partial.addNoArgInstruction(IRETURN);
        } else {
            partial.addNoArgInstruction(ACONST_NULL);
            partial.addNoArgInstruction(ARETURN);
        }
    }

    public void codegen(CLEmitter output) {
        output.addMethod(mods, name, descriptor, null, false);
        if (body != null) {
            body.codegen(output);
        }

        if (returnType == Type.VOID) {
            output.addNoArgInstruction(RETURN);
        }
    }
}
