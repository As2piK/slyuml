package swing.hierarchicalView;

import graphic.entity.AbstractEntityView;

import java.awt.Color;
import java.awt.Dimension;
import java.util.LinkedList;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import swing.JPanelRounded;
import swing.PanelClassDiagram;
import swing.Slyum;
import utility.PersonalizedIcon;
import abstractDiagram.AbstractIDiagramComponent;
import dbDiagram.DBDiagram;
import dbDiagram.IDBComponentsObserver;
import dbDiagram.IDBDiagramComponent;
import dbDiagram.IDBDiagramComponent.UpdateMessage;
import dbDiagram.components.Entity;
import dbDiagram.components.TableEntity;
import dbDiagram.relationships.Association;

/**
 * This class is a hierarchical view of the class diagram. It represents class
 * diagram like a tree with all component include in. JTree Swing component is
 * used to. It implements IComponentsObserver to see changes in class diagram.
 * 
 * @author David Miserez
 * @version 1.0 - 28.07.2011
 */
@SuppressWarnings("serial")
public class HierarchicalDBView extends JPanelRounded implements IDBComponentsObserver, TreeSelectionListener
{
	private final DefaultMutableTreeNode entitiesNode, associationsNode,
			inheritancesNode, dependenciesNode;
	private final JTree tree;
	private final DefaultTreeModel treeModel;

	/**
	 * Create a new hierarchical view of the specified class diagram. The new
	 * view is empty, if class diagram had already components, you must add them
	 * manually.
	 * 
	 * @param dbDiagram
	 *            the class diagram for constructing the hierarchical view.
	 */
	public HierarchicalDBView(DBDiagram dbDiagram)
	{
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBackground(Color.WHITE);
		setForeground(Color.GRAY);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		final DefaultMutableTreeNode root = new DefaultMutableTreeNode(dbDiagram.getName());

		entitiesNode = new DefaultMutableTreeNode("Entities");
		root.add(entitiesNode);

		associationsNode = new DefaultMutableTreeNode("Relations");
		root.add(associationsNode);

		inheritancesNode = new DefaultMutableTreeNode("Inheritances");
		root.add(inheritancesNode);

		dependenciesNode = new DefaultMutableTreeNode("Dependencies");
		root.add(dependenciesNode);

		treeModel = new DefaultTreeModel(root)
		{
			@Override
			public void removeNodeFromParent(MutableTreeNode node)
			{
				((IDiagramNode)node).remove();
				super.removeNodeFromParent(node);
			}
		};
		tree = new JTree(treeModel);

		tree.addTreeSelectionListener(this);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		tree.setCellRenderer(new TreeRenderer());

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportView(tree);
		scrollPane.setBorder(null);
		
		add(scrollPane);

		dbDiagram.addComponentsObserver(this);

		setMinimumSize(new Dimension(150, 200));
	}

	/**
	 * Add the specified association in associations node.
	 * 
	 * @param component
	 *            the new association
	 * @param imgPath
	 *            the icon representing the association in JTree
	 */
	public void addAssociation(Association component, String imgPath)
	{
		addNode(new DBNodeAssociation(component, treeModel, PersonalizedIcon.createImageIcon(imgPath), tree), associationsNode);
	}


	@Override
	public void addTable(TableEntity component)
	{
		addNode(new DBNodeEntity(component, treeModel, tree, PersonalizedIcon.createImageIcon(Slyum.ICON_PATH + "class16.png")), entitiesNode);
	}

	/**
	 * Add a new node in the specified parent node.
	 * 
	 * @param leaf
	 *            the new node to add
	 * @param parent
	 *            the parent of the new node
	 */
	public void addNode(DefaultMutableTreeNode leaf, DefaultMutableTreeNode parent)
	{
		parent.insert(leaf, 0);
		treeModel.reload(parent);
		tree.scrollPathToVisible(new TreePath(leaf.getPath()));
	}

	@Override
	public void changeZOrder(Entity entity, int index)
	{
		LinkedList<AbstractEntityView> evs = PanelClassDiagram.getInstance().getCurrentGraphicView().getSelectedEntities();
		
		final ClassNodeEntity ne = (ClassNodeEntity) searchAssociedNodeIn(entity, entitiesNode);

		entitiesNode.remove(ne);

		entitiesNode.insert(ne, entitiesNode.getChildCount() - index);

		treeModel.reload(entitiesNode);
		
		for (AbstractEntityView ev : evs)
			
			ev.setSelected(true);
	}

	@Override
	public void removeComponent(IDBDiagramComponent component)
	{
		final IDiagramNode associedNode = searchAssociedNode(component);

		if (associedNode != null)
		{
			treeModel.removeNodeFromParent((DefaultMutableTreeNode)associedNode);
			component.deleteObserver((Observer)associedNode);
		}
	}

	/**
	 * Search in the entire structure of JTree the node associated with the
	 * given UML object. Return null if no associated object are found.
	 * 
	 * @param o
	 *            the object associated with a node
	 * @return the node associated with the object; or null if no node are found
	 */
	public IDiagramNode searchAssociedNode(Object o)
	{
		IDiagramNode result = searchAssociedNodeIn(o, entitiesNode);

		if (result == null)
			result = searchAssociedNodeIn(o, associationsNode);

		if (result == null)
			result = searchAssociedNodeIn(o, inheritancesNode);

		if (result == null)
			result = searchAssociedNodeIn(o, dependenciesNode);

		return result;
	}

	/**
	 * Return the node associated with the given UML object. Return null if no
	 * associated object are found.
	 * 
	 * @param o
	 *            the object associated with a node
	 * @param root
	 *            the root node for the JTree
	 * @return the node associated with the object; or null if no node are found
	 */
	public static IDiagramNode searchAssociedNodeIn(Object o, TreeNode root)
	{
		IDiagramNode child = null;

		for (int i = 0; i < root.getChildCount(); i++)
		{
			child = (IDiagramNode) root.getChildAt(i);

			if (child.getAssociedComponent().equals(o))
				return child;

			if (!root.getChildAt(i).isLeaf())
				searchAssociedNodeIn(o, root.getChildAt(i));
		}

		return null;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e)
	{
		final LinkedList<TreePath> paths = new LinkedList<>();
		final TreePath[] treePaths = e.getPaths();

		// sort unselect first, select next
		for (int i = 0; i < treePaths.length; i++)
			if (!e.isAddedPath(treePaths[i]))
				paths.add(treePaths[i]);

		for (final TreePath treePath2 : treePaths)
			if (e.isAddedPath(treePath2))
				paths.add(treePath2);

		for (final TreePath treePath : paths)
		{
			final Object o = treePath.getLastPathComponent();

			if (!(o instanceof IDiagramNode)) // is an associed component
				// node ?
				continue;

			final AbstractIDiagramComponent component = ((IDiagramNode) o).getAssociedComponent();
			component.select();

			if (e.isAddedPath(treePath))
				component.notifyObservers(UpdateMessage.SELECT);

			else
				component.notifyObservers(UpdateMessage.UNSELECT);
		}
	}

	@Override
	public void removeComponent(AbstractIDiagramComponent component) {
		// TODO Auto-generated method stub
		
	}
}
