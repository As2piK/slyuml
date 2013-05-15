package dbDiagram.components;

import classDiagram.verifyName.TypeName;
import change.BufferAttribute;
import change.Change;
import utility.Utility;

/**
 * Represent an attribute in UML structure.
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 */
public class Field extends Variable
{	
	private String defaultValue;

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
	 * @param attribute attribute
	 */
	public Field(Field attribute)
	{
		super(attribute.getName(), new Type(attribute.getType().getName()));
		
		boolean isBlocked = Change.isBlocked();
		Change.setBlocked(true);
		
		name = attribute.name;
		type = new Type(attribute.getType().getName());
		defaultValue = attribute.defaultValue;
		
		Change.setBlocked(isBlocked);
	}
	
	public void setField(Field field)
	{
		boolean isRecord = Change.isRecord();
		Change.record();
		
		setName(field.getName());
		setType(new Type(field.getType().getName()));
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
		//Change.push(new BufferAttribute(this)); TODO change
		this.defaultValue = defaultValue;
		//Change.push(new BufferAttribute(this)); TODO change
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

	@Override
	public String toXML(int depth)
	{
		final String tab = Utility.generateTab(depth);
		//return tab + "<attribute " + "name=\"" + name + "\" type=\"" + type.toXML(depth+1) + "\" const=\"" + constant + "\" visibility=\"" + visibility + "\" " + (defaultValue == null ? "" : "defaultValue=\"" + defaultValue) + "\" isStatic=\"" + _isStatic + "\" " + "/>"; TODO
		return "";
	}
}
