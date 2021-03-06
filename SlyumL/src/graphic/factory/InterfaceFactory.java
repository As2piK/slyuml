package graphic.factory;

import graphic.GraphicComponent;
import graphic.GraphicView;
import graphic.entity.ClassEntityView;
import graphic.entity.InterfaceView;

import java.awt.Rectangle;

import classDiagram.components.InterfaceEntity;
import classDiagram.components.Visibility;

/**
 * InterfaceFactory allows to create a new interface view associated with a new
 * class UML. Give this factory at the graphic view using the method
 * initNewComponent() for initialize a new factory. Next, graphic view will use
 * the factory to allow creation of a new component, according to the
 * specificity of the factory.
 * 
 * @author David Miserez
 * @version 1.0 - 25.07.2011
 */
public class InterfaceFactory extends ClassEntityFactory
{

	/**
	 * Create a new factory allowing the creation of an interface.
	 * 
	 * @param parent
	 *            the graphic view
	 * @param dbDiagram
	 *            the class diagram
	 */
	public InterfaceFactory(GraphicView parent)
	{
		super(parent);
	}

	@Override
	public GraphicComponent create()
	{
		final InterfaceEntity ie = new InterfaceEntity("Interface", Visibility.PUBLIC);
		final ClassEntityView i = new InterfaceView(parent, ie);

		parent.addEntity(i);
		classDiagram.addInterface(ie);

		i.setBounds(new Rectangle(mouseReleased.x - DEFAULT_SIZE.width / 2, mouseReleased.y - DEFAULT_SIZE.height / 2, DEFAULT_SIZE.width, DEFAULT_SIZE.height));
		
		return i;
	}

}
