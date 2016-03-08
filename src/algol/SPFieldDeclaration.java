package algol;

/**
 * Created by Gabriel on 07/03/2016.
 */

import java.util.ArrayList;

class SPFieldDeclaration extends SPAST implements SPMember {

    private ArrayList<String> mods;

    private ArrayList<SPVariableDeclarator> decls;

    private ArrayList<SPStatement> initializations;

    public SPFieldDeclaration(int line, ArrayList<String> mods,
                             ArrayList<SPVariableDeclarator> decls) {
        super(line);
        this.mods = mods;
        this.decls = decls;
        initializations = new ArrayList<SPStatement>();
    }

    public ArrayList<String> mods() {
        return mods;
    }

    public void preAnalyze(Context context, CLEmitter partial) {

        for (SPVariableDeclarator decl : decls) {
            decl.setType(decl.getType().resolve(context));
            partial.addField(mods, decl.getName(), decl.getType().toDescriptor(), false);
        }
    }

   public SPFieldDeclaration analyze(Context context) {
        return this;
    }

    public void codegenInitializations(CLEmitter output) {
        for (SPStatement initialization : initializations) {
            initialization.codegen(output);
        }
    }


    public void codegen(CLEmitter output) {
        for (SPVariableDeclarator decl : decls) {
            output.addField(mods, decl.getName(), decl.getType().toDescriptor(), false);
        }
    }

}

