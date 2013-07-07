package swing.propretiesView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import swing.JPanelRounded;
import abstractDiagram.IDiagramComponent.UpdateMessage;
import dbDiagram.components.Field;
import dbDiagram.components.ForeignKey;
import dbDiagram.relationships.Binary;

/**
 * Show the propreties of an UML entity with Swing components. All inner classes
 * are used for create customized JTable.
 * 
 * @author David Miserez
 * @version 1.0 - 28.07.2011
 */
public class DBEntityPropreties extends GlobalPropreties
{
	
	private static DBEntityPropreties instance = new DBEntityPropreties();

	private static final long serialVersionUID = 7817631106855232540L;

	/**
	 * Get the unique instance of this class.
	 * 
	 * @return the unique instance of EntityPropreties
	 */
	public static DBEntityPropreties getInstance()
	{
		return instance;
	}

	/**
	 * Set the given size for preferredSize, maximumSize and minimumSize to the
	 * given component.
	 * 
	 * @param component
	 *            the component to resize
	 * @param size
	 *            the size
	 */
	public static void setAllSize(JComponent component, Dimension size)
	{
		component.setPreferredSize(size);
		component.setMaximumSize(size);
		component.setMinimumSize(size);
	}

	JTextField textName = new JTextField();
	JComboBox<Field> fieldComboBox = new JComboBox<Field>();
	
	Field currentSelected;

	protected DBEntityPropreties()
	{		

		final JPanelRounded relationPanel = new JPanelRounded();
		relationPanel.setForeground(Color.GRAY);
		relationPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		add(relationPanel);
		
		relationPanel.add(createEntityPropreties());
	}

	public JPanel createEntityPropreties()
	{
		final JPanel panel = new JPanel();
		Dimension size = new Dimension(200, 110);
		setAllSize(panel, size);
		panel.setOpaque(false);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setAlignmentY(TOP_ALIGNMENT);
		panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

		final JPanel panelTitle = new JPanel(new FlowLayout());
		panelTitle.setOpaque(false);
		panelTitle.add(createTitleLabel("Relation"));

		size = new Dimension(200, 20);
		setAllSize(panelTitle, new Dimension((int) size.getWidth(), 40));
		setAllSize(textName, size);

		panelTitle.setAlignmentX(LEFT_ALIGNMENT);
		textName.setAlignmentX(LEFT_ALIGNMENT);

		// Event
		textName.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e)
			{
				if (e.getKeyChar() == '\n')
				{
					final Binary entity = (Binary) currentObject;

					entity.setName(textName.getText());
					entity.notifyObservers();
				}
			}
		});
		
		fieldComboBox.setAlignmentX(LEFT_ALIGNMENT);

		fieldComboBox.addPopupMenuListener(new PopupMenuListener() {
			
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent arg0) {
				fieldComboBox.removeAllItems();
				for (Field f : ((Binary)currentObject).getSource().getFields()) {
					fieldComboBox.addItem(f);
				}
				for (ForeignKey f : ((Binary)currentObject).getSource().getForeignKeys()) {
					if (f.getBinary() == (Binary)currentObject) {
						currentSelected = f;
						fieldComboBox.setSelectedItem(f);
					}
				}
			}
			
			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				//if (currentSelected != null && currentSelected != fieldComboBox.getSelectedItem()) {
					
					System.out.println(currentSelected);
				
					//Ajout de la nouvelle FK
					ForeignKey newFK = new ForeignKey((Field)fieldComboBox.getSelectedItem(), "TODO", (Binary)currentObject);
					int index = ((Binary)currentObject).getSource().getFields().indexOf((Field)fieldComboBox.getSelectedItem());
					((Binary)currentObject).getSource().removeField((Field)fieldComboBox.getSelectedItem());
					((Binary)currentObject).getSource().addField(index, newFK);
					
					//Suppression de l'ancienne FK
					Field newField = new Field(currentSelected);
					index = ((Binary)currentObject).getSource().getFields().indexOf(currentSelected);
					((Binary)currentObject).getSource().removeField(currentSelected);
					((Binary)currentObject).getSource().addField(index, newField);
					
					//Field newField = new Field(currentSelected);
					
					for (Field f : ((Binary)currentObject).getSource().getFields()) {
						System.out.println(f.getClass());
					}
					
				//}
			}
			
			@Override
			public void popupMenuCanceled(PopupMenuEvent arg0) {}
		});

		panel.add(panelTitle);
		
		panel.add(textName);
		
		panel.add(fieldComboBox);

		return panel;
	}	

	public JLabel createTitleLabel(String text)
	{
		final JLabel label = new JLabel(text);
		label.setFont(label.getFont().deriveFont(20.0f));
		label.setAlignmentX(CENTER_ALIGNMENT);

		return label;
	}

	public JPanel createWhitePanel()
	{
		final JPanel panel = new JPanel();
		panel.setBackground(SystemColor.control);
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setAlignmentY(TOP_ALIGNMENT);
		panel.setOpaque(false);
		return panel;
	}

	@Override
	public void updateComponentInformations(UpdateMessage msg)
	{

		if (currentObject == null)
			return;

		if (currentObject instanceof Binary) {

			final Binary entity = (Binary) currentObject;

			if (msg != null && msg.equals(UpdateMessage.UNSELECT))
				if (!entity.getName().equals(textName.getText()))
					entity.setName(textName.getText());
			entity.notifyObservers();

			if (msg != null && msg.equals(UpdateMessage.SELECT)) {
				fieldComboBox.removeAllItems();
				if (((Binary)currentObject).getSource() != null) {
					for (Field f : ((Binary)currentObject).getSource().getFields()) {
						fieldComboBox.addItem(f);
					}
				}
			}	
			textName.setText(entity.getName());
			
			validate();
		} else if (currentObject instanceof dbDiagram.components.TableEntity) {
		
			final dbDiagram.components.TableEntity entity = (dbDiagram.components.TableEntity) currentObject;

	
			if (msg != null && msg.equals(UpdateMessage.UNSELECT))
				if (!entity.getName().equals(textName.getText()))
					if (!entity.setName(textName.getText()))
						textName.setText(entity.getName());
					else
						entity.notifyObservers();
	
			textName.setText(entity.getName());
	
			validate();
		}
	}
}
