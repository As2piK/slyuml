package change;

import dbDiagram.IDBDiagramComponent.UpdateMessage;
import dbDiagram.components.Field;
import dbDiagram.components.Entity;

public class BufferCreationField implements Changeable
{
  private Entity entity;
  private Field attribute;
  private boolean isCreated;
  private int index;
  
  public BufferCreationField(Entity e, Field a, Boolean isCreated, int index)
  {
    entity = e;
    attribute = a;
    this.isCreated = isCreated;
    this.index = index;
  }

  @Override
  public void restore()
  {
    
    if (!isCreated)
    {
      entity.addField(attribute);
      entity.notifyObservers(UpdateMessage.ADD_FIELD_NO_EDIT);
      
      entity.moveAttributePosition(attribute, index - entity.getFields().size() + 1);
      entity.notifyObservers();
    }
    else
    {
      entity.removeField(attribute);
      entity.notifyObservers();
    }
    
  }

}
