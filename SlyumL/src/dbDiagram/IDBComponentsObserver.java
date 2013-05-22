package dbDiagram;

import abstractDiagram.AbstractIComponentsObserver;
import abstractDiagram.AbstractIDiagramComponent;
import classDiagram.components.Entity;
import classDiagram.relationships.Binary;
import classDiagram.relationships.Inheritance;
import classDiagram.relationships.Multi;
import dbDiagram.components.TableEntity;

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
	 * Adds a new binary and notify that a new binary has been added.
	 * 
	 * @param component
	 *            the component that was added.
	 */
	public void addBinary(Binary component);

	/**
	 * Adds a new class and notify that a new class has been added.
	 * 
	 * @param component
	 *            the component that was added.
	 */
	public void addTable(TableEntity component);

	/**
	 * Adds a new inheritance and notify that a new inheritance has been added.
	 * 
	 * @param component
	 *            the component that was added.
	 */
	public void addInheritance(Inheritance component);

	/**
	 * Adds a new multi-association and notify that a new multi-association has
	 * been added.
	 * 
	 * @param component
	 *            the component that was added.
	 */
	public void addMulti(Multi component);

	/**
	 * Changes the index of entity in the array and notifiy observers.
	 * 
	 * @param entity
	 *            the entity to move.
	 * @param index
	 *            the index to move the entity.
	 */
	public void changeZOrder(Entity entity, int index);

	/**
	 * Removes the given component and notify that this component has been
	 * removed.
	 * 
	 * @param component
	 *            the component to remove.
	 */
	public void removeComponent(AbstractIDiagramComponent component);
}
