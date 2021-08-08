package interpreter;

import ast.Statement;
import ast.Statement.Declaration;
import ast.Statement.ExpressionStmt;
import ast.Statement.PrintStatement;

import java.util.List;

import ast.Expression;
import ast.Expression.Additive;
import ast.Expression.Assignment;
import ast.Expression.Constant;
import ast.Expression.Equality;
import ast.Expression.Grouped;
import ast.Expression.Identifier;
import ast.Expression.LogAnd;
import ast.Expression.LogOr;
import ast.Expression.Multiplicative;
import ast.Expression.Relational;
import ast.Expression.Unary;
import lexer.TokenType;

public class Interpreter implements Expression.Visitor<Object>, Statement.Visitor<Object> {
	
	
	@Override
	public Object visitDeclaration(Declaration statement) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public Object visitExpressionStmt(ExpressionStmt statement) {

		evaluate(statement.expression);
		return null;
	}
	
	
	@Override
	public Object visitPrintStmt(PrintStatement statement) {

		Object expression = evaluate(statement.expression);
		System.out.println(toString(expression));
		return null;
	}
	
	
	@Override
	public Object visitConstant(Constant expression) {
		
		return expression.value;
	}

	//TODO
	@Override
	public Object visitIdentifier(Identifier expression) {

		return null;
	}

	
	@Override
	public Object visitGrouped(Grouped expression) {
		
		return evaluate(expression.expression);
	}

	@Override
	public Object visitUnary(Unary expression) {
		
		Object rhs = evaluate(expression.rhs);

		switch(expression.operator.type) {
			case NOT:
				return !isTrue(rhs);
			default:
				break;
		}
		return null;
	}


	@Override
	public Object visitMultiplicative(Multiplicative expression) {
		
		Object lhs = evaluate(expression.lhs);
		Object rhs = evaluate(expression.rhs);
		
		switch(expression.operator.type) {
			case MULTI:
				if(lhs instanceof Integer && rhs instanceof Integer)
					return (int)lhs * (int)rhs;
				if(lhs instanceof Float && rhs instanceof Float)
					return (float)lhs * (float)rhs;
				break;
			case DIV:
				if(lhs instanceof Integer && rhs instanceof Integer)
					return (int)lhs / (int)rhs;
				if(lhs instanceof Float && rhs instanceof Float)
					return (float)lhs / (float)rhs;
				break;
			case MOD:
				if(lhs instanceof Integer && rhs instanceof Integer)
					return (int)lhs % (int)rhs;
				if(lhs instanceof Float && rhs instanceof Float)
					return (float)lhs % (float)rhs;
				break;
			default:
				break;
		}
		return null;
	}
	

	@Override
	public Object visitAdditive(Additive expression) {
		
		Object lhs = evaluate(expression.lhs);
		Object rhs = evaluate(expression.rhs);
		
		switch(expression.operator.type) {
			case PLUS:
				if(lhs instanceof Integer && rhs instanceof Integer)
					return (int)lhs + (int)rhs;
				if(lhs instanceof Float && rhs instanceof Float)
					return (float)lhs + (float)rhs;
				break;
			case MINUS:
				if(lhs instanceof Integer && rhs instanceof Integer)
					return (int)lhs - (int)rhs;
				if(lhs instanceof Float && rhs instanceof Float)
					return (float)lhs - (float)rhs;
				break;
			default:
				break;
		}
		return null;
	}
	

	@Override
	public Object visitRelational(Relational expression) {
		
		Object lhs = evaluate(expression.lhs);
		Object rhs = evaluate(expression.rhs);
		
		switch(expression.operator.type) {
			case GREATER:
				if(lhs instanceof Integer && rhs instanceof Integer)
					return (int)lhs > (int)rhs;
				if(lhs instanceof Float && rhs instanceof Float)
					return (float)lhs > (float)rhs;
				break;
			case GREATEREQ:
				if(lhs instanceof Integer && rhs instanceof Integer)
					return (int)lhs >= (int)rhs;
				if(lhs instanceof Float && rhs instanceof Float)
					return (float)lhs >= (float)rhs;
				break;
			case LESS:
				if(lhs instanceof Integer && rhs instanceof Integer)
					return (int)lhs < (int)rhs;
				if(lhs instanceof Float && rhs instanceof Float)
					return (float)lhs < (float)rhs;
				break;
			case LESSEQ:
				if(lhs instanceof Integer && rhs instanceof Integer)
					return (int)lhs <= (int)rhs;
				if(lhs instanceof Float && rhs instanceof Float)
					return (float)lhs <= (float)rhs;
			default:
				break;
		}
		return null;
	}
	

	@Override
	public Object visitEquality(Equality expression) {
		
		Object lhs = evaluate(expression.lhs);
		Object rhs = evaluate(expression.rhs);
		
		switch(expression.operator.type) {
			case EQUALTO: return !isEqual(lhs, rhs);
			case NEQUALTO: return isEqual(lhs, rhs);
			default:
				break;
		}
		return null;
	}


	@Override
	public Object visitLogAnd(LogAnd expression) {
		
		Object lhs = evaluate(expression.lhs);
		
		if(expression.operator.type == TokenType.AND)
			if(!isTrue(lhs)) return lhs;
		
		return evaluate(expression.rhs);
	}

	@Override
	public Object visitLogOr(LogOr expression) {
		
		Object lhs = evaluate(expression.lhs);
		
		if(expression.operator.type == TokenType.OR)
			if(isTrue(lhs)) return lhs;
		
		return evaluate(expression.rhs);
	}

	@Override
	public Object visitAssignment(Assignment expression) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	private Object evaluate(Expression expression) {
		
		return expression.accept(this);
	}
	
	
	private boolean isTrue(Object object) {
		
	    if (object == null) return false;
	    if (object instanceof Boolean) return (boolean)object;
	    return true;
	}
	
	
	private boolean isEqual(Object lhs, Object rhs) {
		
		if (lhs == null && rhs == null) return true;
	    if (lhs == null) return false;

	    return lhs.equals(rhs);
	}
	
	
	private String toString(Object object) {
		
		if(object == null)
			return "NULL";
		
		if(object instanceof Float) {
		    String text = object.toString();
		    if(text.endsWith(".0")) {
		        text = text.substring(0, text.length() - 1);
		     }
		     return text;
		}
		return object.toString();
	}
	
	
	public void interpret(List<Statement> stmtList) {
		
		//List<Statement> stmtList = new StatementList();
		
		for(Statement statements : stmtList)
			execute(statements);
	}
	
	private void execute(Statement statement) {
	    statement.accept(this);
	  }
}