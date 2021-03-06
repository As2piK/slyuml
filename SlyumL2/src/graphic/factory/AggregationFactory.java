package graphic.factory;

import graphic.GraphicComponent;
import graphic.GraphicView;
import graphic.entity.ClassEntityView;
import graphic.relations.AggregationView;

import java.awt.Color;
import java.awt.Graphics2D;

import classDiagram.relationships.Aggregation;

/**
 * AggregationFactory allows to create a new aggregation view associated with a
 * new association UML. Give this factory at the graphic view using the method
 * initNewComponent() for initialize a new factory. Next, graphic view will use
 * the factory to allow creation of a new component, according to the
 * specificity of the factory.
 * 
 * @author David Miserez
 * @version 1.0 - 25.07.2011
 */
public class AggregationFactory extends RelationFactory
{

	/**
	 * Create a new factory allowing the creation of an aggregation.
	 * 
	 * @param parent
	 *            the graphic view
	 * @param dbDiagram
	 *            the class diagram
	 */
	public AggregationFactory(GraphicView parent)
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

			final Aggregation aggregation = new Aggregation(source.getComponent(), target.getComponent(), false);
			final AggregationView a = new AggregationView(parent, source, target, aggregation, mousePressed, mouseReleased, true);

			parent.addLineView(a);
			classDiagram.addAggregation(aggregation);

			parent.unselectAll();
			a.setSelected(true);

			return a;
		}

		repaint();
		return null;
	}

	@Override
	protected void drawExtremity(Graphics2D g2)
	{
		AggregationView.paintExtremity(g2, mouseLocation, mousePressed, Color.WHITE, Color.DARK_GRAY);
	}
}
