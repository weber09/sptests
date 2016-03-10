package algol;

/**
 * Created by Gabriel on 09/03/2016.
 */
import static algol.CLConstants.*;

class SPVariable extends SPExpression implements SPLhs {

    private String name;

    private IDefn iDefn;

    private boolean analyzeLhs;

    public SPVariable(int line, String name) {
        super(line);
        this.name = name;
    }

    public String name() {
        return name;
    }

    public IDefn iDefn() {
        return iDefn;
    }

    public SPExpression analyze(Context context) {
        iDefn = context.lookup(name);
        if (iDefn == null) {
            // Not a local, but is it a field?
            Type definingType = context.definingType();
            Field field = definingType.fieldFor(name);
            if (field == null) {
                type = Type.ANY;
                SPAST.compilationUnit.reportSemanticError(line,
                        "Cannot find name: " + name);
            } else {
                // Rewrite a variable denoting a field as an
                // explicit field selection
                type = field.type();
                SPExpression newTree = new SPFieldSelection(line(), new SPThis(line), name);
                return (SPExpression)newTree.analyze(context);
            }
        } else {
            if (!analyzeLhs && iDefn instanceof LocalVariableDefn
                    && !((LocalVariableDefn) iDefn).isInitialized()) {
                SPAST.compilationUnit.reportSemanticError(line, "Variable "
                        + name + " might not have been initialized");
            }
            type = iDefn.type();
        }
        return this;
    }

    public SPExpression analyzeLhs(Context context) {
        analyzeLhs = true;
        SPExpression newTree = analyze(context);
        if (newTree instanceof SPVariable) {
            // Could (now) be a SPFieldSelection, but if it's
            // (still) a SPVariable
            if (iDefn != null && !(iDefn instanceof LocalVariableDefn)) {
                SPAST.compilationUnit.reportSemanticError(line(), name
                        + " is a bad lhs to a  =");
            }
        }
        return newTree;
    }

    public void codegen(CLEmitter output) {
        if (iDefn instanceof LocalVariableDefn) {
            int offset = ((LocalVariableDefn) iDefn).offset();
            if (type.isReference()) {
                switch (offset) {
                    case 0:
                        output.addNoArgInstruction(ALOAD_0);
                        break;
                    case 1:
                        output.addNoArgInstruction(ALOAD_1);
                        break;
                    case 2:
                        output.addNoArgInstruction(ALOAD_2);
                        break;
                    case 3:
                        output.addNoArgInstruction(ALOAD_3);
                        break;
                    default:
                        output.addOneArgInstruction(ALOAD, offset);
                        break;
                }
            } else {
                // Primitive types
                if (type == Type.INT || type == Type.BOOLEAN
                        || type == Type.CHAR) {
                    switch (offset) {
                        case 0:
                            output.addNoArgInstruction(ILOAD_0);
                            break;
                        case 1:
                            output.addNoArgInstruction(ILOAD_1);
                            break;
                        case 2:
                            output.addNoArgInstruction(ILOAD_2);
                            break;
                        case 3:
                            output.addNoArgInstruction(ILOAD_3);
                            break;
                        default:
                            output.addOneArgInstruction(ILOAD, offset);
                            break;
                    }
                }
            }
        }
    }

    public void codegen(CLEmitter output, String targetLabel, boolean onTrue) {
        if (iDefn instanceof LocalVariableDefn) {
            // Push the value
            codegen(output);

            if (onTrue) {
                // Branch on true
                output.addBranchInstruction(IFNE, targetLabel);
            } else {
                // Branch on false
                output.addBranchInstruction(IFEQ, targetLabel);
            }
        }
    }

    public void codegenLoadLhsLvalue(CLEmitter output) {
    }

    public void codegenLoadLhsRvalue(CLEmitter output) {
        codegen(output);
    }

    public void codegenDuplicateRvalue(CLEmitter output) {
        if (iDefn instanceof LocalVariableDefn) {
            // It's copied atop the stack.
            output.addNoArgInstruction(DUP);
        }
    }

    public void codegenStore(CLEmitter output) {
        if (iDefn instanceof LocalVariableDefn) {
            int offset = ((LocalVariableDefn) iDefn).offset();
            if (type.isReference()) {
                switch (offset) {
                    case 0:
                        output.addNoArgInstruction(ASTORE_0);
                        break;
                    case 1:
                        output.addNoArgInstruction(ASTORE_1);
                        break;
                    case 2:
                        output.addNoArgInstruction(ASTORE_2);
                        break;
                    case 3:
                        output.addNoArgInstruction(ASTORE_3);
                        break;
                    default:
                        output.addOneArgInstruction(ASTORE, offset);
                        break;
                }
            } else {
                // Primitive types
                if (type == Type.INT || type == Type.BOOLEAN
                        || type == Type.CHAR) {
                    switch (offset) {
                        case 0:
                            output.addNoArgInstruction(ISTORE_0);
                            break;
                        case 1:
                            output.addNoArgInstruction(ISTORE_1);
                            break;
                        case 2:
                            output.addNoArgInstruction(ISTORE_2);
                            break;
                        case 3:
                            output.addNoArgInstruction(ISTORE_3);
                            break;
                        default:
                            output.addOneArgInstruction(ISTORE, offset);
                            break;
                    }
                }
            }
        }
    }
}

class SPThis extends SPExpression {

    public SPThis(int line) {
        super(line);
    }


    public SPExpression analyze(Context context) {
        type = ((SPClassDeclaration) context.classContext.definition())
                .thisType();
        return this;
    }

    public void codegen(CLEmitter output) {
        output.addNoArgInstruction(ALOAD_0);
    }

}
