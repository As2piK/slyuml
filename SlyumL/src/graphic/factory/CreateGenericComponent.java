package graphic.factory;

import java.awt.Cursor;

import graphic.GraphicComponent;
import graphic.GraphicView;

/**
 * CreateComponent allows to create a new graphic component view. Give this
 * factory at the graphic view using the method initNewComponent() for
 * initialize a new factory. Next, graphic view will use the factory to allow
 * creation of a new component, according to the specificity of the factory.
 * 
 * @author David Miserez
 * @version 1.0 - 25.07.2011
 */
public abstract class CreateGenericComponent extends GraphicComponent
{

	public CreateGenericComponent(GraphicView parent)
	{
		super(parent);
	}
	
	public abstract Cursor getCursor();
	public abstract void deleteFactory();
}
