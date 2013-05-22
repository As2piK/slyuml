package abstractDiagram.components;

import change.Change;

import java.util.LinkedList;
import java.util.Observable;

import classDiagram.ClassDiagram;
import classDiagram.IClassDiagramComponent;
import classDiagram.components.Type;
import classDiagram.verifyName.TypeName;
import dbDiagram.IDBDiagramComponent;

/**
 * Represent a type in UML structure.
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 */
public abstract class AbstractType extends Observable implements IClassDiagramComponent, IDBDiagramComponent
{

	protected String name = "void";
	
	public abstract String getName();
	
}
