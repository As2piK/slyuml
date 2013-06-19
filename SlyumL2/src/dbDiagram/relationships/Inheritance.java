package dbDiagram.relationships;

import java.util.Observable;

import utility.SMessageDialog;
import utility.Utility;
import dbDiagram.DBDiagram;
import dbDiagram.IDBDiagramComponent;
import dbDiagram.components.TableEntity;

/**
 * Represent a inheritance in UML structure. This inheritance, depends on the
 * entites participating in inheritance, will be a generalization or a
 * relalization.
 *
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 */
public class Inheritance extends Observable implements IDBDiagramComponent
{
	public static boolean validate(TableEntity child, TableEntity parent)
	{
		boolean valide = true;

		valide &= child != parent;

		for (final TableEntity e : child.getAllChilds())
			valide &= !parent.equals(e);

		if (!valide)
			SMessageDialog.showErrorMessage("Error in hierarchical class structure.\nImpossible to create inheritance association.");

		return valide;
	}

	protected TableEntity child, parent;

	protected final int id;

	/**
	 * Create a new inheritance with the given entities child and parent.
	 *
	 * @param child
	 * the child entity
	 * @param parent
	 * the parent entity
	 */
	public Inheritance(TableEntity child, TableEntity parent)
	{
		init(child, parent);

		id = DBDiagram.getNextId();
	}

	/**
	 * Create a new inheritance with the given entities child and parent. Don't
	 * generate a new id and use this given in parameter.
	 *
	 * @param child
	 * the child entity
	 * @param parent
	 * the parent entity
	 */
	public Inheritance(TableEntity child, TableEntity parent, int id)
	{
		init(child, parent);

		this.id = id;
	}

	/**
	 * Get the child for this inheritance.
	 *
	 * @return the child for this inheritance
	 */
	public TableEntity getChild()
	{
		return child;
	}

	@Override
	public int getId()
	{
		return id;
	}

	/**
	 * Get the parent for this inheritance.
	 *
	 * @return the parent for this inheritance
	 */
	public TableEntity getParent()
	{
		return parent;
	}

	/**
	 * Call by construtor for init parameters.
	 *
	 * @param child
	 * the child given in constructor
	 * @param parent
	 * the parent given in constructor
	 */
	private void init(TableEntity child, TableEntity parent)
	{
		this.child = child;
		this.child.addParent(this);

		this.parent = parent;
		this.parent.addChild(this);
	}

	@Override
	public void select()
	{
		setChanged();
	}

	/**
	 * Set the child for this inheritance.
	 *
	 * @param child
	 * the new child for this inheritance
	 */
	public void setChild(TableEntity child)
	{
		this.child.removeParent(this);
		this.child = child;
		child.addParent(this);
	}

	/**
	 * Set the parent for this inheritance.
	 *
	 * @param parent
	 * the new parent for this inheritance
	 */
	public void setParent(TableEntity parent)
	{
		this.parent.removeChild(this);
		this.parent = parent;
		parent.addChild(this);
	}

	public void showOverridesAndImplementations()
	{
	}

	@Override
	public String toString()
	{
		return getChild().getName() + " - " + getParent().getName();
	}



	@Override
	public String toXML(int depth)
	{
		final String tab = Utility.generateTab(depth);

		final String xml = tab + "<inheritance id=\"" + id + "\">\n" + tab + "\t<child>" + child.getId() + "</child>\n" + tab + "\t<parent>" + parent.getId() + "</parent>\n";

		return xml + tab + "</inheritance>";
	}
}