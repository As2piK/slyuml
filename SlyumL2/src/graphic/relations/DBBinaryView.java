package graphic.relations;

import graphic.GraphicView;
import graphic.entity.ClassEntityView;
import graphic.entity.TableEntityView;
import graphic.textbox.ClassTextBoxRole;
import graphic.textbox.DBTextBoxRole;

import java.awt.Point;
import java.util.LinkedList;

import dbDiagram.DBDiagram;
import dbDiagram.relationships.Binary;
import dbDiagram.relationships.Role;


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
 * A BinaryView is associated with an Binary UML component.
 * 
 * @author David Miserez
 * @version 1.0 - 25.07.2011
 */
public class DBBinaryView extends DBAssociationView
{
	/**
	 * Create a new BinaryView between source and target.
	 * 
	 * @param parent
	 *            the graphic view
	 * @param source
	 *            the entity source
	 * @param target
	 *            the entity target
	 * @param binary
	 *            the binary UML
	 * @param posSource
	 *            the position for put the first MagneticGrip
	 * @param posTarget
	 *            the position for put the last MagneticGrip
	 * @param checkRecursivity
	 *            check if the relation is on itself
	 */
	public DBBinaryView(GraphicView parent, TableEntityView source, TableEntityView target, Binary binary, Point posSource, Point posTarget, boolean checkRecursivity)
	{
		super(parent, source, target, binary, posSource, posTarget, checkRecursivity);

		final LinkedList<Role> roles = binary.getRoles();

		DBTextBoxRole tb = new DBTextBoxRole(parent, roles.getFirst(), getFirstPoint());
		tbRoles.add(tb);
		parent.addOthersComponents(tb);

		tb = new DBTextBoxRole(parent, roles.getLast(), getLastPoint());
		tbRoles.add(tb);
		parent.addOthersComponents(tb);
	}
	
	@Override
	public void restore()
	{
		super.restore();
		
		if (this.getClass().equals(DBBinaryView.class))

			parent.getDBDiagram().addBinary((Binary)getAssociedComponent());
		
		repaint();
	}
}