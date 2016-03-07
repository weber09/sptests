package algol;

import java.lang.reflect.Type;

/**
 * Created by Gabriel on 06/03/2016.
 */
public class SPVariableDeclarator extends SPAST {

    public String getName() {
        return name;
    }

    private String name;

    public Type getType() {
        return type;
    }

    private Type type;

    public SPVariableDeclarator(int line, String name, Type type)
    {
        super(line);
        this.name = name;
        this.type = type;
    }

    @Override
    public SPAST analyze() {
        return this;
    }
}
