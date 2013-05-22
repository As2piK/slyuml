package dbDiagram.relationships;

import utility.Utility;
import abstractDiagram.relationships.AbstractRole;
import dbDiagram.DBDiagram;
import dbDiagram.components.Entity;

/**
 * Represent a Role in UML structure. A role make a link between an association
 * and an entity. Specifically, a role, in UML, is represented by an attribute
 * representing an entity participating to the association. A role have a
 * visibility, a name and a multiplicity.
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 */
public class Role extends AbstractRole
{
	private Association associations;

	private Entity entity;
	protected final int id = DBDiagram.getNextId();
	private final Multiplicity multiplicity;

	private String name;

	/**
	 * Create a new role making a link between the given association and the
	 * given entity. A role adding itself to the association and entity given in
	 * parameters.
	 * 
	 * @param association
	 *            the association represented by the role
	 * @param component
	 *            the entity linked by the association
	 * @param name
	 *            the name (label) for this association
	 */
	public Role(Association association, Entity component, String name)
	{
		associations = association;
		entity = component;

		multiplicity = new Multiplicity(1);
		this.name = name;

		component.addRole(this);
		association.addRole(this);
	}

	/**
	 * Get the association for this role.
	 * 
	 * @return the association for this role
	 */
	public Association getAssociation()
	{
		return associations;
	}

	/**
	 * Get the entity for this role.
	 * 
	 * @return the entity for this role
	 */
	public Entity getEntity()
	{
		return entity;
	}

	@Override
	public int getId()
	{
		return id;
	}

	/**
	 * Get the multiplicity for this role.
	 * 
	 * @return the multiplicity for this role.
	 */
	public Multiplicity getMultiplicity()
	{
		return multiplicity;
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
	 * Set the association for this role
	 * 
	 * @param association
	 *            the new association for this role
	 */
	public void setAssociation(Association association)
	{
		associations = association;
		setChanged();
	}

	/**
	 * Set the entity for this role
	 * 
	 * @param entity
	 *            the new entity for this role
	 */
	public void setEntity(Entity entity)
	{
		this.entity = entity;
		setChanged();
	}

	/**
	 * Set the multiplicity for this role
	 * 
	 * @param multiplicity
	 *            the new multiplicity for this role
	 */
	public void setMultiplicity(Multiplicity multiplicity)
	{
		if (multiplicity == null)
			return;

		//saveState();
		this.multiplicity.setLowerBound(multiplicity.getLowerBound());
		this.multiplicity.setUpperBound(multiplicity.getUpperBound());
		//saveState();

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


	@Override
	public String toString()
	{
		if (name == null || name.isEmpty())
			return "";

		return name;
	}

	@Override
	public String toXML(int depth)
	{
		final String tab = Utility.generateTab(depth);
		String role = tab + "<role " + "componentId=\"" + entity.getId() + "\" " + (name == null ? "" : "name=\"" + name.replaceAll("<", "&lt;") + "\" ");

		if (multiplicity == null)
			role += "\"/>";
		else
			role += "\">\n" + multiplicity.toXML(depth + 1) + "\n" + tab + "</role>";

		return role;
	}
}
