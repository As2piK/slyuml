package graphic.textbox;

import graphic.GraphicView;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Observable;
import java.util.Observer;

import utility.Utility;
import classDiagram.IClassDiagramComponent;
import classDiagram.IClassDiagramComponent.UpdateMessage;
import dbDiagram.components.Field;

/**
 * A TextBox is a graphic component from Slyum containing a String. The
 * particularity of a TextBox is it can be moved with mouse and its String can
 * be edited by double-click on it.
 * 
 * A TextBoxAttribute is a TextBox displaying an Attribute (UML). When editing
 * the text, this TextBox parse the String to change it into an Attribute. It
 * listening Attribute changes for auto-update itself.
 * 
 * @author David Miserez
 * @version 1.0 - 25.07.2011
 */
public class TextBoxField extends TextBox implements Observer
{

	private final Field field;

	/**
	 * Create a new TextBoxAttribute with the given Attribute.
	 * 
	 * @param parent
	 *            the graphic view
	 * @param field
	 *            the attribute
	 */
	public TextBoxField(GraphicView parent, Field field)
	{
		super(parent, field.getName());

		this.field = field;
		field.addObserver(this);
	}

	@Override
	public void createEffectivFont()
	{
		effectivFont = getFont();
	}

	@Override
	public IClassDiagramComponent getAssociedComponent()
	{
		//return field; TODO
		return null;
	}

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle(bounds);
	}

	@Override
	public String getText()
	{
		return field.getName();
	}

	@Override
	public void setBounds(Rectangle bounds)
	{
		if (bounds == null)
			throw new IllegalArgumentException("bounds is null");

		this.bounds = new Rectangle(bounds);
	}

	@Override
	public void setSelected(boolean select)
	{
		if (isSelected() != select)
		{
			super.setSelected(select);

			field.select();

			if (select)
				field.notifyObservers(UpdateMessage.SELECT);
			else
				field.notifyObservers(UpdateMessage.UNSELECT);
		}
	}

	@Override
	public void setText(String text)
	{
		field.setText(text);

		super.setText(field.getName());
	}

	@Override
	protected String truncate(Graphics2D g2, String text, int width)
	{
		return Utility.truncate(g2, text, width);
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		if (arg1 != null && arg1 instanceof UpdateMessage)
			switch ((UpdateMessage) arg1)
			{
				case SELECT:
					setSelected(true);
					break;
				case UNSELECT:
					setSelected(false);
					break;
			}
		else
		{

			super.setText(field.getName());
		}

		repaint();
	}

}