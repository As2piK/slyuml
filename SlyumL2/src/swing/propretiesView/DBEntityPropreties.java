package swing.propretiesView;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import classDiagram.relationships.Role;
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
	
	private JPanel keyPanel = new JPanel();

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

	JTextField roleTextField = new JTextField();
	JTextField textFkName = new JTextField();
	
	Field currentFKSelected;
	Field currentPKSelected;

	protected DBEntityPropreties()
	{		
		setBorder(null);
		setBackground(Color.WHITE);
		createEntityPropreties();
		createFKPKPanel();
	}

	private void createFKPKPanel() {


		final JPanelRounded RolesPanel = new JPanelRounded();
		RolesPanel.setForeground(Color.GRAY);
		RolesPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		final GridBagConstraints gbc_RolesPanel = new GridBagConstraints();
		gbc_RolesPanel.fill = GridBagConstraints.BOTH;
		gbc_RolesPanel.gridx = 1;
		gbc_RolesPanel.gridy = 0;
		add(RolesPanel, gbc_RolesPanel);
		final GridBagLayout gbl_RolesPanel = new GridBagLayout();
		gbl_RolesPanel.columnWidths = new int[] { 0, 0 };
		gbl_RolesPanel.rowHeights = new int[] { 0, 0, 0 };
		gbl_RolesPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_RolesPanel.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		RolesPanel.setLayout(gbl_RolesPanel);

		final JLabel lblKeys = new JLabel("Keys");
		lblKeys.setFont(new Font("Tahoma", Font.PLAIN, 17));
		final GridBagConstraints gbc_lblKeys = new GridBagConstraints();
		gbc_lblKeys.insets = new Insets(0, 0, 5, 0);
		gbc_lblKeys.gridx = 0;
		gbc_lblKeys.gridy = 0;
		RolesPanel.add(lblKeys, gbc_lblKeys);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setPreferredSize(new Dimension(0, 0));
		final GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		RolesPanel.add(scrollPane, gbc_scrollPane);

		scrollPane.setViewportView(keyPanel);
		
	}
	
	public void createEntityPropreties()
	{

		final JPanelRounded relationPanel = new JPanelRounded();
		relationPanel.setForeground(Color.GRAY);
		relationPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		final GridBagConstraints gbc_relationPanel = new GridBagConstraints();
		gbc_relationPanel.insets = new Insets(0, 0, 0, 5);
		gbc_relationPanel.fill = GridBagConstraints.BOTH;
		gbc_relationPanel.gridx = 0;
		gbc_relationPanel.gridy = 0;
		add(relationPanel, gbc_relationPanel);
		final GridBagLayout gbl_relationPanel = new GridBagLayout();
		gbl_relationPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_relationPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_relationPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_relationPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		relationPanel.setLayout(gbl_relationPanel);
		

		final JLabel lblRelation = new JLabel("Relation");
		final GridBagConstraints gbc_lblRelation = new GridBagConstraints();
		gbc_lblRelation.gridwidth = 2;
		gbc_lblRelation.insets = new Insets(0, 0, 5, 0);
		gbc_lblRelation.gridx = 0;
		gbc_lblRelation.gridy = 0;
		relationPanel.add(lblRelation, gbc_lblRelation);
		lblRelation.setFont(new Font("Tahoma", Font.PLAIN, 16));
		

		JLabel lblKeyName = new JLabel("Key name");
		final GridBagConstraints gbc_lblKeyName = new GridBagConstraints();
		gbc_lblKeyName.anchor = GridBagConstraints.WEST;
		gbc_lblKeyName.insets = new Insets(0, 0, 5, 5);
		gbc_lblKeyName.gridx = 0;
		gbc_lblKeyName.gridy = 1;
		relationPanel.add(lblKeyName, gbc_lblKeyName);
		
		final GridBagConstraints gbc_keyNameLabel = new GridBagConstraints();
		gbc_keyNameLabel.anchor = GridBagConstraints.WEST;
		gbc_keyNameLabel.insets = new Insets(0, 0, 5, 0);
		gbc_keyNameLabel.gridx = 1;
		gbc_keyNameLabel.gridy = 1;
		relationPanel.add(textFkName, gbc_keyNameLabel);
		
		textFkName.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e)
			{
				if (e.getKeyChar() == '\n')
				{
					final Binary entity = (Binary) currentObject;

					//entity.getFk().setName(textFkName.getText()); TODO
					entity.notifyObservers();
				}
			}
		});

		relationPanel.add(textFkName, gbc_keyNameLabel);
		textFkName.setColumns(10);
		

		JLabel lblLabel = new JLabel("Label");
		final GridBagConstraints gbc_lblLabel = new GridBagConstraints();
		gbc_lblLabel.anchor = GridBagConstraints.WEST;
		gbc_lblLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblLabel.gridx = 0;
		gbc_lblLabel.gridy = 2;
		relationPanel.add(lblLabel, gbc_lblLabel);
		
		final GridBagConstraints gbc_textFieldLabel = new GridBagConstraints();
		gbc_textFieldLabel.anchor = GridBagConstraints.WEST;
		gbc_textFieldLabel.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldLabel.gridx = 1;
		gbc_textFieldLabel.gridy = 2;
		relationPanel.add(roleTextField, gbc_textFieldLabel);
		
		roleTextField.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e)
			{
				if (e.getKeyChar() == '\n')
				{
					final Binary entity = (Binary) currentObject;

					entity.setLabel(roleTextField.getText());
					entity.notifyObservers();
				}
			}
		});

		relationPanel.add(roleTextField, gbc_textFieldLabel);
		roleTextField.setColumns(10);

		add(relationPanel, gbl_relationPanel);
		
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
				if (!entity.getName().equals(roleTextField.getText()))
					entity.setName(roleTextField.getText());
			entity.notifyObservers();

			if (msg != null && msg.equals(UpdateMessage.SELECT)) {
				
				keyPanel.removeAll();
				keyPanel.add(new SlyumKeyPanel((Binary)currentObject, true));
				keyPanel.add(new SlyumKeyPanel((Binary)currentObject, false));
			}	
			roleTextField.setText(entity.getName());
			
			validate();
		} else if (currentObject instanceof dbDiagram.components.TableEntity) {
		
			final dbDiagram.components.TableEntity entity = (dbDiagram.components.TableEntity) currentObject;

	
			if (msg != null && msg.equals(UpdateMessage.UNSELECT))
				if (!entity.getName().equals(roleTextField.getText()))
					if (!entity.setName(roleTextField.getText()))
						roleTextField.setText(entity.getName());
					else
						entity.notifyObservers();
	
			roleTextField.setText(entity.getName());
	
			validate();
		}
	}
}
