package graphic.factory;

import graphic.GraphicComponent;
import graphic.GraphicView;
import graphic.entity.ClassEntityView;
import graphic.relations.InheritanceView;

import java.awt.Color;
import java.awt.Graphics2D;

import utility.SMessageDialog;

import classDiagram.relationships.Inheritance;

/**
 * InheritanceFactory allows to create a new inheritance view associated with a
 * new association UML. Give this factory at the graphic view using the method
 * initNewComponent() for initialize a new factory. Next, graphic view will use
 * the factory to allow creation of a new component, according to the
 * specificity of the factory.
 * 
 * @author David Miserez
 * @version 1.0 - 25.07.2011
 */
public class InheritanceFactory extends RelationFactory
{
	public final String ERROR_CREATION_MESSAGE = "Inheritance creation failed.\nYou must make a bond between two classes or class -> interface.";

	/**
	 * Create a new factory allowing the creation of an inheritance.
	 * 
	 * @param parent
	 *            the graphic view
	 * @param dbDiagram
	 *            the class diagram
	 */
	public InheritanceFactory(GraphicView parent)
	{
		super(parent);
	}

	@Override
	public GraphicComponent create()
	{
		try
		{
			if (componentMousePressed instanceof ClassEntityView && componentMouseReleased instanceof ClassEntityView)
			{
				final ClassEntityView source = (ClassEntityView) componentMousePressed;
				final ClassEntityView target = (ClassEntityView) componentMouseReleased;

				if (!Inheritance.validate(source.getComponent(), target.getComponent()))
				{
					repaint();
					return null;
				}

				final Inheritance inheritance = new Inheritance(source.getComponent(), target.getComponent());
				final InheritanceView i = new InheritanceView(parent, source, target, inheritance, mousePressed, mouseReleased, true);

				parent.addLineView(i);
				classDiagram.addInheritance(inheritance);

				parent.unselectAll();
				i.setSelected(true);

				return i;
			}
		} catch (final IllegalArgumentException e)
		{
			System.err.println("Inheritance relation between class (child) and interface (parent) is not possible.");
		}

		repaint();
		return null;
	}

	@Override
	protected void drawExtremity(Graphics2D g2)
	{
		InheritanceView.paintExtremity(g2, mousePressed, mouseLocation, Color.DARK_GRAY);
	}
	
	@Override
	protected void creationFailed()
	{
		SMessageDialog.showErrorMessage(ERROR_CREATION_MESSAGE);
	}
}
