/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package change;

import dbDiagram.components.TableEntity;
import dbDiagram.components.Visibility;

/**
 *
 * @author David
 */
public class BufferDB implements Changeable
{
	private TableEntity entity;
	private String name;
	
	
	public BufferDB(TableEntity e)
	{
	    entity = e;
		name = e.getName();
	}

	@Override
	public void restore()
	{
		entity.setName(name);
		
		entity.notifyObservers();
	}
}
