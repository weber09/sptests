package algol;

/**
 * Created by Gabriel on 28/03/2016.
 */

import static algol.CLConstants.*;

abstract class SPBooleanBinaryExpression extends SPBinaryExpression {
    protected SPBooleanBinaryExpression(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }
}

class SPEqualOp extends SPBooleanBinaryExpression {

    public SPEqualOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    public SPExpression analyze(Context context) {
        lhs = lhs.analyze(context);
        rhs = rhs.analyze(context);

        Type lhsBaseType = lhs.type();
        if(lhsBaseType.isArray()){
            lhsBaseType = lhsBaseType.getBaseType();
        }

        Type rhsBaseType = rhs.type();
        if(rhsBaseType.isArray()){
            rhsBaseType = rhsBaseType.getBaseType();
        }

        if ((lhsBaseType == Type.INT && rhsBaseType == Type.INT) ||
            lhsBaseType == Type.DECIMAL || rhsBaseType == Type.DECIMAL ||
            (lhsBaseType == Type.LONG && rhsBaseType == Type.LONG) ||
            (lhsBaseType == Type.BOOLEAN && rhsBaseType == Type.BOOLEAN)){
            type = Type.BOOLEAN;
        } else {
            type = Type.ANY;
            SPAST.compilationUnit.reportSemanticError(line(), "Invalid operand types for =");
        }

        return this;
    }

    @Override
    public void codegen(CLEmitter output) {

        String elseLabel = output.createLabel();
        String endIfLabel = output.createLabel();

        Type lhsBaseType = lhs.type();
        if(lhsBaseType.isArray()){
            lhsBaseType = lhsBaseType.getBaseType();
        }

        Type rhsBaseType = rhs.type();
        if(rhsBaseType.isArray()){
            rhsBaseType = rhsBaseType.getBaseType();
        }

        lhs.codegen(output);

        if(lhsBaseType == Type.INT && rhsBaseType == Type.DECIMAL){
            output.addNoArgInstruction(I2D);
        }

        rhs.codegen(output);

        if(rhsBaseType == Type.INT && lhsBaseType == Type.DECIMAL){
            output.addNoArgInstruction(I2D);
        }

        if(lhsBaseType == Type.DECIMAL || rhsBaseType == Type.DECIMAL){
            output.addNoArgInstruction(DCMPL);
            output.addBranchInstruction(IFNE, elseLabel);
        }else{
            output.addBranchInstruction(IF_ICMPNE, elseLabel);
        }

        output.addNoArgInstruction(ICONST_1); // true
        output.addBranchInstruction(GOTO, endIfLabel);
        output.addLabel(elseLabel);
        output.addNoArgInstruction(ICONST_0); // false
        output.addLabel(endIfLabel);
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

    @Override
    public void codegen(CLEmitter output) {

        String elseLabel = output.createLabel();
        String endIfLabel = output.createLabel();

        lhs.codegen(output);
        rhs.codegen(output);

        output.addBranchInstruction(IFEQ, elseLabel);

        output.addNoArgInstruction(ICONST_1); // true
        output.addBranchInstruction(GOTO, endIfLabel);
        output.addLabel(elseLabel);
        output.addNoArgInstruction(ICONST_0); // false
        output.addLabel(endIfLabel);
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

    @Override
    public void codegen(CLEmitter output) {
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

    @Override
    public void codegen(CLEmitter output) {
    }
}

