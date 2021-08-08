package lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import parser.CodeXRuntimeException;

public class Lexer {
	
	private int pos, line;
	private char ch;
	private String input;
	
	public Lexer(String sourceCode) {

		this.pos = 0;
		this.line = 1;
		this.input = sourceCode;
		this.ch = this.input.charAt(0);
	}
	
	@SuppressWarnings("serial")
	private static class TokenizationError extends CodeXRuntimeException {}
	
	private TokenizationError error(int line, String value, String message) {
		CodeXRuntimeException.error(line, value, message);
		return new TokenizationError();
	}
	
	
	private Map<String, TokenType> getKeyword(){
		
		Map<String, TokenType> keyword = new HashMap<>();
		
		keyword.put("XOUT", TokenType.XOUT);
		keyword.put("XIN", TokenType.XIN);
		keyword.put("IF", TokenType.IF);
		keyword.put("ELSE", TokenType.ELSE);
		keyword.put("WHILE", TokenType.WHILE);
		keyword.put("INT", TokenType.INT);
		keyword.put("FP", TokenType.FP);
		keyword.put("CHAR", TokenType.CHAR);
		keyword.put("STR", TokenType.STR);
		keyword.put("BOOL", TokenType.BOOL);
		
		return keyword;
	}
	
	
	private char getNextChar() {
		
		this.pos++;
		
		if(this.pos >= this.input.length()) {
			this.ch = '\u0000';
			return this.ch;
		}
		
		if(this.ch == '\n') {
			this.line++;
		}
		
		this.ch = this.input.charAt(this.pos); 
		return this.ch;
	}
	
	
	private boolean isAcceptableChar(char ch) {
		
		if(Character.isLetterOrDigit(this.ch) || 
			this.ch == '.' ||  this.ch == '_' || 
			hasInvalidChar(String.valueOf(this.ch)))
				return true;
		return false;
	}
	
	
	private boolean hasInvalidChar(String string) {
		
		String invalidChar = "`$@[]\\:#.";
		for(int i=0; i < string.length(); i++) {
			  char currentChar = string.charAt(i);
			  if(invalidChar.contains(Character.toString(currentChar))) {
				  return true;
			  }
		 }
		return false;
	}
	
	
	private boolean hasIdentDuplicate(String value, List<String> identList) {
		
		for(String ident : identList) {
			if(value.equalsIgnoreCase(ident)) {
				return true;
			}
		}
		return false;
	}

	
	private void lineComment() {
		
		while(this.ch != '\n' && this.ch != '\u0000') {
			getNextChar();
		}
	}
	
	
	private void blockComment() {
		
		int line = this.line;
		getNextChar();
			
		while(this.ch != '?') {	
			getNextChar();
			if(this.ch == '\u0000')
				throw error(line, "","Unexpected end of comment at " + line);
		}
	}		
	

	private Token charLit() {
		
		getNextChar();
		char value = 0;
		if(this.ch >= 32 && this.ch <= 126) {
			value = this.ch;
			if(getNextChar() == '\'') 
				return new Token(TokenType.CONST_CHAR, String.valueOf(value), value, this.line);
		}
		throw error(this.line, " at '" + value + "'", " Invalid character constant");
	}

	
	private Token stringLit() {
		
		String value = "";
		while(getNextChar() != '"') {
			if(this.ch == '\u0000' || this.ch == '\n')
				throw error(this.line, " at '" + value + "'", " Unclosed string literal");
			value += this.ch;
		}
		return new Token(TokenType.CONST_STR, value, value, this.line);
	} 
	

	private Token ident(List<String> identList) {

		String value = "";
		long decimalPoint = 0;
		boolean isNum = true;
		
		while(isAcceptableChar(this.ch)) {
			value += this.ch;
				if(!Character.isDigit(this.ch) && this.ch != '.')
					isNum = false;
			getNextChar();
		}

		decimalPoint = value.chars().filter(ch -> ch == '.').count();
		
		if(isNum) {
			if(decimalPoint == 0)
				return new Token(TokenType.CONST_INT, value, Integer.parseInt(value), this.line);
			else if(decimalPoint == 1) 
				return new Token(TokenType.CONST_FLOAT, value, Float.parseFloat(value),this.line);
			else 
				throw error(line, " at '" + value + "'", " Invalid numerical constant");
		}
		
		else if(getKeyword().containsKey(value)) 
			return new Token(getKeyword().get(value), value, null, this.line);
		
		else if(value.equals("true") || value.equals("false"))
			return new Token(TokenType.CONST_BOOL, value, Boolean.parseBoolean(value), this.line);
		
		else if(Character.isLetter(value.charAt(0)) && value.length() <= 20 && !hasInvalidChar(value)) {
			identList.add(value);
			return new Token(TokenType.IDENT, value, value, this.line);
		}
			
		else 
			throw error(line, " at '" + value + "'", " Invalid token");
		
	}
	
	
	private Token succedingChar(char expectedChar, Token ifTrue, Token ifFalse) {
		
		if(getNextChar() == expectedChar) {
			getNextChar();
			return ifTrue;
		}
		return ifFalse;
	}
	
	
	public List<Token> getToken() {
		
		List<Token> tokenList = new ArrayList<Token>();
		List<String> identList = new ArrayList<String>();
		
		while(this.ch != '\u0000') {
			while(Character.isWhitespace(this.ch)) 
				getNextChar();
			
			switch(this.ch) {
				case '\u0000': break;
				case '+': getNextChar(); tokenList.add(new Token(TokenType.PLUS , "+", null, this.line)); break;
				case '-': getNextChar(); tokenList.add(new Token(TokenType.MINUS, "-", null, this.line)); break;
				case '*': getNextChar(); tokenList.add(new Token(TokenType.MULTI, "*", null, this.line)); break;
				case '/': getNextChar(); tokenList.add(new Token(TokenType.DIV, "/", null, this.line)); break;
				case '%': getNextChar(); tokenList.add(new Token(TokenType.MOD, "%", null, this.line)); break;
				case '^': getNextChar(); tokenList.add(new Token(TokenType.EXP, "^", null, this.line)); break;
				case '>': tokenList.add(succedingChar('=', new Token(TokenType.GREATEREQ, ">=", null, this.line), 
														   new Token(TokenType.GREATER, ">", null, this.line))); break;
				case '<': tokenList.add(succedingChar('=', new Token(TokenType.LESSEQ, "<=", null, this.line), 
														   new Token(TokenType.LESS, "<", null, this.line))); break;
				case '=': tokenList.add(succedingChar('=', new Token(TokenType.EQUALTO, "==", null, this.line), 
						                                   new Token(TokenType.EQUAL, "=", null, this.line))); break;
				case '~': tokenList.add(succedingChar('=', new Token(TokenType.NEQUALTO, "~=", null, this.line), 
						                                   new Token(TokenType.NOT, "~", null, this.line))); break;
				case '&': getNextChar(); tokenList.add(new Token(TokenType.AND, "&", null, this.line)); break;
				case '|': getNextChar(); tokenList.add(new Token(TokenType.OR, "|", null, this.line)); break;
				case '(': getNextChar(); tokenList.add(new Token(TokenType.LPAREN, "(", null, this.line)); break;
				case ')': getNextChar(); tokenList.add(new Token(TokenType.RPAREN, ")", null, this.line)); break;
				case '{': getNextChar(); tokenList.add(new Token(TokenType.LBRACE, "{", null, this.line)); break;
				case '}': getNextChar(); tokenList.add(new Token(TokenType.RBRACE, "}", null, this.line)); break;
				case ';': getNextChar(); tokenList.add(new Token(TokenType.SEMI, ";", null, this.line)); break;
				case ',': getNextChar(); tokenList.add(new Token(TokenType.COMMA, ",", null, this.line)); break;
				case '?': blockComment(); getNextChar(); break;
				case '!': lineComment(); getNextChar(); break;
 				case '\'':tokenList.add(charLit()); getNextChar(); break;
				case '"': tokenList.add(stringLit()); getNextChar(); break;
				default : tokenList.add(ident(identList)); break;
			}
		}
		tokenList.add(new Token(TokenType.EOF, "EOF", null, this.line));
		return tokenList;
	}
}