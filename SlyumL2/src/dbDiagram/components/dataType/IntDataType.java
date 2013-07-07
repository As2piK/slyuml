package dbDiagram.components.dataType;

public class IntDataType extends AbstractDataType {

	public static final String name = "INT";
	
	private int defaultValue;
	
	public Integer getDefaultValue() {
		return defaultValue;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int getSize() {
		return 11;
	}

	@Override
	public void setSize(int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDefaultValue(Object value) {
		this.defaultValue = defaultValue;
		
	}
	
}
