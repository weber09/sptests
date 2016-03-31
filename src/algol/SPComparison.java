package algol;

/**
 * Created by Gabriel on 28/03/2016.
 */

import static algol.CLConstants.*;

abstract class SPComparison extends SPBooleanBinaryExpression {

    protected SPComparison(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }
    
    public SPExpression analyze(Context context) {
        lhs = lhs.analyze(context);
        rhs = rhs.analyze(context);
        //lhs.type().mustMatchExpected(line(), Type.INT);
        //rhs.type().mustMatchExpected(line(), lhs.type());
        type = Type.BOOLEAN;
        return this;
    }
}

class SPGreaterThanOp extends SPComparison {

    public SPGreaterThanOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    public void codegen(CLEmitter output, String targetLabel, boolean onTrue) {
        lhs.codegen(output);
        rhs.codegen(output);
        output.addBranchInstruction(onTrue ? IF_ICMPGT : IF_ICMPLE, targetLabel);
    }
}

class SPGreaterEqualOp extends SPComparison {

    public SPGreaterEqualOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    public void codegen(CLEmitter output, String targetLabel, boolean onTrue) {
        lhs.codegen(output);
        rhs.codegen(output);
        output.addBranchInstruction(onTrue ? IF_ICMPGE : IF_ICMPLT, targetLabel);
    }
}

class SPLessThanOp extends SPComparison {

    public SPLessThanOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    public void codegen(CLEmitter output, String targetLabel, boolean onTrue) {
        lhs.codegen(output);
        rhs.codegen(output);
        output.addBranchInstruction(onTrue ? IF_ICMPLT : IF_ICMPGE, targetLabel);
    }
}

class SPLessEqualOp extends SPComparison {

    public SPLessEqualOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    public void codegen(CLEmitter output, String targetLabel, boolean onTrue) {
        lhs.codegen(output);
        rhs.codegen(output);
        output.addBranchInstruction(onTrue ? IF_ICMPLE : IF_ICMPGT, targetLabel);
    }
}

