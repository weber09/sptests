package algol;

import java.io.ByteArrayInputStream;

/**
 * Created by Gabriel on 04/03/2016.
 */
public class AlgolMain {

    public static void main(String[] args)
    {
        String program = "algoritmo \"soma\" var a, b, c : inteiro inicio a <- 2 b <- 3 c <- a + b fimalgoritmo";

        compile(program.getBytes());
    }

    public static void compile(byte[] codeBytes) {
        AlgolParserTokenManager scanner = new AlgolParserTokenManager(new SimpleCharStream(new ByteArrayInputStream(codeBytes)));

        SPCompilationUnit ast = null;
        AlgolParser parser = new AlgolParser(scanner);

        try {
            ast = parser.compilationUnit();
        }
        catch(ParseException exception)
        {
        }

        ast.preAnalyze();

        ast.analyze(null);

        String outputDir = "D:\\Documentos";

        CLEmitter clEmitter = new CLEmitter(true);
        clEmitter.destinationDir(outputDir);
        ast.codegen(clEmitter);
    }

}
