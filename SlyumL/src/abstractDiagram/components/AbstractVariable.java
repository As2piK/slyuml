package abstractDiagram.components;

import java.util.Observable;

import abstractDiagram.AbstractIDiagramComponent;


public abstract class AbstractVariable extends Observable implements AbstractIDiagramComponent {

	public abstract void setText(String text);
	
}
