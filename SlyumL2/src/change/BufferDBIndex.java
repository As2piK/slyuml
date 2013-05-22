/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package change;

import dbDiagram.IDBDiagramComponent;
import java.util.LinkedList;

/**
 *
 * @author David
 */
public class BufferDBIndex<T extends Object> implements Changeable
{
	private IDBDiagramComponent entity;
	private T o;
	private int index;
	private LinkedList<T> list;
	
	public BufferDBIndex(IDBDiagramComponent e, LinkedList<T> list, T o)
	{
		entity = e;
		this.o = o;
		this.list = list;
		index = list.indexOf(o);
	}

	@Override
	public void restore()
	{
		IDBDiagramComponent i = (IDBDiagramComponent)o;
		list.remove(o);
		list.add(index, o);
		
		entity.select();
		entity.notifyObservers();
		i.select();
		i.notifyObservers(IDBDiagramComponent.UpdateMessage.SELECT);
	}
}
