package algol;

/**
 * Created by Gabriel on 23/03/2016.
 */
import java.util.ArrayList;
import static algol.CLConstants.*;

/**
 * The AST node for a "new" array operation. It keeps track of its base type and
 * a list of its dimensions.
 */

class SPNewArrayOp extends SPExpression {

    private Type typeSpec;

    private ArrayList<SPExpression> dimExprs;

    public SPNewArrayOp(int line, Type typeSpec, ArrayList<SPExpression> dimExprs) {
        super(line);
        this.typeSpec = typeSpec;
        this.dimExprs = dimExprs;
    }

    public SPExpression analyze(Context context) {
        type = typeSpec.resolve(context);
        for (int i = 0; i < dimExprs.size(); i++) {
            dimExprs.set(i, dimExprs.get(i).analyze(context));
            dimExprs.get(i).type().mustMatchExpected(line, Type.INT);
        }
        return this;
    }

    public void codegen(CLEmitter output) {
        // Code to push diemension exprs on to the stack
        for (SPExpression dimExpr : dimExprs) {
            dimExpr.codegen(output);
        }

        // Generate the appropriate array creation instruction
        if (dimExprs.size() == 1) {
            output.addArrayInstruction(
                    type.componentType().isReference() ? ANEWARRAY : NEWARRAY,
                    type.componentType().jvmName());
        } else {
            output.addMULTIANEWARRAYInstruction(type.toDescriptor(), dimExprs
                    .size());
        }
    }
}
