package abstractDiagram.components;

import java.util.Observable;

import abstractDiagram.IDiagramComponent;


public abstract class AbstractVariable extends Observable implements IDiagramComponent {

	public abstract void setText(String text);
	
}
