package algol;

/**
 * Created by Gabriel on 04/04/2016.
 */
public class SemanticError {

    private int line;

    private String message;

    public SemanticError(int line, String message, Object... arguments){
        this.line = line;
        this.message = String.format(message, arguments);
    }

    @Override
    public String toString() {
        return String.format("Linha: %d:\n%s", line, message);
    }
}
