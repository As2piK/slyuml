package abstractDiagram.relationships;

import java.util.Observable;

import classDiagram.ClassDiagram;
import classDiagram.IClassDiagramComponent;

/**
 * Represent a Role in UML structure. A role make a link between an association
 * and an entity. Specifically, a role, in UML, is represented by an attribute
 * representing an entity participating to the association. A role have a
 * visibility, a name and a multiplicity.
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 */
public abstract class AbstractRole extends Observable implements IClassDiagramComponent
{
	protected final int id = ClassDiagram.getNextId();

	private String name;


	@Override
	public int getId()
	{
		return id;
	}

	/**
	 * Get the name for this role.
	 * 
	 * @return the name for this role
	 */
	public String getName()
	{
		return name;
	}

	@Override
	public void select()
	{
		setChanged();
	}

	/**
	 * Set the name for this role
	 * 
	 * @param name
	 *            the new name for this role
	 */
	public void setName(String name)
	{	  
	  //saveState();
		this.name = name;
		//saveState();
		
		setChanged();
	}
}
