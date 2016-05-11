package algol;

/**
 * Created by Gabriel on 06/03/2016.
 */

        import java.util.ArrayList;
        import java.util.Map;
        import java.util.HashMap;
        import java.util.Set;

class Context {

    protected Context surroundingContext;

    protected ClassContext classContext;

    protected CompilationUnitContext compilationUnitContext;

    protected Map<String, IDefn> entries;

    protected Context(Context surrounding, ClassContext classContext,
                      CompilationUnitContext compilationUnitContext) {
        this.surroundingContext = surrounding;
        this.classContext = classContext;
        this.compilationUnitContext = compilationUnitContext;
        this.entries = new HashMap<String, IDefn>();
    }

    public void addEntry(int line, String name, IDefn definition) {
        if (entries.containsKey(name)) {
            SPAST.compilationUnit.reportSemanticError(line, "redefining name: "
                    + name);
        } else {
            entries.put(name, definition);
        }
    }

    public IDefn lookup(String name) {
        IDefn iDefn = (IDefn) entries.get(name);
        return iDefn != null ? iDefn
                : surroundingContext != null ? surroundingContext.lookup(name)
                : null;
    }

    public Type lookupType(String name) {
        TypeNameDefn defn = (TypeNameDefn) compilationUnitContext.lookup(name);
        return defn == null ? null : defn.type();
    }

    public void addType(int line, Type type) {
        IDefn iDefn = new TypeNameDefn(type);
        compilationUnitContext.addEntry(line, type.simpleName(), iDefn);
        if (!type.toString().equals(type.simpleName())) {
            compilationUnitContext.addEntry(line, type.toString(), iDefn);
        }
    }


    public Type definingType() {
        return ((SPTypeDecl) classContext.definition()).thisType();
    }


    public Context surroundingContext() {
        return surroundingContext;
    }

    public ClassContext classContext() {
        return classContext;
    }

    public CompilationUnitContext compilationUnitContext() {
        return compilationUnitContext;
    }


    public MethodContext methodContext() {
        Context context = this;
        while (context != null && !(context instanceof MethodContext)) {
            context = context.surroundingContext();
        }
        return (MethodContext) context;
    }

    public Set<String> names() {
        return entries.keySet();
    }
}

class CompilationUnitContext extends Context {

    public ArrayList<SemanticError> semanticErrors;

    public CompilationUnitContext() {
        super(null, null, null);
        semanticErrors = new ArrayList<>();
        compilationUnitContext = this;
    }
}


class ClassContext extends Context {

    private SPAST definition;


    public ClassContext(SPAST definition, Context surrounding) {
        super(surrounding, null, surrounding.compilationUnitContext());
        classContext = this;
        this.definition = definition;
    }

    public SPAST definition() {
        return definition;
    }

}


class LocalContext extends Context {

    protected int offset;

    public LocalContext(Context surrounding) {
        super(surrounding, surrounding.classContext(), surrounding
                .compilationUnitContext());
        offset = (surrounding instanceof LocalContext) ? ((LocalContext) surrounding)
                .offset()
                : 0;
    }

    public int offset() {
        return offset;
    }

    public int nextOffset() {
        return offset++;
    }
}


class MethodContext extends LocalContext {

    private boolean isStatic;

    private Type methodReturnType;

    private boolean hasReturnStatement = false;

    public MethodContext(Context surrounding, boolean isStatic,
                         Type methodReturnType) {
        super(surrounding);
        this.isStatic = isStatic;
        this.methodReturnType = methodReturnType;
        offset = 0;
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void confirmMethodHasReturn() {
        hasReturnStatement = true;
    }

    public boolean methodHasReturn() {
        return hasReturnStatement;
    }

    public Type methodReturnType() {
        return methodReturnType;
    }
}


