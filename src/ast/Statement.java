package ast;

import java.util.ArrayList;
import java.util.List;

public abstract class Statement {
	
	
	public interface Visitor<R> {
		R visitPrintStmt(PrintStatement statement);
		R visitExpressionStmt(ExpressionStmt statement);
		R visitDeclaration(Declaration statement);
	}
	
	public abstract <R> R accept(Visitor<R> visitor);
	
	public static class Declaration extends Statement {
		
		public DataType type;
		public Expression initialization;
		
		public Declaration(DataType type, Expression initialization) {
			this.type = type;
			this.initialization = initialization;
		}
		
		public DataType getType() {
			return type;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			
			return visitor.visitDeclaration(this);
		}
	}
	
	
	public static class PrintStatement extends Statement {
		
		public Expression expression;
		
		public PrintStatement(Expression expression) {
			this.expression = expression;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {

			return visitor.visitPrintStmt(this);
		}
	}
	
	
	public static class InputStatement extends Statement {
		
		public Expression expression;
		
		public InputStatement(Expression expression) {
			this.expression = expression;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	
	public static class IfStatement extends Statement {
		
		public Expression condition;
		public  Statement thenStatement, elseStatement;
		
		public IfStatement(Expression condition, Statement thenStatement, Statement elseStatement) {
			this.condition = condition;
			this.thenStatement = thenStatement;
			this.elseStatement = elseStatement;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	
	public static class WhileStatement extends Statement {
		
		public Expression condition;
		public Statement todoStatement;
		
		public WhileStatement(Expression condition, Statement todoStatement) {
			this.condition = condition;
			this.todoStatement = todoStatement;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	
	public static class ExpressionStmt extends Statement {
		
		public Expression expression;
		
		public ExpressionStmt(Expression expression) {
			this.expression = expression;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {

			return visitor.visitExpressionStmt(this);
		}
	}
	
	
	public static class BlockStatement extends Statement {
		
		public StatementList statementList;
		
		public BlockStatement(StatementList statementList) {
			this.statementList = statementList;
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	
	public static class StatementList extends Statement {
		
		public List<Statement> stmtList;
		
		public StatementList() {
			stmtList = new ArrayList<Statement>();
		}
		
		public List<Statement> getList() {
			return stmtList;
		}
		
		public void addElement(Statement statement) {
			getList().add(statement);
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	
	public static class DeclarationList extends Statement {
		
		public List<Declaration> decList;
		
		public DeclarationList() {
			decList = new ArrayList<Declaration>();
		}
		
		public List<Declaration> getList() {
			return decList;
		}
		
		public void addElement(Declaration declaration) {
			getList().add(declaration);
		}

		@Override
		public <R> R accept(Visitor<R> visitor) {
			// TODO Auto-generated method stub
			return null;
		}
	}
}