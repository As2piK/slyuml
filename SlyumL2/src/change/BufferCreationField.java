package change;

import abstractDiagram.IDiagramComponent.UpdateMessage;
import dbDiagram.components.Field;
import dbDiagram.components.TableEntity;

public class BufferCreationField implements Changeable
{
  private TableEntity entity;
  private Field attribute;
  private boolean isCreated;
  private int index;
  
  public BufferCreationField(TableEntity e, Field a, Boolean isCreated, int index)
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
      
      entity.moveFieldPosition(attribute, index - entity.getFields().size() + 1);
      entity.notifyObservers();
    }
    else
    {
      entity.removeField(attribute);
      entity.notifyObservers();
    }
    
  }

}
