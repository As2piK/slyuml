/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package change;

import abstractDiagram.IDiagramComponent.UpdateMessage;
import classDiagram.components.Attribute;

/**
 *
 * @author David
 */
public class BufferAttribute extends BufferClassVariable
{
	private Attribute attribute, copy;
	
	public BufferAttribute(Attribute attribute)
	{
		super(attribute);
		this.attribute = attribute;
		copy = new Attribute(attribute);
	}

	@Override
	public void restore()
	{
		super.restore();
		attribute.setAttribute(copy);
		
		attribute.select();
		attribute.notifyObservers(UpdateMessage.SELECT);
	}
	
}
