package algol;

/**
 * Created by Gabriel on 23/03/2016.
 */

import java.util.ArrayList;
import static algol.CLConstants.*;

class SPArrayInitializer
        extends SPExpression {

    private ArrayList<SPExpression> initials;


    public SPArrayInitializer(int line, Type expected,
                             ArrayList<SPExpression> initials) {
        super(line);
        type = expected;
        this.initials = initials;
    }

    public SPExpression analyze(Context context) {
        type = type.resolve(context);
        if (!type.isArray()) {
            SPAST.compilationUnit.reportSemanticError(line,
                    "Cannot initialize a " + type.toString()
                            + " with an array sequence {...}");
            return this; // un-analyzed
        }
        Type componentType = type.componentType();
        for (int i = 0; i < initials.size(); i++) {
            SPExpression component = initials.get(i);
            initials.set(i, component = component.analyze(context));
            if (!(component instanceof SPArrayInitializer)) {
                component.type().mustMatchExpected(line,
                        componentType);
            }
        }
        return this;
    }

    public void codegen(CLEmitter output) {
        Type componentType = type.componentType();

        new SPLiteralInt(line, String.valueOf(initials.size()))
                .codegen(output);

        output.addArrayInstruction(componentType.isReference()
                ? ANEWARRAY
                : NEWARRAY, componentType.jvmName());

        for (int i = 0; i < initials.size(); i++) {
            SPExpression initExpr = initials.get(i);

            output.addNoArgInstruction(DUP);

            new SPLiteralInt(line, String.valueOf(i)).codegen(output);

            initExpr.codegen(output);

            if (componentType == Type.INT) {
                output.addNoArgInstruction(IASTORE);
            } else if (componentType == Type.BOOLEAN) {
                output.addNoArgInstruction(BASTORE);
            } else if (componentType == Type.CHAR) {
                output.addNoArgInstruction(CASTORE);
            } else if (!componentType.isPrimitive()) {
                output.addNoArgInstruction(AASTORE);
            }else if (componentType == Type.DECIMAL) {
                output.addNoArgInstruction(DASTORE);
            } else if (componentType == Type.LONG) {
                output.addNoArgInstruction(LASTORE);
            }
        }
    }
}
