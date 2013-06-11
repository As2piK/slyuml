package graphic.entity;

import graphic.GraphicView;

import javax.swing.JOptionPane;

import classDiagram.components.Method;
import dbDiagram.DBDiagram;
import dbDiagram.components.Field;
import dbDiagram.components.TableEntity;

/**
 * Represent the view of a class in UML structure.
 * 
 * @author David Miserez
 * @version 1.0 - 25.07.2011
 */
public class TableView extends TableEntityView
{

	/**
	 * Create a new view from the given class.
	 * 
	 * @param parent
	 *            the graphic view
	 * @param component
	 *            the class (UML)
	 */
	public TableView(GraphicView parent, TableEntity component)
	{
		super(parent, component);
	}

	@Override
	protected void prepareNewAttribute(Field field)
	{
		//TODO
	}

	@Override
	public void restore()
	{
		super.restore();
		
		parent.addEntity(this);
		restoreEntity();
		
		repaint();
	}
	
	protected void restoreEntity()
	{
		parent.getDBDiagram().addTable((TableEntity)getAssociedComponent());
	}
}
