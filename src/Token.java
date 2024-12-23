public class Token {
    public TokenType type;
    public Object literal;
    public String lexeme;
    public int offset;

    Token(TokenType type, Object literal, String lexeme, int offset) {
        this.type = type;
        this.literal = literal;
        this.lexeme = lexeme;
        this.offset = offset;
    }
}
