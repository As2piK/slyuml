/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package change;

import dbDiagram.components.Entity;
import dbDiagram.components.Visibility;

/**
 *
 * @author David
 */
public class BufferDB implements Changeable
{
	private Entity entity;
	private String name;
	private boolean isAbstract;
    private Visibility visibility;
	
	
	public BufferDB(Entity e)
	{
	    entity = e;
		name = e.getName();
		isAbstract = e.isAbstract();
		visibility = e.getVisibility();
	}

	@Override
	public void restore()
	{
		entity.setAbstract(isAbstract);
		entity.setVisibility(visibility);
		entity.setName(name);
		
		entity.notifyObservers();
	}
}
