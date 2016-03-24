package algol;

/**
 * Created by Gabriel on 08/03/2016.
 */
import static algol.CLConstants.*;

abstract class SPBinaryExpression extends SPExpression {

    protected SPExpression lhs;

    protected SPExpression rhs;

    protected int opcode;

    protected SPBinaryExpression(int line, SPExpression lhs, SPExpression rhs) {
        super(line);
        this.lhs = lhs;
        this.rhs = rhs;
    }
}

class SPPlusOp extends SPBinaryExpression {

    public SPPlusOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    public SPExpression analyze(Context context) {
        lhs = lhs.analyze(context);
        rhs = rhs.analyze(context);
        if (lhs.type() == Type.STRING || rhs.type() == Type.STRING) {
            return (new SPStringConcatenationOp(line, lhs, rhs))
                    .analyze(context);
        } else if (lhs.type() == Type.INT && rhs.type() == Type.INT) {
            type = Type.INT;
            opcode = IADD;
        } else if(lhs.type() == Type.DECIMAL || rhs.type() == Type.DECIMAL) {
            type = Type.DECIMAL;
            opcode = DADD;
        } else if(lhs.type() == Type.LONG && rhs.type() == Type.LONG){
          type = Type.LONG;
            opcode = LADD;
        } else {
            type = Type.ANY;
            SPAST.compilationUnit.reportSemanticError(line(), "Invalid operand types for +");
        }
        return this;
    }

    private void convert(CLEmitter output, SPExpression exp){
        if(type != Type.DECIMAL)
            return;

        if(exp.type() == Type.DECIMAL)
            return;

        int convertOpCode = I2D;
        if(exp.type() == Type.LONG)
            convertOpCode = L2D;

        output.addNoArgInstruction(convertOpCode);
    }


    public void codegen(CLEmitter output) {

        lhs.codegen(output);

        convert(output, lhs);

        rhs.codegen(output);

        convert(output, rhs);

        output.addNoArgInstruction(opcode);
    }
}

class SPSubtractOp extends SPBinaryExpression {

    public SPSubtractOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    public SPExpression analyze(Context context) {
        lhs = lhs.analyze(context);
        rhs = rhs.analyze(context);
        if (lhs.type() == Type.INT && rhs.type() == Type.INT) {
            type = Type.INT;
            opcode = ISUB;
        } else if(lhs.type() == Type.DECIMAL || rhs.type() == Type.DECIMAL) {
            type = Type.DECIMAL;
            opcode = DSUB;
        } else if(lhs.type() == Type.LONG && rhs.type() == Type.LONG){
            type = Type.LONG;
            opcode = LSUB;
        } else {
            type = Type.ANY;
            SPAST.compilationUnit.reportSemanticError(line(), "Invalid operand types for -");
        }
        return this;
    }

    private void convert(CLEmitter output, SPExpression exp){
        if(type != Type.DECIMAL)
            return;

        int convertOpCode = I2D;
        if(exp.type() == Type.LONG)
            convertOpCode = L2D;

        output.addNoArgInstruction(convertOpCode);
    }


    public void codegen(CLEmitter output) {

        lhs.codegen(output);

        convert(output, lhs);

        rhs.codegen(output);

        convert(output, rhs);

        output.addNoArgInstruction(opcode);
    }

}

class SPMultiplyOp extends SPBinaryExpression {


    public SPMultiplyOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    public SPExpression analyze(Context context) {
        lhs = lhs.analyze(context);
        rhs = rhs.analyze(context);
        if (lhs.type() == Type.INT && rhs.type() == Type.INT) {
            type = Type.INT;
            opcode = IMUL;
        } else if(lhs.type() == Type.DECIMAL || rhs.type() == Type.DECIMAL) {
            type = Type.DECIMAL;
            opcode = DMUL;
        } else if(lhs.type() == Type.LONG && rhs.type() == Type.LONG){
            type = Type.LONG;
            opcode = LMUL;
        } else {
            type = Type.ANY;
            SPAST.compilationUnit.reportSemanticError(line(), "Invalid operand types for *");
        }
        return this;
    }

    private void convert(CLEmitter output, SPExpression exp){
        if(type != Type.DECIMAL)
            return;

        int convertOpCode = I2D;
        if(exp.type() == Type.LONG)
            convertOpCode = L2D;

        output.addNoArgInstruction(convertOpCode);
    }


    public void codegen(CLEmitter output) {

        lhs.codegen(output);

        convert(output, lhs);

        rhs.codegen(output);

        convert(output, rhs);

        output.addNoArgInstruction(opcode);
    }
}

class SPDivisionOp extends SPBinaryExpression {


    public SPDivisionOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    public SPExpression analyze(Context context) {
        lhs = lhs.analyze(context);
        rhs = rhs.analyze(context);
        if (lhs.type() == Type.INT && rhs.type() == Type.INT) {
            type = Type.INT;
            opcode = IDIV;
        } else if(lhs.type() == Type.DECIMAL || rhs.type() == Type.DECIMAL) {
            type = Type.DECIMAL;
            opcode = DDIV;
        } else if(lhs.type() == Type.LONG && rhs.type() == Type.LONG){
            type = Type.LONG;
            opcode = LDIV;
        } else {
            type = Type.ANY;
            SPAST.compilationUnit.reportSemanticError(line(), "Invalid operand types for /");
        }
        return this;
    }

    private void convert(CLEmitter output, SPExpression exp){
        if(type != Type.DECIMAL)
            return;

        int convertOpCode = I2D;
        if(exp.type() == Type.LONG)
            convertOpCode = L2D;

        output.addNoArgInstruction(convertOpCode);
    }


    public void codegen(CLEmitter output) {

        lhs.codegen(output);

        convert(output, lhs);

        rhs.codegen(output);

        convert(output, rhs);

        output.addNoArgInstruction(opcode);
    }
}

