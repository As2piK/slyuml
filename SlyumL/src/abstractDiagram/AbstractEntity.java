package abstractDiagram;

import classDiagram.components.Type;

/**
 * Abstract class containing all classes parameters (attributes, methods,
 * visibility, ...)
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 */
public abstract class AbstractEntity extends Type
{
	public AbstractEntity(String name) {
		super(name);
	}	

	public AbstractEntity(String name, int id) {
		super(name, id);
	}	
	
}
