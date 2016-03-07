package algol;

/**
 * Created by Gabriel on 06/03/2016.
 */

public class SPClass extends SPAST {


    private String name;

    public SPClass(int line, String name) {
        super(line);
        this.name = name;
    }

    public String getName(){ return name;}

    @Override
    public SPAST analyze() {
        return this;
    }
}
