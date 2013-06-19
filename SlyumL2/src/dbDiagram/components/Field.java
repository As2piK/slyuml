package dbDiagram.components;

import utility.Utility;
import change.BufferField;
import change.Change;
import dbDiagram.verifyName.TypeName;

/**
 * Represent an attribute in UML structure.
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 */
public class Field extends Variable
{	
	private String defaultValue;
	private String type; //TODO
	private boolean isPK;

	/**
	 * Construct a new attribute.
	 * 
	 * @param name
	 *            the name of the attribute.
	 * @param type
	 *            the type of the attribute.
	 */
	public Field(String name, Type type)
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
		super(field.getName(), new Type(field.getType().getName()));
		
		boolean isBlocked = Change.isBlocked();
		Change.setBlocked(true);
		
		name = field.name;
		type = "test";
		defaultValue = field.defaultValue;
		
		Change.setBlocked(isBlocked);
	}
	
	public void setAttribute(Field attribute)
	{
		boolean isRecord = Change.isRecord();
		Change.record();
		
		setName(attribute.getName());
		setType(new Type(attribute.getType().getName()));
		setDefaultValue(attribute.getDefaultValue());
		
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
	public void setText(String text)
	{
		if (text.length() == 0)
			return;

		String newName;
		Type type = getType();
		text = text.trim();
		Visibility newVisibility = Visibility.getVisibility(text.charAt(0));

		final String[] subString = text.split(":");

		newName = subString[0].trim();

		if (subString.length == 2)
		{
			subString[1] = subString[1].trim();
			
			if (!TypeName.getInstance().verifyName(subString[1]))
				return;
			
			type = new Type(subString[1]);
		}
		
		boolean isRecord = Change.isRecord();
		Change.record();
		
		setType(type);
		setName(newName);
		
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
	public Type getType() {
		return super.type;
	}
}
