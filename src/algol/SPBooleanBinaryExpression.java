package algol;

/**
 * Created by Gabriel on 28/03/2016.
 */

import static algol.CLConstants.*;

abstract class SPBooleanBinaryExpression extends SPBinaryExpression {
    protected SPBooleanBinaryExpression(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    public void codegen(CLEmitter output) {
        String elseLabel = output.createLabel();
        String endIfLabel = output.createLabel();
        this.codegen(output, elseLabel, false);
        output.addNoArgInstruction(ICONST_1); // true
        output.addBranchInstruction(GOTO, endIfLabel);
        output.addLabel(elseLabel);
        output.addNoArgInstruction(ICONST_0); // false
        output.addLabel(endIfLabel);
    }

}

class SPEqualOp extends SPBooleanBinaryExpression {

    public SPEqualOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    public SPExpression analyze(Context context) {
        lhs = lhs.analyze(context);
        rhs = rhs.analyze(context);
        lhs.type().mustMatchExpected(line(), rhs.type());
        type = Type.BOOLEAN;
        return this;
    }

    public void codegen(CLEmitter output, String targetLabel, boolean onTrue) {
        lhs.codegen(output);
        rhs.codegen(output);

        int opcode = onTrue ? IFEQ : IFNE;

        output.addBranchInstruction(opcode, targetLabel);
    }
}

class SPNotEqualOp extends SPBooleanBinaryExpression {

    public SPNotEqualOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    public SPExpression analyze(Context context) {
        lhs = lhs.analyze(context);
        rhs = rhs.analyze(context);
        lhs.type().mustMatchExpected(line(), rhs.type());
        type = Type.BOOLEAN;
        return this;
    }

    public void codegen(CLEmitter output, String targetLabel, boolean onTrue) {
        lhs.codegen(output);
        rhs.codegen(output);

        int opcode = onTrue ? IFNE : IFEQ;

        output.addBranchInstruction(opcode, targetLabel);
    }
}

class SPLogicalAndOp extends SPBooleanBinaryExpression {

    public SPLogicalAndOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    public SPExpression analyze(Context context) {
        lhs = lhs.analyze(context);
        rhs = rhs.analyze(context);
        lhs.type().mustMatchExpected(line(), Type.BOOLEAN);
        rhs.type().mustMatchExpected(line(), Type.BOOLEAN);
        type = Type.BOOLEAN;
        return this;
    }

    public void codegen(CLEmitter output, String targetLabel, boolean onTrue) {
        if (onTrue) {
            String falseLabel = output.createLabel();
            lhs.codegen(output, falseLabel, false);
            rhs.codegen(output, targetLabel, true);
            output.addLabel(falseLabel);
        } else {
            lhs.codegen(output, targetLabel, false);
            rhs.codegen(output, targetLabel, false);
        }
    }
}

class SPLogicalOrOp extends SPBooleanBinaryExpression {

    public SPLogicalOrOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    public SPExpression analyze(Context context) {
        lhs = lhs.analyze(context);
        rhs = rhs.analyze(context);
        lhs.type().mustMatchExpected(line(), Type.BOOLEAN);
        rhs.type().mustMatchExpected(line(), Type.BOOLEAN);
        type = Type.BOOLEAN;
        return this;
    }

    public void codegen(CLEmitter output, String targetLabel, boolean onTrue) {
        if (onTrue) {
            String trueLabel = output.createLabel();
            lhs.codegen(output, trueLabel, false);
            rhs.codegen(output, targetLabel, false);
            output.addLabel(trueLabel);
        } else {
            lhs.codegen(output, targetLabel, false);
            rhs.codegen(output, targetLabel, false);
        }
    }
}

