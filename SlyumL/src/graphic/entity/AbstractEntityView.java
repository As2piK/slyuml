package graphic.entity;

import graphic.GraphicView;
import graphic.MovableComponent;

import java.util.Observer;

import abstractDiagram.AbstractEntity;

/**
 * Represent the view of an entity in UML structure.
 * 
 * @author David Miserez
 * @version 1.0 - 25.07.2011
 */
public abstract class AbstractEntityView extends MovableComponent implements Observer
{

	public AbstractEntityView(GraphicView parent) {
		super(parent);
	}

	public abstract void adjustWidth();
	
	public abstract void setDisplayAttributes(boolean display);
	
	public abstract void updateHeight();
	
	public abstract AbstractEntity getComponent();
}
