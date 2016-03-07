package algol;

/**
 * Created by Gabriel on 06/03/2016.
 */


abstract class SPAST {

    protected int line;

    public SPAST(int line) {
        this.line = line;
    }

    public int GetLine()
    {
        return line;
    }

    public abstract SPAST analyze();


}
