import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);
        while (true) {
            System.out.print("> ");
            String line = reader.readLine();
            if (line.isBlank()) break;
            run(line);
            Error.hadError = false;
        }
    }

    public static void run(String source) {
        Lexer lexer = new Lexer(source);
        ArrayList<Token> tokens = lexer.lexTokens();
        if (Error.hadError) {
            return;
        }

        Parser parser = new Parser(tokens);
        Expr expr = parser.parseTokens();

        if (Error.hadError) {
            return;
        }

        Evaluator evaluator = new Evaluator();
        System.out.printf("%.10f\n", evaluator.evaluate(expr));
    }
}