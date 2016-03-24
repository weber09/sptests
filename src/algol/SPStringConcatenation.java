package algol;

/**
 * Created by Gabriel on 08/03/2016.
 */
import static algol.CLConstants.*;

class SPStringConcatenationOp extends SPBinaryExpression {

    public SPStringConcatenationOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }


    public SPExpression analyze(Context context) {
        type = Type.STRING;
        return this;
    }

    public void codegen(CLEmitter output) {
        // Firstly, create a StringBuilder
        output.addReferenceInstruction(NEW, "java/lang/StringBuilder");
        output.addNoArgInstruction(DUP);
        output.addMemberAccessInstruction(INVOKESPECIAL,
                "java/lang/StringBuilder", "<init>", "()V");

        // Lhs and Rhs
        nestedCodegen(output);

        // Finally, make into a String
        output.addMemberAccessInstruction(INVOKEVIRTUAL,
                "java/lang/StringBuilder", "toString", "()Ljava/lang/String;");
    }

    void nestedCodegen(CLEmitter output) {
        // Lhs
        if (lhs instanceof SPStringConcatenationOp) {
            // This appends lhs
            ((SPStringConcatenationOp) lhs).nestedCodegen(output);
        } else {
            lhs.codegen(output);
            output.addMemberAccessInstruction(INVOKEVIRTUAL,
                    "java/lang/StringBuilder", "append", "("
                            + lhs.type().argumentTypeForAppend()
                            + ")Ljava/lang/StringBuilder;");
        }

        // Rhs
        if (rhs instanceof SPStringConcatenationOp) {
            // This appends rhs
            ((SPStringConcatenationOp) rhs).nestedCodegen(output);
        } else {
            rhs.codegen(output);
            output.addMemberAccessInstruction(INVOKEVIRTUAL,
                    "java/lang/StringBuilder", "append", "("
                            + rhs.type().argumentTypeForAppend()
                            + ")Ljava/lang/StringBuilder;");
        }
    }

}
