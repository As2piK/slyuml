package dbDiagram;

import java.util.Observer;

import abstractDiagram.AbstractIDiagramComponent;

/**
 * Interface implemented by all class diagram component.
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 * 
 */
public interface IDBDiagramComponent extends AbstractIDiagramComponent
{
	public enum UpdateMessage
	{
		ADD_FIELD, ADD_FIELD_NO_EDIT, MODIF, SELECT, UNSELECT
	};
}
