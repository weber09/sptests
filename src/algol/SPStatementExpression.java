package algol;

/**
 * Created by Gabriel on 08/03/2016.
 */

class SPStatementExpression extends SPStatement {
    SPExpression expr;

    public SPStatementExpression(int line, SPExpression expr) {
        super(line);
        this.expr = expr;
    }

    public SPStatement analyze(Context context) {
        if (expr.isStatementExpression) {
            expr = expr.analyze(context);
        }
        return this;
    }

    public void codegen(CLEmitter output) {
        expr.codegen(output);
    }
}
