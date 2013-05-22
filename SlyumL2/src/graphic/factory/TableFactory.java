package graphic.factory;

import graphic.GraphicComponent;
import graphic.GraphicView;
import graphic.entity.TableEntityView;
import graphic.entity.TableView;

import java.awt.Rectangle;

import dbDiagram.components.TableEntity;
import dbDiagram.components.Visibility;

/**
 * ClassFactory allows to create a new class view associated with a new class
 * UML. Give this factory at the graphic view using the method
 * initNewComponent() for initialize a new factory. Next, graphic view will use
 * the factory to allow creation of a new component, according to the
 * specificity of the factory.
 * 
 * @author David Miserez
 * @version 1.0 - 25.07.2011
 */
public class TableFactory extends TableEntityFactory
{

	/**
	 * Create a new factory allowing the creation of a class.
	 * 
	 * @param parent
	 *            the graphic view
	 * @param dbDiagram
	 *            the class diagram
	 */
	public TableFactory(GraphicView parent)
	{
		super(parent);
	}

	@Override
	public GraphicComponent create()
	{
		final TableEntity tableEntity = new TableEntity("Table", Visibility.PUBLIC);
		final TableEntityView c = new TableView(parent, tableEntity);

		c.setBounds(new Rectangle(mouseReleased.x - DEFAULT_SIZE.width / 2, mouseReleased.y - DEFAULT_SIZE.height / 2, DEFAULT_SIZE.width, DEFAULT_SIZE.height));

		parent.addEntity(c);
		dbDiagram.addTable(tableEntity);
		return c;
	}

}
