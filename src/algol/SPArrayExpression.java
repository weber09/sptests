package algol;

/**
 * Created by Gabriel on 23/03/2016.
 */

import java.util.ArrayList;

import static algol.CLConstants.*;

class SPArrayExpression
        extends SPExpression implements SPLhs {

    private SPExpression theArray;

    private ArrayList<SPExpression> indexExprs;

    public SPArrayExpression(int line, SPExpression theArray,
                            ArrayList<SPExpression> indexExprs) {
        super(line);
        this.theArray = theArray;
        this.indexExprs = indexExprs;
    }

    public SPExpression analyze(Context context) {
        theArray = theArray.analyze(context);

        if (!(theArray.type().isArray())) {
            SPAST.compilationUnit.reportSemanticError(line(), "attempt to index a non-array object");
            this.type = Type.ANY;
        } else {
            this.type = theArray.type().componentType();
        }

        for(int i = 0; i < indexExprs.size(); i++){
            indexExprs.set(i, indexExprs.get(i).analyze(context));
            indexExprs.get(i).type().mustMatchExpected(line(), Type.INT);
        }

        return this;
    }

    public SPExpression analyzeLhs(Context context) {
        analyze(context);
        return this;
    }

    public void codegen(CLEmitter output) {
        theArray.codegen(output);

        if(indexExprs.size() > 1) {
            for (int i = 0; i < indexExprs.size() - 1; i++) {
                indexExprs.get(i).codegen(output);
                output.addNoArgInstruction(AALOAD);
            }
        }

        indexExprs.get(indexExprs.size() - 1).codegen(output);

        Type baseType = type;
        if(type.isArray()){
            baseType = type.getBaseType();
        }

        if (baseType == Type.INT) {
            output.addNoArgInstruction(IALOAD);
        } else if (baseType == Type.BOOLEAN) {
            output.addNoArgInstruction(BALOAD);
        } else if (baseType == Type.CHAR) {
            output.addNoArgInstruction(CALOAD);
        } else if (!baseType.isPrimitive()) {
            output.addNoArgInstruction(AALOAD);
        } else if (baseType == Type.DECIMAL) {
            output.addNoArgInstruction(DALOAD);
        } else if (baseType == Type.LONG) {
            output.addNoArgInstruction(LALOAD);
        }
    }

    public void codegenLoadLhsLvalue(CLEmitter output) {
        theArray.codegen(output);
        if(indexExprs.size() > 1) {
            for (int i = 0; i < indexExprs.size() - 1; i++) {
                indexExprs.get(i).codegen(output);
                output.addNoArgInstruction(AALOAD);
            }
        }
        indexExprs.get(indexExprs.size() - 1).codegen(output);
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

        Type baseType = type;
        if(type.isArray()) {
            baseType = baseType.getBaseType();
        }


        if (baseType == Type.INT) {
            output.addNoArgInstruction(IASTORE);
        } else if (baseType == Type.BOOLEAN) {
            output.addNoArgInstruction(BASTORE);
        } else if (baseType == Type.CHAR) {
            output.addNoArgInstruction(CASTORE);
        } else if (!baseType.isPrimitive()) {
            output.addNoArgInstruction(AASTORE);
        }else if (baseType == Type.DECIMAL) {
            output.addNoArgInstruction(DASTORE);
        } else if (baseType == Type.LONG) {
            output.addNoArgInstruction(LASTORE);
        }

    }
}
