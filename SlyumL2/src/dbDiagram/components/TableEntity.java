package dbDiagram.components;

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
	 * Construct a new class.
	 * 
	 * @param name
	 *            the name of the class
	 * @param visibility
	 *            the visibility of the class
	 */
	public TableEntity(String name, Visibility visibility)
	{
		super(name, visibility);
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
	public TableEntity(String name, Visibility visibility, int id)
	{
		super(name, visibility, id);
	}

	@Override
	protected String getEntityType()
	{
		return EntityType.CLASS.toString();
	}
}
