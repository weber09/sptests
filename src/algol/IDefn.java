package algol;

/**
 * Created by Gabriel on 07/03/2016.
 */
interface IDefn {
    public Type type();
}

class TypeNameDefn implements IDefn {

    private Type type;

    public TypeNameDefn(Type type) {
        this.type = type;
    }

    public Type type() {
        return type;
    }

}

class LocalVariableDefn implements IDefn {

    private Type type;

    private int offset;

    private boolean isInitialized;

    public LocalVariableDefn(Type type, int offset) {
        this.type = type;
        this.offset = offset;
    }

    public Type type() {
        return type;
    }

    public int offset() {
        return offset;
    }

    public void initialize() {
        this.isInitialized = true;
    }

    public boolean isInitialized() {
        return isInitialized;
    }

}
