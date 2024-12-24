import java.util.ArrayList;
import java.util.Map;

public class Parser {
    private final ArrayList<Token> tokens;
    private int current = 0;
    private final Map<TokenType, Integer> precedenceMap = Map.ofEntries(
            Map.entry(TokenType.PLUS,  1),
            Map.entry(TokenType.MINUS, 1),
            Map.entry(TokenType.STAR,  2),
            Map.entry(TokenType.SLASH, 2),
            Map.entry(TokenType.CARET, 3)
    );
    private final Map<TokenType, Boolean> leftAssocMap = Map.ofEntries(
            Map.entry(TokenType.PLUS,  true),
            Map.entry(TokenType.MINUS, true),
            Map.entry(TokenType.STAR,  true),
            Map.entry(TokenType.SLASH, true),
            Map.entry(TokenType.CARET, false)
    );

    Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public Expr parseTokens() {
        try {
            Expr result = expr(0);
            if (peek().type != TokenType.EOF) {
                Error.report(String.format("unexpected character '%s'", peek().lexeme), peek().offset);
            }
            return result;
        } catch (RuntimeException error) {
            return null;
        }
    }

    private Expr prefix() {
        Token token = eat();
        switch (token.type) {
            case MINUS:
                return new Expr.Unary(token, expr(1));
            case LEFT_PAREN:
                Expr expr = expr(0);
                if (eat().type != TokenType.RIGHT_PAREN) {
                    Error.report("unmatched parentheses", token.offset);
                    throw new RuntimeException();
                }
                return expr;
            case NUMBER_LITERAL:
                return new Expr.Literal(token.literal);
            default:
                Error.report(String.format("unexpected character '%s'", token.lexeme), token.offset);
                throw new RuntimeException();
        }
    }

    private Expr expr(int precedence) {
        Expr left = prefix();
        while (matchOperator() && precedenceMap.get(peek().type) >= precedence) {
            Token op = eat();
            int newPrecedence = precedenceMap.get(op.type);
            if (leftAssocMap.get(op.type)) {
                newPrecedence++;
            }
            Expr right = expr(newPrecedence);
            left = new Expr.Binary(left, op, right);
        }
        return left;
    }

    private boolean isAtEnd() {
        return current >= tokens.size();
    }

    private Token peek() {
        if (current + 1 >= tokens.size()) {
            return tokens.getLast();
        }
        return tokens.get(current);
    }

    private Token eat() {
        if (isAtEnd()) {
            return tokens.getLast();
        }
        return tokens.get(current++);
    }

    private boolean matchOperator() {
        return switch (peek().type) {
            case PLUS, MINUS, STAR, SLASH, CARET -> true;
            default -> false;
        };
    }
}