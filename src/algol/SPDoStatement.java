package algol;

import static algol.CLConstants.*;

/**
 * Created by Gabriel on 30/03/2016.
 */

class SPDoStatement extends SPStatement{

    private SPExpression condition;

    private SPStatement body;

    public SPDoStatement(int line, SPExpression condition, SPStatement body) {
        super(line);
        this.condition = condition;
        this.body = body;
    }

    public SPDoStatement analyze(Context context) {
        condition = condition.analyze(context);
        condition.type().mustMatchExpected(line(), Type.BOOLEAN);
        body = (SPStatement) body.analyze(context);
        return this;
    }

    public void codegen(CLEmitter output) {
        // Need two labels
        String loop = output.createLabel();
        String out = output.createLabel();

        output.addLabel(loop);
        // Codegen body
        body.codegen(output);

        condition.codegen(output, out, false);
        output.addBranchInstruction(GOTO, loop);

        output.addLabel(out);
    }

}
