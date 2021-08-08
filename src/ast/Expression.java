package ast;

import lexer.Token;

public abstract class Expression {
	
	
	public interface Visitor<R> {
		R visitConstant(Constant expression);
		R visitIdentifier(Identifier expression);
		R visitGrouped(Grouped expression);
		R visitUnary(Unary expression);
		R visitMultiplicative(Multiplicative expression);
		R visitAdditive(Additive expression);
		R visitRelational(Relational expression);
		R visitEquality(Equality expression);
		R visitLogAnd(LogAnd expression);
		R visitLogOr(LogOr expression);
		R visitAssignment(Assignment expression);
	}
	
	
	public abstract <R> R accept(Visitor<R> visitor);
	
	
	public static class Constant extends Expression {
		
		public Object value;
		
		public Constant(Object value) {
			this.value = value;
		}

		@Override	
		public <R> R accept(Visitor<R> visitor) {
			
			return visitor.visitConstant(this);
		}
	}
	
	
	public static class Identifier extends Expression {
		
		public Token token;
		
		public Identifier(Token token) {
			this.token = token;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {

			return visitor.visitIdentifier(this);
		}
	}
	
	
	public static class Grouped extends Expression {
		
		public Expression expression;
		
		public Grouped(Expression expression) {
			this.expression = expression;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {

			return visitor.visitGrouped(this);
		}
	}
	
	
	public static class Unary extends Expression {
		
		public Expression rhs;
		public Token operator;
		
		public Unary(Token operator, Expression rhs) {
			this.operator = operator;
			this.rhs = rhs;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {

			return visitor.visitUnary(this);
		}
	}
	
	
	public static class Multiplicative extends Expression {
		
		public Expression lhs, rhs;
		public Token operator;
	
		public Multiplicative(Expression lhs, Token operator, Expression rhs) {
			this.lhs = lhs;
			this.operator = operator;
			this.rhs = rhs;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {

			return visitor.visitMultiplicative(this);
		}
	}
	
	
	public static class Additive extends Expression {
		
		public Expression lhs, rhs;
		public Token operator;
	
		public Additive(Expression lhs, Token operator, Expression rhs) {
			this.lhs = lhs;
			this.operator = operator;
			this.rhs = rhs;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {

			return visitor.visitAdditive(this);
		}
	}
	
	
	public static class Relational extends Expression {
		
		public Expression lhs, rhs;
		public Token operator;
	
		public Relational(Expression lhs, Token operator, Expression rhs) {
			this.lhs = lhs;
			this.operator = operator;
			this.rhs = rhs;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {

			return visitor.visitRelational(this);
		}
	}
	
	
	public static class Equality extends Expression {
		
		public Expression lhs, rhs;
		public Token operator;
	
		public Equality(Expression lhs, Token operator, Expression rhs) {
			this.lhs = lhs;
			this.operator = operator;
			this.rhs = rhs;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {

			return visitor.visitEquality(this);
		}
	}
	
	
	public static class LogAnd extends Expression {
		
		public Expression lhs;
		public Expression rhs;
		public Token operator;
	
		public LogAnd(Expression lhs, Token operator, Expression rhs) {
			this.lhs = lhs;
			this.operator = operator;
			this.rhs = rhs;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {

			return visitor.visitLogAnd(this);
		}
	}
	
	
	public static class LogOr extends Expression {
		
		public Expression lhs, rhs;
		public Token operator;
	
		public LogOr(Expression lhs, Token operator, Expression rhs) {
			this.lhs = lhs;
			this.operator = operator;
			this.rhs = rhs;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {

			return visitor.visitLogOr(this);
		}
	}

	
	public static class Assignment extends Expression {
			
		public Expression rhs, lhs;
		public Token operator;
		
		public Assignment(Expression lhs, Token operator, Expression rhs) {
			this.lhs = lhs;
			this.operator = operator;
			this.rhs = rhs;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {

			return visitor.visitAssignment(this);
		}
	}
}