package swing.propretiesView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import swing.JPanelRounded;
import swing.SButton;
import swing.Slyum;
import utility.PersonalizedIcon;
import utility.Utility;
import abstractDiagram.IDiagramComponent.UpdateMessage;
import classDiagram.verifyName.TypeName;
import dbDiagram.components.Field;
import dbDiagram.components.TableEntity;
import dbDiagram.components.dataType.AbstractDataType;
import dbDiagram.components.dataType.AbstractDataType.DataType;

/**
 * Show the propreties of an UML entity with Swing components. All inner classes
 * are used for create customized JTable.
 * 
 * @author David Miserez
 * @version 1.0 - 28.07.2011
 */
public class TableProperties extends GlobalPropreties
{
	private class FieldTableModel extends AbstractTableModel implements Observer, TableModelListener, MouseListener
	{
		private static final long serialVersionUID = 5735895585153401565L;

		private final String[] columnNames = { "Name", "Type", "Size", "Default", "PK", "Nullable" };

		private final LinkedList<Object[]> data = new LinkedList<Object[]>();

		private final HashMap<Field, Integer> mapIndex = new HashMap<Field, Integer>();

		public void addField(Field field)
		{
			data.add(new Object[] { field.getName(), field.getType(), field.getType().getSize(), field.getType().getDefaultValue(), field.isPK(), field.isNullable() });

			field.addObserver(this);
			mapIndex.put(field, data.size() - 1);

			fireTableRowsInserted(0, data.size());
		}

		public void clearAll()
		{
			data.clear();
			mapIndex.clear();
			fireTableDataChanged();
		}

		@Override
		public Class<? extends Object> getColumnClass(int c)
		{
			if (getValueAt(0, c) == null) {
				System.out.println(c);
			}
			return getValueAt(0, c).getClass();
		}

		@Override
		public int getColumnCount()
		{
			return columnNames.length;
		}

		@Override
		public String getColumnName(int col)
		{
			return columnNames[col];
		}

		@SuppressWarnings("unchecked")
		public HashMap<Field, Integer> getMapIndex()
		{
			return (HashMap<Field, Integer>) mapIndex.clone();
		}

		@Override
		public int getRowCount()
		{
			return data.size();
		}

		@Override
		public Object getValueAt(int row, int col)
		{
			return data.get(row)[col];
		}

		@Override
		public boolean isCellEditable(int row, int col)
		{
			return true;
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			if (currentObject == null || !(currentObject instanceof TableEntity))
				return;

			// Get the selected field
			final int index = fieldsTable.getSelectionModel().getLeadSelectionIndex();
			final Field field = Utility.getKeysByValue(mapIndex, index).iterator().next();

			// Unselect all fields
			for (final Field f : ((TableEntity) currentObject).getFields())
			{
				if (f.equals(field))
					continue;

				f.select();
				f.notifyObservers(UpdateMessage.UNSELECT);
			}

			// Select the selected field
			field.select();
			field.notifyObservers(UpdateMessage.SELECT);
		}

		
		@Override
		public void mouseReleased(MouseEvent e)
		{
		}

		public void setField(Field field, int index)
		{
			data.set(index, new Object[] { field.getName(), field.getType().getName(), field.getType().getSize(), field.getType().getDefaultValue(), field.isPK(), field.isNullable() });

			fireTableRowsUpdated(index, index);
		}

		@Override
		public void setValueAt(Object value, int row, int col)
		{
			data.get(row)[col] = value;

			fireTableCellUpdated(row, col);
		}

		@Override
		public void tableChanged(TableModelEvent e)
		{
			final int row = e.getFirstRow();
			final int column = e.getColumn();

			if (column == -1)
				return;

			final TableModel model = (TableModel) e.getSource();
			final Object data = model.getValueAt(row, column);
			final Field field = Utility.getKeysByValue(mapIndex, row).iterator().next();
			
			switch (column)
			{
				case 0: // nom
				    
					if (field.setName((String) data))
						setValueAt(field.getName(), row, column);

					break;

				case 1: // type
					String s = ((AbstractDataType) data).toString();
					
					field.setType(DataType.getNewDataTypeObjectFromDataType(DataType.getDataTypeFromName(s)));

					break;

				case 2 : // Size
					field.getType().setSize((Integer)data);
					break;
					
				case 3 : // DefaultValue
					field.getType().setDefaultValue(data);
					break;
					
				case 4: // PK
					field.setPK((Boolean) data);
					break;
					
				case 5: // Nullable
					field.setNullable((Boolean) data);
					break;
			}

			field.notifyObservers(UpdateMessage.SELECT);
			field.getType().notifyObservers();

			fieldsTable.addRowSelectionInterval(row, row);
		}

		@Override
		public void update(Observable arg0, Object arg1)
		{
			final Field field = (Field) arg0;
			try
			{
				final int index = mapIndex.get(field);

				if (index == -1)
					return;

				if (arg1 != null && arg1 instanceof UpdateMessage)
					switch ((UpdateMessage) arg1)
					{
						case SELECT:
							btnRemoveField.setEnabled(true);
							btnUpField.setEnabled(index > 0);
							btnDownField.setEnabled(index < mapIndex.size() - 1);
							showInProperties();
							fieldsTable.addRowSelectionInterval(index, index);
							break;
						case UNSELECT:
							fieldsTable.removeRowSelectionInterval(index, index);
							break;
					}

				setField(field, index);
			} catch (final Exception e)
			{

			}
		}

	}

	private static TableProperties instance = new TableProperties();

	private static final long serialVersionUID = 7817631106855232540L;

	/**
	 * Get the unique instance of this class.
	 * 
	 * @return the unique instance of EntityPropreties
	 */
	public static TableProperties getInstance()
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

	JTable fieldsTable;
	
	private final JButton btnAddField,
			btnRemoveField,
			btnUpField,
			btnDownField;
	
	private final JLabel imgNoField, imgNoMethod, imgMethodSelected,
			imgNoParameter;

	JPanel panelParameters;

	private JScrollPane scrollPaneFields;

	JTextField textName = new JTextField();

	protected TableProperties()
	{
		String small = ".png";
		
		if (Slyum.getSmallIcons())
			small = "_small.png";
			
		btnRemoveField = new SButton(PersonalizedIcon.createImageIcon(Slyum.ICON_PATH + "button_red_delete" + small), Color.RED, "Remove");
		btnUpField = new SButton(PersonalizedIcon.createImageIcon(Slyum.ICON_PATH + "button_violet_up" + small), Color.MAGENTA, "Up");
		btnDownField = new SButton(PersonalizedIcon.createImageIcon(Slyum.ICON_PATH + "button_violet_down" + small), Color.MAGENTA, "Down");
		btnAddField = new SButton(PersonalizedIcon.createImageIcon(Slyum.ICON_PATH + "button_plus_blue" + small), Color.BLUE, "Add");
		
		imgNoField = new JLabel(PersonalizedIcon.createImageIcon(Slyum.ICON_PATH + "empty_field.png"));
		imgNoMethod = new JLabel(PersonalizedIcon.createImageIcon(Slyum.ICON_PATH + "empty_method.png"));
		imgMethodSelected = new JLabel(PersonalizedIcon.createImageIcon(Slyum.ICON_PATH + "select_method.png"));
		imgNoParameter = new JLabel(PersonalizedIcon.createImageIcon(Slyum.ICON_PATH + "empty_parameter.png"));

		imgNoMethod.setAlignmentX(CENTER_ALIGNMENT);
		imgNoField.setAlignmentX(CENTER_ALIGNMENT);
		imgMethodSelected.setAlignmentX(CENTER_ALIGNMENT);
		imgNoParameter.setAlignmentX(CENTER_ALIGNMENT);

		imgNoParameter.setVisible(false);

		setBackground(Color.WHITE);
		fieldsTable = new JTable(new FieldTableModel());
		fieldsTable.setPreferredScrollableViewportSize(new Dimension(200, 0));

		fieldsTable.getModel().addTableModelListener((FieldTableModel) fieldsTable.getModel());

		fieldsTable.addMouseListener((FieldTableModel) fieldsTable.getModel());

		TableColumn column = null;
		for (int i = 0; i < fieldsTable.getColumnCount(); i++)
		{
			column = fieldsTable.getColumnModel().getColumn(i);

			if (i < 2)
				column.setPreferredWidth(70);

			else if (i == 2)
				column.setPreferredWidth(20);
			else
				column.setPreferredWidth(10);
			
			if (i == 1) {
				JComboBox<AbstractDataType> typeBox = new JComboBox<AbstractDataType>();
				for (DataType t : DataType.values()) {
					typeBox.addItem(DataType.getNewDataTypeObjectFromDataType(t));
				}
				column.setCellEditor(new DefaultCellEditor(typeBox));
			}
			
		}

		JPanelRounded p = new JPanelRounded();
		p.setForeground(Color.GRAY);
		p.setBackground(new Color(240, 240, 240));
		p.setAlignmentY(TOP_ALIGNMENT);
		p.setBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9));
		{
			final GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[] { 0, 0 };
			gbl_panel.rowHeights = new int[] { 0, 0 };
			gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
			gbl_panel.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
			p.setLayout(gbl_panel);
		}

		p.setMaximumSize(new Dimension(0, Integer.MAX_VALUE));

		{
			final GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
			gbc_btnNewButton.anchor = GridBagConstraints.NORTH;
			gbc_btnNewButton.gridx = 0;
			gbc_btnNewButton.gridy = 0;
			p.add(createEntityPropreties(), gbc_btnNewButton);
		}

		add(p);

		p = new JPanelRounded();
		p.setForeground(Color.GRAY);
		p.setBackground(new Color(240, 240, 240));
		p.setAlignmentY(TOP_ALIGNMENT);
		p.setBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9));
		p.setLayout(new BorderLayout());
		JPanel panel = createWhitePanel();
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		JScrollPane scrollPane = scrollPaneFields = new JScrollPane(fieldsTable);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setBorder(new LineBorder(Color.GRAY, 1, true));
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setVisible(false);
		
		panel.add(createTitleLabel("Fields"));
		panel.add(scrollPane);
		panel.add(imgNoField);

		JPanel panelButton = new JPanel();
		panelButton.setOpaque(false);
		panelButton.setLayout(new BoxLayout(panelButton, BoxLayout.PAGE_AXIS));

		{
			final JButton button = new SButton(PersonalizedIcon.createImageIcon(Slyum.ICON_PATH + "button_plus_blue" + small), Color.BLUE, "Add");
			button.setBorderPainted(false);
			button.setContentAreaFilled(false);
			button.setAlignmentX(CENTER_ALIGNMENT);
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					((TableEntity) currentObject).addField(new Field("field", DataType.getNewDataTypeObjectFromDataType(DataType.INT)));
					((TableEntity) currentObject).notifyObservers(UpdateMessage.ADD_FIELD);
				}
			});

			panelButton.add(button);
		}

		{
			btnUpField.setAlignmentX(CENTER_ALIGNMENT);
			btnUpField.setEnabled(false);
			btnUpField.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					// Get the selected field
					final int index = fieldsTable.getSelectionModel().getLeadSelectionIndex();
					final Field field = Utility.getKeysByValue(((FieldTableModel) fieldsTable.getModel()).getMapIndex(), index).iterator().next();

					((TableEntity) currentObject).moveFieldPosition(field, -1);
					((TableEntity) currentObject).notifyObservers();
					field.select();
					field.notifyObservers(UpdateMessage.SELECT);
				}
			});

			panelButton.add(btnUpField);
		}
		{
			btnDownField.setAlignmentX(CENTER_ALIGNMENT);
			btnDownField.setEnabled(false);
			btnDownField.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					// Get the selected field
					final int index = fieldsTable.getSelectionModel().getLeadSelectionIndex();
					final Field field = Utility.getKeysByValue(((FieldTableModel) fieldsTable.getModel()).getMapIndex(), index).iterator().next();

					((TableEntity) currentObject).moveFieldPosition(field, 1);
					((TableEntity) currentObject).notifyObservers();
					field.select();
					field.notifyObservers(UpdateMessage.SELECT);
				}
			});

			panelButton.add(btnDownField);
		}

		{
			btnRemoveField.setAlignmentX(CENTER_ALIGNMENT);
			btnRemoveField.setEnabled(false);
			btnRemoveField.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					// Get the selected field
					final int index = fieldsTable.getSelectionModel().getLeadSelectionIndex();
					Field field = Utility.getKeysByValue(((FieldTableModel) fieldsTable.getModel()).getMapIndex(), index).iterator().next();

					((TableEntity) currentObject).removeField(field);
					((TableEntity) currentObject).notifyObservers();

					for (int i = 0; i <= 1; i++)
					{
						try
						{
							field = Utility.getKeysByValue(((FieldTableModel) fieldsTable.getModel()).getMapIndex(), index - i).iterator().next();
						} catch (final NoSuchElementException e)
						{
							continue;
						}

						field.select();
						field.notifyObservers(UpdateMessage.SELECT);
						break;
					}
				}
			});

			panelButton.add(btnRemoveField);

		}

		p.add(panel, BorderLayout.CENTER);
		p.add(panelButton, BorderLayout.EAST);
		add(p);

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
		panelTitle.add(createTitleLabel("Entity"));

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
					final TableEntity entity = (TableEntity) currentObject;

					if (!entity.setName(textName.getText()))
						textName.setText(entity.getName());
					else
						entity.notifyObservers();
				}
			}
		});

		panel.add(panelTitle);
		panel.add(textName);

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

		if (currentObject instanceof TableEntity) {
		
			final TableEntity entity = (TableEntity) currentObject;
			final FieldTableModel modelFields = (FieldTableModel) fieldsTable.getModel();
	
			final LinkedList<Field> fields = entity.getFields();
	
			if (msg != null && msg.equals(UpdateMessage.UNSELECT))
				if (!entity.getName().equals(textName.getText()))
					if (!entity.setName(textName.getText()))
						textName.setText(entity.getName());
					else
						entity.notifyObservers();
	
			textName.setText(entity.getName());
	
			modelFields.clearAll();
	
			for (int i = 0; i < fields.size(); i++)
				modelFields.addField(fields.get(i));
	
			scrollPaneFields.setVisible(fields.size() > 0);
	
			imgNoField.setVisible(fields.size() <= 0);
	
			btnRemoveField.setEnabled(false);
			btnUpField.setEnabled(false);
			btnDownField.setEnabled(false);
	
			validate();
		} else if (currentObject instanceof dbDiagram.components.TableEntity) {
		
			final dbDiagram.components.TableEntity entity = (dbDiagram.components.TableEntity) currentObject;
			final FieldTableModel modelFields = (FieldTableModel) fieldsTable.getModel();
	
			final LinkedList<Field> field = entity.getFields();
	
			if (msg != null && msg.equals(UpdateMessage.UNSELECT))
				if (!entity.getName().equals(textName.getText()))
					if (!entity.setName(textName.getText()))
						textName.setText(entity.getName());
					else
						entity.notifyObservers();
	
			textName.setText(entity.getName());
	
			modelFields.clearAll();
	
			for (int i = 0; i < field.size(); i++)
				modelFields.addField(field.get(i));
	
			scrollPaneFields.setVisible(field.size() > 0);
	
			imgNoField.setVisible(field.size() <= 0);
	
			btnRemoveField.setEnabled(false);
			btnUpField.setEnabled(false);
			btnDownField.setEnabled(false);
	
			validate();
		}
	}
}
