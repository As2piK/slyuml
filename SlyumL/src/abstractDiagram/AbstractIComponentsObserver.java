package abstractDiagram;

import classDiagram.components.AssociationClass;
import classDiagram.components.ClassEntity;
import classDiagram.components.Entity;
import classDiagram.components.InterfaceEntity;
import classDiagram.relationships.Aggregation;
import classDiagram.relationships.Binary;
import classDiagram.relationships.Composition;
import classDiagram.relationships.Dependency;
import classDiagram.relationships.Inheritance;
import classDiagram.relationships.InnerClass;
import classDiagram.relationships.Multi;

/**
 * Interface implemented by all listeners of class diagram. When the class
 * diagram add, remove or change a new component, it notify all listeners with
 * the specified method.
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 */
public interface AbstractIComponentsObserver
{
	/**
	 * Changes the index of entity in the array and notifiy observers.
	 * 
	 * @param entity
	 *            the entity to move.
	 * @param index
	 *            the index to move the entity.
	 */
	public void changeZOrder(Entity entity, int index);
}
