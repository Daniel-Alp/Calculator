public class Evaluator implements Expr.Visitor {
    public Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary unary) {
        double right = (double)evaluate(unary.right);
        if (unary.op.type == TokenType.MINUS) {
            return -right;
        }
        return null; // unreachable
    }

    @Override
    public Object visitBinaryExpr(Expr.Binary binary) {
        double left = (double)evaluate(binary.left);
        double right = (double)evaluate(binary.right);
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
