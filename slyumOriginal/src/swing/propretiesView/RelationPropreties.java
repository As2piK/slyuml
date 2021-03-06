package swing.propretiesView;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import swing.JPanelRounded;
import swing.propretiesView.GlobalPropreties;
import classDiagram.IDiagramComponent.UpdateMessage;
import classDiagram.relationships.Association;
import classDiagram.relationships.Dependency;
import classDiagram.relationships.Role;

/**
 * Show the propreties of an association and its roles with Swing components.
 * 
 * @author David Miserez
 * @version 1.0 - 28.07.2011
 */
public class RelationPropreties extends GlobalPropreties
{
	private static RelationPropreties instance = new RelationPropreties();

	private static final long serialVersionUID = -6963860886146010082L;

	/**
	 * Get the unique instance of this class.
	 * 
	 * @return the unique instance of RelationPropreties
	 */
	public static RelationPropreties getInstance()
	{
		return instance;
	}

	private final JCheckBox chckbxDirect;
	private final JLabel lblLabel;
	private final JPanel panelRoles = new JPanel();

	private final JTextField textFieldLabel;

	/**
	 * Create the panel.
	 */
	public RelationPropreties()
	{
		setBorder(null);
		setBackground(Color.WHITE);
		// Generated by WindowBuilder from Google.
		final GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		final JPanelRounded AssociationPanel = new JPanelRounded();
		AssociationPanel.setForeground(Color.GRAY);
		AssociationPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		final GridBagConstraints gbc_AssociationPanel = new GridBagConstraints();
		gbc_AssociationPanel.insets = new Insets(0, 0, 0, 5);
		gbc_AssociationPanel.fill = GridBagConstraints.BOTH;
		gbc_AssociationPanel.gridx = 0;
		gbc_AssociationPanel.gridy = 0;
		add(AssociationPanel, gbc_AssociationPanel);
		final GridBagLayout gbl_AssociationPanel = new GridBagLayout();
		gbl_AssociationPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_AssociationPanel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_AssociationPanel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_AssociationPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		AssociationPanel.setLayout(gbl_AssociationPanel);

		final JLabel lblAssociation = new JLabel("Association");
		final GridBagConstraints gbc_lblAssociation = new GridBagConstraints();
		gbc_lblAssociation.gridwidth = 2;
		gbc_lblAssociation.insets = new Insets(0, 0, 5, 0);
		gbc_lblAssociation.gridx = 0;
		gbc_lblAssociation.gridy = 0;
		AssociationPanel.add(lblAssociation, gbc_lblAssociation);
		lblAssociation.setFont(new Font("Tahoma", Font.PLAIN, 16));

		lblLabel = new JLabel("Label");
		final GridBagConstraints gbc_lblLabel = new GridBagConstraints();
		gbc_lblLabel.anchor = GridBagConstraints.WEST;
		gbc_lblLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblLabel.gridx = 0;
		gbc_lblLabel.gridy = 1;
		AssociationPanel.add(lblLabel, gbc_lblLabel);

		textFieldLabel = new JTextField();
		textFieldLabel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (currentObject != null)
					if (currentObject instanceof Association)
					{
						((Association) currentObject).setLabel(textFieldLabel.getText());
						((Association) currentObject).notifyObservers();
					}
					else if (currentObject instanceof Dependency)
					{
						((Dependency) currentObject).setLabel(textFieldLabel.getText());
						((Dependency) currentObject).notifyObservers();
					}

			}
		});
		final GridBagConstraints gbc_textFieldLabel = new GridBagConstraints();
		gbc_textFieldLabel.anchor = GridBagConstraints.WEST;
		gbc_textFieldLabel.insets = new Insets(0, 0, 5, 0);
		gbc_textFieldLabel.gridx = 1;
		gbc_textFieldLabel.gridy = 1;
		AssociationPanel.add(textFieldLabel, gbc_textFieldLabel);
		textFieldLabel.setColumns(10);

		final JLabel lblDirected = new JLabel("Directed");
		final GridBagConstraints gbc_lblDirected = new GridBagConstraints();
		gbc_lblDirected.anchor = GridBagConstraints.WEST;
		gbc_lblDirected.insets = new Insets(0, 0, 5, 5);
		gbc_lblDirected.gridx = 0;
		gbc_lblDirected.gridy = 2;
		AssociationPanel.add(lblDirected, gbc_lblDirected);

		chckbxDirect = new JCheckBox("");
		chckbxDirect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (currentObject != null && currentObject instanceof Association)
				{
					((Association) currentObject).setDirected(chckbxDirect.isSelected());
					((Association) currentObject).notifyObservers();
				}
			}
		});
		final GridBagConstraints gbc_chckbxDirect = new GridBagConstraints();
		gbc_chckbxDirect.insets = new Insets(0, 0, 5, 0);
		gbc_chckbxDirect.anchor = GridBagConstraints.WEST;
		gbc_chckbxDirect.gridx = 1;
		gbc_chckbxDirect.gridy = 2;
		AssociationPanel.add(chckbxDirect, gbc_chckbxDirect);

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

		final JLabel lblRoles = new JLabel("Roles");
		lblRoles.setFont(new Font("Tahoma", Font.PLAIN, 17));
		final GridBagConstraints gbc_lblRoles = new GridBagConstraints();
		gbc_lblRoles.insets = new Insets(0, 0, 5, 0);
		gbc_lblRoles.gridx = 0;
		gbc_lblRoles.gridy = 0;
		RolesPanel.add(lblRoles, gbc_lblRoles);

		final JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setPreferredSize(new Dimension(0, 0));
		final GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		RolesPanel.add(scrollPane, gbc_scrollPane);
		panelRoles.setBorder(null);

		scrollPane.setViewportView(panelRoles);
	}

	@Override
	public void updateComponentInformations(UpdateMessage msg)
	{
		if (currentObject != null)
			if (currentObject instanceof Association)
			{
				final Association association = (Association) currentObject;

				if (msg != null && msg.equals(UpdateMessage.UNSELECT))
				{
					association.setName(textFieldLabel.getText());
					association.notifyObservers();

					for (final Component c : panelRoles.getComponents())

						((SlyumRolePanel) c).confirm();
				}

				chckbxDirect.setEnabled(true);
				chckbxDirect.setSelected(association.isDirected());
				textFieldLabel.setText(association.getLabel());

				if (panelRoles.getComponentCount() == 0 || msg == UpdateMessage.SELECT)
				{
					for (final Component c : panelRoles.getComponents())
					{
						((SlyumRolePanel) c).stopObserving();
						panelRoles.removeAll();
					}

					for (final Role role : association.getRoles())

						panelRoles.add(new SlyumRolePanel(role));

				}
			}
			else if (currentObject instanceof Dependency)
			{
				for (final Component c : panelRoles.getComponents())
				{
					((SlyumRolePanel) c).stopObserving();
					panelRoles.removeAll();
				}

				final Dependency dependency = (Dependency) currentObject;

				if (msg != null && msg.equals(UpdateMessage.UNSELECT))
				{
					dependency.setLabel(textFieldLabel.getText());
					dependency.notifyObservers();
				}

				chckbxDirect.setEnabled(false);
				textFieldLabel.setText(dependency.getLabel());
			}
	}

}
