package algol;

/**
 * Created by Gabriel on 06/03/2016.
 */
import java.util.ArrayList;

class SPCompilationUnit extends SPAST {

    private String fileName;

    private TypeName packageName;

    private ArrayList<TypeName> imports;

    private ArrayList<SPAST> typeDeclarations;

    private ArrayList<CLFile> clFiles;

    private CompilationUnitContext context;

    public CompilationUnitContext getContext(){
        return context;
    }

    private boolean isInError;

    public String fileName(){
        return fileName;
    }

    public SPCompilationUnit(String fileName, int line, TypeName packageName,
                            ArrayList<TypeName> imports, ArrayList<SPAST> typeDeclarations) {
        super(line);
        this.fileName = fileName;
        this.packageName = packageName;
        this.imports = imports;
        this.typeDeclarations = typeDeclarations;
        clFiles = new ArrayList<CLFile>();
        compilationUnit = this;
    }

    public String packageName()  {
        return "AlgolBytecodes";
        //return packageName == null ? "" : packageName.toString();
    }

    public boolean errorHasOccurred() {
        return isInError;
    }

    public void reportSemanticError(int line, String message,
                                    Object... arguments) {
        isInError = true;
        System.err.printf("%s:%d: ", fileName, line);
        System.err.printf(message, arguments);
        System.err.println();
        SemanticError semanticError = new SemanticError(line, message, arguments);
        if(context != null)
            context.semanticErrors.add(semanticError);
    }

    public void preAnalyze() {
        context = new CompilationUnitContext();

        context.addType(0, Type.OBJECT);
        context.addType(0, Type.STRING);

        if(imports != null) {
            for (TypeName imported : imports) {
                try {
                    Class<?> classRep = Class.forName(imported.toString());
                    context.addType(imported.line(), Type.typeFor(classRep));
                } catch (Exception e) {
                    SPAST.compilationUnit.reportSemanticError(imported.line(),
                            "Unable to find %s", imported.toString());
                }
            }
        }

        CLEmitter.initializeByteClassLoader();
        if(typeDeclarations != null) {
            for (SPAST typeDeclaration : typeDeclarations) {
                ((SPTypeDecl) typeDeclaration).declareThisType(context);
            }
        }

        CLEmitter.initializeByteClassLoader();
        if(typeDeclarations != null) {
            for (SPAST typeDeclaration : typeDeclarations) {
                ((SPTypeDecl) typeDeclaration).preAnalyze(context);
            }
        }
    }

    public SPAST analyze(Context context) {
        for (SPAST typeDeclaration : typeDeclarations) {
            typeDeclaration.analyze(this.context);
        }
        return this;
    }


    public void codegen(CLEmitter output) {
        for (SPAST typeDeclaration : typeDeclarations) {
            typeDeclaration.codegen(output);
            output.write();
            clFiles.add(output.clFile());
        }
    }

    public ArrayList<CLFile> clFiles() {
        return clFiles;
    }

}
