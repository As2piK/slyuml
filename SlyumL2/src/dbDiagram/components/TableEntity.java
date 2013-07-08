package dbDiagram.components;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import swing.XMLParser.EntityType;
import utility.Utility;
import abstractDiagram.AbstractEntity;
import change.BufferCreationField;
import change.BufferDB;
import change.Change;
import dbDiagram.relationships.Binary;
import dbDiagram.relationships.Inheritance;
import dbDiagram.relationships.Role;

/**
 * Abstract class containing all classes parameters (attributes, methods,
 * visibility, ...)
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 */
public class TableEntity extends AbstractEntity
{	
	protected PrimaryKey primaryKey = new PrimaryKey("pk");
	protected LinkedList<Field> fields = new LinkedList<>();
	protected List<Inheritance> childs = new LinkedList<>();
	protected List<Inheritance> parents = new LinkedList<>();
	protected List<Role> roles = new LinkedList<>();

	protected String stereotype = "";

	public TableEntity(String name)
	{
		super(name);
	}

	public TableEntity(String name, int id)
	{
		super(name, id);
		
	}
	
	public TableEntity(TableEntity e)
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
		Change.push(new BufferCreationField(this, field, true, i));
		Change.push(new BufferCreationField(this, field, false, i));

		setChanged();
	}
	
	public void addField(int index, Field field) {
		
		if (field == null)
			throw new IllegalArgumentException("field is null");

		fields.add(index, field);
		int i = fields.indexOf(field);
		Change.push(new BufferCreationField(this, field, true, i));
		Change.push(new BufferCreationField(this, field, false, i));

		setChanged();
		
	}

	/**
	 * Add a new child.
	 * 
	 * @param child
	 *            the new child
	 */
	public void addChild(Inheritance child)
	{
		if (child == null)
			throw new IllegalArgumentException("child is null");

		childs.add(child);

		setChanged();
	}

	/**
	 * Add a new parent.
	 * 
	 * @param parent
	 *            the new parent
	 */
	public void addParent(Inheritance parent)
	{
		if (parent == null)
			throw new IllegalArgumentException("parent is null");

		parents.add(parent);

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

	public LinkedList<TableEntity> getAllParents()
	{
		final LinkedList<TableEntity> allParents = new LinkedList<TableEntity>();
		allParents.add(this);

		for (final Inheritance p : parents)
			allParents.addAll(p.getParent().getAllParents());

		return allParents;
	}

	/**
	 * Get a copy of the attribute's list.
	 * 
	 * @return an array containing all attributes of the entity.
	 */
	public LinkedList<Field> getFields()
	{
		final LinkedList<Field> copy = new LinkedList<Field>();

		for (final Field f : fields)
			copy.add(f);

		return copy;
	}
	
	public LinkedList<ForeignKey> getForeignKeys() {
		final LinkedList<ForeignKey> fks = new LinkedList<ForeignKey>();
		for (final Field f : fields)
			if (f instanceof ForeignKey)
				fks.add((ForeignKey)f);
		return fks;
	}
	
	public LinkedList<Field> getPrimaryKeys() {
		final LinkedList<Field> copy = new LinkedList<Field>();

		for (final Field f : fields)
			if (f.isPK())
				copy.add(f);

		return copy;
	}

	/**
	 * Use in XML exportation. Get the type of the entity.
	 * 
	 * @return the type of the entity.
	 */
	protected String getEntityType()
	{
		return EntityType.CLASS.toString();
	}

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

	public boolean isChildOf(TableEntity entity)
	{
		boolean isChild = false;

		for (final Inheritance i : parents)
			isChild |= i.getParent().isChildOf(entity);

		return isChild || equals(entity);
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
	public void moveFieldPosition(Field attribute, int offset)
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
			//Change.push(new BufferDBIndex<T>(this, list, o));
			
			list.remove(o);
			list.add(index + offset, o);
			
			//Change.push(new BufferDBIndex<T>(this, list, o)); //TODO

			setChanged();
		}
	}

	/**
	 * Remove the attribute.
	 * 
	 * @param field
	 *            the attribute to remove
	 * @return true if the attribute has been removed; false otherwise
	 */
	public boolean removeField(Field field)
	{
		if (field == null)
			throw new IllegalArgumentException("field is null");

		int i = fields.indexOf(field);

		if (fields.remove(field))
		{
			Change.push(new BufferCreationField(this, field, false, i));
			Change.push(new BufferCreationField(this, field, true, i));

			setChanged();
			return true;
		}
		else
			return false;
	}

	/**
	 * Remove the child.
	 * 
	 * @param child
	 *            the child to remove
	 */
	public void removeChild(Inheritance child)
	{
		childs.remove(child);

		setChanged();
	}

	/**
	 * Remove the parent.
	 * 
	 * @param parent
	 *            the parent to remove
	 */
	public void removeParent(Inheritance parent)
	{
		parents.remove(parent);

		setChanged();
	}
	
	@Override
	public boolean setName(String name)
	{
		BufferDB bc = new BufferDB(this);
		boolean b;
		
		b = super.setName(name);
		
		if (b)
		{
			Change.push(bc);
			Change.push(new BufferDB(this));
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
		final String tab = Utility.generateTab(depth);

		String xml = tab + "<entity " + "id=\"" + getId() + "\" " + "name=\"" + super.toXML(0) + "\" " + "entityType=\"" + getEntityType() + "\" ";

		if (fields.size() == 0 && getLastBalise(depth).isEmpty())
			return xml + "/>";

		xml += ">\n";

		for (final Field field : fields)
			xml += field.toXML(depth + 1) + "\n";

		xml += getLastBalise(depth + 1);

		return xml + tab + "</entity>";
	}

	public PrimaryKey getPrimaryKey() {
		return primaryKey;
	}
}
