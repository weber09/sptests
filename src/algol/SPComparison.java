package algol;

/**
 * Created by Gabriel on 28/03/2016.
 */

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

    @Override
    public void codegen(CLEmitter output) {
        lhs.codegen(output);
        rhs.codegen(output);
    }
}

class SPGreaterEqualOp extends SPComparison {

    public SPGreaterEqualOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    @Override
    public void codegen(CLEmitter output) {
        lhs.codegen(output);
        rhs.codegen(output);
    }
}

class SPLessThanOp extends SPComparison {

    public SPLessThanOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    @Override
    public void codegen(CLEmitter output) {
        lhs.codegen(output);
        rhs.codegen(output);
    }
}

class SPLessEqualOp extends SPComparison {

    public SPLessEqualOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    @Override
    public void codegen(CLEmitter output) {
        lhs.codegen(output);
        rhs.codegen(output);
    }
}

