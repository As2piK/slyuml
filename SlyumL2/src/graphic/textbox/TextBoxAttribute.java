package graphic.textbox;

import graphic.GraphicView;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.Observable;
import java.util.Observer;

import abstractDiagram.AbstractIDiagramComponent;
import abstractDiagram.components.AbstractVariable;
import utility.Utility;
import classDiagram.IClassDiagramComponent;
import classDiagram.IClassDiagramComponent.UpdateMessage;
import classDiagram.components.Attribute;

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
public class TextBoxAttribute extends TextBox implements Observer
{
	/**
	 * Get a String representing the Attribute.
	 * 
	 * @param attribute
	 *            the attribute to convert to String
	 * @return a String representing the Attribute.
	 */
	public static String getStringFromAttribute(AbstractVariable variable)
	{
		if (variable instanceof Attribute) {
			final String isConst = ((Attribute)variable).isConstant() ? " {const}" : "";
			return ((Attribute)variable).getVisibility().toCar() + " " + ((Attribute)variable).getName() + " : " + ((Attribute)variable).getType() + isConst;
		} else {
			return "875"; //TODO
		}
	}

	private final AbstractVariable variable;

	/**
	 * Create a new TextBoxAttribute with the given Attribute.
	 * 
	 * @param parent
	 *            the graphic view
	 * @param variable
	 *            the attribute
	 */
	public TextBoxAttribute(GraphicView parent, AbstractVariable variable)
	{
		super(parent, getStringFromAttribute(variable));

		if (variable == null)
			throw new IllegalArgumentException("attribute is null");

		this.variable = variable;
		variable.addObserver(this);
	}

	@Override
	public void createEffectivFont()
	{
		effectivFont = getFont();
	}

	@Override
	public AbstractIDiagramComponent getAssociedComponent()
	{
		return variable;
	}

	@Override
	public Rectangle getBounds()
	{
		return new Rectangle(bounds);
	}

	@Override
	public String getText()
	{
		return getStringFromAttribute(variable);
	}

	@Override
	public void initAttributeString(AttributedString ats)
	{
		if (variable instanceof Attribute) {
			if (((Attribute)variable).isConstant())
				ats.addAttribute(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
	
			if (((Attribute)variable).isStatic())
				ats.addAttribute(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON, 2, ats.getIterator().getEndIndex());
		}
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

			variable.select();

			if (select)
				variable.notifyObservers(UpdateMessage.SELECT);
			else
				variable.notifyObservers(UpdateMessage.UNSELECT);
		}
	}

	@Override
	public void setText(String text)
	{
		variable.setText(text);

		super.setText(getStringFromAttribute(variable));
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
			final String text = getStringFromAttribute(variable);

			super.setText(text);
		}

		repaint();
	}

}
