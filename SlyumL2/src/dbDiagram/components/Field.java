package dbDiagram.components;

import utility.Utility;
import change.BufferField;
import change.Change;
import dbDiagram.components.dataType.AbstractDataType;
import dbDiagram.components.dataType.AbstractDataType.DataType;
import dbDiagram.components.dataType.IntDataType;

/**
 * Represent an attribute in UML structure.
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 */
public class Field extends Variable
{	
	private String defaultValue;
	private AbstractDataType type; //TODO
	private boolean isPK;
	private boolean isNullable;

	/**
	 * Construct a new attribute.
	 * 
	 * @param name
	 *            the name of the attribute.
	 * @param type
	 *            the type of the attribute.
	 */
	public Field(String name, AbstractDataType type)
	{
		super(name, type);

		boolean isBlocked = Change.isBlocked();
		Change.setBlocked(true);
		
		setDefaultValue("");
		
		Change.setBlocked(isBlocked);
	}
	
	/**
	 * Constructor of copy.
	 * @param field attribute
	 */
	public Field(Field field)
	{
		super(field.getName(), new IntDataType());
		
		boolean isBlocked = Change.isBlocked();
		Change.setBlocked(true);
		
		name = field.name;
		type = DataType.getNewDataTypeFromObject(field.getType());
		defaultValue = field.defaultValue;
		
		Change.setBlocked(isBlocked);
	}
	
	public void setField(Field field)
	{
		boolean isRecord = Change.isRecord();
		Change.record();
		
		setName(field.getName());
		
		setType(DataType.getNewDataTypeFromObject(field.getType()));
		
		setDefaultValue(field.getDefaultValue());
		
		if(!isRecord)
			Change.stopRecord();
		
		notifyObservers();
	}

	/**
	 * Get the default value of the attribute.
	 * 
	 * @return the default value of the attribute.
	 */
	public String getDefaultValue()
	{
		return defaultValue;
	}

	/**
	 * Set the default value of the attribute.
	 * 
	 * @param defaultValue
	 *            the new default value.
	 */
	public void setDefaultValue(String defaultValue)
	{
		Change.push(new BufferField(this));
		this.defaultValue = defaultValue;
		Change.push(new BufferField(this));
		setChanged();
	}

	/**
	 * Change this attribute according to the text. If the syntax of the text is
	 * incorrect, this method will not make changes.
	 * 
	 * @param text
	 *            the text representing an UML Attribute
	 */
	public void setText(String text)	//TODO Check for adding type in name
	{
		if (text.length() == 0)
			return;

		AbstractDataType type = getType();
		text = text.trim();

		
		boolean isRecord = Change.isRecord();
		Change.record();
		
		setType(type);
		setName(text);
		
		if(!isRecord)
			Change.stopRecord();
		
		notifyObservers();
	}

	public void setPK(boolean isPK) {
		this.isPK = isPK;
	}
	
	public boolean isPK() {
		return isPK;
	}
	
	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}
	
	public boolean isNullable() {
		return isNullable;
	}
	
	@Override
	public String toXML(int depth)
	{
		final String tab = Utility.generateTab(depth);
		return tab + "<attribute " + "name=\"" + name + "\" type=\"" + type + "\" const=\"" + constant + "\" visibility=\"\" " + (defaultValue == null ? "" : "defaultValue=\"" + defaultValue) + "\" isStatic=\"\" " + "/>";
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void select() {
		// TODO Auto-generated method stub
		
	}
	
	//TODO
	public AbstractDataType getType() {
		return super.type;
	}
}
