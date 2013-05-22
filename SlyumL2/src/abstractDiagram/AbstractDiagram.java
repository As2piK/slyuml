package abstractDiagram;

import classDiagram.IClassComponentsObserver;
import dbDiagram.IDBComponentsObserver;

/**
 * This class contains all structurals UML components. Add classes, interfaces,
 * associations, inheritances, dependecies from here. It implements
 * IComponentObserver and notify all listeners when a new UML components is
 * added, removed or modified.
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.201
 * 
 */
public abstract class AbstractDiagram implements AbstractIComponentsObserver
{

	public boolean addComponentsObserver(IClassComponentsObserver c) {return false;}

	public boolean addComponentsObserver(IDBComponentsObserver c) {return false;}
	
	/**
	 * Changes the index of entity in the array and notifiy observers.
	 * 
	 * @param entity
	 *            the entity to move.
	 * @param index
	 *            the index to move the entity.
	 */
	
	public abstract String getName();
	public abstract void removeAll();
	public abstract String toXML(int no);
	public void changeZOrder(AbstractEntity entity, int index) {};
}
