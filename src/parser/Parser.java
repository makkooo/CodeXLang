package parser;

import java.util.ArrayList;
import java.util.List;

import lexer.Token;
import lexer.TokenType;
import ast.Statement;
import ast.Statement.Declaration;
import ast.Statement.DeclarationList;
import ast.Statement.StatementList;
import ast.Expression;
import ast.DataType;

public class Parser {
	
	private final List<Token> tokenList;
	private int current = 0;

	public Parser(List<Token> tokenList) {
		
	    this.tokenList = tokenList;
	}
	
	
	@SuppressWarnings("serial")
	private static class ParseError extends CodeXRuntimeException {}
	
	
	private ParseError error(Token token, String message) {
		CodeXRuntimeException.error(previousToken(), message);
		return new ParseError();
	}
	
	
	private Token getNextToken() {
		
		if(!isAtLastToken())
			current++;
		return previousToken();
	}
	
	
	private Token currentToken() {
		
		return tokenList.get(current);
	}
	
	
	private Token previousToken() {
		
		return tokenList.get(current - 1);
	}
	
	
	private Token consume(TokenType type, String message) {
		
		if(isAcceptableToken(type))
			return getNextToken();
		throw error(currentToken(), message);
	}
	
	
	private boolean isAtLastToken() {
		
		return currentToken().type == TokenType.EOF;
	}
	
	
	private boolean hasMatchingToken(TokenType... type) {
		
		for(TokenType tokType : type) {
			if(isAcceptableToken(tokType)) {
				getNextToken();
				return true;
			}
		}
		return false;
	}
	
	
	private boolean isAcceptableToken(TokenType type) {
		
		if(isAtLastToken())
			return false;
		return currentToken().type == type;
	}
	
	/*
	public void parseProgram() {
		try {
			parseStatementList(); 	
		} catch (ParseError e) {
			moveToNextStmt();
		}
	}*/
	
	
	public List<Statement> parseStatementList() {
		
		List<Statement> statementList = new ArrayList<>();
		
		//StatementList stmtList = new StatementList();
		while(!isAtLastToken()) {
			if(!isStatement())
				moveToNextStmt();
			else
				statementList.add(parseStatement());
		}
		return statementList;
	}
	
	
	public Statement parseStatement() {
		
		if(hasMatchingToken(TokenType.INT, TokenType.FP, TokenType.CHAR, TokenType.BOOL, TokenType.STR)) 
			return parseDeclarationList();
			
		if(hasMatchingToken(TokenType.IF)) 
			return parseIfStatement();
	
		if(hasMatchingToken(TokenType.WHILE)) 
			return parseWhileStatement();
			
		if(hasMatchingToken(TokenType.XOUT)) 
			return parsePrintStatement();
			
		if(hasMatchingToken(TokenType.XIN)) 
			return parseInputStatement();
			
		if(hasMatchingToken(TokenType.LBRACE)) 
			return parseBlockStatement();
			
		else
			return parseExpressionStmt();
	}
	
	
	private Statement parseDeclarationList() {
		
		Statement.DeclarationList decList = new DeclarationList();
		Declaration declaration = parseDeclaration();
		decList.addElement(declaration);
		
		while(hasMatchingToken(TokenType.COMMA)) {
			Declaration newDeclaration = new Declaration(declaration.getType(), parseInitializer());
			decList.addElement(newDeclaration);
		}
		consume(TokenType.SEMI, "Expected ';'");
		return decList;
	}
	
	
	private Declaration parseDeclaration() {
		
		return new Statement.Declaration(parseDataType(), parseInitializer());
	}
	
	
	private DataType parseDataType() {
		
		switch(previousToken().type) {
			case INT: return new DataType.IntDataType();
			case FP:  return new DataType.FloatDataType();
			case BOOL:return new DataType.BooleanDataType();
			case CHAR:return new DataType.CharDataType();
			case STR: return new DataType.StringDataType();
			default:
				return null;
		}
	}
	
	
	private Expression parseInitializer() {
		
		return parseExpression();
	}
	
	
	private Statement parseExpressionStmt() {
		
		Expression expression = parseExpression();
		consume(TokenType.SEMI, "Expected ';'");
		return new Statement.ExpressionStmt(expression);
	}
	
	
	private Statement parseIfStatement() {
		
		consume(TokenType.LPAREN, "Expected '(' after 'IF'");
		Expression condition = parseExpression();
		consume(TokenType.RPAREN, "Expected ')' after IF statement");
		
		Statement thenStatement = parseStatement();
		Statement elseStatement = null;
		
		if(hasMatchingToken(TokenType.ELSE))
			elseStatement = parseStatement();
		
		return new Statement.IfStatement(condition, thenStatement, elseStatement);
	}
	
	
	private Statement parseWhileStatement() {
		
		consume(TokenType.LPAREN, "Expected '(' after 'WHILE'");
		Expression condition = parseExpression();
		consume(TokenType.RPAREN, "Expected ')' after WHILE statement");
		
		Statement todoStatement = parseStatement();
		
		return new Statement.WhileStatement(condition, todoStatement);
	}
	
	
	private Statement parsePrintStatement() {

		consume(TokenType.LPAREN, "Expected '(' after 'XOUT'");
		Expression expression = parseExpression();
		consume(TokenType.RPAREN, "Expected ')' after XOUT statement");
		
		consume(TokenType.SEMI, "Expected ';'");
		return new Statement.PrintStatement(expression);
	}
	
	
	private Statement parseInputStatement() {
		
		consume(TokenType.LPAREN, "Expected '(' after 'XIN'");
		Expression expression = parsePrimary();
		consume(TokenType.RPAREN, "Expected ')' after XIN statement");
		
		consume(TokenType.SEMI, "Expected ';'");
		return new Statement.InputStatement(expression);
	}
	
	
	private StatementList parseBlockStatement() {
		
		StatementList stmtList = new StatementList();
		
		while(!isAcceptableToken(TokenType.RBRACE) && !isAtLastToken())
			stmtList.addElement(parseStatement());
		
		consume(TokenType.RBRACE, "Expected '}' after statement block");
		return stmtList;
	}
	
	
	public Expression parseExpression() {

		return parseAssignment();
	}
	
	
	private Expression parseAssignment() {
		
		Expression expression = parseLogOr();
		
		if(hasMatchingToken(TokenType.EQUAL)) {
			Token operand = previousToken();
			Expression rhs = parseExpression();
			new Expression.Assignment(expression, operand, rhs);
		}
		return expression;
	}
	

	private Expression parseLogOr() {
		
		Expression expression = parseLogAnd();
		
		if(hasMatchingToken(TokenType.OR)) {
			Token operand = previousToken();
			Expression rhs = parseLogAnd();
			return new Expression.LogOr(expression, operand, rhs);
		}
		return expression;
	}
	
	
	private Expression parseLogAnd() {
		
		Expression expression = parseEquality();
		
		if(hasMatchingToken(TokenType.AND)) {
			Token operand = previousToken();
			Expression rhs = parseEquality();
			return new Expression.LogOr(expression, operand, rhs);
		}
		return expression;
	}
	
	
	private Expression parseEquality() {
		
		Expression expression = parseRelational();
		
		if(hasMatchingToken(TokenType.EQUALTO, TokenType.NEQUALTO)) {
			Token operand = previousToken();
			Expression rhs = parseRelational();
			return new Expression.LogOr(expression, operand, rhs);
		}
		return expression;
	}
	
	
	private Expression parseRelational() {
		
		Expression expression = parseAdditive();
		
		while(hasMatchingToken(TokenType.GREATER, TokenType.GREATEREQ, TokenType.LESS, TokenType.LESSEQ)) {
			Token operand = previousToken();
			Expression rhs = parseAdditive();
			return new Expression.Relational(expression, operand, rhs);
		}
		return expression;
	}
	
	
	private Expression parseAdditive() {
		
		Expression expression = parseMultiplicative();
		
		while(hasMatchingToken(TokenType.PLUS, TokenType.MINUS)) {
			Token operand = previousToken();
			Expression rhs = parseMultiplicative();
			return new Expression.Additive(expression, operand, rhs);
		}
		return expression;
	}
	
	
	private Expression parseMultiplicative() {
		
		Expression expression = parseUnary();
		
		while(hasMatchingToken(TokenType.MULTI, TokenType.DIV)) {
			Token operand = previousToken();
			Expression rhs = parseUnary();
			return new Expression.Multiplicative(expression, operand, rhs);
		}
		return expression;
	}
	
	
	private Expression parseUnary() {
		
		if(hasMatchingToken(TokenType.NOT)) {
			Token operand = previousToken();
			Expression rhs = parseUnary();
			return new Expression.Unary(operand, rhs);
		}
		return parsePrimary();
	}
	
	
	private Expression parsePrimary() {
		
		if(hasMatchingToken(TokenType.CONST_INT, TokenType.CONST_FLOAT, TokenType.CONST_CHAR,
							TokenType.CONST_BOOL, TokenType.CONST_STR)) {
			return new Expression.Constant(previousToken().value);
		}
		
		if(hasMatchingToken(TokenType.IDENT)) {
			return new Expression.Identifier(previousToken());
		}
			
		
		if(hasMatchingToken(TokenType.LPAREN)) {
			Expression expression = parseExpression();
			consume(TokenType.RPAREN, "Expect ')' after expression");
			return new Expression.Grouped(expression);
		}
		
		throw error(currentToken(), "Unexpected token " + currentToken().value);
	}
	
	private boolean isStatement() {
		switch(currentToken().type){
			case IDENT:
			case SEMI :
			case IF :
			case WHILE :
			case XOUT:
			case XIN:
			case LPAREN:
			case LBRACE:
			case INT:
			case FP:
			case CHAR:
			case BOOL:
			case STR:
				return true;
			default:
				return false;
		}
	}
	
	
	private void moveToNextStmt() {
		
		getNextToken();

		while(!isAtLastToken()) {
			if(previousToken().type == TokenType.SEMI) return;

			switch(currentToken().type) {
				case IF:
				case WHILE:
				case XOUT:
				case XIN:
				case INT:
				case FP:
				case CHAR:
				case BOOL:
				case STR:
					return;
				default:
					break;
			}
			getNextToken();
		}  	
	}
}