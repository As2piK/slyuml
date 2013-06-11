package graphic.factory;

import graphic.GraphicComponent;
import graphic.GraphicView;
import graphic.entity.ClassView;
import graphic.entity.ClassEntityView;
import graphic.entity.TableEntityView;
import graphic.relations.ClassBinaryView;
import graphic.relations.DBBinaryView;
import graphic.relations.MultiLineView;
import graphic.relations.MultiView;

import java.awt.Point;
import java.awt.Rectangle;

import dbDiagram.relationships.Binary;
import utility.SMessageDialog;


/**
 * BinaryFactory allows to create a new binary view associated with a new
 * association UML. Give this factory at the graphic view using the method
 * initNewComponent() for initialize a new factory. Next, graphic view will use
 * the factory to allow creation of a new component, according to the
 * specificity of the factory.
 * 
 * @author David Miserez
 * @version 1.0 - 25.07.2011
 */
public class BinaryDBRelationFactory extends DBRelationFactory
{
	public final String ERROR_CREATION_MESSAGE = "Association creation failed.\nYou must make a bond between two tables.";

	/**
	 * Create a new factory allowing the creation of a binary.
	 * 
	 * @param parent
	 *            the graphic view
	 * @param dbDiagram
	 *            the class diagram
	 */
	public BinaryDBRelationFactory(GraphicView parent)
	{
		super(parent);
	}

	@Override
	public GraphicComponent create()
	{
		if (componentMousePressed instanceof TableEntityView && componentMouseReleased instanceof TableEntityView)
		{
			final TableEntityView source = (TableEntityView) componentMousePressed;
			final TableEntityView target = (TableEntityView) componentMouseReleased;

			final Binary binary = new Binary(source.getComponent(), target.getComponent(), false);

			final DBBinaryView b = new DBBinaryView(parent, source, target, binary, mousePressed, mouseReleased, true);

			parent.addLineView(b);
			dbDiagram.addBinary(binary);

			parent.unselectAll();
			b.setSelected(true);

			return b;
		}
		return null; //TODO
		/*else
		{
			final MultiView multiView;
			final ClassView classView;

			if (componentMousePressed.getClass() == MultiView.class && componentMouseReleased instanceof ClassView)
			{
				multiView = (MultiView) componentMousePressed;
				classView = (ClassView) componentMouseReleased;
			}
			else if (componentMouseReleased.getClass() == MultiView.class && componentMousePressed instanceof ClassView)
			{
				multiView = (MultiView) componentMouseReleased;
				classView = (ClassView) componentMousePressed;
			}
			else
			{
				repaint();
				return null;
			}

			final Multi multi = (Multi) multiView.getAssociedComponent();
			final Role role = new Role(multi, (ClassEntity) classView.getAssociedComponent(), "");

			Rectangle bounds = multiView.getBounds();
			final Point multiPos = new Point((int) bounds.getCenterX(), (int) bounds.getCenterY());
			bounds = classView.getBounds();
			final Point classPos = new Point((int) bounds.getCenterX(), (int) bounds.getCenterY());

			final MultiLineView mlv = new MultiLineView(parent, multiView, classView, role, multiPos, classPos, false);
			multiView.addMultiLineView(mlv);

	        repaint();
			return mlv;
		}*/
	}
	
	@Override
	protected void creationFailed()
	{
		SMessageDialog.showErrorMessage(ERROR_CREATION_MESSAGE);
	}
}
