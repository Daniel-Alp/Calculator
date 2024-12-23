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
            Error.hadError = false;
            run(line);
        }
    }

    public static void run(String source) {
        Lexer lexer = new Lexer(source);
        ArrayList<Token> tokens = lexer.lexTokens();

        if (Error.hadError) return;

        tokens.forEach(token -> {
            System.out.printf("%s %s\n",token.type, token.lexeme);
        });
    }
}