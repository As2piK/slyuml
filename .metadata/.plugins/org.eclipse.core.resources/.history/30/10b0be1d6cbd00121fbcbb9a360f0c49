package graphic.entity;

import graphic.GraphicView;
import classDiagram.components.Attribute;
import classDiagram.components.ClassEntity;
import classDiagram.components.Method;

/**
 * Represent the view of a table in UML structure.
 * 
 * @author Jonathan Schumacher
 * @version 1.0 - 2013
 */
public class TableView extends ClassEntityView
{

	/**
	 * Create a new view from the given table.
	 * 
	 * @param parent
	 *            the graphic view
	 * @param component
	 *            the class (UML)
	 */
	public TableView(GraphicView parent, ClassEntity component)
	{
		super(parent, component);
	}

	@Override
	protected void prepareNewAttribute(Attribute attribute)
	{
	}

	@Override
	protected void prepareNewMethod(Method method)
	{
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
		parent.getClassDiagram().addClass((ClassEntity)getAssociedComponent());
	}
}
