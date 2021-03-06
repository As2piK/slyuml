package graphic;

import graphic.entity.AssociationClassView;
import graphic.entity.ClassView;
import graphic.entity.EntityView;
import graphic.entity.InterfaceView;
import graphic.factory.CreateComponent;
import graphic.factory.MultiFactory;
import graphic.relations.AggregationView;
import graphic.relations.BinaryView;
import graphic.relations.CompositionView;
import graphic.relations.DependencyView;
import graphic.relations.InheritanceView;
import graphic.relations.InnerClassView;
import graphic.relations.LineCommentary;
import graphic.relations.LineView;
import graphic.relations.MultiView;
import graphic.textbox.TextBoxCommentary;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.MediaSize;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import swing.IListenerComponentSelectionChanged;
import swing.PanelClassDiagram;
import swing.PropertyLoader;
import swing.SPanelElement;
import swing.SPanelStyleComponent;
import swing.SPanelZOrder;
import swing.Slyum;
import swing.SColorChooser;
import utility.PersonalizedIcon;
import utility.SMessageDialog;
import utility.SizedCursor;
import utility.Utility;
import change.BufferBounds;
import change.BufferColor;
import change.Change;
import classDiagram.ClassDiagram;
import classDiagram.IComponentsObserver;
import classDiagram.IDiagramComponent;
import classDiagram.components.AssociationClass;
import classDiagram.components.ClassEntity;
import classDiagram.components.Entity;
import classDiagram.components.InterfaceEntity;
import classDiagram.relationships.Aggregation;
import classDiagram.relationships.Binary;
import classDiagram.relationships.Composition;
import classDiagram.relationships.Dependency;
import classDiagram.relationships.Inheritance;
import classDiagram.relationships.InnerClass;
import classDiagram.relationships.Multi;
import classDiagram.relationships.Role;
import javax.swing.SwingUtilities;

/**
 * This class is the main container for all diagrams components view
 * (classComponent, interfaceComponent, relation, ...). It performs class
 * diagram, movables and resizables events. Each graphic component have a
 * reference on the graphic view.
 * 
 * @author David Miserez
 * @version 1.0 - 25.07.2011
 */
@SuppressWarnings("serial")
public class GraphicView extends GraphicComponent implements MouseMotionListener, MouseListener, IComponentsObserver, Printable, KeyListener, MouseWheelListener
{
	public final static boolean BACKGROUND_GRADIENT = true;
	public final static boolean CTRL_FOR_GRIP = false;
	public final static Color BASIC_COLOR = new Color(170, 190, 240);
	public final static int GRID_COLOR = Color.DARK_GRAY.getRGB();
	public final static int GRID_POINT_OPACITY = 255;
	public final static int GRID_SIZE = 10;
	public final static boolean GRID_VISIBLE = true;
	public final static boolean IS_AUTOMATIC_GRID_COLOR = false;
	public final static boolean IS_GRID_OPACITY_ENABLE = false;
	public final static boolean IS_GRID_ENABLE = true;
	public final static double SCALE_STEP = 0.1;

	/**
	 * Compute mouse entered and exited event. the componentMouseHover can be
	 * the same as the current component. In this case, no event will be called.
	 * 
	 * @param component
	 *            the current mouse hover component
	 * @param componentMouseHover
	 *            the previous mouse hover component
	 * @param e
	 *            the mouse event
	 */
	public static void computeComponentEventEnter(GraphicComponent component, GraphicComponent componentMouseHover, MouseEvent e)
	{
		if (component != componentMouseHover)
		{
			if (componentMouseHover != null)
				componentMouseHover.gMouseExited(e);

			if (component != null)
				component.gMouseEntered(e);
		}
	}

	public static boolean getBackgroundGradient()
	{
		final String gradientBool = PropertyLoader.getInstance().getProperties().getProperty(PropertyLoader.BACKGROUND_GRADIENT);
		boolean gradient = BACKGROUND_GRADIENT;

		if (gradientBool != null)
			gradient = Boolean.parseBoolean(gradientBool);

		return gradient;
	}

	/**
	 * Get the basic color for the graphic view. The basic color is the color
	 * define by default for new graphic view.
	 * 
	 * @return
	 */
	public static Color getBasicColor()
	{
		final String basicColor = PropertyLoader.getInstance().getProperties().getProperty(PropertyLoader.COLOR_GRAPHIC_VIEW);
		Color color;

		if (basicColor == null)
			color = BASIC_COLOR;
		else
			color = new Color(Integer.parseInt(basicColor));

		return color;
	}

	public static int getGridColor()
	{
		final String prop = PropertyLoader.getInstance().getProperties().getProperty(PropertyLoader.GRID_COLOR);
		int color = GRID_COLOR;

		if (prop != null)
			color = Integer.parseInt(prop);

		return color;
	}
	
	public static int getGridSize()
	{
		final String prop = PropertyLoader.getInstance().getProperties().getProperty(PropertyLoader.GRID_SIZE);
		int size = GRID_SIZE;

		if (prop != null)
			size = Integer.parseInt(prop);

		return isGridEnable() ? size : 1;
	}

	public static boolean isGridVisible()
	{
		final String prop = PropertyLoader.getInstance().getProperties().getProperty(PropertyLoader.GRID_VISIBLE);
		boolean visible = GRID_VISIBLE;

		if (prop != null)
			visible = Boolean.parseBoolean(prop);

		return visible;
	}

	public static int getGridOpacity()
	{
		if (!isGridOpacityEnable())
			return 255;
			
		final String prop = PropertyLoader.getInstance().getProperties().getProperty(PropertyLoader.GRID_POINT_OPACITY);
		int opacity = GRID_POINT_OPACITY;

		if (prop != null)
			opacity = Integer.parseInt(prop);

		return opacity;
	}

	public static boolean isAutomatiqueGridColor()
	{
		final String prop = PropertyLoader.getInstance().getProperties().getProperty(PropertyLoader.AUTOMATIC_GRID_COLOR);
		boolean enable = IS_AUTOMATIC_GRID_COLOR;

		if (prop != null)
			enable = Boolean.parseBoolean(prop);

		return enable;
	}

	public static boolean isCtrlForGrip()
	{
		final String ctrlForGrip = PropertyLoader.getInstance().getProperties().getProperty(PropertyLoader.CTRL_FOR_GRIP);
		boolean ctrlForGripBool = CTRL_FOR_GRIP;

		if (ctrlForGrip != null)
			ctrlForGripBool = Boolean.parseBoolean(ctrlForGrip);

		return ctrlForGripBool;
	}

	public static boolean isGridOpacityEnable()
	{
		final String prop = PropertyLoader.getInstance().getProperties().getProperty(PropertyLoader.GRID_OPACITY_ENABLE);
		boolean enable = IS_GRID_OPACITY_ENABLE;

		if (prop != null)
			enable = Boolean.parseBoolean(prop);

		return enable;
	}

	public static boolean isGridEnable()
	{
		final String prop = PropertyLoader.getInstance().getProperties().getProperty(PropertyLoader.GRID_ENABLE);
		boolean enable = IS_GRID_ENABLE;

		if (prop != null)
			enable = Boolean.parseBoolean(prop);

		return enable;
	}

	/**
	 * Search a component in the given who are on the location given. This
	 * method uses the isAtPosition() method from GraphicComponent for know if
	 * the location given is on the component or not. Return null if no
	 * component are found. This method return the first component found in the
	 * list.
	 * 
	 * @param components
	 *            the list of component extent from GraphicComponent
	 * @param pos
	 *            the location for find a component
	 * @return the component found or null if no component are found
	 */
	public static <T extends GraphicComponent> T searchComponentWithPosition(LinkedList<T> components, Point pos)
	{
		for (final T c : components)
			if (c.isAtPosition(pos))
				return c;

		return null; // no component found
	}

	public static void setAutomaticGridColor(boolean enable)
	{
		PropertyLoader.getInstance().getProperties().put(PropertyLoader.AUTOMATIC_GRID_COLOR, String.valueOf(enable));
		PropertyLoader.getInstance().push();
	}

	public static void setBackgroundGradient(boolean isGradient)
	{
		PropertyLoader.getInstance().getProperties().put(PropertyLoader.BACKGROUND_GRADIENT, String.valueOf(isGradient));
		PropertyLoader.getInstance().push();
	}

	public static void setGridSize(int size)
	{
		PropertyLoader.getInstance().getProperties().put(PropertyLoader.GRID_SIZE, String.valueOf(size));
		PropertyLoader.getInstance().push();
		
		// Update the components bounds for adapting with new grid.
		for (GraphicView gv : PanelClassDiagram.getInstance().getAllGraphicView())
			
			for (final GraphicComponent c : gv.getAllComponents())
				c.setBounds(c.getBounds());
	}

	/**
	 * Set a new basic color for graphic views. The basic color is the color
	 * define by default for new graphic view.
	 * 
	 * @param color
	 */
	public static void setBasicColor(Color color)
	{
		PropertyLoader.getInstance().getProperties().put(PropertyLoader.COLOR_GRAPHIC_VIEW, String.valueOf(color.getRGB()));
		PropertyLoader.getInstance().push();
	}

	public static void setCtrlForClick(boolean active)
	{
		PropertyLoader.getInstance().getProperties().put(PropertyLoader.CTRL_FOR_GRIP, String.valueOf(active));
		PropertyLoader.getInstance().push();
	}

	public static void setGridColor(int color)
	{
		PropertyLoader.getInstance().getProperties().put(PropertyLoader.GRID_COLOR, String.valueOf(color));
		PropertyLoader.getInstance().push();
	}

	public static void setGridOpacityEnable(boolean enable)
	{
		PropertyLoader.getInstance().getProperties().put(PropertyLoader.GRID_OPACITY_ENABLE, String.valueOf(enable));
		PropertyLoader.getInstance().push();
	}
	
	public static void setGridEnable(boolean enable)
	{
		PropertyLoader.getInstance().getProperties().put(PropertyLoader.GRID_ENABLE, String.valueOf(enable));
		PropertyLoader.getInstance().push();
	}

	public static void setGridPointOpacity(int opacity)
	{
		PropertyLoader.getInstance().getProperties().put(PropertyLoader.GRID_POINT_OPACITY, String.valueOf(opacity));
		PropertyLoader.getInstance().push();
	}
	
	public static void setGridVisibley(boolean visible)
	{
		PropertyLoader.getInstance().getProperties().put(PropertyLoader.GRID_VISIBLE, String.valueOf(visible));
		PropertyLoader.getInstance().push();
	}
	
	public boolean getPaintBackgroundLast()
	{
		return paintBackgroundLast;
	}
	
	public void setPaintBackgroundLast(boolean enable)
	{
		paintBackgroundLast = enable;
	}
	
	public void paintBackgroundFirst()
	{
		setPaintBackgroundLast(false);
		repaint();
	}

	private final ClassDiagram classDiagram;
	// last component mouse pressed
	private GraphicComponent componentMousePressed;

	private CreateComponent currentFactory;

	private final LinkedList<EntityView> entities = new LinkedList<>();

	private final LinkedList<LineView> linesView = new LinkedList<>();

	// use in printing
	private int m_maxNumPage = 1;

	private Point mousePressedLocation = new Point();
	private final LinkedList<MultiView> multiViews = new LinkedList<>();
	private String name;

	private final LinkedList<TextBoxCommentary> notes = new LinkedList<>();

	private final LinkedList<GraphicComponent> othersComponents = new LinkedList<>();

	// Selection rectangle.
	private Rectangle rubberBand = new Rectangle();

	// last component mouse hovered
	private GraphicComponent saveComponentMouseHover;
	private final JPanel scene;

	private final JScrollPane scrollPane;	

	private float zoom = 1.0f;
	private boolean stopRepaint = false;
	private boolean paintBackgroundLast = false;
	
	private Rectangle visibleRect = new Rectangle();
	private int mouseButton = 0;
	
	private LinkedList<IListenerComponentSelectionChanged> lcsc = new LinkedList<>();
	
	public double getScale()
	{
		return PanelClassDiagram.getInstance().getsSlider().getValue() / 100.0;
	}
	
	/**
	 * Create a new graphic view representing the class diagram given. The new
	 * graphic view is empty when created. If classDiagram given is not empty,
	 * you must manually add existing component.
	 * 
	 * @param classDiagram
	 *            the class diagram associated with this graphic view.
	 */
	public GraphicView(ClassDiagram classDiagram)
	{
		super();

		if (classDiagram == null)
			throw new IllegalArgumentException("classDiagram is null");

		this.classDiagram = classDiagram;

		scene = new JPanel(null) {

			@Override
			public void paintComponent(Graphics g)
			{
				updatePreferredSize(); // for scrolling
				
				super.paintComponent(g);

				paintScene((Graphics2D) g);
			}
			
			
			@Override
			public void repaint(int x, int y, int width, int height)
			{
				repaint(new Rectangle(x, y, width, height));
			}
			
			@Override
			public void repaint(Rectangle r)
			{
				if (stopRepaint)
					return;
				
				super.repaint(growForRepaint(Utility.scaleRect(r, getScale())));
			}

			@Override
			public void paintImmediately(int x, int y, int w, int h)
			{
				paintImmediately(new Rectangle(x, y, w, h));
			}

			@Override
			public void paintImmediately(Rectangle r)
			{
				if (stopRepaint)
					return;
				
				super.paintImmediately(r.x, r.y, r.width, r.height);
			}
			
			
			
			private Rectangle growForRepaint(Rectangle rect)
			{
				return Utility.growRectangle(new Rectangle(rect), getGridSize() + 10);
			}

			@Override
			public void setCursor(Cursor cursor)
			{
				if (currentFactory != null) // cursor change are disabled during
					// creation of new component
					cursor = currentFactory.getCursor();

				super.setCursor(cursor);
			}
		};

		scrollPane = new JScrollPane(scene);
		scrollPane.getVerticalScrollBar().setUnitIncrement(50);
		
		scene.addMouseWheelListener(this);
		scene.addKeyListener(this);
		scene.addMouseMotionListener(this);
		scene.addMouseListener(this);
		
		saveComponentMouseHover = this;

		classDiagram.addComponentsObserver(this);

		setColor(getBasicColor());

		JMenuItem menuItem;

		popupMenu.addSeparator();

		// Menu item add class
		menuItem = makeMenuItem("Add Class", Slyum.ACTION_NEW_CLASS, "class16");
		popupMenu.add(menuItem);

		// Menu item add interface
		menuItem = makeMenuItem("Add Interface", Slyum.ACTION_NEW_INTERFACE, "interface16");
		popupMenu.add(menuItem);

		// Menu item add class association
		menuItem = makeMenuItem("Add Association class", Slyum.ACTION_NEW_CLASS_ASSOCIATION, "classAssoc16");
		popupMenu.add(menuItem);

		popupMenu.addSeparator();

		// Menu item add generalize
		menuItem = makeMenuItem("Add Inheritance", Slyum.ACTION_NEW_GENERALIZE, "generalize16");
		popupMenu.add(menuItem);

		// Menu item add inner class
		menuItem = makeMenuItem("Add inner class", Slyum.ACTION_NEW_INNER_CLASS, "innerClass16");
		popupMenu.add(menuItem);

		// Menu item add dependency
		menuItem = makeMenuItem("Add Dependency", Slyum.ACTION_NEW_DEPENDENCY, "dependency16");
		popupMenu.add(menuItem);

		// Menu item add association
		menuItem = makeMenuItem("Add Association", Slyum.ACTION_NEW_ASSOCIATION, "association16");
		popupMenu.add(menuItem);

		// Menu item add aggregation
		menuItem = makeMenuItem("Add Aggregation", Slyum.ACTION_NEW_AGGREGATION, "aggregation16");
		popupMenu.add(menuItem);

		// Menu item add composition
		menuItem = makeMenuItem("Add Composition", Slyum.ACTION_NEW_COMPOSITION, "composition16");
		popupMenu.add(menuItem);

		// Menu item add composition
		menuItem = makeMenuItem("Add Multi-association", Slyum.ACTION_NEW_MULTI, "multi16");
		popupMenu.add(menuItem);

		popupMenu.addSeparator();

		// Menu item add note
		menuItem = makeMenuItem("Add Note", Slyum.ACTION_NEW_NOTE, "note16");
		popupMenu.add(menuItem);

		// Menu item link note
		menuItem = makeMenuItem("Link Note", Slyum.ACTION_NEW_LINK_NOTE, "linkNote16");
		popupMenu.add(menuItem);
		
		addSPanelListener();
	}
	
	private void addSPanelListener()
	{
		addListenerSelectionChanged(SPanelElement.getInstance());
		addListenerSelectionChanged(SPanelStyleComponent.getInstance());
		addListenerSelectionChanged(SPanelZOrder.getInstance());
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if ("Color".equals(e.getActionCommand()))
		{
			final SColorChooser scc = new SColorChooser(getColor());
			scc.setVisible(true);

			if (scc.isAccepted())
				setColor(scc.getColor());
		}
		else
			super.actionPerformed(e);

		Slyum.getInstance().actionPerformed(e);
	}

	@Override
	public void addAggregation(Aggregation component)
	{
		final GraphicComponent result = searchAssociedComponent(component);

		if (result == null)
		{
			final LinkedList<Role> roles = component.getRoles();
			final EntityView source = (EntityView) searchAssociedComponent(roles.getFirst().getEntity());
			final EntityView target = (EntityView) searchAssociedComponent(roles.getLast().getEntity());

			addComponentIn(new AggregationView(this, source, target, component, source.middleBounds(), target.middleBounds(), false), linesView);
		}
	}

	@Override
	public void addAssociationClass(AssociationClass component)
	{
		final GraphicComponent result = searchAssociedComponent(component);

		if (result == null)
		{
			final BinaryView bv = (BinaryView) searchAssociedComponent(component.getAssociation());
			addComponentIn(new AssociationClassView(this, component, bv, new Rectangle(100, 100, 100, 100)), entities);
		}
	}

	@Override
	public void addBinary(Binary component)
	{
		final GraphicComponent result = searchAssociedComponent(component);

		if (result == null)
		{
			final LinkedList<Role> roles = component.getRoles();
			final EntityView source = (EntityView) searchAssociedComponent(roles.getFirst().getEntity());
			final EntityView target = (EntityView) searchAssociedComponent(roles.getLast().getEntity());

			addComponentIn(new BinaryView(this, source, target, component, source.middleBounds(), target.middleBounds(), false), linesView);
		}
	}

	@Override
	public void addClass(ClassEntity component)
	{
		final GraphicComponent result = searchAssociedComponent(component);

		if (result == null)
			addComponentIn(new ClassView(this, component), entities);
	}

	public <T extends GraphicComponent> boolean addComponentIn(T component, LinkedList<T> list)
	{
		if (component == null)
			throw new IllegalArgumentException("component is null");

		if (!list.contains(component) && list.add(component))
		{
			getScene().paintImmediately(component.getBounds());
			return true;
		}

		return false;
	}

	@Override
	public void addComposition(Composition component)
	{
		final GraphicComponent result = searchAssociedComponent(component);

		if (result == null)
		{
			final LinkedList<Role> roles = component.getRoles();
			final EntityView source = (EntityView) searchAssociedComponent(roles.getFirst().getEntity());
			final EntityView target = (EntityView) searchAssociedComponent(roles.getLast().getEntity());

			addComponentIn(new CompositionView(this, source, target, component, source.middleBounds(), target.middleBounds(), false), linesView);
		}
	}

	@Override
	public void addDependency(Dependency component)
	{
		final GraphicComponent result = searchAssociedComponent(component);

		if (result == null)
		{
			final EntityView source = (EntityView) searchAssociedComponent(component.getSource());
			final EntityView target = (EntityView) searchAssociedComponent(component.getTarget());

			addComponentIn(new DependencyView(this, source, target, component, source.middleBounds(), target.middleBounds(), false), linesView);
		}
	}

	/**
	 * Add a new entity view. Adding a component means it will be drawn and
	 * managed by the graphic view.
	 * 
	 * @param component
	 *            the entity to add.
	 * @return true if the component has been added; false otherwise
	 */
	public boolean addEntity(EntityView component)
	{
		return addComponentIn(component, entities);
	}

	@Override
	public void addInheritance(Inheritance component)
	{
		final GraphicComponent result = searchAssociedComponent(component);

		if (result == null)
		{
			final EntityView child = (EntityView) searchAssociedComponent(component.getChild());
			final EntityView parent = (EntityView) searchAssociedComponent(component.getParent());

			addComponentIn(new InheritanceView(this, child, parent, component, child.middleBounds(), parent.middleBounds(), false), linesView);
		}
	}

	@Override
	public void addInnerClass(InnerClass component)
	{
		final GraphicComponent result = searchAssociedComponent(component);

		if (result == null)
		{
			final EntityView child = (EntityView) searchAssociedComponent(component.getChild());
			final EntityView parent = (EntityView) searchAssociedComponent(component.getParent());

			addComponentIn(new InnerClassView(this, child, parent, component, child.middleBounds(), parent.middleBounds(), false), linesView);
		}
	}

	@Override
	public void addInterface(InterfaceEntity component)
	{
		final GraphicComponent result = searchAssociedComponent(component);

		if (result == null)
			addComponentIn(new InterfaceView(this, component), entities);
	}

	/**
	 * Add a new line view. Adding a component means it will be drawn and
	 * managed by the graphic view.
	 * 
	 * @param component
	 *            the line to add.
	 * @return true if the component has been added; false otherwise
	 */
	public boolean addLineView(LineView component)
	{
		return addComponentIn(component, linesView);
	}

	@Override
	public void addMulti(Multi component)
	{
		final GraphicComponent result = searchAssociedComponent(component);

		if (result == null)

			addComponentIn(MultiFactory.createMulti(this, component), multiViews);
	}

	/**
	 * Add a new multi line view. Adding a component means it will be drawn and
	 * managed by the graphic view.
	 * 
	 * @param component
	 *            the multi line to add.
	 * @return true if the component has been added; false otherwise
	 */
	public boolean addMultiView(MultiView component)
	{
		return addComponentIn(component, multiViews);
	}

	/**
	 * Add a new note. Adding a component means it will be drawn and managed by
	 * the graphic view.
	 * 
	 * @param component
	 *            the note to add.
	 * @return true if the component has been added; false otherwise
	 */
	public boolean addNotes(TextBoxCommentary component)
	{
		return addComponentIn(component, notes);
	}

	/**
	 * Add a new graphic component. Adding a component means it will be drawn
	 * and managed by the graphic view.
	 * 
	 * @param component
	 *            the new graphic component to add.
	 * @return true if the component has been added; false otherwise
	 */
	public boolean addOthersComponents(GraphicComponent component)
	{
		return addComponentIn(component, othersComponents);
	}
	
	public boolean addListenerSelectionChanged(IListenerComponentSelectionChanged i)
	{
		return lcsc.add(i);
	}
	
	public boolean removeListenerSelectionChanged(IListenerComponentSelectionChanged i)
	{
		return lcsc.remove(i);
	}

	/**
	 * Get all entities contents in this graphic view and adjust their width.
	 * See adjustWidth() method from EntityView.
	 */
	public void adjustWidthAllEntities()
	{
		adjustEntities(getEntitiesView());
	}

	/**
	 * Get all selected entities contents in this graphic view and adjust their
	 * width. See adjustWidth() method from EntityView.
	 */
	public void adjustWidthSelectedEntities()
	{
		adjustEntities(getSelectedEntities());
	}
	
	/**
	 * Get entities contents in this graphic view and adjust their
	 * width. See adjustWidth() method from EntityView.
	 * @param list the entities list to selected
	 */
	public void adjustEntities(LinkedList<EntityView> list)
	{
		boolean isRecord = Change.isRecord();
		Change.record();
		
		for (final EntityView ev : list)
			
			ev.adjustWidth();

		if (!isRecord)
			Change.stopRecord();
	}

	/**
	 * Align all selected entities with the hightest or lowest selected entity.
	 * Make same space between all selected entities.
	 * 
	 * @param true for align top; false for align bottom
	 */
	public void alignHorizontal(boolean top)
	{		
		int totalWidth = 0, bottom = Integer.MIN_VALUE;

		final LinkedList<EntityView> sorted = sortXLocation(getSelectedEntities());

		if (sorted.size() < 2)
			return;
		
		for (final EntityView c : sorted)
		{
			final Rectangle bounds = c.getBounds();

			if (bounds.y > bottom)
				bottom = bounds.y;

			totalWidth += c.getBounds().width;
		}

		final Rectangle limits = Utility.getLimits(sorted);

		int space = (limits.width - totalWidth) / (sorted.size() - 1);

		space = space < 0 ? 10 : space;

		int offset = limits.x;

		boolean isRecord = Change.isRecord();
		Change.record();
		
		for (final GraphicComponent c : sorted)
		{
			final Rectangle bounds = c.getBounds();

			Change.push(new BufferBounds(c));
			c.setBounds(new Rectangle(offset, top ? limits.y : bottom, bounds.width, bounds.height));
			Change.push(new BufferBounds(c));
			offset += bounds.width + space;
		}
		
		if (!isRecord)
			Change.stopRecord();
	}

	/**
	 * Align all selected entities with the leftmost or rightmost selected
	 * entity. Make same space between all selected entities.
	 * 
	 * @param true for align left; false for align right
	 */
	public void alignVertical(boolean left)
	{
		boolean isRecord = Change.isRecord();
		Change.record();
		
		int totalHeight = 0, right = Integer.MIN_VALUE;

		final LinkedList<EntityView> sorted = sortYLocation(getSelectedEntities());

		if (sorted.size() < 2)
			return;

		for (final EntityView c : sorted)
		{
			final Rectangle bounds = c.getBounds();

			if (bounds.x > right)
				right = bounds.x;

			totalHeight += c.getBounds().height;
		}

		final Rectangle limits = Utility.getLimits(sorted);

		int space = (limits.height - totalHeight) / (sorted.size() - 1);

		space = space < 10 ? 20 : space;

		int offset = limits.y;

		for (final GraphicComponent c : sorted)
		{
			final Rectangle bounds = c.getBounds();

			Change.push(new BufferBounds(c));
			c.setBounds(new Rectangle(left ? limits.x : right, offset, bounds.width, bounds.height));
			Change.push(new BufferBounds(c));
			offset += bounds.height + space;
		}
		
		if (!isRecord)
			Change.stopRecord();
	}

	/**
	 * Get all selected component and change their color.
	 * 
	 * @param newColor
	 *            the new color for selected components
	 */
	public void changeColorForSelectedItems(Color newColor)
	{
		LinkedList<GraphicComponent> gc = getSelectedComponents();
		
		if (gc.isEmpty())
			
			setColor(newColor);
		
		else
		{
			boolean isRecord = Change.isRecord();
			Change.record();
			
			for (final GraphicComponent c : getSelectedComponents())
			{
				// Set default style before save color.
				c.setDefaultStyle();
				
				Change.push(new BufferColor(c));
				c.setColor(newColor);
				Change.push(new BufferColor(c));
			}
			
			if (!isRecord)
				Change.stopRecord();
		}
	}

	/**
	 * Change the size of the police. It is the responsibility of graphic
	 * component to consider the new size or not. The police size is compute
	 * with the actuel size * zoom. By default zoom is at 1.0f, adding a new
	 * grow will add the zoom coefficient (zoom + grow).
	 * 
	 * @param grow
	 *            new value for zoom. A grow positive will make the police size
	 *            greater, a negativ grow make the police smaller.
	 */
	public void changeSizeText(float grow)
	{
		setZoom(getZoom() + grow);
	}

	@Override
	public void changeZOrder(Entity first, int index)
	{
		final EntityView firstView = (EntityView) searchAssociedComponent(first);

		entities.remove(firstView);
		entities.add(index, firstView);

		firstView.repaint();
	}

	/**
	 * Unselect all component.
	 */
	public void unselectAll()
	{
		final LinkedList<GraphicComponent> components = getAllComponents();

		for (final GraphicComponent c : components)

			c.setSelected(false);
	}

	/**
	 * Hide the rubber band, compute components who was in rubber band and
	 * select them.
	 */
	public int clearRubberBand()
	{
		int nbrComponentSelected = 0;

		// use for repaint old position
		final Rectangle previousRect = new Rectangle(rubberBand);

		// get all selectable components
		final LinkedList<GraphicComponent> components = new LinkedList<GraphicComponent>();
		components.addAll(entities);
		components.addAll(linesView);
		components.addAll(multiViews);
		components.addAll(notes);

		for (final GraphicComponent c : components)
		{
			// see if the intersection area is the same that component area.
			final Rectangle bounds = c.getBounds();
			final Rectangle intersection = rubberBand.intersection(bounds);

			final int intersectionArea = intersection.width * intersection.height;
			final int componentArea = bounds.width * bounds.height;

			// if it the same, the component is completely covered by the
			// rubber band.
			if (intersectionArea == componentArea)
			{
				c.setSelected(true);
				nbrComponentSelected++;
			}
		}

		rubberBand = new Rectangle();

		scene.repaint(previousRect);

		return nbrComponentSelected;
	}
	
	/**
	 * Call when a component is selected or unselected.
	 * 
	 * @param select a component is selected or not
	 */
	public void componentSelected(boolean select)
	{
		for (IListenerComponentSelectionChanged i : lcsc)
			i.componentSelectionChanged();
	}

	@Override
	public Point computeAnchorLocation(Point first, Point next)
	{
		return next;
	}

	/**
	 * Return if the graphic component is in the graphic view or not.
	 * 
	 * @param component
	 *            the component to find.
	 * @return true if the component is in the graphic view; false otherwise
	 */
	public boolean containComponent(GraphicComponent component)
	{
		if (component == null)
			throw new IllegalArgumentException("component is null");

		return getAllComponents().contains(component);
	}
	
	public int countSelectedComponents()
	{
		return getSelectedComponents().size();
	}
	
	public int countSelectedEntities()
	{
		return getSelectedEntities().size();
	}
	
	public int countEntities()
	{
		return getEntitiesView().size();
	}

	/**
	 * Remove the current factory.
	 */
	public void deleteCurrentFactory()
	{
		currentFactory.deleteFactory();
		currentFactory = null;
	}

	/**
	 * Not use in Slumy 1.0. Draw beta margin of A4 format.
	 * 
	 * @param g2
	 *            the graphic context
	 */
	public void drawA4Margin(Graphics2D g2)
	{
		final int screenDpi = Toolkit.getDefaultToolkit().getScreenResolution();
		float width = MediaSize.ISO.A4.getX(Size2DSyntax.INCH);
		float height = MediaSize.ISO.A4.getY(Size2DSyntax.INCH);

		width *= screenDpi;
		height *= screenDpi;

		final Rectangle limits = Utility.getLimits(entities);
		g2.drawRect(limits.x, limits.y, (int) width, (int) height);
	}

	/**
	 * Get all components contains in graphic view.
	 * 
	 * @return an array of all graphic component
	 */
	public LinkedList<GraphicComponent> getAllComponents()
	{
		final LinkedList<GraphicComponent> components = new LinkedList<GraphicComponent>();

		// Order is important, this method is used to compute mouse event.
		components.addAll(entities);
		components.addAll(linesView);
		components.addAll(multiViews);
		components.addAll(notes);
		components.addAll(othersComponents);

		return components;
	}

	@Override
	public Rectangle getBounds()
	{
		return scene.getBounds();
	}

	/**
	 * Get the class diagram associated with this graphic view.
	 * 
	 * @return
	 */
	public ClassDiagram getClassDiagram()
	{
		return classDiagram;
	}
	
	@Override
	public Color getColor()
	{
		return getBasicColor();
	}

	/**
	 * Search a component in the graphic view who are at the given location.
	 * Return the graphic view if no component are found.
	 * 
	 * @param pos
	 *            the location for find a component
	 * @return the component at the location; or the graphic view if no
	 *         component are at this location
	 */
	public GraphicComponent getComponentAtPosition(Point pos)
	{
		final GraphicComponent component = getComponentListAtPosition(getAllComponents(), pos);

		return component == null ? this : component;
	}

	/**
	 * Same as searchComponentWithPosition(), but inverse the order of graphic
	 * components.
	 * 
	 * @param list
	 *            the list where find component
	 * @param pos
	 *            the location for find graphic components
	 * @return the component found; or null if no component are found.
	 */
	public <T extends GraphicComponent> T getComponentListAtPosition(LinkedList<T> list, Point pos)
	{
		// last component first
		final Iterator<T> iter = list.descendingIterator();
		final LinkedList<T> inversed = new LinkedList<T>();

		while (iter.hasNext())

			inversed.add(iter.next());

		final T component = searchComponentWithPosition(inversed, pos);

		return component;
	}

	/**
	 * Same as getComponentListAtPosition(), but search only diagram elements
	 * (include entities, relations and notes). See getDiagramElements for know
	 * arrays used. This method permit to define a component we will not find.
	 * 
	 * @param pos
	 *            the location for find diagram components
	 * @param except
	 *            remove this component from arrays and no longer be found
	 * @return the component found or null if no component are found
	 */
	public GraphicComponent getDiagramElementAtPosition(Point pos, GraphicComponent except)
	{
		final LinkedList<GraphicComponent> components = getDiagramElements();
		components.remove(except);

		return getComponentListAtPosition(components, pos);
	}

	/**
	 * Get all diagram elements contains in the graphic view. Diagram elements
	 * are : - Lines views - Entities views - Multi views - Notes views
	 * 
	 * @return an array containing all diagram elements
	 */
	public LinkedList<GraphicComponent> getDiagramElements()
	{
		// Don't change order!!!!
		final LinkedList<GraphicComponent> components = new LinkedList<GraphicComponent>();
		components.addAll(linesView);
		components.addAll(entities);
		components.addAll(multiViews);
		components.addAll(notes);

		return components;
	}

	/**
	 * Get all entities containing in graphic view.
	 * 
	 * @return an array containing all entities
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<EntityView> getEntitiesView()
	{
		return (LinkedList<EntityView>) entities.clone();
	}

	/**
	 * Same as getComponentListAtPosition(), but only for entities.
	 * 
	 * @param pos
	 *            the location of entity to find
	 * @return the entity found or null if no entity are found
	 */
	public EntityView getEntityAtPosition(Point pos)
	{
		return getComponentListAtPosition(entities, pos);
	}

	/**
	 * Get all lines views containing in graphic view.
	 * 
	 * @return an array containing all lines views
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<LineView> getLinesView()
	{
		return (LinkedList<LineView>) linesView.clone();
	}

	/**
	 * Search all relations associated with the given graphic component. An
	 * associated relation is an association who have at one or more extremities
	 * the graphic component given.
	 * 
	 * @param component
	 *            the component to find its relations
	 * @return an array containing all relations associated with the given
	 *         component
	 */
	public LinkedList<LineView> getLinesViewAssociedWith(GraphicComponent component)
	{
		final LinkedList<LineView> list = new LinkedList<LineView>();

		for (final LineView lv : linesView)

			if (lv.getFirstPoint().getAssociedComponentView().equals(component) || lv.getLastPoint().getAssociedComponentView().equals(component))
				list.add(lv);

		return list;
	}

	/**
	 * Same as getComponentListAtPosition(), but only for lines views.
	 * 
	 * @param pos
	 *            the location of line view to find
	 * @return the line view found or null if no line view are found
	 */
	public LineView getLineViewAtPosition(Point pos)
	{
		return getComponentListAtPosition(linesView, pos);
	}

	@SuppressWarnings("unchecked")
	public LinkedList<MultiView> getMultiView()
	{
		return (LinkedList<MultiView>) multiViews.clone();
	}

	/**
	 * Get the name for this graphic view.
	 * 
	 * @return the name for this graphic view
	 */
	public String getName()
	{
		return name == null || name.isEmpty() ? "view no name" : name;
	}

	/**
	 * Get the scene (JPanel) where graphic view draw components.
	 * 
	 * @return the scene
	 */
	public JPanel getScene()
	{
		return scene;
	}

	/**
	 * Make a picture (BufferedImage) representing the scene.
	 * 
	 * @return a picture representing the scene
	 */
	public BufferedImage getScreen(int type)
	{
		final int margin = 20;
		int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = 0, maxY = 0;
		final LinkedList<GraphicComponent> components = getAllComponents();

		// Compute the rectangle englobing all graphic components.
		for (final GraphicComponent c : components)
		{
			final Rectangle bounds = c.getBounds();
			final Point max = new Point(bounds.x + bounds.width, bounds.y + bounds.height);

			if (minX > bounds.x)
				minX = bounds.x;
			if (minY > bounds.y)
				minY = bounds.y;
			if (maxX < max.x)
				maxX = max.x;
			if (maxY < max.y)
				maxY = max.y;
		}

		final Rectangle bounds = new Rectangle(minX, minY, maxX - minX, maxY - minY);

		// Create the buffered image with margin.
		final BufferedImage img = new BufferedImage(bounds.width + margin * 2, bounds.height + margin * 2, type);
		final Graphics2D g2 = img.createGraphics();
		Utility.setRenderQuality(g2);

		// Unselect all component (we will not see graphic selection style in
		// image exportation).
		unselectAll();

		// Translate the rectangle containing all graphic components at origin.
		g2.translate(-bounds.x + margin, -bounds.y + margin);

		if (type == BufferedImage.TYPE_INT_RGB)
		{
			g2.setColor(Color.WHITE);
			g2.fillRect(bounds.x - margin, bounds.y - margin, bounds.width + margin * 2, bounds.height + margin * 2);
		}
		
		// Paint all components on picture.
		for (final GraphicComponent c : components)
			c.paintComponent(g2);

		return img;
	}

	/**
	 * Get the scrollPane containing the scene.
	 * 
	 * @return
	 */
	public JScrollPane getScrollPane()
	{
		return scrollPane;
	}

	/**
	 * Get all selected component containing in graphic view.
	 * 
	 * @return all selected component
	 */
	public LinkedList<GraphicComponent> getSelectedComponents()
	{
		final LinkedList<GraphicComponent> components = new LinkedList<GraphicComponent>();
		final LinkedList<GraphicComponent> selected = new LinkedList<GraphicComponent>();

		components.addAll(entities);
		components.addAll(linesView);
		components.addAll(multiViews);
		components.addAll(notes);

		for (final GraphicComponent c : components)

			if (c.isSelected())

				selected.add(c);

		return selected;
	}

	/**
	 * Get all selected entities containing in graphic view.
	 * 
	 * @return all selected entities
	 */
	public LinkedList<EntityView> getSelectedEntities()
	{
		final LinkedList<EntityView> selectedEntities = new LinkedList<EntityView>();

		for (final GraphicComponent c : entities)
			if (c.isSelected())
				selectedEntities.add((EntityView) c);

		return selectedEntities;
	}

	/**
	 * Make a picture (BufferedImage) with the selected components.
	 * 
	 * @return a picture representing the scene
	 */
	public BufferedImage getSelectedScreen()
	{
		final int margin = 20;
		int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = 0, maxY = 0;
		final LinkedList<GraphicComponent> components = getSelectedComponents();

		// Compute the rectangle englobing all selected graphic components.
		for (final GraphicComponent c : components)
		{
			final Rectangle bounds = c.getBounds();
			final Point max = new Point(bounds.x + bounds.width, bounds.y + bounds.height);

			if (minX > bounds.x)
				minX = bounds.x;
			if (minY > bounds.y)
				minY = bounds.y;
			if (maxX < max.x)
				maxX = max.x;
			if (maxY < max.y)
				maxY = max.y;
		}

		final Rectangle bounds = new Rectangle(minX, minY, maxX - minX, maxY - minY);

		if (bounds.isEmpty())
			return null;

		// Create the buffered image with margin.
		final BufferedImage img = new BufferedImage(bounds.width + margin * 2, bounds.height + margin * 2, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g2 = img.createGraphics();
		Utility.setRenderQuality(g2);

		// Unselect all component (we will not see graphic selection style in
		// image exportation).
		unselectAll();

		// Translate the rectangle containing all graphic components at origin.
		g2.translate(-bounds.x + margin, -bounds.y + margin);

		// Paint all components on picture.
		for (final GraphicComponent c : getAllComponents())
			c.paintComponent(g2);

		// Reselect selected components.
		for (final GraphicComponent c : components)
			c.setSelected(true);

		return img;
	}

	/**
	 * Get the police zoom. Use for compute police size.
	 * 
	 * @return the police zoom
	 */
	public float getZoom()
	{
		return zoom;
	}

	@Override
	public void gMouseDragged(MouseEvent e)
	{
		if (mouseButton == MouseEvent.BUTTON1)
			redrawRubberBand(mousePressedLocation, e.getPoint());
	}

	@Override
	public void gMousePressed(MouseEvent e)
	{
		super.gMousePressed(e);

		switch (mouseButton)
		{
		case MouseEvent.BUTTON1:
			scene.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			if (!e.isControlDown())
				unselectAll();
			break;
			
		case MouseEvent.BUTTON2:
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Image image = PersonalizedIcon.createImageIcon(Slyum.ICON_PATH + "drag_hand.png").getImage();
			image = SizedCursor.getPreferredSizedCursor(image);
			Cursor brokenCursor = toolkit.createCustomCursor(image , new Point(0, 0), "drag_hand");
			scene.setCursor(brokenCursor);
			break;
		}			
	}

	@Override
	public void gMouseReleased(MouseEvent e)
	{
		super.gMouseReleased(e);
		clearRubberBand();
		final int nbrComponentSelected = countSelectedEntities();

		if (nbrComponentSelected > 0 && e.getButton() == MouseEvent.BUTTON1)
			new StyleCross(this, e.getPoint(), nbrComponentSelected);

		scene.setCursor(Cursor.getDefaultCursor());
	}

	/**
	 * Redirect all mouse event to the factory given and draw the factory. A
	 * factory is use for creating new graphic component. For remove a current
	 * factory, calls deleteCurrentFactory().
	 * 
	 * @param factory
	 *            the new factory
	 */
	public void initNewComponent(CreateComponent factory)
	{
		if (factory == null)
			throw new IllegalArgumentException("factory is null");

		// If current factory exists, delete it!
		if (currentFactory != null)
			deleteCurrentFactory();

		currentFactory = factory;

		scene.setCursor(factory.getCursor());
	}

	@Override
	public boolean isAtPosition(Point mouse)
	{
		return true;
	}
	
	public void copyDiagramToClipboard()
	{
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new Utility.ImageSelection(getSelectedScreen()), null);
	}
	
	public void deleteSelectedComponents()
	{
		final LinkedList<GraphicComponent> selected = getSelectedComponents();
		
		if (selected.size() == 0 || SMessageDialog.showQuestionMessageYesNo("Are you sur to delete this component and all its associated components?") == JOptionPane.NO_OPTION)

			return;
		
		boolean isRecord = Change.isRecord();
		Change.record();

		for (final GraphicComponent c : selected)

			c.delete();
		
		if (!isRecord)
			Change.stopRecord();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_DELETE)
	
			deleteSelectedComponents();
		
		else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			
			unselectAll();
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	public void linkNewNoteWithSelectedEntities()
	{
		setStopRepaint(true);
		LinkedList<GraphicComponent> e = getSelectedComponents();
		
		TextBoxCommentary tbc = null;
		
		boolean isRecord = Change.isRecord();
		Change.record();
		
		if (e.isEmpty())
			
			tbc = new TextBoxCommentary(parent, TextBoxCommentary.DEFAULT_TEXT, this);
		
		else
		{
			tbc = new TextBoxCommentary(parent, TextBoxCommentary.DEFAULT_TEXT, e.getFirst());
			
			e.remove(0);
			
			for (GraphicComponent ev : e)
				if (LineCommentary.checkCreate(ev, tbc, false))
					parent.addLineView(new LineCommentary(parent, ev, tbc, new Point(), new Point(), false));
		}
		
		Rectangle b = tbc.getBounds();
		Rectangle loc = getScene().getVisibleRect();
		
		b.x = (int)(loc.getCenterX() * getInversedScale());
		b.y = (int)(loc.getCenterY() * getInversedScale());
				
		tbc.setBounds(b);
		
		parent.addNotes(tbc);
		
		if (!isRecord)
			Change.stopRecord();
		
		goRepaint();
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		e = adapteMouseEvent(e);
		GraphicComponent component;

		if (currentFactory != null)

			component = currentFactory;
		else

			component = getComponentAtPosition(e.getPoint());

		if (mouseButton != MouseEvent.BUTTON2 || component == this)
			
			component.gMouseClicked(e);
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		MouseEvent ea = adapteMouseEvent(e);
		GraphicComponent component;
		Rectangle newVisibleRect;
		
		if (mouseButton == MouseEvent.BUTTON2)
		{
			int dx = (int)(mousePressedLocation.x * getScale()-e.getX()),
				dy = (int)(mousePressedLocation.y * getScale()-e.getY());
			
			visibleRect.translate(dx, dy);
			
			newVisibleRect = new Rectangle(visibleRect);
		}
		else
			newVisibleRect = new Rectangle((int)((double)ea.getX() * getScale()),
					(int)((double)ea.getY() * getScale()),
					1,
					1);

		if (currentFactory != null)
			component = currentFactory;
		else
			component = getComponentAtPosition(ea.getPoint());

		computeComponentEventEnter(component, saveComponentMouseHover, ea);

		if (mouseButton != MouseEvent.BUTTON2)
			componentMousePressed.gMouseDragged(ea);

		saveComponentMouseHover = component;

		getScene().scrollRectToVisible(newVisibleRect);
		visibleRect = getScene().getVisibleRect();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// this event, in Slyum, is call manually when mouseMove is called.
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		// this event, in Slyum, is call manually when mouseMove is called.
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		e = adapteMouseEvent(e);
		GraphicComponent component;

		if (currentFactory != null)
			component = currentFactory;
		else
			component = getComponentAtPosition(e.getPoint());

		// Compute mouseEntered and mouseExited event.
		computeComponentEventEnter(component, saveComponentMouseHover, e);
		
		component.gMouseMoved(e);

		// Save the last component mouse hovered. Useful for compute
		// mouseEntered and mouseExited event.
		saveComponentMouseHover = component;
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		scene.requestFocusInWindow(); // get the focus
		mouseButton = e.getButton();

		e = adapteMouseEvent(e);
		mousePressedLocation = e.getPoint();
		visibleRect = getScene().getVisibleRect();
		mousePressedLocation = e.getPoint();

		GraphicComponent component;
		
		if (currentFactory != null)
			component = currentFactory;
		else
			component = getComponentAtPosition(e.getPoint());
		
		// Save the last component mouse pressed.
		componentMousePressed = component;
		
		if (e.getButton() != MouseEvent.BUTTON2 || component == this)
		
			component.gMousePressed(e);		
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		e = adapteMouseEvent(e);

		if (mouseButton != MouseEvent.BUTTON2)
			
			componentMousePressed.gMouseReleased(e);
		
		getScene().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mouseWheelMoved(final MouseWheelEvent e)
	{
		if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL)
			
			if (e.isControlDown())
			{				
				if (e.getWheelRotation() < 0)
				{ backScale(); }
				else
				{ forwardScale(); }
			}
			else
			{
				JScrollBar s = scrollPane.getVerticalScrollBar();
				s.setValue(s.getValue() + s.getUnitIncrement() * (e.getUnitsToScroll() < 0 ? -1 : 1));	
			}
	}
	
	public void moveZOrderUpSelectedEntities()
	{
		LinkedList<EntityView> evs = getSelectedEntities();
		LinkedList<EntityView> evsSorted = new LinkedList<>();
		int current = 0, max = -1, size = evs.size();
		
		for (int i = 0; i < size; i++)
		{
			for (EntityView ev : evs)
			{
				int io = getEntitiesView().indexOf(ev);
				
				if (io > max)
				{
					max = io;
					current = evs.indexOf(ev);
				}
			}
			evsSorted.add(evs.remove(current));
			current = 0; max = -1;
		}
		
		boolean isRecord = Change.isRecord();
		Change.record();
		
		for (EntityView ev : evsSorted)
			getClassDiagram().changeZOrder(ev.getComponent(), getEntitiesView().indexOf(ev) + 1);
		
		if (!isRecord)
			Change.stopRecord();
	}
	
	public void moveZOrderDownSelectedEntities()
	{
		boolean isRecord = Change.isRecord();
		Change.record();
		
		for (EntityView ev : getSelectedEntities())
			
			getClassDiagram().changeZOrder(ev.getComponent(), getEntitiesView().indexOf(ev) - 1);
		
		if (!isRecord)
			Change.stopRecord();
	}
	
	public void moveZOrderTopSelectedEntities()
	{
		boolean isRecord = Change.isRecord();
		Change.record();
		
		for (EntityView ev : getSelectedEntities())
			
			getClassDiagram().changeZOrder(ev.getComponent(), getEntitiesView().size() - 1);
		
		if (!isRecord)
			Change.stopRecord();
	}
	
	public void moveZOrderBottomSelectedEntities()
	{
		boolean isRecord = Change.isRecord();
		Change.record();
		
		for (EntityView ev : getSelectedEntities())
			
			getClassDiagram().changeZOrder(ev.getComponent(), 0);
		
		if (!isRecord)
			Change.stopRecord();
	}
	
	protected MouseEvent adapteMouseEvent(MouseEvent e)
	{
		return  new MouseEvent(e.getComponent(),
				e.getID(),
				e.getWhen(),
				e.getModifiers(),
				(int)(e.getX() * getInversedScale()),
				(int)(e.getY() * getInversedScale()),
				e.getXOnScreen(),
				e.getYOnScreen(),
				e.getClickCount(),
				e.isPopupTrigger(),
				e.getButton()
			);
	}

	public double getInversedScale()
	{
		return 1 / getScale();
	}

	/**
	 * Paint the background with the specified color. A point grid is drawing
	 * with space specified by gridSize.
	 * 
	 * @param gridSize
	 *            space between point. 0 for no grid.
	 * @param color
	 *            the background-color.
	 */
	protected void paintBackground(int gridSize, Color color, Graphics2D g2)
	{
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		
		final Rectangle vr = getScene().getVisibleRect();
		final boolean gradient = getBackgroundGradient();

		// Paint a gradient from top to bottom.
		if (gradient)
			g2.setPaint(new GradientPaint(0, 0, color, 0, scene.getHeight(), color.brighter()));
		else
			g2.setColor(color);
		
		g2.fillRect(vr.x, vr.y, vr.width, vr.height);
		
		// Draw grid
		if (isVisible() && isGridEnable() && isGridVisible() && getGridSize() >= 10) // Don't draw a grid lesser than 10 (too slow).
		{
			final int grayLevel = Utility.getColorGrayLevel(getColor());
			Color gridColor = new Color(getGridColor());

			if (isAutomatiqueGridColor())

				gridColor = new Color(grayLevel, grayLevel, grayLevel, getGridOpacity());
			
			else

				gridColor = new Color(gridColor.getRed(), gridColor.getGreen(), gridColor.getBlue(), getGridOpacity());
						
			g2.setColor(gridColor);
			
			for (int x = (vr.x/gridSize)*gridSize; x < vr.x + vr.width + gridSize; x += gridSize)
				for (int y = (vr.y/gridSize)*gridSize; y < vr.y + vr.height + gridSize; y += gridSize)
						g2.drawLine(x, y, x, y);
		}
	}

	@Override
	public void paintComponent(Graphics2D g2)
	{
		paintScene(g2);
	}

	/**
	 * Paint the rubber band for selected multiple items.
	 * 
	 * @param rubberBand
	 *            the bounds of the rubber band
	 * @param color
	 *            the color of the rubber band
	 * @param g2
	 *            the graphic context
	 */
	protected void paintRubberBand(Rectangle rubberBand, Color color, Graphics2D g2)
	{
		final Color transparentColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), 50);

		final BasicStroke dashed = new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);

		g2.setStroke(dashed);
		g2.setColor(transparentColor);
		g2.fillRect(rubberBand.x, rubberBand.y, rubberBand.width, rubberBand.height);

		g2.setColor(color);
		g2.drawRect(rubberBand.x, rubberBand.y, rubberBand.width, rubberBand.height);
	}

	/**
	 * Paint the scene. Same has paintComponent, but different name because
	 * paintComponent is already use by the parent GraphicComponent.
	 * 
	 * @param g2
	 *            the graphic context
	 */
	public void paintScene(Graphics2D g2)
	{
		int gridSize = getGridSize();
		
		// Paint background.
		paintBackground(gridSize, getBasicColor(), g2);

		if (!isVisible())
			return;
		
		Utility.setRenderQuality(g2);
		
		double scale = getScale(),
		inversedScale = getInversedScale();
		
		g2.scale(scale, scale);

		// Paint components
		for (final GraphicComponent c : getAllComponents())
			c.paintComponent(g2);
		
		for (final GraphicComponent c : getSelectedComponents())
			c.drawSelectedEffect(g2);

		if (currentFactory != null)
			currentFactory.paintComponent(g2);

		// Paint rubberBand
		final int grayLevel = Utility.getColorGrayLevel(getColor());
		final Color rubberBandColor = new Color(grayLevel, grayLevel, grayLevel);

		paintRubberBand(rubberBand, isAutomatiqueGridColor() ? rubberBandColor : new Color(getGridColor()), g2);
		
		g2.scale(inversedScale, inversedScale);
		
		if (getPaintBackgroundLast())
			paintBackground(gridSize, getBasicColor(), g2);
	}

	@Override
	public int print(Graphics pg, PageFormat pageFormat, int pageIndex) throws PrinterException
	{
		// This method was found on internet (see class PanelClassDiagram).
		// Printing have not been tested a lot (beta).
		// This method compute the number of pages required for drawing a
		// picture and cut this picture for each pages.
		final BufferedImage m_bi = getScreen(BufferedImage.TYPE_INT_ARGB_PRE);

		if (pageIndex >= m_maxNumPage || m_bi == null)

			return NO_SUCH_PAGE;

		pg.translate((int) pageFormat.getImageableX(), (int) pageFormat.getImageableY());

		final int wPage = (int) pageFormat.getImageableWidth();

		final int hPage = (int) pageFormat.getImageableHeight();

		final int w = m_bi.getWidth(getScene());

		final int h = m_bi.getHeight(getScene());

		if (w == 0 || h == 0)

			return NO_SUCH_PAGE;

		final int nCol = Math.max((int) Math.ceil((double) w / wPage), 1);
		final int nRow = Math.max((int) Math.ceil((double) h / hPage), 1);

		m_maxNumPage = nCol * nRow;

		final int iCol = pageIndex % nCol;

		final int iRow = pageIndex / nCol;

		final int x = iCol * wPage;

		final int y = iRow * hPage;

		final int wImage = Math.min(wPage, w - x);

		final int hImage = Math.min(hPage, h - y);

		pg.drawImage(m_bi, 0, 0, wImage, hImage, x, y, x + wImage, y + hImage, getScene());

		System.gc();

		return PAGE_EXISTS;
	}

	/**
	 * Clear and repaint the rubber band with the new bounds compute from Points
	 * given in parameters.
	 * 
	 * @param origin
	 *            where the user clicked for make a rubber band
	 * @param mouse
	 *            the current mouse location
	 */
	protected void redrawRubberBand(Point origin, Point mouse)
	{
		// Save previous bounds for redrawing.
		final Rectangle repaintRect = new Rectangle(rubberBand);

		rubberBand = new Rectangle(origin.x, origin.y, mouse.x - origin.x, mouse.y - origin.y);

		rubberBand = Utility.normalizeRect(rubberBand);

		scene.repaint(repaintRect);
		scene.repaint(new Rectangle(rubberBand));
	}

	/**
	 * Remove all components in graphic view.
	 */
	public void removeAll()
	{
		for (final GraphicComponent c : getAllComponents())

			c.delete();

		Change.clear();
	}
	
	@Override
	public void setColor(Color color)
	{
		setBasicColor(color);
		
		repaint();
	}

	/**
	 * Remove the given component from graphic view. Remove the associed UML
	 * component too (if exists).
	 * 
	 * @param component
	 *            the component to remove
	 * @return true if the component has been removed; false otherwise.
	 */
	public boolean removeComponent(GraphicComponent component)
	{
		if (component == null)
			throw new IllegalArgumentException("component is null");

		boolean success = false;

		// Remove the component, don't know where it is, test all arrays.
		success |= entities.remove(component);
		success |= linesView.remove(component);
		success |= othersComponents.remove(component);
		success |= multiViews.remove(component);
		success |= notes.remove(component);

		if (success)
					
			component.repaint();

		return success;
	}

	@Override
	public void removeComponent(IDiagramComponent component)
	{
		final GraphicComponent g = searchAssociedComponent(component);

		if (g != null)

			removeComponent(g);
	}

	@Override
	public void repaint()
	{		
		scene.repaint();
	}

	/**
	 * Search a graphic component associated with the object given. Some graphic
	 * component have an UML component associated. Return null if no component
	 * are associated.
	 * 
	 * @param search
	 *            the object associated with a graphic component
	 * @return the graphic component associated with the given object; or null
	 *         if no graphic component are found
	 */
	public GraphicComponent searchAssociedComponent(Object search)
	{
	    if (search == null)
	        return null;
	    
		for (final GraphicComponent c : getAllComponents())
			if (c.getAssociedComponent() == search)
				return c;

		return null;
	}
	
	/**
	 * Select the given component and unselect all other.
	 * @param gc the component to selected
	 */
	public void selectOnly(GraphicComponent gc)
	{
		unselectAll();
		gc.setSelected(true);
		gc.notifyObservers();
	}

	/**
	 * Select all diagram elements.
	 */
	public void selectAll()
	{
		for (final GraphicComponent c : getDiagramElements())

			c.setSelected(true);
	}
	
	public void goRepaint()
	{
		setStopRepaint(false);
		repaint();
	}
	
	public void setStopRepaint(boolean enable)
	{
		stopRepaint = enable;
	}
	
	public boolean getStopRepaint()
	{
		return stopRepaint;
	}

	@Override
	public void setBounds(Rectangle bounds)
	{
		scene.setBounds(bounds);
	}

	/**
	 * Change the current pressed component by the component given.
	 * 
	 * @param component
	 *            the new pressed component
	 */
	public void setComponentMousePressed(GraphicComponent component)
	{
		if (component == null)
			throw new IllegalArgumentException("component is null");

		componentMousePressed = component;
	}
	
	public void adaptListToWindow(LinkedList<? extends GraphicComponent> list)
	{
		final Rectangle limits = Utility.getLimits(list);
				  Rectangle vr = getScene().getVisibleRect(),
				  l;
		
		l = Utility.scaleRect(limits, getScale());
		
		double xRatio = l.getWidth() / vr.getWidth();
		double yRatio = l.getHeight() / vr.getHeight();
		
		if (xRatio > yRatio)
			setScale(getScale() / xRatio);
		else
			setScale(getScale() / yRatio);
		
		// Must wait scaling.
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run()
			{
				getScene().scrollRectToVisible(Utility.scaleRect(limits, getScale()));
			}
		});
	}
	
	public void adaptDiagramToWindow()
	{
		adaptListToWindow(getAllComponents());
	}
	
	public void adaptSelectionToWindow()
	{
		adaptListToWindow(getSelectedComponents());
	}
	
	public void setScale(double scale)
	{
		PanelClassDiagram.getInstance().getsSlider().setValue((int)(scale * 100.0));
	}
	
	public void backScale()
	{
		setScale(getScale() + SCALE_STEP);
	}
	
	public void forwardScale()
	{
		setScale(getScale() - SCALE_STEP);
	}

	/**
	 * Set the font zoom. The zoom is multiplied with the font size for
	 * computing new size. Default value : 1.0f.
	 * 
	 * @param zoom
	 *            the new zoom
	 */
	public void setZoom(float zoom)
	{
		if (zoom < 0.5)
			return;

		this.zoom = zoom;

		// The font size can only be compute with a graphic context. We must
		// call a repainting for update the font size before redrawing the
		// scene.
		getScene().paintImmediately(0, 0, 1, 1);
		getScene().repaint();

		// Adjut the width for selected entities.
		adjustWidthSelectedEntities();

		for (final EntityView entity : getEntitiesView())

			entity.updateHeight();
	}

	/**
	 * Get all selected entities and display or hide their attributes.
	 * 
	 * @param show
	 *            true for showing attributes, false otherwise
	 */
	public void showAttributsForSelectedEntity(boolean show)
	{
		for (final EntityView ev : getSelectedEntities())

			ev.setDisplayAttributes(show);
	}

	/**
	 * Get all selected entities and display or hide their methods.
	 * 
	 * @param show
	 *            true for showing methods, false otherwise
	 */
	public void showMethodsForSelectedEntity(boolean show)
	{
		for (final EntityView ev : getSelectedEntities())

			ev.setDisplayMethods(show);
	}

	/**
	 * Sort the given list by x location. The first element of the list will be
	 * the leftmost component in graphicview.
	 * 
	 * @param list
	 *            the lost to sort
	 * @return the list sorted
	 */
	public LinkedList<EntityView> sortXLocation(LinkedList<EntityView> list)
	{
		@SuppressWarnings("unchecked") final LinkedList<EntityView> cpyList = (LinkedList<EntityView>) list.clone();
		final LinkedList<EntityView> sorted = new LinkedList<EntityView>();

		// sort list from x location
		while (cpyList.size() > 0)
		{
			int i = 0, left = Integer.MAX_VALUE;

			for (int j = 0; j < cpyList.size(); j++)
			{
				final Rectangle current = cpyList.get(j).getBounds();

				if (left > current.x)
				{
					left = current.x;
					i = j;
				}
			}

			sorted.addLast(cpyList.get(i));
			cpyList.remove(i);
		}

		return sorted;
	}

	/**
	 * Sort the given list by y location. The first element of the list will be
	 * the hightest component in graphicview.
	 * 
	 * @param list
	 *            the lost to sort
	 * @return the list sorted
	 */
	public LinkedList<EntityView> sortYLocation(LinkedList<EntityView> list)
	{
		@SuppressWarnings("unchecked") final LinkedList<EntityView> cpyList = (LinkedList<EntityView>) list.clone();
		final LinkedList<EntityView> sorted = new LinkedList<EntityView>();

		// sort list from y location
		while (cpyList.size() > 0)
		{
			int i = 0, top = Integer.MAX_VALUE;

			for (int j = 0; j < cpyList.size(); j++)
			{
				final Rectangle current = cpyList.get(j).getBounds();

				if (top > current.y)
				{
					top = current.y;
					i = j;
				}
			}

			sorted.addLast(cpyList.get(i));
			cpyList.remove(i);
		}

		return sorted;
	}

	@Override
	public String toXML(int depth)
	{
		final String tab = Utility.generateTab(depth);

		String xml = tab + "<umlView name=\"" + getName() + "\" ";

		xml += "grid=\"" + getGridSize() + "\">\n";

		for (final GraphicComponent c : getAllComponents())
			xml += c.toXML(depth + 1);

		return xml + tab + "</umlView>";
	}

	/**
	 * Compute a new preferred size for the scrollPane. Calls when graphic
	 * component is resized or moved.
	 */
	public void updatePreferredSize()
	{
		final Rectangle r = Utility.getLimits(getAllComponents());

		// Add margin.
		final int width = (int)((double)(r.x + r.width + 1000) * getScale());
		final int height = (int)((double)(r.y + r.height + 1000) * getScale());

		getScene().setPreferredSize(new Dimension(width, height));
		getScene().revalidate();
	}
}
