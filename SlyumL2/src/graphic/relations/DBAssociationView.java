package graphic.relations;

import graphic.GraphicComponent;
import graphic.GraphicView;
import graphic.entity.AssociationClassView;
import graphic.entity.ClassEntityView;
import graphic.entity.TableEntityView;
import graphic.textbox.TextBoxLabelTitle;

import java.awt.Graphics2D;
import java.awt.Point;

import abstractDiagram.IDiagramComponent;
import abstractDiagram.IDiagramComponent.UpdateMessage;
import dbDiagram.components.ForeignKey;
import dbDiagram.components.TableEntity;
import dbDiagram.components.dataType.IntDataType;
import dbDiagram.relationships.Association;
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
 * @author David Miserez
 * @version 1.0 - 25.07.2011
 */
public abstract class DBAssociationView extends RelationView
{
	private final Association association;
	private ForeignKey fk;

	public DBAssociationView(GraphicView parent, TableEntityView source, TableEntityView target, Association association, Point posSource, Point posTarget, boolean checkRecursivity)
	{
		super(parent, source, target, association, posSource, posTarget, checkRecursivity);

		this.association = association;
		association.setDirected(true);

		final TextBoxLabelTitle tb = new TextBoxLabelTitle(parent, association, this);
		tbRoles.add(tb);
		parent.addOthersComponents(tb);
		
		fk = new ForeignKey("fk_" + source.getComponent(), "fk_" + source.getComponent(), new IntDataType(), (Binary)association);
		
		source.getComponent().addField(fk);
		source.getComponent().notifyObservers(UpdateMessage.ADD_FIELD);
		
	}

	@Override
	public IDiagramComponent getAssociedComponent()
	{
		return association;
	}

	@Override
	public void paintComponent(Graphics2D g2)
	{
		super.paintComponent(g2);

		if (association.isDirected())
			DependencyView.paintExtremity(g2, points.get(points.size() - 2).getAnchor(), points.getLast().getAnchor());
	}

	@Override
	public boolean relationChanged(GraphicComponent oldCompo, GraphicComponent newCompo)
	{
		if (!(newCompo instanceof ClassEntityView) || newCompo.getClass() == AssociationClassView.class)

			return false;

		final TableEntity oldEntity = (TableEntity) oldCompo.getAssociedComponent();
		final Role role = association.searchRoleByEntity(oldEntity);
		role.setEntity((TableEntity) newCompo.getAssociedComponent());

		return super.relationChanged(oldCompo, newCompo);
	}

	@Override
	public void setSelected(boolean select)
	{
		if (isSelected() == select)
			return;

		super.setSelected(select);

		association.select();

		if (select)
			association.notifyObservers(UpdateMessage.SELECT);
		else
			association.notifyObservers(UpdateMessage.UNSELECT);
	}
	
	public ForeignKey getFk() {
		return fk;
	}
}
