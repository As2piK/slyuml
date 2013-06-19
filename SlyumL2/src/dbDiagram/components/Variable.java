package dbDiagram.components;

import java.util.Observable;

import javax.swing.JOptionPane;

import abstractDiagram.components.AbstractVariable;
import utility.Utility;
import change.BufferClassVariable;
import change.BufferDBVariable;
import change.Change;
import dbDiagram.DBDiagram;
import dbDiagram.IDBDiagramComponent;
import dbDiagram.verifyName.VariableName;

/**
 * Represent a variable in UML structure.
 * 
 * @author David Miserez
 * @version 1.0 - 24.07.2011
 */
public class Variable extends AbstractVariable
{
	public static final String REGEX_SEMANTIC_ATTRIBUTE = "[a-zA-Z_"+Type.accents+"][\\w_"+Type.accents+"]*";
	
	public static boolean checkSemantic(String name)
	{
		return name.matches(REGEX_SEMANTIC_ATTRIBUTE);
	}

	protected boolean constant = false;

	protected final int id = DBDiagram.getNextId();
	protected String name;
	protected Type type = PrimitiveType.INTEGER_TYPE;

	/**
	 * Create a new variable with the given name and type.
	 * 
	 * @param name
	 *            the name for the variable
	 * @param type
	 *            the type for the variable
	 * @throws SyntaxeException 
	 */
	public Variable(String name, Type type)
	{
		boolean isBlocked = Change.isBlocked();
		Change.setBlocked(true);
		
		while (!setName(name))
		    throw new IllegalArgumentException("Syntaxe error.");
		
		setType(type);
		
		Change.setBlocked(isBlocked);
	}
	
	/**
	 * Constructor of copy.
	 * @param variable variable
	 */
	public Variable(Variable variable)
	{
		this.name = variable.name;
		this.type = new Type(variable.type.getName());
	}
	
	public void setVariable(Variable variable)
	{
		boolean isRecord = Change.isRecord();
		Change.record();
		
		setName(variable.getName());
		setType(variable.getType());
		
		if(!isRecord)
			Change.stopRecord();
		
		notifyObservers();
	}

	@Override
	public int getId()
	{
		return id;
	}

	/**
	 * Get the name for this variable.
	 * 
	 * @return the name for this variable
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Get the type for this variable.
	 * 
	 * @return the type for this variable.
	 */
	public Type getType()
	{
		setChanged();

		return type;
	}

	@Override
	public void select()
	{
		setChanged();
	}

	/**
	 * Set the name for this variable.
	 * 
	 * @param name
	 *            the new name for this variable
	 */
	public boolean setName(String name)
	{
		if (!VariableName.getInstance().verifyName(name) || name.equals(getName()))
			return false;

		Change.push(new BufferDBVariable(this));
		this.name = name;
		Change.push(new BufferDBVariable(this));

		setChanged();

		return true;
	}

	/**
	 * Set the type for this variable.
	 * 
	 * @param type
	 *            the new type for this variable
	 */
	public void setType(Type type)
	{		
		if (getType() != null && type.getName().equals(getType().getName()))
			return;
		
		Change.push(new BufferDBVariable(this));
		this.type = type;
		Change.push(new BufferDBVariable(this));

		setChanged();
	}

	@Override
	public String toString()
	{
		return name + " : " + type;
	}

	@Override
	public String toXML(int depth)
	{
		final String tab = Utility.generateTab(depth);

		return tab + "<variable " + "name=\"" + name + "\" " + "type=\"" + type.toXML(depth+1) + "\" " + "const=\"" + constant + "\"/>";
	}
	
	@Override
	public void setText(String text) {
		JOptionPane.showMessageDialog(null, "ERREUR #321");
	}
}
