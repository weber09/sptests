package algol;

/**
 * Created by Gabriel on 07/03/2016.
 */
interface SPTypeDecl {

    void declareThisType(Context context);

    void preAnalyze(Context context);

    String name();

    Type superType();

    Type thisType();

}
