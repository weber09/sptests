package algol;

/**
 * Created by Gabriel on 08/03/2016.
 */

abstract class SPExpression extends SPStatement {

    protected Type type;

    protected boolean isStatementExpression;

    protected SPExpression(int line) {
        super(line);
        isStatementExpression = false; // by default
    }

    public Type type() {
        return type;
    }

    public boolean isStatementExpression() {
        return isStatementExpression;
    }


    public abstract SPExpression analyze(Context context);


    public void codegen(CLEmitter output, String targetLabel, boolean onTrue) {
        System.err.println("Error in code generation");
    }

}

