package dbDiagram.components.dataType;

public class VarcharDataType extends AbstractDataType {

	public static final String name = "VARCHAR";
	
	private int size;
	
	private String defaultValue = "";
	
	@Override
	public String getName() {
		return name;
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}
	
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = (String)defaultValue;
	}

	@Override
	public String toString() {
		return name;
	}
}
