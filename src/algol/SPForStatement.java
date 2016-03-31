package algol;

/**
 * Created by Gabriel on 30/03/2016.
 */

import static algol.CLConstants.*;

class SPForStatement extends SPStatement{

    private SPStatement controlVariableInitialization;

    private SPStatement controlVariableIncrement;

    private SPExpression condition;

    private SPStatement body;

    public SPForStatement(int line, SPStatement controlVariableInitialization, SPStatement controlVariableIncrement, SPExpression condition, SPStatement body) {
        super(line);
        this.controlVariableInitialization = controlVariableInitialization;
        this.controlVariableIncrement = controlVariableIncrement;
        this.condition = condition;
        this.body = body;
    }

    public SPForStatement analyze(Context context) {
        controlVariableInitialization = (SPStatement)controlVariableInitialization.analyze(context);
        controlVariableIncrement = (SPStatement)controlVariableIncrement.analyze(context);
        condition = condition.analyze(context);
        body = (SPStatement) body.analyze(context);
        return this;
    }

    public void codegen(CLEmitter output) {
        // Need two labels
        String test = output.createLabel();
        String out = output.createLabel();

        controlVariableInitialization.codegen(output);
        output.addLabel(test);

        condition.codegen(output, out, true);

        body.codegen(output);

        controlVariableIncrement.codegen(output);

        output.addBranchInstruction(GOTO, test);

        output.addLabel(out);
    }

}
