package abstractDiagram.components;

import java.util.Observable;

import abstractDiagram.IDiagramComponent;

/**
 * Represent a type in UML structure.
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 */
public abstract class AbstractType extends Observable implements IDiagramComponent
{

	protected String name = "";
	
	public abstract String getName();
	
}
