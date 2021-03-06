package graphic.relations;

import graphic.GraphicView;
import graphic.entity.EntityView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import classDiagram.relationships.Aggregation;

/**
 * The LineView class represent a collection of lines making a link between two
 * GraphicComponent. When it creates, the LineView have one single line between
 * the two GraphicComponent. By clicking on the line, the user can personnalize
 * the LineView by adding new grips. When drawing, the LineView will draw a
 * segment between each grips. Grips are movable and a LineView have two special
 * grips; MagneticGrip. These grips are associated with a GraphicComponent and
 * can't be placed elsewhere.
 * 
 * A RelationView have an associated UML component.
 * 
 * An AssociationView is associated with an association UML component.
 * 
 * An AggregationView is associated with an Aggregation UML component.
 * 
 * @author David Miserez
 * @version 1.0 - 25.07.2011
 */
public class AggregationView extends BinaryView
{
	/**
	 * Paint the extremity of the relation in the direction given by the source
	 * point.
	 * 
	 * @param g2
	 *            the graphic context
	 * @param source
	 *            this point define the direction of the arrow
	 * @param target
	 *            this point define the location of the arrow
	 * @param borderColor
	 *            the color border
	 */
	public static void paintExtremity(Graphics2D g2, Point source, Point target, Color color, Color borderColor)
	{
		final double deltaX = target.x - source.x;
		final double deltaY = target.y - source.y;
		final double alpha = Math.atan2(deltaY, deltaX);
		final double length = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

		double x = Math.cos(alpha) * (length - 13.0) + source.x;
		double y = Math.sin(alpha) * (length - 13.0) + source.y;

		final Point ref = new Point((int) x, (int) y);

		x = Math.cos(alpha) * (length - 26.0) + source.x;
		y = Math.sin(alpha) * (length - 26.0) + source.y;

		final Point ref2 = new Point((int) x, (int) y);

		final int vectorX = target.x - (int) (Math.cos(alpha) * (length - 7.0) + source.x);
		final int vectorY = target.y - (int) (Math.sin(alpha) * (length - 7.0) + source.y);

		final int vectorXN1 = -vectorY;
		final int vectorYN1 = vectorX;
		final int vectorXN2 = vectorY;
		final int vectorYN2 = -vectorX;

		final int[] pointsX = new int[] { target.x, ref.x + vectorXN1, ref2.x, ref.x + vectorXN2 };
		final int[] pointsY = new int[] { target.y, ref.y + vectorYN1, ref2.y, ref.y + vectorYN2 };

		g2.setStroke(new BasicStroke());
		g2.setColor(color);
		g2.fillPolygon(pointsX, pointsY, pointsX.length);
		g2.setColor(borderColor);
		g2.drawPolygon(pointsX, pointsY, pointsX.length);
	}

	/**
	 * Create a new AggregationView between source and target.
	 * 
	 * @param parent
	 *            the graphic view
	 * @param source
	 *            the entity source
	 * @param target
	 *            the entity target
	 * @param aggregation
	 *            the aggregation UML
	 * @param posSource
	 *            the position for put the first MagneticGrip
	 * @param posTarget
	 *            the position for put the last MagneticGrip
	 * @param checkRecursivity
	 *            check if the relation is on itself
	 */
	public AggregationView(GraphicView parent, EntityView source, EntityView target, Aggregation aggregation, Point posSource, Point posTarget, boolean checkRecursivity)
	{
		super(parent, source, target, aggregation, posSource, posTarget, checkRecursivity);
	}

	@Override
	protected void drawExtremity(Graphics2D g2, Point source, Point target)
	{
		paintExtremity(g2, points.get(1).getAnchor(), points.getFirst().getAnchor(), Color.WHITE, getColor());
	}

	@Override
	public void restore()
	{
		super.restore();
		parent.getClassDiagram().addAggregation((Aggregation)getAssociedComponent());
		
		repaint();
	}
}
