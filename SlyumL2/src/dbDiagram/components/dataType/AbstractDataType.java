package dbDiagram.components.dataType;

import java.util.LinkedList;

import abstractDiagram.components.AbstractType;
import dbDiagram.DBDiagram;

/**
 * Represent a type in UML structure.
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 */
public abstract class AbstractDataType extends AbstractType
{		
	
	public enum DataType {
		INT,
		VARCHAR;
		
		public static AbstractDataType getNewDataTypeFromObject(AbstractDataType c) {
			if (c instanceof IntDataType) {
				return new IntDataType();
			} else if (c instanceof VarcharDataType) {
				return new VarcharDataType();
			}
			return new IntDataType();
		}
		
		public static AbstractDataType getNewDataTypeObjectFromDataType(DataType t) {
			if (t == null) {
				return new IntDataType();
			}
			switch (t) {
			case INT : return new IntDataType();
			case VARCHAR : return new VarcharDataType();
			}
			return new IntDataType();
		}
		
		public static DataType getDataTypeFromName(String name) {
			name = name.toLowerCase().trim();
			if (name.equals("int")) {
				return INT;
			}
			if (name.equals("varchar")) {
				return VARCHAR;
			}
			return null;
		}
		
	}
	protected final int id;

	protected String name = "INT";
	
	LinkedList<Integer> arraysSize = new LinkedList<>();

	/**
	 * Create a new type with the specified name.
	 * 
	 * @param name
	 *            the name of the type
	 */
	public AbstractDataType()
	{		
		id = DBDiagram.getNextId();
	}

	/**
	 * Create a new type with the specified name and id.
	 * 
	 * @param name
	 *            the name of the type
	 * @param id
	 *            the id of the type
	 */
	public AbstractDataType(int id)
	{		
		this.id = id;
	}

	@Override
	public int getId()
	{
		return id;
	}

	/**
	 * Get the name of the type.
	 * 
	 * @return the name of the type
	 */
	public abstract String getName();
	public abstract int getSize();
	public abstract void setSize(int size);
	public abstract Object getDefaultValue();
	public abstract void setDefaultValue(Object value);

	@Override
	public void select()
	{
		setChanged();
	}

	@Override
	public abstract String toString();

	@Override
	public String toXML(int depth)
	{		
		return getName().replace("<", "&lt;").replace(">", "&gt;");
	}
}
