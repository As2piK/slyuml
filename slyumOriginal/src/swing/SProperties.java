package swing;

import graphic.GraphicView;
import graphic.entity.EntityView;
import graphic.textbox.TextBox;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import utility.PersonalizedIcon;
import utility.SMessageDialog;
import utility.Utility;
import javax.swing.UIManager;

public class SProperties extends JDialog
{
	private static final long serialVersionUID = 1739834798588561464L;
	private JButton btnColor;
	private JButton btnBackgroundColor;
	private JButton btnDefaultClassColor;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblPreviewFont = new JLabel();
	private JList<String> listName;
	private JList<Integer> listSize;
	private JRadioButton rdbtnAutomaticcolor;
	private JRadioButton rdbtnLow;
	private JRadioButton rdbtnMax;
	private JRadioButton rdbtnMedium;
	private JRadioButton rdbtnSelectedColor;
	private JSlider sliderGridPoint;
	private JSlider sliderGridSize;
	private JCheckBox chckbxOpacityGrid;
	private JCheckBox chckbxUseSmallIcons;
	private JCheckBox chckbxDisableErrorMessage;
	private JCheckBox chckbxDisableCrossPopup;
	private JCheckBox ckbBackgroundGradient;
	private JCheckBox chckbxUseCtrlFor;
	private JCheckBox chckbxShowGrid;
	private JPanel panel_Grid, panel_grid_color, panel_grid_opacity;
	private JCheckBox chckbxEnableGrid;
	
	/**
	 * Create the dialog.
	 */
	public SProperties()
	{
        Utility.setRootPaneActionOnEsc(getRootPane(), new AbstractAction() {
		
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e)
            {
            	setVisible(false);
            }
		});

		setTitle("Slyum - Properties");
		setIconImage(PersonalizedIcon.getLogo().getImage());
		setMinimumSize(new Dimension(600, 600));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setFocusable(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		{
			final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
			contentPanel.add(tabbedPane, BorderLayout.CENTER);
			{
				final JPanel panelFormatting = new JPanel();
				panelFormatting.setBorder(new EmptyBorder(8, 8, 8, 8));
				tabbedPane.addTab("Formatting", new ImageIcon(SProperties.class.getResource(Slyum.ICON_PATH + "fonts.png")), panelFormatting, null);
				final GridBagLayout gbl_panelFormatting = new GridBagLayout();
				gbl_panelFormatting.columnWidths = new int[] { 214, 0, 0 };
				gbl_panelFormatting.rowHeights = new int[] { 23, 0 };
				gbl_panelFormatting.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
				gbl_panelFormatting.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
				panelFormatting.setLayout(gbl_panelFormatting);
				{
					final JPanel panel = new JPanel();
					panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "General", TitledBorder.LEADING, TitledBorder.TOP, null, null));
					final GridBagConstraints gbc_panel = new GridBagConstraints();
					gbc_panel.insets = new Insets(0, 0, 0, 5);
					gbc_panel.fill = GridBagConstraints.BOTH;
					gbc_panel.gridx = 0;
					gbc_panel.gridy = 0;
					panelFormatting.add(panel, gbc_panel);
					final GridBagLayout gbl_panel = new GridBagLayout();
					gbl_panel.columnWidths = new int[] { 0, 0 };
					gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
					gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
					gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
					panel.setLayout(gbl_panel);
					btnDefaultClassColor = new JButton("Default class color");
					final GridBagConstraints gbc_btnDefaultClassColor = new GridBagConstraints();
					gbc_btnDefaultClassColor.fill = GridBagConstraints.HORIZONTAL;
					gbc_btnDefaultClassColor.insets = new Insets(0, 10, 5, 10);
					gbc_btnDefaultClassColor.gridx = 0;
					gbc_btnDefaultClassColor.gridy = 0;
					panel.add(btnDefaultClassColor, gbc_btnDefaultClassColor);
					btnDefaultClassColor.setBackground(EntityView.getBasicColor());
					btnBackgroundColor = new JButton("Background-color");
					final GridBagConstraints gbc_btnBackgroundColor = new GridBagConstraints();
					gbc_btnBackgroundColor.fill = GridBagConstraints.HORIZONTAL;
					gbc_btnBackgroundColor.insets = new Insets(0, 10, 5, 10);
					gbc_btnBackgroundColor.gridx = 0;
					gbc_btnBackgroundColor.gridy = 1;
					panel.add(btnBackgroundColor, gbc_btnBackgroundColor);
					btnBackgroundColor.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0)
						{
							final SColorChooser scc = new SColorChooser(btnBackgroundColor.getBackground());
							scc.setVisible(true);

							if (scc.isAccepted())

								btnBackgroundColor.setBackground(scc.getColor());

						}
					});
					{
						ckbBackgroundGradient = new JCheckBox("Background gradient");
						final GridBagConstraints gbc_ckbBackgroundGradient = new GridBagConstraints();
						gbc_ckbBackgroundGradient.fill = GridBagConstraints.HORIZONTAL;
						gbc_ckbBackgroundGradient.anchor = GridBagConstraints.WEST;
						gbc_ckbBackgroundGradient.insets = new Insets(0, 6, 5, 0);
						gbc_ckbBackgroundGradient.gridx = 0;
						gbc_ckbBackgroundGradient.gridy = 2;
						panel.add(ckbBackgroundGradient, gbc_ckbBackgroundGradient);
					}
					{
						chckbxEnableGrid = new JCheckBox("Enable grid");
						chckbxEnableGrid.addChangeListener(new ChangeListener() {
							public void stateChanged(ChangeEvent arg0)
							{
								setEnableGrid(chckbxEnableGrid.isSelected());
							}
						});
						GridBagConstraints gbc_chckbxEnableGrid = new GridBagConstraints();
						gbc_chckbxEnableGrid.fill = GridBagConstraints.HORIZONTAL;
						gbc_chckbxEnableGrid.insets = new Insets(0, 6, 5, 0);
						gbc_chckbxEnableGrid.gridx = 0;
						gbc_chckbxEnableGrid.gridy = 3;
						panel.add(chckbxEnableGrid, gbc_chckbxEnableGrid);
					}
					{
						panel_Grid = new JPanel()
						{
							private static final long serialVersionUID = 1L;

							@Override
							public void setEnabled(boolean enabled)
							{
								super.setEnabled(enabled);
								
								for (Component c : panel_Grid.getComponents())
									c.setEnabled(enabled);
							}
						};
						panel_Grid.setBorder(new TitledBorder(null, "Grid", TitledBorder.LEADING, TitledBorder.TOP, null, null));
						final GridBagConstraints gbc_panel_1 = new GridBagConstraints();
						gbc_panel_1.fill = GridBagConstraints.VERTICAL;
						gbc_panel_1.gridx = 0;
						gbc_panel_1.gridy = 4;
						panel.add(panel_Grid, gbc_panel_1);
						final GridBagLayout gbl_panel_1 = new GridBagLayout();
						gbl_panel_1.columnWidths = new int[] { 0, 0 };
						gbl_panel_1.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
						gbl_panel_1.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
						gbl_panel_1.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
						panel_Grid.setLayout(gbl_panel_1);
						{
							JPanel panel_2 = new JPanel();
							panel_2.setBorder(new TitledBorder(null, "Size", TitledBorder.LEADING, TitledBorder.TOP, null, null));
							GridBagConstraints gbc_panel_2 = new GridBagConstraints();
							gbc_panel_2.insets = new Insets(0, 0, 5, 0);
							gbc_panel_2.fill = GridBagConstraints.BOTH;
							gbc_panel_2.gridx = 0;
							gbc_panel_2.gridy = 0;
							panel_Grid.add(panel_2, gbc_panel_2);
							GridBagLayout gbl_panel_2 = new GridBagLayout();
							gbl_panel_2.columnWidths = new int[]{200, 0};
							gbl_panel_2.rowHeights = new int[]{23, 0};
							gbl_panel_2.columnWeights = new double[]{0.0, Double.MIN_VALUE};
							gbl_panel_2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
							panel_2.setLayout(gbl_panel_2);
							{
								sliderGridSize = new JSlider();
								sliderGridSize.setMaximum(50);
								sliderGridSize.setMinimum(10);
								sliderGridSize.setPaintLabels(true);
								sliderGridSize.setPaintTicks(true);
								GridBagConstraints gbc_slider = new GridBagConstraints();
								gbc_slider.fill = GridBagConstraints.HORIZONTAL;
								gbc_slider.anchor = GridBagConstraints.NORTHWEST;
								gbc_slider.gridx = 0;
								gbc_slider.gridy = 0;
								panel_2.add(sliderGridSize, gbc_slider);
							}
						}
						{
							chckbxShowGrid = new JCheckBox("Show grid");
							GridBagConstraints gbc_chckbxShowGrid = new GridBagConstraints();
							gbc_chckbxShowGrid.fill = GridBagConstraints.HORIZONTAL;
							gbc_chckbxShowGrid.insets = new Insets(0, 0, 5, 0);
							gbc_chckbxShowGrid.gridx = 0;
							gbc_chckbxShowGrid.gridy = 1;
							panel_Grid.add(chckbxShowGrid, gbc_chckbxShowGrid);
							chckbxShowGrid.addChangeListener(new ChangeListener() {
								public void stateChanged(ChangeEvent arg0)
								{
									setEnableStyleGrid(chckbxShowGrid.isSelected());
								}
							});
						}
						{
							{
								panel_grid_opacity = new JPanel();
								panel_grid_opacity.setBorder(new TitledBorder(null, "Opacity", TitledBorder.LEADING, TitledBorder.TOP, null, null));
								GridBagConstraints gbc_panel_2 = new GridBagConstraints();
								gbc_panel_2.gridheight = 2;
								gbc_panel_2.fill = GridBagConstraints.BOTH;
								gbc_panel_2.insets = new Insets(0, 0, 5, 0);
								gbc_panel_2.gridx = 0;
								gbc_panel_2.gridy = 2;
								panel_Grid.add(panel_grid_opacity, gbc_panel_2);
								GridBagLayout gbl_panel_2 = new GridBagLayout();
								gbl_panel_2.columnWidths = new int[]{0, 0};
								gbl_panel_2.rowHeights = new int[]{0, 0, 0};
								gbl_panel_2.columnWeights = new double[]{1.0, Double.MIN_VALUE};
								gbl_panel_2.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
								panel_grid_opacity.setLayout(gbl_panel_2);
								chckbxOpacityGrid = new JCheckBox("Enable opacity");
								GridBagConstraints gbc_chckbxOpacityGrid = new GridBagConstraints();
								gbc_chckbxOpacityGrid.insets = new Insets(0, 0, 5, 0);
								gbc_chckbxOpacityGrid.anchor = GridBagConstraints.WEST;
								gbc_chckbxOpacityGrid.gridx = 0;
								gbc_chckbxOpacityGrid.gridy = 0;
								panel_grid_opacity.add(chckbxOpacityGrid, gbc_chckbxOpacityGrid);
								{
									sliderGridPoint = new JSlider()
									{
										private static final long serialVersionUID = 1L;

										@Override
										public void setEnabled(boolean enabled)
										{
											super.setEnabled(enabled && chckbxOpacityGrid.isSelected());
										}
									};
									sliderGridPoint.setEnabled(chckbxOpacityGrid.isSelected());
									GridBagConstraints gbc_sliderGridPoint = new GridBagConstraints();
									gbc_sliderGridPoint.fill = GridBagConstraints.HORIZONTAL;
									gbc_sliderGridPoint.gridx = 0;
									gbc_sliderGridPoint.gridy = 1;
									panel_grid_opacity.add(sliderGridPoint, gbc_sliderGridPoint);
									sliderGridPoint.setValue(200);
									sliderGridPoint.setMaximum(255);
									sliderGridPoint.setBorder(null);
								}
								chckbxOpacityGrid.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent arg0)
									{

										final boolean isSelected = chckbxOpacityGrid.isSelected();

										sliderGridPoint.setEnabled(isSelected);

										if (isSelected)
											showOpacityWarning();
									}
								});
							}
						}

						final ButtonGroup bgBackgroundGrid = new ButtonGroup();

						{
							{
								panel_grid_color = new JPanel();
								panel_grid_color.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Color", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
								final GridBagConstraints gbc_panel_2 = new GridBagConstraints();
								gbc_panel_2.fill = GridBagConstraints.BOTH;
								gbc_panel_2.gridx = 0;
								gbc_panel_2.gridy = 5;
								panel_Grid.add(panel_grid_color, gbc_panel_2);
								final GridBagLayout gbl_panel_2 = new GridBagLayout();
								gbl_panel_2.columnWidths = new int[] { 0, 0 };
								gbl_panel_2.rowHeights = new int[] { 0, 0, 0 };
								gbl_panel_2.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
								gbl_panel_2.rowWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
								panel_grid_color.setLayout(gbl_panel_2);
								rdbtnAutomaticcolor = new JRadioButton("Assorted with background");
								rdbtnAutomaticcolor.setHorizontalAlignment(SwingConstants.LEFT);
								final GridBagConstraints gbc_rdbtnAutomaticcolor = new GridBagConstraints();
								gbc_rdbtnAutomaticcolor.fill = GridBagConstraints.HORIZONTAL;
								gbc_rdbtnAutomaticcolor.insets = new Insets(0, 5, 5, 0);
								gbc_rdbtnAutomaticcolor.gridx = 0;
								gbc_rdbtnAutomaticcolor.gridy = 0;
								panel_grid_color.add(rdbtnAutomaticcolor, gbc_rdbtnAutomaticcolor);
								rdbtnAutomaticcolor.addChangeListener(new ChangeListener() {
									public void stateChanged(ChangeEvent arg0)
									{
										btnColor.setEnabled(!rdbtnAutomaticcolor.isSelected());
									}
								});
								bgBackgroundGrid.add(rdbtnAutomaticcolor);
								{
									final JPanel panel_3 = new JPanel();
									final FlowLayout flowLayout = (FlowLayout) panel_3.getLayout();
									flowLayout.setAlignment(FlowLayout.LEFT);
									final GridBagConstraints gbc_panel_3 = new GridBagConstraints();
									gbc_panel_3.anchor = GridBagConstraints.WEST;
									gbc_panel_3.fill = GridBagConstraints.BOTH;
									gbc_panel_3.gridx = 0;
									gbc_panel_3.gridy = 1;
									panel_grid_color.add(panel_3, gbc_panel_3);
									{
										rdbtnSelectedColor = new JRadioButton("Selected color");
										rdbtnSelectedColor.setHorizontalAlignment(SwingConstants.LEFT);
										panel_3.add(rdbtnSelectedColor);
										rdbtnSelectedColor.addChangeListener(new ChangeListener() {
											public void stateChanged(ChangeEvent arg0)
											{
												btnColor.setEnabled(rdbtnSelectedColor.isSelected());
											}
										});
										bgBackgroundGrid.add(rdbtnSelectedColor);
									}
									{
										btnColor = new JButton("Color")
										{
											private static final long serialVersionUID = 1L;

											@Override
											public void setEnabled(boolean b) {
												super.setEnabled(b && rdbtnSelectedColor.isSelected());
											}
										};
										panel_3.add(btnColor);
										btnColor.addActionListener(new ActionListener() {
											public void actionPerformed(ActionEvent arg0)
											{
												final SColorChooser scc = new SColorChooser(new Color(GraphicView.getGridColor()));
												scc.setVisible(true);

												if (scc.isAccepted())
												
													btnColor.setBackground(scc.getColor());
											}
										});
									}
								}
							}
						}
					}
					btnDefaultClassColor.addActionListener(new ActionListener() {

						public void actionPerformed(ActionEvent arg0)
						{
							final SColorChooser scc = new SColorChooser(btnDefaultClassColor.getBackground());
							scc.setVisible(true);

							if (scc.isAccepted())

								btnDefaultClassColor.setBackground(scc.getColor());
						}
					});
				}
				{
					final JPanel panel = new JPanel();
					panel.setBorder(new TitledBorder(null, "Font", TitledBorder.LEADING, TitledBorder.TOP, null, null));
					final GridBagConstraints gbc_panel = new GridBagConstraints();
					gbc_panel.fill = GridBagConstraints.BOTH;
					gbc_panel.gridx = 1;
					gbc_panel.gridy = 0;
					panelFormatting.add(panel, gbc_panel);
					final GridBagLayout gbl_panel = new GridBagLayout();
					gbl_panel.columnWidths = new int[] { 122, 25, 0 };
					gbl_panel.rowHeights = new int[] { 188, 0, 0 };
					gbl_panel.columnWeights = new double[] { 1.0, 1.0, Double.MIN_VALUE };
					gbl_panel.rowWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
					panel.setLayout(gbl_panel);
					{
						final JScrollPane scrollPane = new JScrollPane();
						final GridBagConstraints gbc_scrollPane = new GridBagConstraints();
						gbc_scrollPane.fill = GridBagConstraints.BOTH;
						gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
						gbc_scrollPane.gridx = 0;
						gbc_scrollPane.gridy = 0;
						panel.add(scrollPane, gbc_scrollPane);
						{
							listName = new JList<String>();
							listName.addListSelectionListener(new ListSelectionListener() {
								public void valueChanged(ListSelectionEvent arg0)
								{
									final int size = lblPreviewFont.getFont().getSize();
									lblPreviewFont.setFont(new Font(listName.getSelectedValue().toString(), Font.PLAIN, size));
								}
							});
							scrollPane.setViewportView(listName);
							listName.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
							listName.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
							listName.setModel(new AbstractListModel<String>() {

								private static final long serialVersionUID = -8806070481194611567L;
								
								GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
								String[] values = ge.getAvailableFontFamilyNames();

								public String getElementAt(int index)
								{
									return values[index];
								}

								public int getSize()
								{
									return values.length;
								}
							});

							listName.setSelectedValue(TextBox.getFont().getName(), true);
						}
					}
					{
						final JScrollPane scrollPane = new JScrollPane();
						final GridBagConstraints gbc_scrollPane = new GridBagConstraints();
						gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
						gbc_scrollPane.fill = GridBagConstraints.BOTH;
						gbc_scrollPane.gridx = 1;
						gbc_scrollPane.gridy = 0;
						panel.add(scrollPane, gbc_scrollPane);
						{
							listSize = new JList<Integer>();
							listSize.setModel(new AbstractListModel<Integer>() {
								
								private static final long serialVersionUID = -2073589127443911972L;
								
								int[] values = new int[] { 8, 9, 10, 12, 14, 16, 18, 20, 24, 28, 32, 48, 72 };

								public Integer getElementAt(int index)
								{
									return values[index];
								}

								public int getSize()
								{
									return values.length;
								}
							});
							scrollPane.setViewportView(listSize);
							listSize.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
							listSize.setSelectedValue(TextBox.getFont().getSize(), true);
						}
					}
					{
						final JPanel panel_1 = new JPanel();
						panel_1.setMinimumSize(new Dimension(200, 60));
						panel_1.setMaximumSize(new Dimension(200, 60));
						panel_1.setBorder(new LineBorder(Color.GRAY));
						final GridBagConstraints gbc_panel_1 = new GridBagConstraints();
						gbc_panel_1.gridwidth = 2;
						gbc_panel_1.insets = new Insets(0, 0, 0, 5);
						gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
						gbc_panel_1.gridx = 0;
						gbc_panel_1.gridy = 1;
						panel.add(panel_1, gbc_panel_1);
						{
							lblPreviewFont = new JLabel("ABCDEFG abcdefg 1234");
							lblPreviewFont.setFont(new Font("Tahoma", Font.PLAIN, 20));
							panel_1.add(lblPreviewFont);
						}
					}
				}
				{
					final JPanel panel = new JPanel();
					panel.setBorder(new EmptyBorder(10, 10, 10, 10));
					tabbedPane.addTab("Graphics", new ImageIcon(SProperties.class.getResource(Slyum.ICON_PATH + "pencil.png")), panel, null);
					final GridBagLayout gbl_panel = new GridBagLayout();
					gbl_panel.columnWidths = new int[] { 0, 0 };
					gbl_panel.rowHeights = new int[] { 0, 0, 0 };
					gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
					gbl_panel.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
					panel.setLayout(gbl_panel);
					{
						final ButtonGroup bgGraphicQuality = new ButtonGroup();
						final JPanel panel_1 = new JPanel();
						panel_1.setBorder(new TitledBorder(null, "Quality", TitledBorder.LEADING, TitledBorder.TOP, null, null));
						final GridBagConstraints gbc_panel_1 = new GridBagConstraints();
						gbc_panel_1.insets = new Insets(0, 0, 5, 0);
						gbc_panel_1.fill = GridBagConstraints.BOTH;
						gbc_panel_1.gridx = 0;
						gbc_panel_1.gridy = 0;
						panel.add(panel_1, gbc_panel_1);
						final GridBagLayout gbl_panel_1 = new GridBagLayout();
						gbl_panel_1.columnWidths = new int[] { 0, 0, 0, 0 };
						gbl_panel_1.rowHeights = new int[] { 0, 0 };
						gbl_panel_1.columnWeights = new double[] { 1.0, 1.0, 1.0, Double.MIN_VALUE };
						gbl_panel_1.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
						panel_1.setLayout(gbl_panel_1);
						{
							rdbtnLow = new JRadioButton("Low");
							bgGraphicQuality.add(rdbtnLow);
							final GridBagConstraints gbc_rdbtnLow = new GridBagConstraints();
							gbc_rdbtnLow.insets = new Insets(0, 0, 0, 5);
							gbc_rdbtnLow.gridx = 0;
							gbc_rdbtnLow.gridy = 0;
							panel_1.add(rdbtnLow, gbc_rdbtnLow);
						}
						{
							rdbtnMedium = new JRadioButton("Medium");
							bgGraphicQuality.add(rdbtnMedium);
							final GridBagConstraints gbc_rdbtnMedium = new GridBagConstraints();
							gbc_rdbtnMedium.insets = new Insets(0, 0, 0, 5);
							gbc_rdbtnMedium.gridx = 1;
							gbc_rdbtnMedium.gridy = 0;
							panel_1.add(rdbtnMedium, gbc_rdbtnMedium);
						}
						{
							rdbtnMax = new JRadioButton("Max");
							bgGraphicQuality.add(rdbtnMax);
							final GridBagConstraints gbc_rdbtnMax = new GridBagConstraints();
							gbc_rdbtnMax.gridx = 2;
							gbc_rdbtnMax.gridy = 0;
							panel_1.add(rdbtnMax, gbc_rdbtnMax);
						}
					}
					{
						final JPanel panel_1 = new JPanel();
						panel_1.setBorder(new TitledBorder(null, "Advanced", TitledBorder.LEADING, TitledBorder.TOP, null, null));
						final GridBagConstraints gbc_panel_1 = new GridBagConstraints();
						gbc_panel_1.fill = GridBagConstraints.BOTH;
						gbc_panel_1.gridx = 0;
						gbc_panel_1.gridy = 1;
						panel.add(panel_1, gbc_panel_1);
						final GridBagLayout gbl_panel_1 = new GridBagLayout();
						gbl_panel_1.columnWidths = new int[] { 0, 0 };
						gbl_panel_1.rowHeights = new int[] { 0, 0 };
						gbl_panel_1.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
						gbl_panel_1.rowWeights = new double[] { 1.0, Double.MIN_VALUE };
						panel_1.setLayout(gbl_panel_1);
						{
							final JButton btnNewButton = new JButton("Custom...");
							final SProperties link = this;
							btnNewButton.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0)
								{
									SMessageDialog.showInformationMessage("This will be implemented in futur update.", link);
								}
							});
							final GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
							gbc_btnNewButton.gridx = 0;
							gbc_btnNewButton.gridy = 0;
							panel_1.add(btnNewButton, gbc_btnNewButton);
						}
					}
				}
				{
					final JPanel panel = new JPanel();
					panel.setBorder(new CompoundBorder(new EmptyBorder(10, 10, 10, 10), new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Generals", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51))));
					tabbedPane.addTab("Diagram editor", new ImageIcon(SProperties.class.getResource(Slyum.ICON_PATH + "green_config.png")), panel, null);
					final GridBagLayout gbl_panel = new GridBagLayout();
					gbl_panel.columnWidths = new int[] { 0, 0 };
					gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0 };
					gbl_panel.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
					gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
					panel.setLayout(gbl_panel);
					{
						chckbxUseCtrlFor = new JCheckBox("Use Ctrl for adding grips in relations");
						chckbxUseCtrlFor.setToolTipText("By default, a simple clic adds a new grip on a relation and Ctrl+clic moves the relation. Inverse it by checking this option.");
						final GridBagConstraints gbc_chckbxUseCtrlFor = new GridBagConstraints();
						gbc_chckbxUseCtrlFor.anchor = GridBagConstraints.WEST;
						gbc_chckbxUseCtrlFor.insets = new Insets(0, 5, 5, 0);
						gbc_chckbxUseCtrlFor.gridx = 0;
						gbc_chckbxUseCtrlFor.gridy = 0;
						panel.add(chckbxUseCtrlFor, gbc_chckbxUseCtrlFor);
					}
					{
						chckbxUseSmallIcons = new JCheckBox("Use small icons (need restart application)");
						GridBagConstraints gbc_chckbxUseSmallIcons = new GridBagConstraints();
						gbc_chckbxUseSmallIcons.anchor = GridBagConstraints.WEST;
						gbc_chckbxUseSmallIcons.insets = new Insets(0, 5, 5, 0);
						gbc_chckbxUseSmallIcons.gridx = 0;
						gbc_chckbxUseSmallIcons.gridy = 1;
						panel.add(chckbxUseSmallIcons, gbc_chckbxUseSmallIcons);
					}
					{
						chckbxDisableErrorMessage = new JCheckBox("Show error messages during the creation of components");
						GridBagConstraints gbc_chckbxDisableErrorMessage = new GridBagConstraints();
						gbc_chckbxDisableErrorMessage.anchor = GridBagConstraints.WEST;
						gbc_chckbxDisableErrorMessage.insets = new Insets(0, 5, 5, 0);
						gbc_chckbxDisableErrorMessage.gridx = 0;
						gbc_chckbxDisableErrorMessage.gridy = 2;
						panel.add(chckbxDisableErrorMessage, gbc_chckbxDisableErrorMessage);
					}
					{
						chckbxDisableCrossPopup = new JCheckBox("Show cross popup menu when components are selected");
						GridBagConstraints gbc_chckbxDisableCrossPopup = new GridBagConstraints();
						gbc_chckbxDisableCrossPopup.insets = new Insets(0, 5, 0, 0);
						gbc_chckbxDisableCrossPopup.anchor = GridBagConstraints.WEST;
						gbc_chckbxDisableCrossPopup.gridx = 0;
						gbc_chckbxDisableCrossPopup.gridy = 3;
						panel.add(chckbxDisableCrossPopup, gbc_chckbxDisableCrossPopup);
					}
					tabbedPane.setDisabledIconAt(2, null);
				}
			}
		}

		{
			final JPanel buttonPane = new JPanel();
			buttonPane.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				GridBagLayout gbl_buttonPane = new GridBagLayout();
				gbl_buttonPane.columnWidths = new int[]{327, 75, 54, 81, 0};
				gbl_buttonPane.rowHeights = new int[]{25, 0};
				gbl_buttonPane.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
				gbl_buttonPane.rowWeights = new double[]{0.0, Double.MIN_VALUE};
				buttonPane.setLayout(gbl_buttonPane);
			}
			{
				final JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent e)
					{
						try
						{
							Properties properties = PropertyLoader.getInstance().getProperties();
							
							properties.put(PropertyLoader.COLOR_ENTITIES, String.valueOf(btnDefaultClassColor.getBackground().getRGB()));
							properties.put(PropertyLoader.COLOR_GRAPHIC_VIEW, String.valueOf(btnBackgroundColor.getBackground().getRGB()));
							properties.put(PropertyLoader.BACKGROUND_GRADIENT, String.valueOf(ckbBackgroundGradient.isSelected()));
							properties.put(PropertyLoader.CTRL_FOR_GRIP, String.valueOf(chckbxUseCtrlFor.isSelected()));
							properties.put(PropertyLoader.GRID_POINT_OPACITY, String.valueOf(sliderGridPoint.getValue()));
							properties.put(PropertyLoader.GRID_OPACITY_ENABLE, String.valueOf(chckbxOpacityGrid.isSelected()));
							properties.put(PropertyLoader.SMALL_ICON, String.valueOf(chckbxUseSmallIcons.isSelected()));
							properties.put(PropertyLoader.SHOW_ERROR_MESSAGES, String.valueOf(chckbxDisableErrorMessage.isSelected()));
							properties.put(PropertyLoader.SHOW_CROSS_MENU, String.valueOf(chckbxDisableCrossPopup.isSelected()));
							properties.put(PropertyLoader.GRID_VISIBLE, String.valueOf(chckbxShowGrid.isSelected()));
							properties.put(PropertyLoader.GRID_ENABLE, String.valueOf(chckbxEnableGrid.isSelected()));
									
							String quality = "MAX";

							if (rdbtnLow.isSelected())
								quality = "LOW";
							else if (rdbtnMedium.isSelected())
								quality = "MEDIUM";

							properties.put(PropertyLoader.GRAPHIC_QUALITY, quality);

							properties.put(PropertyLoader.FONT_POLICE, String.valueOf(listName.getSelectedValue()));
							properties.put(PropertyLoader.FONT_SIZE, String.valueOf(listSize.getSelectedValue()));

							properties.put(PropertyLoader.AUTOMATIC_GRID_COLOR, String.valueOf(rdbtnAutomaticcolor.isSelected()));
							properties.put(PropertyLoader.GRID_COLOR, String.valueOf(btnColor.getBackground().getRGB()));

							PropertyLoader.getInstance().push();
							
							GraphicView.setGridSize(sliderGridSize.getValue());
						}
						catch (Exception e1)
						{
							e1.printStackTrace();
						}

						setVisible(false);
						PanelClassDiagram.getInstance().getCurrentGraphicView().repaint();
					}

				});
				{
					JButton btnReset = new JButton("Reset");
					btnReset.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
							
							Properties prop = (Properties) PropertyLoader.getInstance().getProperties().clone();
							PropertyLoader.getInstance().reset();
							
							init();
							
							PropertyLoader.getInstance().setProperty(prop);
						}
					});
					GridBagConstraints gbc_btnReset = new GridBagConstraints();
					gbc_btnReset.anchor = GridBagConstraints.NORTHWEST;
					gbc_btnReset.insets = new Insets(0, 0, 0, 5);
					gbc_btnReset.gridx = 0;
					gbc_btnReset.gridy = 0;
					buttonPane.add(btnReset, gbc_btnReset);
				}
				okButton.setActionCommand("OK");
				GridBagConstraints gbc_okButton = new GridBagConstraints();
				gbc_okButton.anchor = GridBagConstraints.NORTHEAST;
				gbc_okButton.insets = new Insets(0, 0, 0, 5);
				gbc_okButton.gridx = 2;
				gbc_okButton.gridy = 0;
				buttonPane.add(okButton, gbc_okButton);
				getRootPane().setDefaultButton(okButton);
			}
			final JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e)
				{
					setVisible(false);
				}

			});
			cancelButton.setActionCommand("Cancel");
			GridBagConstraints gbc_cancelButton = new GridBagConstraints();
			gbc_cancelButton.anchor = GridBagConstraints.NORTHEAST;
			gbc_cancelButton.gridx = 3;
			gbc_cancelButton.gridy = 0;
			buttonPane.add(cancelButton, gbc_cancelButton);
		}
		setLocationRelativeTo(Slyum.getInstance());
		init();
		setVisible(true);
	}
	
	private void init()
	{
		btnBackgroundColor.setBackground(GraphicView.getBasicColor());
		btnDefaultClassColor.setBackground(EntityView.getBasicColor());
		ckbBackgroundGradient.setSelected(GraphicView.getBackgroundGradient());
		chckbxOpacityGrid.setSelected(GraphicView.isGridOpacityEnable());
		sliderGridPoint.setValue(GraphicView.getGridOpacity());
		sliderGridPoint.setEnabled(chckbxOpacityGrid.isSelected());
		sliderGridSize.setValue(GraphicView.getGridSize());
		btnColor.setBackground(new Color(GraphicView.getGridColor()));
		listName.setSelectedValue(TextBox.getFontName(), true);
		listSize.setSelectedValue(TextBox.getFontSize(), true);
		chckbxUseCtrlFor.setSelected(GraphicView.isCtrlForGrip());
		chckbxShowGrid.setSelected(GraphicView.isGridVisible());
		
		switch (Utility.getGraphicQualityType())
		{
			case LOW:
				rdbtnLow.setSelected(true);
				break;

			case MEDIUM:
				rdbtnMedium.setSelected(true);
				break;

			case MAX:
				rdbtnMax.setSelected(true);
				break;
		}
		chckbxDisableErrorMessage.setSelected(Slyum.isShowErrorMessage());
		chckbxDisableCrossPopup.setSelected(Slyum.isShowCrossMenu());
		chckbxUseSmallIcons.setSelected(Slyum.getSmallIcons());
		chckbxEnableGrid.setSelected(GraphicView.isGridEnable());

		if (GraphicView.isAutomatiqueGridColor())
			rdbtnAutomaticcolor.setSelected(true);
		else
			rdbtnSelectedColor.setSelected(true);
		
		setEnableGrid(chckbxEnableGrid.isSelected());
	}
	
	private void setEnableStyleGrid(boolean enable)
	{
		setEnableComponent(panel_grid_opacity, enable);
		setEnableComponent(panel_grid_color, enable);
	}
	
	private void setEnableGrid(boolean enable)
	{
		setEnableComponent(panel_Grid, chckbxEnableGrid.isSelected());
		setEnableStyleGrid(chckbxShowGrid.isSelected() && chckbxEnableGrid.isSelected());
	}
	
	private void setEnableComponent(JPanel p, boolean enable)
	{
		p.setEnabled(enable);
		
		for (Component child : p.getComponents())
			
			if (child instanceof JPanel)
				
				setEnableComponent((JPanel)child, enable);
			else
				
				child.setEnabled(enable);
	}

	private void showOpacityWarning()
	{
		SMessageDialog.showWarningMessage("This option can decrease performance.", this);
	}

}
