package lexer;

public class Token {
	
	public TokenType type;
	public String lexeme;
	public Object value;
	public int line;
	
	Token(TokenType type, String lexeme, Object value, int line) {
		this.type = type;
		this.lexeme = lexeme;
		this.value = value;
		this.line = line;
	}
}