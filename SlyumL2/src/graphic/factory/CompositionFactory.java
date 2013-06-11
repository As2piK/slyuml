package graphic.factory;

import graphic.GraphicComponent;
import graphic.GraphicView;
import graphic.entity.ClassEntityView;
import graphic.relations.AggregationView;
import graphic.relations.CompositionView;

import java.awt.Color;
import java.awt.Graphics2D;

import classDiagram.relationships.Composition;

/**
 * CompositionFactpry allows to create a new composition view associated with a
 * new association UML. Give this factory at the graphic view using the method
 * initNewComponent() for initialize a new factory. Next, graphic view will use
 * the factory to allow creation of a new component, according to the
 * specificity of the factory.
 * 
 * @author David Miserez
 * @version 1.0 - 25.07.2011
 */
public class CompositionFactory extends ClassRelationFactory
{
	/**
	 * Create a new factory allowing the creation of a composition.
	 * 
	 * @param parent
	 *            the graphic view
	 * @param dbDiagram
	 *            the class diagram
	 */
	public CompositionFactory(GraphicView parent)
	{
		super(parent);
	}

	@Override
	public GraphicComponent create()
	{
		if (componentMousePressed instanceof ClassEntityView && componentMouseReleased instanceof ClassEntityView)
		{
			final ClassEntityView source = (ClassEntityView) componentMousePressed;
			final ClassEntityView target = (ClassEntityView) componentMouseReleased;

			final Composition composition = new Composition(source.getComponent(), target.getComponent(), false);
			final CompositionView c = new CompositionView(parent, source, target, composition, mousePressed, mouseReleased, true);

			parent.addLineView(c);
			classDiagram.addComposition(composition);

			parent.unselectAll();
			c.setSelected(true);

			return c;
		}

		repaint();
		return null;
	}

	@Override
	protected void drawExtremity(Graphics2D g2)
	{
		AggregationView.paintExtremity(g2, mouseLocation, mousePressed, Color.BLACK, Color.DARK_GRAY);
	}
}
