package parser;

import lexer.Token;
import lexer.TokenType;

@SuppressWarnings("serial")
public class CodeXRuntimeException extends RuntimeException {
	
	
	public static void error(Token token, String message) {
		if (token.type == TokenType.EOF) {
			report(token.line, " at end", message);
		} 
		else {
			report(token.line, " at '" + token.lexeme + "'", message);
		}
	}
	
	
	public static void error(int line, String value, String message) {
	    report(line, value, message);
	  }
	
	
	public static void report(int line, String where, String message) {
		System.err.println("[line " + line + "] Error" + where + ": " + message);
	}
}