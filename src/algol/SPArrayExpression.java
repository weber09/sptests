package algol;

/**
 * Created by Gabriel on 23/03/2016.
 */
import static algol.CLConstants.*;

class SPArrayExpression
        extends SPExpression implements SPLhs {

    private SPExpression theArray;

    private SPExpression indexExpr;

    public SPArrayExpression(int line, SPExpression theArray,
                            SPExpression indexExpr) {
        super(line);
        this.theArray = theArray;
        this.indexExpr = indexExpr;
    }

    public SPExpression analyze(Context context) {
        theArray = theArray.analyze(context);
        indexExpr = indexExpr.analyze(context);
        if (!(theArray.type().isArray())) {
            SPAST.compilationUnit.reportSemanticError(line(), "attempt to index a non-array object");
            this.type = Type.ANY;
        } else {
            this.type = theArray.type().componentType();
        }
        indexExpr.type().mustMatchExpected(line(), Type.INT);
        return this;
    }

    public SPExpression analyzeLhs(Context context) {
        analyze(context);
        return this;
    }

    public void codegen(CLEmitter output) {
        theArray.codegen(output);
        indexExpr.codegen(output);
        if (type == Type.INT) {
            output.addNoArgInstruction(IALOAD);
        } else if (type == Type.BOOLEAN) {
            output.addNoArgInstruction(BALOAD);
        } else if (type == Type.CHAR) {
            output.addNoArgInstruction(CALOAD);
        } else if (!type.isPrimitive()) {
            output.addNoArgInstruction(AALOAD);
        } else if (type == Type.DECIMAL) {
            output.addNoArgInstruction(DALOAD);
        } else if (type == Type.LONG) {
            output.addNoArgInstruction(LALOAD);
        }
    }

    public void codegenLoadLhsLvalue(CLEmitter output) {
        theArray.codegen(output);
        indexExpr.codegen(output);
    }

    public void codegenLoadLhsRvalue(CLEmitter output) {
        if (type == Type.STRING) {
            output.addNoArgInstruction(DUP2_X1);
        } else {
            output.addNoArgInstruction(DUP2);
        }
        if (type == Type.INT) {
            output.addNoArgInstruction(IALOAD);
        } else if (type == Type.BOOLEAN) {
            output.addNoArgInstruction(BALOAD);
        } else if (type == Type.CHAR) {
            output.addNoArgInstruction(CALOAD);
        } else if (!type.isPrimitive()) {
            output.addNoArgInstruction(AALOAD);
        }else if (type == Type.DECIMAL) {
            output.addNoArgInstruction(DALOAD);
        } else if (type == Type.LONG) {
            output.addNoArgInstruction(LALOAD);
        }
    }

    public void codegenDuplicateRvalue(CLEmitter output) {
        output.addNoArgInstruction(DUP_X2);
    }

    public void codegenStore(CLEmitter output) {
        if (type == Type.INT) {
            output.addNoArgInstruction(IASTORE);
        } else if (type == Type.BOOLEAN) {
            output.addNoArgInstruction(BASTORE);
        } else if (type == Type.CHAR) {
            output.addNoArgInstruction(CASTORE);
        } else if (!type.isPrimitive()) {
            output.addNoArgInstruction(AASTORE);
        }else if (type == Type.DECIMAL) {
            output.addNoArgInstruction(DASTORE);
        } else if (type == Type.LONG) {
            output.addNoArgInstruction(LASTORE);
        }

    }
}
