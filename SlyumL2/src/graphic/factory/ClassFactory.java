package graphic.factory;

import graphic.GraphicComponent;
import graphic.GraphicView;
import graphic.entity.ClassView;
import graphic.entity.ClassEntityView;

import java.awt.Rectangle;

import classDiagram.components.ClassEntity;
import classDiagram.components.Visibility;

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
public class ClassFactory extends ClassEntityFactory
{

	/**
	 * Create a new factory allowing the creation of a class.
	 * 
	 * @param parent
	 *            the graphic view
	 * @param dbDiagram
	 *            the class diagram
	 */
	public ClassFactory(GraphicView parent)
	{
		super(parent);
	}

	@Override
	public GraphicComponent create()
	{
		final ClassEntity classEntity = new ClassEntity("Class", Visibility.PUBLIC);
		final ClassEntityView c = new ClassView(parent, classEntity);

		c.setBounds(new Rectangle(mouseReleased.x - DEFAULT_SIZE.width / 2, mouseReleased.y - DEFAULT_SIZE.height / 2, DEFAULT_SIZE.width, DEFAULT_SIZE.height));

		parent.addEntity(c);
		classDiagram.addClass(classEntity);
		return c;
	}

}
