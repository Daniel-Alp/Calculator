public abstract class Expr {
    interface Visitor<T> {
        T visitUnaryExpr(Unary unary);
        T visitBinaryExpr(Binary binary);
        T visitLiteralExpr(Literal literal);
    }

    public abstract <T> T accept(Visitor<T> visitor);

    public static class Unary extends Expr {
        final Token op;
        final Expr right;

        Unary(Token op, Expr right) {
            this.op = op;
            this.right = right;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitUnaryExpr(this);
        }
    }

    public static class Binary extends Expr {
        final Expr left;
        final Token op;
        final Expr right;

        Binary(Expr left, Token op, Expr right) {
            this.left = left;
            this.op = op;
            this.right = right;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitBinaryExpr(this);
        }
    }

    public static class Literal extends Expr {
        final Object value;

        Literal(Object value) {
            this.value = value;
        }

        @Override
        public <T> T accept(Visitor<T> visitor) {
            return visitor.visitLiteralExpr(this);
        }
    }
}