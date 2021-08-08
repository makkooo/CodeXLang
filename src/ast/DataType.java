package ast;

public abstract class DataType {

	public interface Visitor<R> {
		R visitIntDataType(IntDataType dataType);
		R visitFloatDataType(FloatDataType dataType);
		R visitBooleanDataType(BooleanDataType dataType);
		R visitCharDataType(CharDataType dataType);
		R visitStringDataType(StringDataType dataType);
	}
	
	public abstract <R> R accept(Visitor<R> visitor);
	
	
	public static class IntDataType extends DataType {

		@Override
		public <R> R accept(Visitor<R> visitor) {

			return visitor.visitIntDataType(this);
		}
	}
	
	
	public static class FloatDataType extends DataType {

		@Override
		public <R> R accept(Visitor<R> visitor) {

			return visitor.visitFloatDataType(this);
		}
	}
	
	
	public static class BooleanDataType extends DataType {

		@Override
		public <R> R accept(Visitor<R> visitor) {

			return visitor.visitBooleanDataType(this);
		}
	}
	
	
	public static class CharDataType extends DataType {

		@Override
		public <R> R accept(Visitor<R> visitor) {

			return visitor.visitCharDataType(this);
		}
	}
	
	
	public static class StringDataType extends DataType {

		@Override
		public <R> R accept(Visitor<R> visitor) {

			return visitor.visitStringDataType(this);
		}
		
	}
}