package algol;

/**
 * Created by Gabriel on 30/03/2016.
 */

import static algol.CLConstants.*;

class SPWhileStatement extends SPStatement{

    private SPExpression condition;

    private SPStatement body;

    public SPWhileStatement(int line, SPExpression condition, SPStatement body) {
        super(line);
        this.condition = condition;
        this.body = body;
    }

    public SPWhileStatement analyze(Context context) {
        condition = condition.analyze(context);
        condition.type().mustMatchExpected(line(), Type.BOOLEAN);
        body = (SPStatement) body.analyze(context);
        return this;
    }

    public void codegen(CLEmitter output) {
        // Need two labels
        String test = output.createLabel();
        String out = output.createLabel();

        // Branch out of the loop on the test condition
        // being false
        output.addLabel(test);
        condition.codegen(output, out, false);

        // Codegen body
        body.codegen(output);

        // Unconditional jump back up to test
        output.addBranchInstruction(GOTO, test);

        // The label below and outside the loop
        output.addLabel(out);
    }

}

