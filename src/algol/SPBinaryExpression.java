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

        Type baseLhsType = lhs.type();
        if(baseLhsType.isArray()){
            baseLhsType = ((ArrayTypeName)baseLhsType).getBaseType();
        }

        Type baseRhsType = rhs.type();
        if(baseRhsType.isArray()){
            baseRhsType = ((ArrayTypeName)baseRhsType).getBaseType();
        }

        if (baseLhsType == Type.INT && baseRhsType == Type.INT) {
            type = Type.INT;
            opcode = IADD;
        } else if(baseLhsType == Type.DECIMAL || baseRhsType == Type.DECIMAL) {
            type = Type.DECIMAL;
            opcode = DADD;
        } else if(baseLhsType == Type.LONG && baseRhsType == Type.LONG){
            type = Type.LONG;
            opcode = LADD;
        } else {
            type = Type.ANY;
            SPAST.compilationUnit.reportSemanticError(line(), "Invalid operand types for +");
        }
        return this;
    }

    private void convert(CLEmitter output, SPExpression exp){
        Type baseType = type;
        if(baseType.isArray()){
            baseType = ((ArrayTypeName)baseType).getBaseType();
        }

        if(baseType != Type.DECIMAL)
            return;

        Type expBaseType = exp.type();
        if(expBaseType.isArray()){
            expBaseType =((ArrayTypeName)expBaseType).getBaseType();
        }

        if(expBaseType == Type.DECIMAL)
            return;

        int convertOpCode = I2D;
        if(expBaseType == Type.LONG)
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

        Type baseLhsType = lhs.type();
        if(baseLhsType.isArray()){
            baseLhsType = ((ArrayTypeName)baseLhsType).getBaseType();
        }

        Type baseRhsType = rhs.type();
        if(baseRhsType.isArray()){
            baseRhsType = ((ArrayTypeName)baseRhsType).getBaseType();
        }

        if (baseLhsType == Type.INT && baseRhsType == Type.INT) {
            type = Type.INT;
            opcode = ISUB;
        } else if(baseLhsType == Type.DECIMAL || baseRhsType == Type.DECIMAL) {
            type = Type.DECIMAL;
            opcode = DSUB;
        } else if(baseLhsType == Type.LONG && baseRhsType == Type.LONG){
            type = Type.LONG;
            opcode = LSUB;
        } else {
            type = Type.ANY;
            SPAST.compilationUnit.reportSemanticError(line(), "Invalid operand types for -");
        }
        return this;
    }

    private void convert(CLEmitter output, SPExpression exp){
        Type baseType = type;
        if(baseType.isArray()){
            baseType = ((ArrayTypeName)baseType).getBaseType();
        }

        if(baseType != Type.DECIMAL)
            return;

        Type expBaseType = exp.type();
        if(expBaseType.isArray()){
            expBaseType =((ArrayTypeName)expBaseType).getBaseType();
        }

        if(expBaseType == Type.DECIMAL)
            return;

        int convertOpCode = I2D;
        if(expBaseType == Type.LONG)
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

        Type baseLhsType = lhs.type();
        if(baseLhsType.isArray()){
            baseLhsType = ((ArrayTypeName)baseLhsType).getBaseType();
        }

        Type baseRhsType = rhs.type();
        if(baseRhsType.isArray()){
            baseRhsType = ((ArrayTypeName)baseRhsType).getBaseType();
        }

        if (baseLhsType == Type.INT && baseRhsType == Type.INT) {
            type = Type.INT;
            opcode = IMUL;
        } else if(baseLhsType == Type.DECIMAL || baseRhsType == Type.DECIMAL) {
            type = Type.DECIMAL;
            opcode = DMUL;
        } else if(baseLhsType == Type.LONG && baseRhsType == Type.LONG){
            type = Type.LONG;
            opcode = LMUL;
        } else {
            type = Type.ANY;
            SPAST.compilationUnit.reportSemanticError(line(), "Invalid operand types for *");
        }
        return this;
    }

    private void convert(CLEmitter output, SPExpression exp){
        Type baseType = type;
        if(baseType.isArray()){
            baseType = ((ArrayTypeName)baseType).getBaseType();
        }

        if(baseType != Type.DECIMAL)
            return;

        Type expBaseType = exp.type();
        if(expBaseType.isArray()){
            expBaseType =((ArrayTypeName)expBaseType).getBaseType();
        }

        if(expBaseType == Type.DECIMAL)
            return;

        int convertOpCode = I2D;
        if(expBaseType == Type.LONG)
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

        Type baseLhsType = lhs.type();
        if(baseLhsType.isArray()){
            baseLhsType = ((ArrayTypeName)baseLhsType).getBaseType();
        }

        Type baseRhsType = rhs.type();
        if(baseRhsType.isArray()){
            baseRhsType = ((ArrayTypeName)baseRhsType).getBaseType();
        }

        if (baseLhsType == Type.INT && baseRhsType == Type.INT) {
            type = Type.INT;
            opcode = IDIV;
        } else if(baseLhsType == Type.DECIMAL || baseRhsType == Type.DECIMAL) {
            type = Type.DECIMAL;
            opcode = DDIV;
        } else if(baseLhsType == Type.LONG && baseRhsType == Type.LONG){
            type = Type.LONG;
            opcode = LDIV;
        } else {
            type = Type.ANY;
            SPAST.compilationUnit.reportSemanticError(line(), "Invalid operand types for /");
        }
        return this;
    }

    private void convert(CLEmitter output, SPExpression exp){
        Type baseType = type;
        if(baseType.isArray()){
            baseType = ((ArrayTypeName)baseType).getBaseType();
        }

        if(baseType != Type.DECIMAL)
            return;

        Type expBaseType = exp.type();
        if(expBaseType.isArray()){
            expBaseType =((ArrayTypeName)expBaseType).getBaseType();
        }

        if(expBaseType == Type.DECIMAL)
            return;

        int convertOpCode = I2D;
        if(expBaseType == Type.LONG)
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

