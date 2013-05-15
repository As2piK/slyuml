package dbDiagram.components;

import java.util.LinkedList;

import swing.XMLParser.EntityType;

/**
 * Represent a class in UML structure.
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 */
public class TableEntity extends Entity
{

	/**
	 * Construct a new table.
	 * 
	 * @param name
	 *            the name of the class
	 * @param visibility
	 *            the visibility of the class
	 */
	public TableEntity(String name)
	{
		super(name);
	}

	/**
	 * Construc a new class. Does not generate a new unique id, but use the
	 * given id in parameters.
	 * 
	 * @param name
	 *            the name of the class
	 * @param visibility
	 *            the visibility of the class
	 * @param id
	 *            the class id
	 */
	public TableEntity(String name, int id)
	{
		super(name, id);
	}

	@Override
	protected String getEntityType()
	{
		return EntityType.CLASS.toString();
	}
}