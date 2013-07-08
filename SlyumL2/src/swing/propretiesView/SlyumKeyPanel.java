package swing.propretiesView;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import swing.JPanelRounded;
import dbDiagram.components.Field;
import dbDiagram.relationships.Binary;

/**
 * Represent a JPanel containing all Swing components for edit role.
 * 
 * @author David Miserez
 * @version 1.0 - 28.07.2011
 */
public class SlyumKeyPanel extends JPanelRounded implements Observer
{
	private static final long serialVersionUID = -8176389461299256256L;
	private final JLabel lblTablename;
	private final JComboBox<Field> keyComboBox;
	private final Binary binary;
	private boolean isPK;

	/**
	 * Create the panel.
	 */
	public SlyumKeyPanel(Binary binary, boolean isPK)
	{
		if (binary == null)
			throw new IllegalArgumentException("binary is null");

		setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		setBackground(new Color(230, 230, 250));
		setForeground(Color.GRAY);
		
		// Generated by WindowBuilder from Google
		this.binary = binary;
		this.isPK = isPK;
		binary.addObserver(this);

		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		if (isPK)
			lblTablename = new JLabel("Primary Key : " + binary.getTarget().getName());
		else
			lblTablename = new JLabel("Foreign Key : " + binary.getSource().getName());
		lblTablename.setFont(new Font("Tahoma", Font.PLAIN, 16));
		final GridBagConstraints gbc_lblTablename = new GridBagConstraints();
		gbc_lblTablename.insets = new Insets(0, 0, 5, 0);
		gbc_lblTablename.gridx = 0;
		gbc_lblTablename.gridy = 0;
		add(lblTablename, gbc_lblTablename);

		keyComboBox = new JComboBox<Field>();
		
		if (isPK) {
			for (Field f : binary.getTarget().getFields()) {
				if (f.isPK()) {
					keyComboBox.addItem(f);
				}
			}
			//keyComboBox.setSelectedItem(binary.getPk());
		} else {
			for (Field f : binary.getSource().getForeignKeys()) {
				keyComboBox.addItem(f);
			}
			//keyComboBox.setSelectedItem(binary.getFk());
		}
		
		final GridBagConstraints gbc_keyComboBox = new GridBagConstraints();
		gbc_keyComboBox.insets = new Insets(0, 0, 5, 0);
		gbc_keyComboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_keyComboBox.gridx = 0;
		gbc_keyComboBox.gridy = 2;
		add(keyComboBox, gbc_keyComboBox);
		keyComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				//TODO Changement PK FK
			}
		});
		
	}

	/**
	 * Remove this observator from the observators list of the role.
	 */
	public void stopObserving()
	{
		binary.deleteObserver(this);
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		//if (isPK)
			//keyComboBox.setSelectedItem(binary.getPk());
		//else
			//keyComboBox.setSelectedItem(binary.getFk());
	}

}
