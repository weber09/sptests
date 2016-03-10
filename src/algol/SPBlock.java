package algol;

/**
 * Created by Gabriel on 08/03/2016.
 */
import java.util.ArrayList;


class SPBlock extends SPStatement {

    private ArrayList<SPStatement> statements;

    private LocalContext context;

    public SPBlock(int line, ArrayList<SPStatement> statements) {
        super(line);
        this.statements = statements;
    }

    public ArrayList<SPStatement> statements() {
        return statements;
    }

    public SPBlock analyze(Context context) {
        this.context = new LocalContext(context);

        for (int i = 0; i < statements.size(); i++) {
            statements.set(i, (SPStatement) statements.get(i).analyze(
                    this.context));
        }
        return this;
    }

    public void codegen(CLEmitter output) {
        for (SPStatement statement : statements) {
            statement.codegen(output);
        }
    }
}
