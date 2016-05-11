package algol;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

/**
 * Created by Gabriel on 04/03/2016.
 */
public class AlgolMain {

    private static SimpleCharStream charStream;

    private static AlgolParserTokenManager scanner;

    private static AlgolParser parser;

    private static boolean errorHasOcurred;
    public boolean errorHasOccurred(){
        return errorHasOcurred;
    }

    private static ArrayList<SemanticError> semanticErrors;
    public static ArrayList<SemanticError> getSemanticErrors(){
        return semanticErrors;
    }

    public static void main(String[] args)
    {
        String program = "algoritmo var v : vetor [0..4] de inteiro i, aux, j : inteiro inicio para i de 0 ate 4 faca escreval(\"Digite um numero: \") leia(aux) v[i] <- aux fimpara para i de 0 ate 3 faca para j de i ate 4 faca se v[j] < v[i] entao aux <- v[i] v[i] <- v[j] v[j] <- aux fimse fimpara fimpara para i de 0 ate 4 faca escreval(v[i]) fimpara fimalgoritmo";

        try {
            compile(program.getBytes());
        }
        catch(Exception e){
            }
    }

    public static void compile(byte[] codeBytes) throws ParseException {

        semanticErrors = new ArrayList<>();

        if (charStream == null)
            charStream = new SimpleCharStream(new ByteArrayInputStream(codeBytes));
        else
            charStream.ReInit(new ByteArrayInputStream(codeBytes));

        if (scanner == null)
            scanner = new AlgolParserTokenManager(charStream);
        else
            scanner.ReInit(charStream);

        errorHasOcurred = false;

        SPCompilationUnit ast;
        if (parser == null)
            parser = new AlgolParser(scanner);
        else
            parser.ReInit(scanner);

        try {
            ast = parser.compilationUnit();
        } catch (ParseException exception) {
            System.out.println(exception.getMessage());
            throw exception;
        }

        errorHasOcurred |= ast.errorHasOccurred();
        semanticErrors.clear();
        if(ast.getContext() != null) {
            semanticErrors.addAll(ast.getContext().semanticErrors);
        }

        try {
            ast.preAnalyze();
        }
        catch(Exception exc)
        {
            System.out.println(exc.getMessage());
            throw exc;
        }
        errorHasOcurred |= ast.errorHasOccurred();
        semanticErrors.clear();
        if(ast.getContext() != null) {
            semanticErrors.addAll(ast.getContext().semanticErrors);
        }

        try {
            ast.analyze(null);
        }
        catch(Exception exc1) {
            System.out.println(exc1.getMessage());
            throw exc1;
        }

        errorHasOcurred |= ast.errorHasOccurred();
        semanticErrors.clear();
        if(ast.getContext() != null){
            semanticErrors.addAll(ast.getContext().semanticErrors);
        }

        String outputDir = "C:\\AlgolBytecodes";

        try {
            CLEmitter clEmitter = new CLEmitter(true);
            clEmitter.destinationDir(outputDir);
            ast.codegen(clEmitter);

        }
        catch(Exception exc2){
            System.out.println(exc2.getMessage());
            throw exc2;
        }

        errorHasOcurred |= ast.errorHasOccurred();
        semanticErrors.clear();
        if(ast.getContext() != null) {
            semanticErrors.addAll(ast.getContext().semanticErrors);
        }
    }



}
