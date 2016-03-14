package algol;

/**
 * Created by Gabriel on 08/03/2016.
 */
import static algol.CLConstants.*;

class SPFieldSelection extends SPExpression implements SPLhs {

    /** The target expression. */
    protected SPExpression target;


    /** The field name. */
    private String fieldName;

    /** The Field representing this field. */
    private Field field;

    public SPFieldSelection(int line,
                           SPExpression target, String fieldName) {
        super(line);
        this.target = target;
        this.fieldName = fieldName;
    }

    public SPExpression analyze(Context context) {
        target = (SPExpression) target.analyze(context);
        Type targetType = target.type();

        // We use a workaround for the "length" field of arrays.
        if ((targetType.isArray()) && fieldName.equals("length")) {
            type = Type.INT;
        } else {
            // Other than that, targetType has to be a
            // ReferenceType
            if (targetType.isPrimitive()) {
                SPAST.compilationUnit.reportSemanticError(line(),
                        "Target of a field selection must "
                                + "be a defined type");
                type = Type.ANY;
                return this;
            }
            field = targetType.fieldFor(fieldName);
            if (field == null) {
                SPAST.compilationUnit.reportSemanticError(line(),
                        "Cannot find a field: " + fieldName);
                type = Type.ANY;
            } else {
                context.definingType().checkAccess(line, (Member) field);
                type = field.type();
                }
            }
        return this;
    }

    public SPExpression analyzeLhs(Context context) {
        SPExpression result = analyze(context);
        if (field.isFinal()) {
           SPAST.compilationUnit.reportSemanticError(line, "The field "
                    + fieldName + " in type " + target.type.toString()
                    + " is declared final.");
        }
        return result;
    }

    public void codegen(CLEmitter output) {
        target.codegen(output);

        // We use a workaround for the "length" field of arrays
        if ((target.type().isArray()) && fieldName.equals("length")) {
            output.addNoArgInstruction(ARRAYLENGTH);
        } else {
            int mnemonic = field.isStatic() ? GETSTATIC : GETFIELD;
            output.addMemberAccessInstruction(mnemonic,
                    target.type().jvmName(), fieldName, type.toDescriptor());
        }
    }

    public void codegen(CLEmitter output, String targetLabel, boolean onTrue) {
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

    public void codegenLoadLhsLvalue(CLEmitter output) {
        if (!field.isStatic()) {
            target.codegen(output);
        }
    }

    public void codegenLoadLhsRvalue(CLEmitter output) {
        String descriptor = field.type().toDescriptor();
        if (field.isStatic()) {
            output.addMemberAccessInstruction(GETSTATIC, target.type()
                    .jvmName(), fieldName, descriptor);
        } else {
            output.addNoArgInstruction(type == Type.STRING ? DUP_X1 : DUP);
            output.addMemberAccessInstruction(GETFIELD,
                    target.type().jvmName(), fieldName, descriptor);
        }
    }

    public void codegenDuplicateRvalue(CLEmitter output) {
        if (field.isStatic()) {
            output.addNoArgInstruction(DUP);
        } else {
            output.addNoArgInstruction(DUP_X1);
        }
    }

    public void codegenStore(CLEmitter output) {
        String descriptor = field.type().toDescriptor();
        if (field.isStatic()) {
            output.addMemberAccessInstruction(PUTSTATIC, target.type()
                    .jvmName(), fieldName, descriptor);
        } else {
            output.addMemberAccessInstruction(PUTFIELD,
                    target.type().jvmName(), fieldName, descriptor);
        }
    }

}
