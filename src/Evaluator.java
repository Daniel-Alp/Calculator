public class Evaluator implements Expr.Visitor<Object> {
    public double evaluate(Expr expr) {
        return (double)expr.accept(this);
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary unary) {
        double right = evaluate(unary.right);
        if (unary.op.type == TokenType.MINUS) {
            return -right;
        }
        return null; // unreachable
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary binary) {
        double left = evaluate(binary.left);
        double right = evaluate(binary.right);
        return switch (binary.op.type) {
            case PLUS  -> left + right;
            case MINUS -> left - right;
            case STAR  -> left * right;
            case SLASH -> left / right;
            case CARET -> Math.pow(left, right);
            default -> null; // unreachable
        };
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal literal) {
        return literal.value;
    }
}
