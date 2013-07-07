package dbDiagram.components;

import dbDiagram.components.dataType.AbstractDataType;
import dbDiagram.relationships.Binary;

public class ForeignKey  extends Field {

	private String fkName;
	private Binary binary;
	

	public ForeignKey(String name, String fkName, AbstractDataType type, Binary binary)
	{	super(name, type);
		this.fkName = fkName;
		this.binary = binary;
	}
	
	public ForeignKey(Field f, String fkName, Binary binary) {
		super(f.getName(), f.getType());
		this.fkName = fkName;
		this.binary = binary;
	}
	
	public String getFkName() {
		return fkName;
	}
	
	public void setFkName(String fkName) {
		this.fkName = fkName;
	}
	
	public Binary getBinary() {
		return binary;
	}
	
	public void setBinary(Binary binary) {
		this.binary = binary;
	}
	
}