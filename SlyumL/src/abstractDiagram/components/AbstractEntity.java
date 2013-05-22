package abstractDiagram.components;

import java.util.LinkedList;
import java.util.List;

import abstractDiagram.relationships.AbstractRole;
import classDiagram.components.Type;
import classDiagram.relationships.Role;

/**
 * Abstract class containing all classes parameters (attributes, methods,
 * visibility, ...)
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 */
public abstract class AbstractEntity extends Type
{	
	protected List<AbstractRole> roles = new LinkedList<>();

	protected String stereotype = "";

	public AbstractEntity(String name)
	{
		super(name);
	}
	
	public AbstractEntity(String name, int id)
	{
		super(name, id);
	}
	
	public AbstractEntity(AbstractEntity e)
	{
		super(e.name, e.id);
	}
	
}
