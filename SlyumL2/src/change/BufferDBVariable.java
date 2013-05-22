/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package change;

import dbDiagram.components.Variable;

/**
 *
 * @author David
 */
public class BufferDBVariable implements Changeable
{
	private Variable variable, copy;
	
	public BufferDBVariable(Variable variable)
	{
		this.variable = variable;
		copy = new Variable(variable);
	}

	@Override
	public void restore()
	{		
		variable.setVariable(copy);
	}
	
}
