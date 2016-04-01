package algol;

/**
 * Created by Gabriel on 01/04/2016.
 */

import java.util.ArrayList;
import java.util.Arrays;

import static algol.CLConstants.*;

class SPScannerDeclarator extends SPStatement {

    private SPExpression scannerVariable;

    public SPScannerDeclarator() {
        super(0);
        scannerVariable = new SPVariable(0, "sp_inner_scanner");
    }

    public SPScannerDeclarator analyze(Context context) {

        scannerVariable = scannerVariable.analyze(context);

        return this;
    }

    public void codegen(CLEmitter output) {
        output.addReferenceInstruction(NEW, "java/util/Scanner");
        output.addNoArgInstruction(DUP);
        output.addMemberAccessInstruction(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;");
        output.addMemberAccessInstruction(INVOKESPECIAL, "java/util/Scanner", "<init>", "(Ljava/io/InputStream;)V");
        ((SPLhs)scannerVariable).codegenStore(output);
    }

}
