import java.util.ArrayList;
import java.util.Map;

public class Evaluator {
    private final ArrayList<Token> tokens;
    private int current = 0;
    private final Map<TokenType, Integer> precedenceMap = Map.ofEntries(
            Map.entry(TokenType.PLUS,  1),
            Map.entry(TokenType.MINUS, 2),
            Map.entry(TokenType.STAR,  3),
            Map.entry(TokenType.SLASH, 4),
            Map.entry(TokenType.CARET, 5)
    );

    Evaluator(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public double evaluateTokens() throws Exception{
        return expr(0);
    }

    private double prefix() throws Exception{
        Token token = eat();
        switch (token.type) {
            case MINUS:
                return -expr(0);
            case LEFT_PAREN:
                double result = expr(0);
                Token right_paren = eat();
                if (right_paren.type != TokenType.RIGHT_PAREN) {
                    throw new Exception(String.format("unclosed parentheses at %d", right_paren.offset));
                }
                return result;
            case NUMBER_LITERAL:
                 return (double)token.literal;
            default:
                if (token.type == TokenType.EOF) {
                    throw new Exception("unterminated expression");
                }
                throw new Exception(String.format("unexpected character '%s' at %d", token.lexeme, token.offset+1));
        }
    }

    //todo handle associativity
    //todo cleanup code
    private double expr(int precedence) throws Exception{
        double left = prefix();
        while (peek().type != TokenType.EOF && peek().type != TokenType.RIGHT_PAREN) {
            Token op = peek();
            int opPrecedence = precedenceMap.get(op.type);
            if (opPrecedence < precedence) {
                break;
            }
            eat();
            double right = expr(opPrecedence);
            left = switch (op.type) {
                case PLUS  -> left + right;
                case MINUS -> left - right;
                case STAR  -> left * right;
                case SLASH -> left / right;
                case CARET -> Math.pow(left, right);
                default -> throw new Exception(String.format("unexpected character '%s' ad %d", op.lexeme, op.offset+1));
            };
        }
        return left;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token eat() {
        if (current >= tokens.size()) return tokens.getLast();
        return tokens.get(current++);
    }
}