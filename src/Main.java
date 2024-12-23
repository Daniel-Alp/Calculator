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
            if (line.isEmpty()) break;
            run(line);
        }
    }

    public static void run(String source) {
        try {
            Lexer lexer = new Lexer(source);
            ArrayList<Token> tokens = lexer.lexTokens();
            Evaluator evaluator = new Evaluator(tokens);
            System.out.println(evaluator.evaluateTokens());
        } catch (Exception error) {
            System.out.printf("Error: %s\n", error.getMessage());
        }
    }
}