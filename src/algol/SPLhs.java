package algol;

/**
 * Created by Gabriel on 08/03/2016.
 */
interface SPLhs {

    SPExpression analyzeLhs(Context context);

    void codegenLoadLhsLvalue(CLEmitter output);

    void codegenLoadLhsRvalue(CLEmitter output);


    void codegenDuplicateRvalue(CLEmitter output);

    void codegenStore(CLEmitter output);

}

