package dbDiagram.components;

import java.util.LinkedList;
import java.util.List;

import dbDiagram.relationships.Role;

/**
 * Abstract class containing all classes parameters (attributes, methods,
 * visibility, ...)
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 */
public abstract class Entity extends Type
{	
	protected LinkedList<Field> fields = new LinkedList<>();
	protected List<Role> roles = new LinkedList<>();

	protected String stereotype = "";

	public Entity(String name)
	{
		super(name);

	}

	public Entity(String name, int id)
	{
		super(name, id);

	}

	public Entity(Entity e)
	{
		super(e.name, e.id);

	}

	/**
	 * Add a new attribute.
	 * 
	 * @param field
	 *            the new attribute.
	 */
	public void addField(Field field)
	{
		if (field == null)
			throw new IllegalArgumentException("field is null");

		fields.add(field);
		int i = fields.indexOf(field);
		//Change.push(new BufferCreationAttribute(this, field, true, i)); TODO change
		//Change.push(new BufferCreationAttribute(this, field, false, i)); TODO change

		setChanged();
	}

	/**
	 * Add a new role.
	 * 
	 * @param role
	 *            the new role
	 */
	public void addRole(Role role)
	{
		if (role == null)
			throw new IllegalArgumentException("role is null");

		roles.add(role);

		setChanged();
	}

	public LinkedList<Entity> getAllChilds()
	{
		/*final LinkedList<Entity> allChilds = new LinkedList<Entity>();
		allChilds.add(this);

		for (final Inheritance p : childs)
			allChilds.addAll(p.getChild().getAllChilds());

		return allChilds; TODO */
		return null;
	}

	public LinkedList<Entity> getAllParents()
	{
		/*final LinkedList<Entity> allParents = new LinkedList<Entity>();
		allParents.add(this);

		for (final Inheritance p : parents)
			allParents.addAll(p.getParent().getAllParents());

		return allParents; TODO */
		return null;
	}

	/**
	 * Get a copy of the attribute's list.
	 * 
	 * @return an array containing all attributes of the entity.
	 */
	public LinkedList<Field> getAttributes()
	{
		final LinkedList<Field> copy = new LinkedList<Field>();

		for (final Field a : fields)
			copy.add(a);

		return copy;
	}

	/**
	 * Use in XML exportation. Get the type of the entity.
	 * 
	 * @return the type of the entity.
	 */
	protected abstract String getEntityType();

	/**
	 * Us in XML exportation. Get a string to add new tags if necessary.
	 * 
	 * @param depth
	 *            the number of tabs to add before each tag
	 * @return the tag to add before the closure tag.
	 */
	protected String getLastBalise(int depth)
	{
		return ""; // no last balise
	}

	/**
	 * Get the stereotype of the entity.
	 * 
	 * @return the stereotype of the entity.
	 */
	public String getStereotype()
	{
		return stereotype;
	}

	public boolean isChildOf(Entity entity)
	{
		/* boolean isChild = false;

		for (final Inheritance i : parents)
			isChild |= i.getParent().isChildOf(entity);

		return isChild || equals(entity);
		TODO */
		return false;
	}

	public boolean isParentOf(Entity entity)
	{
		/* boolean isParent = false;

		for (final Inheritance i : childs)
			isParent |= i.getChild().isParentOf(entity);

		return isParent || equals(entity);
		TODO */
		return false;
	}

	/**
	 * Move the attribute's position in the array by the given offset. Offset is
	 * added to the current index to compute the new index. The offset can be
	 * positive or negative.
	 * 
	 * @param attribute
	 *            the attribute to move
	 * @param offset
	 *            the offset for compute the new index
	 */
	public void moveAttributePosition(Field attribute, int offset)
	{
		moveComponentPosition(fields, attribute, offset);
	}

	/**
	 * Move the object's position in the given array by the given offset. Offset
	 * is added to the current index to compute the new index. The offset can be
	 * positive or negative.
	 * 
	 * @param list
	 *            the list containing the object to move
	 * @param o
	 *            the object to move
	 * @param offset
	 *            the offset for compute the new index
	 */
	private <T extends Object> void moveComponentPosition(LinkedList<T> list, T o, int offset)
	{
		final int index = list.indexOf(o);

		if (index != -1)
		{
			//Change.push(new BufferIndex<T>(this, list, o)); TODO change

			list.remove(o);
			list.add(index + offset, o);

			//Change.push(new BufferIndex<T>(this, list, o)); TODO change

			setChanged();
		}
	}

	/**
	 * Remove the attribute.
	 * 
	 * @param attribute
	 *            the attribute to remove
	 * @return true if the attribute has been removed; false otherwise
	 */
	public boolean removeField(Field attribute)
	{
		if (attribute == null)
			throw new IllegalArgumentException("attribute is null");

		int i = fields.indexOf(attribute);

		if (fields.remove(attribute))
		{
			//Change.push(new BufferCreationAttribute(this, attribute, false, i)); TODO change
			//Change.push(new BufferCreationAttribute(this, attribute, true, i)); TODO change

			setChanged();
			return true;
		}
		else
			return false;
	}


	@Override
	public boolean setName(String name)
	{
		//BufferClass bc = new BufferClass(this); TODO change
		boolean b;

		b = super.setName(name);

		if (b)
		{
			//Change.push(bc); TODO change
			//Change.push(new BufferClass(this)); TODO change
		}

		return b;
	}

	/**
	 * Set the stereotype of the entity.
	 * 
	 * @param stereotype
	 *            the new stereotype
	 */
	public void setStereotype(String stereotype)
	{
		if (stereotype == null)
			throw new IllegalArgumentException("stereotype is null");

		this.stereotype = stereotype;
	}

	@Override
	public String toXML(int depth)
	{
		/*
		final String tab = Utility.generateTab(depth);

		String xml = tab + "<entity " + "id=\"" + getId() + "\" " + "name=\"" + super.toXML(0) + "\" " + "visibility=\"" + visibility + "\" " + "entityType=\"" + getEntityType() + "\" " + "isAbstract=\"" + isAbstract() + "\" ";

		if (fields.size() == 0 && methods.size() == 0 && getLastBalise(depth).isEmpty())
			return xml + "/>";

		xml += ">\n";

		for (final Field attribute : fields)
			xml += attribute.toXML(depth + 1) + "\n";

		for (final Field operation : methods)
			xml += operation.toXML(depth + 1) + "\n";

		xml += getLastBalise(depth + 1);

		return xml + tab + "</entity>"; TODO*/
		return "";
	}
}