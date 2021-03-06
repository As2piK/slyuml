package dbDiagram;

import java.util.LinkedList;

import abstractDiagram.AbstractDiagram;
import abstractDiagram.AbstractIDiagramComponent;
import utility.Utility;
import dbDiagram.components.AssociationClass;
import dbDiagram.components.TableEntity;
import dbDiagram.components.Entity;
import dbDiagram.components.InterfaceEntity;
import dbDiagram.relationships.Aggregation;
import dbDiagram.relationships.Binary;
import dbDiagram.relationships.Composition;
import dbDiagram.relationships.Dependency;
import dbDiagram.relationships.Inheritance;
import dbDiagram.relationships.InnerClass;
import dbDiagram.relationships.Multi;

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
public class DBDiagram extends AbstractDiagram implements IDBComponentsObserver
{
	private static int currentID = 0;

	public static int getNextId()
	{
		return ++currentID;
	}

	LinkedList<IDBDiagramComponent> components = new LinkedList<>();
	LinkedList<Entity> entities = new LinkedList<>();
	private String name;
	LinkedList<IDBComponentsObserver> observers = new LinkedList<>();

	/**
	 * Creates a new class diagram with the specified name.
	 * 
	 * @param name
	 *            The name of class diagram.
	 */
	public DBDiagram(String name)
	{
		if (name.isEmpty())
			throw new IllegalArgumentException("name is null");

		this.name = name;
	}

	@Override
	public void addAggregation(Aggregation component)
	{
		for (final IDBComponentsObserver c : observers)
			c.addAggregation(component);

		addComponent(component);
	}

	@Override
	public void addAssociationClass(AssociationClass component) //TODO DELETE
	{
	}

	@Override
	public void addBinary(Binary component)
	{
		for (final IDBComponentsObserver c : observers)
			c.addBinary(component);

		addComponent(component);
	}

	@Override
	public void addTable(TableEntity component)
	{
		for (final IDBComponentsObserver c : observers)

			c.addTable(component);

		addComponent(component);
		entities.addFirst(component);
	}

	/**
	 * Add a new in class diagram. /!\ Does not notify listners.
	 * 
	 * @param component
	 *            the new component.
	 * @return true if the component has been added; false otherwise
	 */
	private boolean addComponent(IDBDiagramComponent component)
	{
		if (component.getId() > currentID)
			setCurrentId(component.getId() + 1);

		if (!components.contains(component))
		{
			components.addFirst(component);
			return true;
		}

		return false;
	}

	/**
	 * Add a new observer who will be notified when the class diagram changed.
	 * 
	 * @param c
	 *            the new obserer.
	 * @return true if the observer has been added; false otherwise.
	 */
	public boolean addComponentsObserver(IDBComponentsObserver c)
	{
		return observers.add(c);
	}

	@Override
	public void addComposition(Composition component)
	{
		for (final IDBComponentsObserver c : observers)
			c.addComposition(component);

		addComponent(component);
	}

	@Override
	public void addDependency(Dependency component)
	{
		for (final IDBComponentsObserver c : observers)
			c.addDependency(component);

		//addComponent(component);
	}

	@Override
	public void addInheritance(Inheritance component)
	{
		for (final IDBComponentsObserver c : observers)
			c.addInheritance(component);

		addComponent(component);
	}

	@Override
	public void addInnerClass(InnerClass component)
	{

		for (final IDBComponentsObserver c : observers)
			c.addInnerClass(component);

		addComponent(component);
	}

	@Override
	public void addInterface(InterfaceEntity component)
	{
		for (final IDBComponentsObserver c : observers)
			c.addInterface(component);

		addComponent(component);
		entities.addFirst(component);
	}

	@Override
	public void addMulti(Multi component)
	{
		if (components.contains(component))
			return;
		
		for (final IDBComponentsObserver c : observers)
			c.addMulti(component);

		addComponent(component);
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

		for (final IDBComponentsObserver c : observers)
			c.changeZOrder(entity, index);
	}

	/**
	 * Return a copy of the array containing all class diagram elements.
	 * 
	 * @return a copy of the array containing all class diagram elements
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<IDBDiagramComponent> getComponents()
	{
		return (LinkedList<IDBDiagramComponent>) components.clone();
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
	public void removeAll()
	{
		while (components.size() > 0)

			removeComponent(components.get(0));
	}

	@Override
	public void removeComponent(IDBDiagramComponent component)
	{
		components.remove(component);

		// Optimizes this (create more array for specific elements, not just an
		// array for all components.
		if (component instanceof Entity)
			entities.remove(component);

		for (final IDBComponentsObserver c : observers)
			c.removeComponent(component);
	}

	/**
	 * remove the given IComponentsObserver from the list of observers.
	 * 
	 * @param c
	 *            the IComponentsObserver to remove
	 * @return true if IComponentsObserver has been removed; else otherwise.
	 */
	public boolean removeComponentsObserver(IDBComponentsObserver c)
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
	public IDBDiagramComponent searchComponentById(int id)
	{
		for (final IDBDiagramComponent c : components)

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
	public void setCurrentId(int id)
	{
		currentID = id;
	}

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
		for (final IDBDiagramComponent component : components)
			xml += component.toXML(depth + 1) + "\n";

		return xml + tab + "</diagramElements>";
	}

	@Override
	public void removeComponent(AbstractIDiagramComponent component) {
		removeComponent((IDBDiagramComponent) component);
	}
}
