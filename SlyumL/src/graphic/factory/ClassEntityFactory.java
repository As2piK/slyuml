package graphic.factory;

import graphic.GraphicComponent;
import graphic.GraphicView;
import graphic.entity.ClassEntityView;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;


/**
 * EntityFactory allows to create a new entity view associated with a new entity
 * UML. Give this factory at the graphic view using the method
 * initNewComponent() for initialize a new factory. Next, graphic view will use
 * the factory to allow creation of a new component, according to the
 * specificity of the factory.
 * 
 * @author David Miserez
 * @version 1.0 - 25.07.2011
 */
public abstract class ClassEntityFactory extends ClassComponentFactory
{
	public final static Dimension DEFAULT_SIZE = new Dimension(150, 61);
	private final Rectangle bounds;

	public ClassEntityFactory(GraphicView parent)
	{
		super(parent);

		bounds = new Rectangle(0, 0, DEFAULT_SIZE.width, DEFAULT_SIZE.height);
	}

	@Override
	public abstract GraphicComponent create();

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle(bounds);
	}

	@Override
	public Cursor getCursor()
	{
		return new Cursor(Cursor.MOVE_CURSOR);
	}

	@Override
	public void gMouseMoved(MouseEvent e)
	{
		Rectangle repaintBounds = new Rectangle(bounds);
		repaintBounds.grow(20, 20);

		final Point middle = new Point(e.getX() - DEFAULT_SIZE.width / 2, e.getY() - DEFAULT_SIZE.height / 2);

		int gs = GraphicView.getGridSize();
		int x = (middle.x / gs) * gs;
		int y = (middle.y / gs) * gs;
		
		bounds.setLocation(x, y);

		parent.getScene().repaint(repaintBounds);
		repaintBounds = new Rectangle(bounds);
		repaintBounds.grow(20, 20);
		parent.getScene().repaint(repaintBounds);
	}

	@Override
	public void gMouseReleased(MouseEvent e)
	{
		super.gMouseReleased(e);

		repaint();
	}

	@Override
	public void paintComponent(Graphics2D g2)
	{
		final Color basicColor = ClassEntityView.getBasicColor();
		final Color fillColor = new Color(basicColor.getRed(), basicColor.getGreen(), basicColor.getBlue(), 20);

		final GradientPaint backGradient = new GradientPaint(bounds.x, bounds.y, fillColor, bounds.x + bounds.width, bounds.y + bounds.height, fillColor.darker());

		g2.setPaint(backGradient);
		g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

		g2.setColor(fillColor.darker().darker().darker().darker().darker());
		g2.setStroke(new BasicStroke(ClassEntityView.BORDER_WIDTH));
		g2.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
	}

	@Override
	public void repaint()
	{
		final Rectangle repaintBounds = new Rectangle(bounds);
		repaintBounds.grow(10, 10);
		parent.getScene().repaint(repaintBounds);

	}
}
