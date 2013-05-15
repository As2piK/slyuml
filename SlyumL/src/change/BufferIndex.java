/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package change;

import classDiagram.IClassDiagramComponent;
import java.util.LinkedList;

/**
 *
 * @author David
 */
public class BufferIndex<T extends Object> implements Changeable
{
	private IClassDiagramComponent entity;
	private T o;
	private int index;
	private LinkedList<T> list;
	
	public BufferIndex(IClassDiagramComponent e, LinkedList<T> list, T o)
	{
		entity = e;
		this.o = o;
		this.list = list;
		index = list.indexOf(o);
	}

	@Override
	public void restore()
	{
		IClassDiagramComponent i = (IClassDiagramComponent)o;
		list.remove(o);
		list.add(index, o);
		
		entity.select();
		entity.notifyObservers();
		i.select();
		i.notifyObservers(IClassDiagramComponent.UpdateMessage.SELECT);
	}
}
