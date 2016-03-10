package algol;

/**
 * Created by Gabriel on 08/03/2016.
 */
import static algol.CLConstants.*;

abstract class SPBinaryExpression extends SPExpression {

    protected String operator;

    protected SPExpression lhs;

    protected SPExpression rhs;

    protected SPBinaryExpression(int line, String operator, SPExpression lhs,
                                SPExpression rhs) {
        super(line);
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }
}

class SPPlusOp extends SPBinaryExpression {

    public SPPlusOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, "+", lhs, rhs);
    }

    public SPExpression analyze(Context context) {
        lhs = (SPExpression) lhs.analyze(context);
        rhs = (SPExpression) rhs.analyze(context);
        if (lhs.type() == Type.STRING || rhs.type() == Type.STRING) {
            return (new SPStringConcatenationOp(line, lhs, rhs))
                    .analyze(context);
        } else if (lhs.type() == Type.INT && rhs.type() == Type.INT) {
            type = Type.INT;
        } else {
            type = Type.ANY;
            SPAST.compilationUnit.reportSemanticError(line(),
                    "Invalid operand types for +");
        }
        return this;
    }


    public void codegen(CLEmitter output) {
        if (type == Type.INT) {
            lhs.codegen(output);
            rhs.codegen(output);
            output.addNoArgInstruction(IADD);
        }
    }

}


class SPSubtractOp extends SPBinaryExpression {

    public SPSubtractOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, "-", lhs, rhs);
    }

    public SPExpression analyze(Context context) {
        lhs = (SPExpression) lhs.analyze(context);
        rhs = (SPExpression) rhs.analyze(context);
        lhs.type().mustMatchExpected(line(), Type.INT);
        rhs.type().mustMatchExpected(line(), Type.INT);
        type = Type.INT;
        return this;
    }

    /**
     * Generating code for the - operation involves generating code for the two
     * operands, and then the subtraction instruction.
     *
     * @param output
     *            the code emitter (basically an abstraction for producing the
     *            .class file).
     */

    public void codegen(CLEmitter output) {
        lhs.codegen(output);
        rhs.codegen(output);
        output.addNoArgInstruction(ISUB);
    }

}

class SPMultiplyOp extends SPBinaryExpression {


    public SPMultiplyOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, "*", lhs, rhs);
    }

    public SPExpression analyze(Context context) {
        lhs = (SPExpression) lhs.analyze(context);
        rhs = (SPExpression) rhs.analyze(context);
        lhs.type().mustMatchExpected(line(), Type.INT);
        rhs.type().mustMatchExpected(line(), Type.INT);
        type = Type.INT;
        return this;
    }

    public void codegen(CLEmitter output) {
        lhs.codegen(output);
        rhs.codegen(output);
        output.addNoArgInstruction(IMUL);
    }
}

class SPDivisionOp extends SPBinaryExpression {


    public SPDivisionOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, "*", lhs, rhs);
    }

    public SPExpression analyze(Context context) {
        lhs = (SPExpression) lhs.analyze(context);
        rhs = (SPExpression) rhs.analyze(context);
        lhs.type().mustMatchExpected(line(), Type.INT);
        rhs.type().mustMatchExpected(line(), Type.INT);
        type = Type.INT;
        return this;
    }

    public void codegen(CLEmitter output) {
        lhs.codegen(output);
        rhs.codegen(output);
        output.addNoArgInstruction(IDIV);
    }
}

