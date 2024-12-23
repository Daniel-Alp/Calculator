import java.util.ArrayList;

public class Lexer {
    private final String source;
    private int current = 0;
    private int start = 0;
    private final ArrayList<Token> tokens = new ArrayList<>();

    Lexer(String source) {
        this.source = source;
    }

    public ArrayList<Token> lexTokens() {
        while (!isAtEnd()) {
            start = current;
            char c = eat();
            switch (c) {
                case '+': addToken(TokenType.PLUS); break;
                case '-': addToken(TokenType.MINUS); break;
                case '*': addToken(TokenType.STAR); break;
                case '/': addToken(TokenType.SLASH); break;
                case '^': addToken(TokenType.CARET); break;
                case '(': addToken(TokenType.LEFT_PAREN); break;
                case ')': addToken(TokenType.RIGHT_PAREN); break;
                case ' ':
                case '\t':
                    break;
                default:
                    if (Character.isDigit(c)) {
                        number();
                    } else {
                        Error.report(String.format("unexpected character '%s'", c), start+1);
                    }
            }
        }
        tokens.add(new Token(TokenType.EOF, null, "", source.length()));

        return tokens;
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) {
            return '\0';
        }
        return source.charAt(current+1);
    }

    private char eat() {
        return source.charAt(current++);
    }

    private void addToken(TokenType tokenType) {
        addToken(tokenType, null);
    }

    private void addToken(TokenType tokenType, Object literal) {
        tokens.add(new Token(tokenType, literal, source.substring(start, current), current));
    }

    void number() {
        while (Character.isDigit(peek())) {
            eat();
        }
        if (peek() == '.' && Character.isDigit(peekNext())) {
            eat();
            while (Character.isDigit(peek())){
                eat();
            }
        }
        String digits = source.substring(start, current);
        addToken(TokenType.NUMBER_LITERAL, Double.parseDouble(digits));
    }
}
