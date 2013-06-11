package swing.hierarchicalView;

import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import dbDiagram.IDBDiagramComponent.UpdateMessage;
import dbDiagram.components.Field;
import abstractDiagram.AbstractIDiagramComponent;
import swing.Slyum;
import utility.PersonalizedIcon;

/**
 * A JTree node associated with an attribute UML.
 * 
 * @author David Miserez
 * @version 1.0 - 28.07.2011
 */
public class TableNodeField extends DefaultMutableTreeNode implements ICustomizedIconNode, Observer, IDiagramNode
{
	private static final long serialVersionUID = -2998185646864433535L;
	private final Field field;
	private final JTree tree;
	private final DefaultTreeModel treeModel;

	/**
	 * Create a new node associated with an attribute.
	 * 
	 * @param field
	 *            the attribute associated
	 * @param treeModel
	 *            the model of the JTree
	 * @param tree
	 *            the JTree
	 */
	public TableNodeField(Field field, DefaultTreeModel treeModel, JTree tree)
	{
		super(field.getName());

		if (treeModel == null)
			throw new IllegalArgumentException("treeModel is null");

		if (tree == null)
			throw new IllegalArgumentException("tree is null");

		this.field = field;
		this.treeModel = treeModel;
		this.tree = tree;

		field.addObserver(this);
	}

	@Override
	public AbstractIDiagramComponent getAssociedComponent()
	{
		return field;
	}

	@Override
	public ImageIcon getCustomizedIcon()
	{
		return PersonalizedIcon.createImageIcon(Slyum.ICON_PATH + "attribute.png");
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		if (arg1 != null && arg1 instanceof UpdateMessage)
		{
			final TreePath path = new TreePath(getPath());

			switch ((UpdateMessage) arg1)
			{
				case SELECT:
					tree.addSelectionPath(path.getParentPath());
					tree.addSelectionPath(path);
					break;
				case UNSELECT:
					tree.removeSelectionPath(path);
					break;
			}
		}
		else
		{
			setUserObject(field.getName());
			treeModel.reload(getParent());
		}
	}
	
	@Override
	public void remove()
	{
	}
}
