package algol;

/**
 * Created by Gabriel on 06/03/2016.
 */


abstract class SPAST {

    public static SPCompilationUnit compilationUnit;

    protected int line;

    protected SPAST(int line) {
        this.line = line;
    }

    public int line() {
        return line;
    }

    public abstract SPAST analyze(Context context);

    public void partialCodegen(Context context, CLEmitter partial) {
    }

    public abstract void codegen(CLEmitter output);

}
