/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package change;

import classDiagram.components.Variable;

/**
 *
 * @author David
 */
public class BufferClassVariable implements Changeable
{
	private Variable variable, copy;
	
	public BufferClassVariable(Variable variable)
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
