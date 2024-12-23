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

    Parser(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public Expr parseTokens() {
        return expr(0);
    }

    private Expr prefix() {
        Token token = eat();
        switch (token.type) {
            case MINUS:
                return new Expr.Unary(token, expr(1));
            case LEFT_PAREN:
                Expr expr = expr(0);
                // unwind after first error
                if (Error.hadError) {
                    return null;
                }
                if (eat().type != TokenType.RIGHT_PAREN) {
                    Error.report("unclosed parentheses", peek().offset);
                    return null;
                }
                return expr;
            case NUMBER_LITERAL:
                return new Expr.Literal(token.literal);
            default:
                if (token.type == TokenType.EOF) {
                    Error.report("unterminated expression", token.offset);
                } else {
                    Error.report(String.format("unexpected character '%s'", token.lexeme), token.offset);
                }
                return null;
        }
    }

    private Expr expr(int precedence) {
        Expr left = prefix();
        while (peek().type != TokenType.EOF && peek().type != TokenType.RIGHT_PAREN) {
            // unwind after first error
            if (Error.hadError) {
                return null;
            }
            Token op = peek();
            if (!precedenceMap.containsKey(op.type)) {
                Error.report(String.format("unexpected character '%s'", op.lexeme), op.offset);
                return null;
            }
            int opPrecedence = precedenceMap.get(op.type);
            if (opPrecedence < precedence) {
                break;
            }
            eat();
            Expr right = expr(opPrecedence);
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
}