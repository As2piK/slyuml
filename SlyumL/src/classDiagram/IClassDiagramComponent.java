package classDiagram;

import abstractDiagram.AbstractIDiagramComponent;

/**
 * Interface implemented by all class diagram component.
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 * 
 */
public interface IClassDiagramComponent extends AbstractIDiagramComponent
{
	public enum UpdateMessage
	{
		ADD_ATTRIBUTE, ADD_METHOD, ADD_ATTRIBUTE_NO_EDIT, ADD_METHOD_NO_EDIT, MODIF, SELECT, UNSELECT
	};
	
}
