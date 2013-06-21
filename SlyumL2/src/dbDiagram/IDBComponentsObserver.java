package dbDiagram;

import abstractDiagram.AbstractIComponentsObserver;
import abstractDiagram.IDiagramComponent;
import dbDiagram.components.TableEntity;
import dbDiagram.relationships.Binary;

/**
 * Interface implemented by all listeners of class diagram. When the class
 * diagram add, remove or change a new component, it notify all listeners with
 * the specified method.
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 */
public interface IDBComponentsObserver extends AbstractIComponentsObserver
{
	
	/**
	 * Adds a new class and notify that a new class has been added.
	 * 
	 * @param component
	 *            the component that was added.
	 */
	public void addTable(TableEntity component);

	public void addBinary(Binary binary);
	
	/**
	 * Changes the index of entity in the array and notifiy observers.
	 * 
	 * @param entity
	 *            the entity to move.
	 * @param index
	 *            the index to move the entity.
	 */
	public void changeZOrder(TableEntity entity, int index);

	/**
	 * Removes the given component and notify that this component has been
	 * removed.
	 * 
	 * @param component
	 *            the component to remove.
	 */
	public void removeComponent(IDiagramComponent component);
}
