package algol;

/**
 * Created by Gabriel on 31/03/2016.
 */

import java.util.ArrayList;

import static algol.CLConstants.*;

class SPWriteStatement extends SPStatement{

    private boolean writeLine;

    private ArrayList<SPExpression> params;

    public SPWriteStatement(int line, boolean writeLine, ArrayList<SPExpression> params) {
        super(line);
        this.writeLine = writeLine;
        this.params = params;
    }

    public SPStatement analyze(Context context) {
        for (int i = 0; i < params.size(); i++) {
            params.set(i, params.get(i).analyze( context));
        }
        return this;
    }

    public void codegen(CLEmitter output) {

        String writeMethod = writeLine ? "println" : "print";

        for(SPExpression param : params) {
            output.addMemberAccessInstruction(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            param.codegen(output);

            String descriptor = param.type().toDescriptor();
            if(param.type().isArray()){
                descriptor = param.type().getBaseType().toDescriptor();
            }

            String methodSignature = String.format("(%s)V", descriptor);
            output.addMemberAccessInstruction(INVOKEVIRTUAL, "java/io/PrintStream", writeMethod, methodSignature);
        }

    }
}
