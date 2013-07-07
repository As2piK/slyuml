package dbDiagram.components;

import java.util.LinkedList;

public class PrimaryKey extends ExternalKey {

	private LinkedList<Field> fields;

	public PrimaryKey(String pkName) {
		super(pkName);
		
		fields = new LinkedList<Field>();
	}
	
	public void addField(Field f) {
		if (!fields.contains(f)) {
			fields.add(f);
		}
	}
	
	public void removeField(Field f) {
		fields.remove(f);
	}

	public LinkedList<Field> getFields() {
		return fields;
	}
	
}