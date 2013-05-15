package abstractDiagram;

import java.util.LinkedList;

import utility.Utility;
import classDiagram.IClassDiagramComponent;
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
 * TODO
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.201
 * 
 */
public abstract class AbstractDiagram implements AbstractIComponentsObserver
{

	LinkedList<AbstractIDiagramComponent> components = new LinkedList<>();
	LinkedList<Entity> entities = new LinkedList<>();
	private String name;
	LinkedList<AbstractIComponentsObserver> observers = new LinkedList<>();

	/**
	 * Creates a new class diagram with the specified name.
	 * 
	 * @param name
	 *            The name of class diagram.
	 */
	public AbstractDiagram(String name)
	{
		if (name.isEmpty())
			throw new IllegalArgumentException("name is null");

		this.name = name;
	}

	/**
	 * Add a new observer who will be notified when the class diagram changed.
	 * 
	 * @param c
	 *            the new obserer.
	 * @return true if the observer has been added; false otherwise.
	 */
	public boolean addComponentsObserver(AbstractIComponentsObserver c)
	{
		return observers.add(c);
	}

	@Override
	public void changeZOrder(Entity entity, int index)
	{
		if (index < 0 || index >= entities.size())
			return;
		
		//Change.push(new BufferZOrder(entity, entities.indexOf(entity)));

		entities.remove(entity);
		entities.add(index, entity);
		
		//Change.push(new BufferZOrder(entity, index));

		for (final AbstractIComponentsObserver c : observers)
			c.changeZOrder(entity, index);
	}

	/**
	 * Get the name of class diagram.
	 * 
	 * @return the name of class diagram
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Remove all components in class diagram.
	 */
	public abstract void removeAll();

	/**
	 * remove the given IComponentsObserver from the list of observers.
	 * 
	 * @param c
	 *            the IComponentsObserver to remove
	 * @return true if IComponentsObserver has been removed; else otherwise.
	 */
	public boolean removeComponentsObserver(AbstractIComponentsObserver c)
	{
		return observers.remove(c);
	}

	/**
	 * Search the IDiagramComponent corresponding to the given id. Return null
	 * if no component are found.
	 * 
	 * @param id
	 *            id of the component to search
	 * @return the component corresponding to the given id, or null if no
	 *         component are found.
	 */
	public AbstractIDiagramComponent searchComponentById(int id)
	{
		for (final AbstractIDiagramComponent c : components)

			if (c.getId() == id)

				return c;

		return null;
	}

	/**
	 * Set the current id.
	 * 
	 * @param id
	 *            the new id to set
	 */
	public abstract void setCurrentId(int id);

	/**
	 * Set the name of class diagram.
	 * 
	 * @param name
	 *            the new name of class diagram.
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Return a String representing the structure of all components in class
	 * diagram in XML format (get the XSD in documents).
	 * 
	 * @param depth
	 *            the number of tabs to put before a new tag
	 * @return the string representing the structure of class diagram in XML.
	 */
	public String toXML(int depth)
	{
		final String tab = Utility.generateTab(depth);

		String xml = tab + "<diagramElements>\n";

		// write for each component its XML structure.
		for (final AbstractIDiagramComponent component : components)
			xml += component.toXML(depth + 1) + "\n";

		return xml + tab + "</diagramElements>";
	}
}
