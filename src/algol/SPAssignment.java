package algol;

/**
 * Created by Gabriel on 08/03/2016.
 */

import static algol.CLConstants.*;

abstract class SPAssignment extends SPBinaryExpression {

    abstract void setLhs(SPExpression lhs);

    abstract void setRhs(SPExpression rhs);

    public SPAssignment(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }
}

class SPAssignOp extends SPAssignment {

    public void setLhs(SPExpression lhs)
    {
        this.lhs = lhs;
    }

    public void setRhs(SPExpression rhs)
    {
        this.rhs = rhs;
    }

    public SPAssignOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    private boolean mustMatchTypes(){

        if(rhs.type().matchesExpected(lhs.type())) {
            return true;
        }

        if(lhs.type() == Type.DECIMAL && (rhs.type() == Type.LONG || rhs.type() == Type.INT))
            return true;

       return lhs.type() == Type.LONG && rhs.type() == Type.INT;
    }

    public SPExpression analyze(Context context) {
        if (!(lhs instanceof SPLhs)) {
            SPAST.compilationUnit.reportSemanticError(line(), "Illegal lhs for assignment");
        } else {
            lhs = ((SPLhs) lhs).analyzeLhs(context);
        }
        rhs = rhs.analyze(context);

        if(!mustMatchTypes()){
            SPAST.compilationUnit.reportSemanticError(line, "Type %s doesn't match type %s", this, lhs.type());
        }

        type = rhs.type();
        /*if (lhs instanceof SPVariable) {
            IDefn defn = ((SPVariable) lhs).iDefn();
            if (defn != null) {
                // Local variable; consider it to be initialized now.
                ((LocalVariableDefn) defn).initialize();
            }
        }*/
        return this;
    }

    public void codegen(CLEmitter output) {
        ((SPLhs) lhs).codegenLoadLhsLvalue(output);
        rhs.codegen(output);
        if (!isStatementExpression) {
            // Generate code to leave the Rvalue atop stack
            ((SPLhs) lhs).codegenDuplicateRvalue(output);
        }

        if(lhs.type() == Type.DECIMAL){
            if(rhs.type() == Type.INT)
                output.addNoArgInstruction(I2D);
            else if(rhs.type() == Type.LONG)
                output.addNoArgInstruction(L2D);
        } else if(lhs.type() == Type.LONG) {
            output.addNoArgInstruction(I2L);
        }

        ((SPLhs) lhs).codegenStore(output);
    }

}

class SPPlusAssignOp extends SPAssignment {

    public void setLhs(SPExpression lhs)
    {
        this.lhs = lhs;
    }

    public void setRhs(SPExpression rhs)
    {
        this.rhs = rhs;
    }

    public SPPlusAssignOp(int line, SPExpression lhs, SPExpression rhs) {
        super(line, lhs, rhs);
    }

    /**
     * Analyze the lhs and rhs, rewrite rhs as lhs + rhs (string concatenation)
     * if lhs is a String, and set the result type.
     *
     * @param context
     *            context in which names are resolved.
     * @return the analyzed (and possibly rewritten) AST subtree.
     */

    public SPExpression analyze(Context context) {
        if (!(lhs instanceof SPLhs)) {
            SPAST.compilationUnit.reportSemanticError(line(),
                    "Illegal lhs for assignment");
            return this;
        } else {
            lhs = (SPExpression) ((SPLhs) lhs).analyzeLhs(context);
        }
        rhs = (SPExpression) rhs.analyze(context);
        if (lhs.type().equals(Type.INT)) {
            rhs.type().mustMatchExpected(line(), Type.INT);
            type = Type.INT;
        } else if (lhs.type().equals(Type.STRING)) {
            rhs = (new SPStringConcatenationOp(line, lhs, rhs)).analyze(context);
            type = Type.STRING;
        } else {
            SPAST.compilationUnit.reportSemanticError(line(),
                    "Invalid lhs type for +=: " + lhs.type());
        }
        return this;
    }

    public void codegen(CLEmitter output) {
        ((SPLhs) lhs).codegenLoadLhsLvalue(output);
        if (lhs.type().equals(Type.STRING)) {
            rhs.codegen(output);
        } else {
            ((SPLhs) lhs).codegenLoadLhsRvalue(output);
            rhs.codegen(output);
            output.addNoArgInstruction(IADD);
        }
        if (!isStatementExpression) {
            ((SPLhs) lhs).codegenDuplicateRvalue(output);
        }
        ((SPLhs) lhs).codegenStore(output);
    }
}

