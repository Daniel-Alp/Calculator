public class PrettyPrint implements Expr.Visitor<String> {
    public String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitUnaryExpr(Expr.Unary unary) {
        return String.format("(%s %s)", unary.op.lexeme, print(unary.right));
    }

    @Override
    public String visitBinaryExpr(Expr.Binary binary) {
        return String.format("(%s %s %s)", print(binary.left), binary.op.lexeme, print(binary.right));
    }

    @Override
    public String visitLiteralExpr(Expr.Literal literal) {
        return String.valueOf(literal.value);
    }
}
