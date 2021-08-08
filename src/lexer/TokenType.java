package lexer;

public enum TokenType {
		
	//Arithmetic Operators
	PLUS,	MINUS,	
	DIV,	MULTI,	
	MOD,	EXP,
	EQUAL,
		
	//Relational Operators
	GREATER,	GREATEREQ,
	LESS,		LESSEQ,	
	EQUALTO,	NEQUALTO,
		
	//Logical Operators
	AND,	OR,		NOT,
		
	//Separator
	LPAREN,		RPAREN,		
	LBRACE,		RBRACE,		
	SEMI,		COMMA,
		
	//Keywords
	XOUT,	XIN,		
	IF,		ELSE,		
	WHILE,
	
	INT,	FP,
	CHAR,	BOOL,
	STR,
		
	CONST_FLOAT,
	CONST_INT,
	CONST_BOOL,
	CONST_STR,
	CONST_CHAR,
	IDENT,
	EOF,
}