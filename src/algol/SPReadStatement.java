package algol;

/**
 * Created by Gabriel on 31/03/2016.
 */

import java.util.ArrayList;

import static algol.CLConstants.*;

class SPReadStatement extends SPStatement{

    private ArrayList<SPExpression> params;

    private SPExpression scannerVar;

    public SPReadStatement(int line, ArrayList<SPExpression> params, SPExpression scannerVar) {
        super(line);
        this.params = params;
        this.scannerVar = scannerVar;
    }

    public SPStatement analyze(Context context) {
        scannerVar = scannerVar.analyze(context);

        for (int i = 0; i < params.size(); i++) {
            params.set(i, params.get(i).analyze( context));
        }
        return this;
    }

    public void codegen(CLEmitter output) {

        for(SPExpression param : params) {

            String methodName = getMethodName(param.type);
            String methodSignature = String.format("()%s", param.type.toDescriptor());

            //loads the pointer to the scanner var on the stack
            scannerVar.codegen(output);
            //invokes the method of the scanner class to read from the standard input
            output.addMemberAccessInstruction(INVOKEVIRTUAL, "java/util/Scanner", methodName, methodSignature);
            //stores the readed value on its corresponding variable
            ((SPLhs)param).codegenStore(output);
        }

    }

    private String getMethodName(Type type) {

        if (type == Type.INT) {
            return "nextInt";
        } else if (type == Type.STRING) {
            return "nextLine";
        } else if (type == Type.DECIMAL) {
            return "nextDouble";
        } else if (type == Type.LONG) {
            return "nextLong";
        } else if (type == Type.BOOLEAN) {
            return "nextBoolean";
        }

        return "";
    }

}
