package graphic.factory;

import graphic.GraphicComponent;
import graphic.ClassGraphicView;
import graphic.entity.ClassEntityView;
import graphic.relations.InnerClassView;

import java.awt.Color;
import java.awt.Graphics2D;

import utility.SMessageDialog;

import classDiagram.relationships.Inheritance;
import classDiagram.relationships.InnerClass;

public class InnerClassFactory extends InheritanceFactory
{
	public final String ERROR_CREATION_MESSAGE = "Inner class creation failed.\nYou must make a bond between two classes or class -> interface.";

	public InnerClassFactory(ClassGraphicView parent)
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

				final InnerClass innerClass = new InnerClass(source.getComponent(), target.getComponent());
				final InnerClassView i = new InnerClassView(parent, source, target, innerClass, mousePressed, mouseReleased, true);

				parent.addLineView(i);
				classDiagram.addInnerClass(innerClass);

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
		InnerClassView.paintExtremity(g2, mousePressed, mouseLocation, Color.DARK_GRAY);
	}
	
	@Override
	protected void creationFailed()
	{
		SMessageDialog.showErrorMessage(ERROR_CREATION_MESSAGE);
	}
}
