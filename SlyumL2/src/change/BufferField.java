/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package change;

import abstractDiagram.IDiagramComponent.UpdateMessage;
import dbDiagram.components.Field;

/**
 *
 * @author David
 */
public class BufferField extends BufferDBVariable
{
	private Field attribute, copy;
	
	public BufferField(Field attribute)
	{
		super(attribute);
		this.attribute = attribute;
		copy = new Field(attribute);
	}

	@Override
	public void restore()
	{
		super.restore();
		attribute.setField(copy);
		
		attribute.select();
		attribute.notifyObservers(UpdateMessage.SELECT);
	}
	
}
