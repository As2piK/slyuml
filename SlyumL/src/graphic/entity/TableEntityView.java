package graphic.entity;

import graphic.GraphicComponent;
import graphic.GraphicView;
import graphic.textbox.TextBox;
import graphic.textbox.TextBoxAttribute;
import graphic.textbox.TextBoxEntityName;
import graphic.textbox.TextBoxMethod;
import graphic.textbox.TextBoxMethod.ParametersViewStyle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Observable;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;

import abstractDiagram.AbstractIDiagramComponent;
import classDiagram.IClassDiagramComponent;
import classDiagram.components.Method;
import swing.PropertyLoader;
import swing.SPanelZOrder;
import swing.Slyum;
import utility.PersonalizedIcon;
import utility.SMessageDialog;
import utility.Utility;
import change.BufferBounds;
import change.Change;
import dbDiagram.IDBDiagramComponent;
import dbDiagram.IDBDiagramComponent.UpdateMessage;
import dbDiagram.components.Entity;
import dbDiagram.components.Field;

/**
 * Represent the view of an entity in UML structure.
 * 
 * @author David Miserez
 * @version 1.0 - 25.07.2011
 */
public abstract class TableEntityView extends AbstractEntityView
{
	public static final Color baseColor = new Color(255, 247, 225);
	private static Color basicColor = new Color(baseColor.getRGB());

	public static final float BORDER_WIDTH = 1.2f;
	public static final int VERTICAL_SPACEMENT = 10; // margin

	/**
	 * Get the default color used then a new entity is created.
	 * 
	 * @return the basic color.
	 */
	public static Color getBasicColor()
	{
		String colorEntities = PropertyLoader.getInstance().getProperties().getProperty(PropertyLoader.COLOR_ENTITIES);
		Color color;
		
		if (colorEntities == null)
			color = basicColor;
		else
			color = new Color(Integer.parseInt(colorEntities));
		
		return color;
	};

	/**
	 * Compute the point intersecting the lines given. Return Point(-1.0f,
	 * -1.0f) if liens are //.
	 * 
	 * @param line1
	 *            the first line
	 * @param line2
	 *            the second line
	 * @return the intersection point of the two lines
	 */
	public static Point2D ptIntersectsLines(Line2D line1, Line2D line2)
	{
		// convert line2D to point
		final Point p1 = new Point((int) line1.getP1().getX(), (int) line1.getP1().getY());
		final Point p2 = new Point((int) line1.getP2().getX(), (int) line1.getP2().getY());
		final Point p3 = new Point((int) line2.getP1().getX(), (int) line2.getP1().getY());
		final Point p4 = new Point((int) line2.getP2().getX(), (int) line2.getP2().getY());

		// compute intersection point between two line
		// (http://en.wikipedia.org/wiki/Line-line_intersection)
		final int denom = (p1.x - p2.x) * (p3.y - p4.y) - (p1.y - p2.y) * (p3.x - p4.x);

		// no intersection (lines //)
		if (denom == 0)
			return new Point2D.Float(-1.0f, -1.0f);

		final int x = ((p1.x * p2.y - p1.y * p2.x) * (p3.x - p4.x) - (p1.x - p2.x) * (p3.x * p4.y - p3.y * p4.x)) / denom;
		final int y = ((p1.x * p2.y - p1.y * p2.x) * (p3.y - p4.y) - (p1.y - p2.y) * (p3.x * p4.y - p3.y * p4.x)) / denom;

		return new Point2D.Float(x, y);
	}

	/**
	 * Search the intersection point between the border of a rectangle and the
	 * line defined by first and next point. The rectangle is decomposed in for
	 * lines and each line go to infinite. So all lines intersect an edge of the
	 * rectangle. We must compute if segments intersect each others or not.
	 * 
	 * @param bounds
	 *            the rectangle
	 * @param first
	 *            the first point
	 * @param next
	 *            the next point
	 * @return the intersection point; or null if no points found
	 */
	public static Point searchNearestEgde(Rectangle bounds, Point first, Point next)
	{
		// One offset needed to avoid intersection with the wrong line.
		if (bounds.x + bounds.width < first.x)
			first.x = bounds.x + bounds.width - 1;
		else if (bounds.x > first.x)
			first.x = bounds.x + 1;

		if (bounds.y + bounds.height < first.y)
			first.y = bounds.height + bounds.y - 1;
		else if (bounds.y > first.y)
			first.y = bounds.y + 1;

		final Line2D relationLine = new Line2D.Float(first.x, first.y, next.x, next.y);
		final Line2D lineTop = new Line2D.Float(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y);
		final Line2D lineRight = new Line2D.Float(bounds.x + bounds.width, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height);
		final Line2D lineBottom = new Line2D.Float(bounds.x + bounds.width, bounds.y + bounds.height, bounds.x, bounds.y + bounds.height);
		final Line2D lineLeft = new Line2D.Float(bounds.x, bounds.y + bounds.height, bounds.x, bounds.y);

		final Point2D ptIntersectTop = ptIntersectsLines(relationLine, lineTop);
		final Point2D ptIntersectRight = ptIntersectsLines(relationLine, lineRight);
		final Point2D ptIntersectBottom = ptIntersectsLines(relationLine, lineBottom);
		final Point2D ptIntersectLeft = ptIntersectsLines(relationLine, lineLeft);

		// line is to infinite, we must verify that the point find interst the
		// correct edge and the relation.
		final int distTop = (int) lineTop.ptSegDist(ptIntersectTop) + (int) relationLine.ptSegDist(ptIntersectTop);
		final int distRight = (int) lineRight.ptSegDist(ptIntersectRight) + (int) relationLine.ptSegDist(ptIntersectRight);
		final int distBottom = (int) lineBottom.ptSegDist(ptIntersectBottom) + (int) relationLine.ptSegDist(ptIntersectBottom);
		final int distLeft = (int) lineLeft.ptSegDist(ptIntersectLeft) + (int) relationLine.ptSegDist(ptIntersectLeft);

		if (ptIntersectTop != null && distTop == 0)
			return new Point((int) ptIntersectTop.getX(), (int) ptIntersectTop.getY());
		else if (ptIntersectRight != null && distRight == 0)
			return new Point((int) ptIntersectRight.getX(), (int) ptIntersectRight.getY());
		else if (ptIntersectBottom != null && distBottom == 0)
			return new Point((int) ptIntersectBottom.getX(), (int) ptIntersectBottom.getY());
		else if (ptIntersectLeft != null && distLeft == 0)
			return new Point((int) ptIntersectLeft.getX(), (int) ptIntersectLeft.getY());
		else
			return null; // no point found!
	}

	/**
	 * Set the basic color. Basic color is used as default color while creating
	 * a new entity.
	 * 
	 * @param color
	 *            the new basic color
	 */
	public static void setBasicColor(Color color)
	{
		basicColor = new Color(color.getRGB());
	}

	protected LinkedList<TextBoxAttribute> attributesView = new LinkedList<TextBoxAttribute>();

	private Rectangle bounds = new Rectangle();
	protected Entity component;
	private Color defaultColor;

	private boolean displayAttributes = true;
	protected boolean displayMethods = true;

	private final TextBoxEntityName entityName;

	protected LinkedList<TextBoxMethod> methodsView = new LinkedList<TextBoxMethod>();

	private TextBox pressedTextBox;
	private JMenuItem menuItemDelete, menuItemMoveUp, menuItemMoveDown;

	private Cursor saveCursor = Cursor.getDefaultCursor();

	protected GraphicComponent saveTextBoxMouseHover;

	private static final Font stereotypeFontBasic = new Font(Slyum.getInstance().defaultFont.getFamily(), 0, 11); // TODO
	private Font stereotypeFont = stereotypeFontBasic;

	public TableEntityView(final GraphicView parent, Entity component)
	{
		super(parent);

		if (component == null)
			throw new IllegalArgumentException("component is null");

		this.component = component;

		// Create a textBox for display the entity name.
		entityName = new TextBoxEntityName(parent, component);

		// Create the popup menu.
		JMenuItem menuItem;

		popupMenu.addSeparator();

		menuItem = makeMenuItem("Add attribute", "AddAttribute", "attribute");
		popupMenu.add(menuItem);

		menuItem = makeMenuItem("Add method", "AddMethod", "method");
		popupMenu.add(menuItem);

		popupMenu.addSeparator();
		
		menuItemMoveUp = menuItem = makeMenuItem("Move up", Slyum.ACTION_TEXTBOX_UP, "direction_up");
		menuItemMoveUp.setEnabled(false);
		popupMenu.add(menuItem);
		
		menuItemMoveDown = menuItem = makeMenuItem("Move down", Slyum.ACTION_TEXTBOX_DOWN, "direction_down");
		menuItemMoveDown.setEnabled(false);
		popupMenu.add(menuItem);
		
		popupMenu.addSeparator();

		menuItemDelete = menuItem = makeMenuItem("Delete", "Delete", "delete16");
		popupMenu.add(menuItem);

		popupMenu.addSeparator();

		JMenu subMenu = new JMenu("View");
		subMenu.setIcon(PersonalizedIcon.createImageIcon(Slyum.ICON_PATH + "visibility.png"));
		ButtonGroup group = new ButtonGroup();

		JRadioButtonMenuItem rbMenuItem = makeRadioButtonMenuItem("All", "ViewAll", group);
		rbMenuItem.setSelected(true);
		subMenu.add(rbMenuItem);

		rbMenuItem = makeRadioButtonMenuItem("Only Attributes", "ViewAttribute", group);
		subMenu.add(rbMenuItem, 1);

		rbMenuItem = makeRadioButtonMenuItem("Only Methods", "ViewMethods", group);
		subMenu.add(rbMenuItem, 2);

		rbMenuItem = makeRadioButtonMenuItem("Nothing", "ViewNothing", group);
		subMenu.add(rbMenuItem);

		popupMenu.add(subMenu);

		subMenu = new JMenu("Methods View");
		subMenu.setIcon(PersonalizedIcon.createImageIcon("resources/icon/visibility.png"));
		group = new ButtonGroup();

		rbMenuItem = makeRadioButtonMenuItem("Type and Name", "ViewTypeAndName", group);
		rbMenuItem.setSelected(true);
		subMenu.add(rbMenuItem);

		rbMenuItem = makeRadioButtonMenuItem("Type", "ViewType", group);
		subMenu.add(rbMenuItem, 1);

		rbMenuItem = makeRadioButtonMenuItem("Name", "ViewName", group);
		subMenu.add(rbMenuItem, 2);

		rbMenuItem = makeRadioButtonMenuItem("Nothing", "ViewMethodNothing", group);
		subMenu.add(rbMenuItem);

		popupMenu.add(subMenu);

		popupMenu.addSeparator();

		SPanelZOrder p = SPanelZOrder.getInstance();
		menuItem = makeMenuItem("Move top", "ZOrderTOP", "top");
		p.getBtnTop().linkComponent(menuItem);
		popupMenu.add(menuItem);
		
		menuItem = makeMenuItem("Up", "ZOrderUP", "up");
		p.getBtnUp().linkComponent(menuItem);
		popupMenu.add(menuItem);
		
		menuItem = makeMenuItem("Down", "ZOrderDown", "down");
		p.getBtnDown().linkComponent(menuItem);
		popupMenu.add(menuItem);
		
		menuItem = makeMenuItem("Move bottom", "ZOrderBottom", "bottom");
		p.getBtnBottom().linkComponent(menuItem);
		popupMenu.add(menuItem);

		component.addObserver(this);

		setColor(getBasicColor());
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);

		if ("AddMethod".equals(e.getActionCommand()))
			addMethod();

		else if ("AddAttribute".equals(e.getActionCommand()))
			addAttribute();

		else if ("Delete".equals(e.getActionCommand()))
		{
			if (SMessageDialog.showQuestionMessageYesNo("Are you sur to delete this component and all its associated components?") == JOptionPane.NO_OPTION)

				return;

			if (pressedTextBox != null)

				removeTextBox(pressedTextBox);
			else

				delete();
		}
		else if ("ViewAttribute".equals(e.getActionCommand()))
		{
			parent.showAttributsForSelectedEntity(true);
			parent.showMethodsForSelectedEntity(false);
		}
		else if ("ViewMethods".equals(e.getActionCommand()))
		{
			parent.showAttributsForSelectedEntity(false);
			parent.showMethodsForSelectedEntity(true);
		}
		else if ("ViewAll".equals(e.getActionCommand()))
		{
			parent.showAttributsForSelectedEntity(true);
			parent.showMethodsForSelectedEntity(true);
		}
		else if ("ViewNothing".equals(e.getActionCommand()))
		{
			parent.showAttributsForSelectedEntity(false);
			parent.showMethodsForSelectedEntity(false);
		}
		else if ("ViewTypeAndName".equals(e.getActionCommand()))
			methodViewChangeClicked(ParametersViewStyle.TYPE_AND_NAME);
		else if ("ViewType".equals(e.getActionCommand()))
			methodViewChangeClicked(ParametersViewStyle.TYPE);
		else if ("ViewName".equals(e.getActionCommand()))
			methodViewChangeClicked(ParametersViewStyle.NAME);
		else if ("ViewMethodNothing".equals(e.getActionCommand()))
			methodViewChangeClicked(ParametersViewStyle.NOTHING);
		else if (Slyum.ACTION_TEXTBOX_UP.equals(e.getActionCommand()) || Slyum.ACTION_TEXTBOX_DOWN.equals(e.getActionCommand()))
		{
			int offset = 1;

			if (Slyum.ACTION_TEXTBOX_UP.equals(e.getActionCommand()))

				offset = -1;

			if (pressedTextBox.getClass() == TextBoxAttribute.class)
			{
				final Field field = (Field) ((TextBoxAttribute) pressedTextBox).getAssociedComponent();
				component.moveAttributePosition(field, offset);
			}

			component.notifyObservers();
		}
	}
	
	/**
	 * Create a new attribute with default type and name.
	 */
	public void addAttribute()
	{
		final Field field = new Field("field", null); //TODO Default type
		prepareNewAttribute(field);

		component.addField(field);
		component.notifyObservers(UpdateMessage.ADD_FIELD);
	}

	/**
	 * Create a new attribute view with the given attribute. If editing is a
	 * true, the new attribute view will be in editing mode while it created.
	 * 
	 * @param field
	 *            the attribute UML
	 * @param editing
	 *            true if creating a new attribute view in editing mode; false
	 *            otherwise
	 */
	public void addField(Field field, boolean editing)
	{
		final TextBoxAttribute newTextBox = new TextBoxAttribute(parent, field);
		attributesView.add(newTextBox);

		updateHeight();

		if (editing)
			newTextBox.editing();
	}

	/**
	 * Create a new method with default type and name, without parameter.
	 */
	public void addMethod() //TODO DELETE
	{
	}

	/**
	 * Create a new method view with the given method. If editing is a true, the
	 * new method view will be in editing mode while it created.
	 * 
	 * @param method
	 *            the method UML
	 * @param editing
	 *            true if creating a new method view in editing mode; false
	 *            otherwise
	 */
	public void addMethod(Method method, boolean editing) //TODO TO DELETE
	{
	}

	/**
	 * Adjust the width according to its content.
	 */
	public void adjustWidth()
	{
		int width = Short.MIN_VALUE;

		for (final TextBox tb : getAllTextBox())
		{
			final int tbWidth = tb.getTextDim().width;

			if (tbWidth > width)
				width = tbWidth; // get the longer content
		}

		// change the width according to the grid
		final Rectangle bounds = getBounds();
		
		Change.push(new BufferBounds(this));
		setBounds(new Rectangle(bounds.x, bounds.y, width + GraphicView.getGridSize() + 15, bounds.height));
		Change.push(new BufferBounds(this));
	}

	@Override
	public Point computeAnchorLocation(Point first, Point next)
	{
		return searchNearestEgde(getBounds(), first, next);
	}

	/**
	 * Compute the height of the class with margin and content.
	 * 
	 * @param classNameHeight
	 *            the height of class name
	 * @param stereotypeHeight
	 *            the height of stereotype
	 * @param elementsHeight
	 *            the height of each element (methods, attributes)
	 * @return the height of the class
	 */
	public int computeHeight(int classNameHeight, int stereotypeHeight, int elementsHeight)
	{
		int height = VERTICAL_SPACEMENT;

		if (!component.getStereotype().isEmpty())
			height += stereotypeHeight;

		height += classNameHeight;

		if (displayMethods)
			height += elementsHeight * methodsView.size();

		if (displayAttributes)
			height += elementsHeight * attributesView.size();

		return height + 30;
	}

	@Override
	public void delete()
	{
		super.delete();

		parent.removeComponent(leftMovableSquare);
		parent.removeComponent(rightMovableSquare);
	}

	@Override
	public void drawSelectedEffect(Graphics2D g2)
	{
		final Color backColor = getColor();
		final Color fill = new Color(backColor.getRed(), backColor.getGreen(), backColor.getBlue(), 100);

		final Color border = backColor.darker();
		final BasicStroke borderStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 2.0f }, 0.0f);

		g2.setColor(fill);
		g2.fillRect(ghost.x, ghost.y, ghost.width, ghost.height);

		g2.setColor(border);
		g2.setStroke(borderStroke);
		g2.drawRect(ghost.x, ghost.y, ghost.width - 1, ghost.height - 1);
	}

	/**
	 * Draw a border representing a selection.
	 * 
	 * @param g2
	 *            the graphic context
	 */
	public void drawSelectedStyle(Graphics2D g2)
	{
		final int PADDING = 2;
		final Color selectColor = new Color(100, 100, 100);

		final BasicStroke dashed = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 2f }, 0.0f);

		final Rectangle inRectangle = new Rectangle(bounds.x + PADDING, bounds.y + PADDING, bounds.width - 2 * PADDING, bounds.height - 2 * PADDING);

		final Rectangle outRectangle = new Rectangle(bounds.x - PADDING, bounds.y - PADDING, bounds.width + 2 * PADDING, bounds.height + 2 * PADDING);

		g2.setStroke(dashed);
		g2.setColor(selectColor);

		g2.drawRect(inRectangle.x, inRectangle.y, inRectangle.width, inRectangle.height);
		g2.drawRect(outRectangle.x, outRectangle.y, outRectangle.width, outRectangle.height);
	}

	/**
	 * get all textBox displayed by the entity. TextBox returned are: - textBox
	 * for entity name - textBox for attributes - textBox for methods
	 * 
	 * @return an array containing all TextBox
	 */
	public LinkedList<TextBox> getAllTextBox()
	{
		final LinkedList<TextBox> tb = new LinkedList<TextBox>();

		tb.add(entityName);
		tb.addAll(methodsView);
		tb.addAll(attributesView);

		return tb;
	}

	@Override
	public IDBDiagramComponent getAssociedComponent() //TODO
	{
		return component;
	}

	@Override
	public Rectangle getBounds()
	{
		if (bounds == null)
			bounds = new Rectangle();
		
		return new Rectangle(bounds);
	}

	/**
	 * Get the entity (UML) associed with this entity view. Same as
	 * getAssociedComponent().
	 * 
	 * @return the component associed.
	 */
	public Entity getComponent()
	{
		return component;
	}

	@Override
	public void gMouseClicked(MouseEvent e)
	{
		super.gMouseClicked(e);

		final TextBox textBox = GraphicView.searchComponentWithPosition(getAllTextBox(), e.getPoint());

		if (textBox != null)
		{
			final AbstractIDiagramComponent idc = textBox.getAssociedComponent();

			if (idc != null)
			{
				idc.select();
				idc.notifyObservers(UpdateMessage.SELECT);
			}

			if (e.getClickCount() == 2)

				textBox.editing();
		}
	}

	@Override
	public void gMouseEntered(MouseEvent e)
	{
		super.gMouseEntered(e);

		setMouseHoverStyle();

		saveCursor = parent.getScene().getCursor();
		parent.getScene().setCursor(new Cursor(Cursor.MOVE_CURSOR));
	}

	@Override
	public void gMouseExited(MouseEvent e)
	{
		super.gMouseExited(e);

		if (saveTextBoxMouseHover != null)
		{
			saveTextBoxMouseHover.gMouseExited(e);
			saveTextBoxMouseHover = null;
		}

		setDefaultStyle();

		parent.getScene().setCursor(saveCursor);
	}

	@Override
	public void gMouseMoved(MouseEvent e)
	{
		final GraphicComponent textBoxMouseHover = GraphicView.searchComponentWithPosition(getAllTextBox(), e.getPoint());
		GraphicView.computeComponentEventEnter(textBoxMouseHover, saveTextBoxMouseHover, e);

		saveTextBoxMouseHover = textBoxMouseHover;
	}

	@Override
	public void gMousePressed(MouseEvent e)
	{
		pressedTextBox = searchTextBoxAtLocation(e.getPoint());
		super.gMousePressed(e);
	}
	
	/**
	 * Search and return the Textbox (methods and attributes) at the given location.
	 * @param location the location where find a TextBox
	 * @return the found TextBox
	 */
	private TextBox searchTextBoxAtLocation(Point location)
	{
		final LinkedList<TextBox> tb = getAllTextBox();
		tb.remove(entityName);
		return GraphicView.searchComponentWithPosition(tb, location);
	}

	@Override
	public boolean isAtPosition(Point mouse)
	{
		return bounds.contains(mouse);
	}

	/**
	 * Return if attributes are displayed or not.
	 * 
	 * @return true if attributes are displayed; false otherwise
	 */
	public boolean isAttributeDisplayed()
	{
		return displayAttributes;
	}

	/**
	 * Return if methods are displayed or not.
	 * 
	 * @return true if methods are displayed; false otherwise
	 */
	public boolean isMethodsDisplayed()
	{
		return displayMethods;
	}

	@Override
	public void maybeShowPopup(MouseEvent e, JPopupMenu popupMenu)
	{
		if (e.isPopupTrigger())
		{
			String text = "Delete ";

			// if context menu is requested on a TextBox, customize popup menu.
			if (pressedTextBox != null)
			{
				text += pressedTextBox.getText();
				menuItemMoveUp.setEnabled(attributesView.indexOf(pressedTextBox) != 0 && methodsView.indexOf(pressedTextBox) != 0);
				menuItemMoveDown.setEnabled((attributesView.size() == 0 || attributesView.indexOf(pressedTextBox) != attributesView.size() - 1) && (methodsView.size() == 0 || methodsView.indexOf(pressedTextBox) != methodsView.size() - 1));
			}
			else
			{
				text += component.getName();
				menuItemMoveUp.setEnabled(false);
				menuItemMoveDown.setEnabled(false);
			}
			menuItemDelete.setText(text);
		}
		
		super.maybeShowPopup(e, popupMenu);
	}

	/**
	 * Change the display style of parameters for all methods.
	 * 
	 * @param newStyle
	 *            the new display style
	 */
	public void methodViewChange(ParametersViewStyle newStyle)
	{
		for (final TextBoxMethod tbm : methodsView)

			tbm.setParametersViewStyle(newStyle);
	}

	/**
	 * Change the display style of parameters for the pressed TextBox if exists,
	 * or for all otherwise.
	 * 
	 * @param newStyle
	 *            the new display style
	 */
	private void methodViewChangeClicked(ParametersViewStyle newStyle)
	{
		if (pressedTextBox instanceof TextBoxMethod)

			((TextBoxMethod) pressedTextBox).setParametersViewStyle(newStyle);

		else

			for (final AbstractEntityView ev : parent.getSelectedEntities())

				if (ev instanceof TableEntityView)
				((TableEntityView)ev).methodViewChange(newStyle);
	}

	@Override
	public void paintComponent(Graphics2D g2)
	{
		if (!isVisible())
			return;

		final Color textColor = new Color(40, 40, 40);
		final Color borderColor = new Color(65, 65, 65);
		final GradientPaint backGradient = new GradientPaint(bounds.x, bounds.y, getColor(), bounds.x + bounds.width, bounds.y + bounds.height, getColor().darker());

		final String className = component.getName();

		final FontMetrics classNameMetrics = g2.getFontMetrics(entityName.getEffectivFont());
		final int classNameWidth = classNameMetrics.stringWidth(className);
		final int classNameHeight = classNameMetrics.getHeight();

		final Dimension classNameSize = new Dimension(classNameWidth, classNameHeight);

		stereotypeFont = stereotypeFont.deriveFont(stereotypeFontBasic.getSize() * parent.getZoom());

		g2.setFont(stereotypeFont);
		final String stereotype = Utility.truncate(g2, "<<" + component.getStereotype() + " >>", bounds.width - 15);

		final FontMetrics stereotypeMetrics = g2.getFontMetrics(stereotypeFont);
		final int stereotypeWidth = stereotypeMetrics.stringWidth(stereotype);
		final int stereotypeHeight = stereotypeMetrics.getHeight();

		final Dimension stereotypeSize = new Dimension(stereotypeWidth, stereotypeHeight);

		final FontMetrics metrics = g2.getFontMetrics(entityName.getEffectivFont());
		final int textBoxHeight = metrics.getHeight();

		bounds.height = computeHeight(classNameSize.height, stereotypeHeight, textBoxHeight);

		final Rectangle bounds = getBounds();

		int offset = bounds.y + VERTICAL_SPACEMENT / 2;
		final int stereotypeLocationWidth = bounds.x + (bounds.width - stereotypeSize.width) / 2;

		entityName.setBounds(new Rectangle(1, 1, bounds.width - 15, textBoxHeight + 2));
		final Rectangle entityNameBounds = entityName.getBounds();
		final int classNameLocationX = bounds.x + (bounds.width - entityNameBounds.width) / 2;

		// draw background
		g2.setPaint(backGradient);
		g2.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

		// draw border
		g2.setStroke(new BasicStroke(BORDER_WIDTH));
		g2.setColor(borderColor);
		g2.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

		// draw stereotype
		if (!component.getStereotype().isEmpty())
		{
			offset += stereotypeSize.height;

			g2.setFont(stereotypeFont);
			g2.setColor(textColor);
			g2.drawString(stereotype, stereotypeLocationWidth, offset);
		}

		// draw class name
		offset += /* classNameSize.height + */VERTICAL_SPACEMENT / 2;

		entityName.setBounds(new Rectangle(classNameLocationX, offset, bounds.width - 15, textBoxHeight + 2));
		entityName.paintComponent(g2);

		offset += entityNameBounds.height;

		// draw separator
		offset += 10;
		g2.setStroke(new BasicStroke(BORDER_WIDTH));
		g2.setColor(borderColor);
		g2.drawLine(bounds.x, offset, bounds.x + bounds.width, offset);

		// draw attributes
		if (displayAttributes && attributesView.size() > 0)
			// draw methods
			for (final TextBoxAttribute tb : attributesView)
			{
				tb.setBounds(new Rectangle(bounds.x + 8, offset + 2, bounds.width - 15, textBoxHeight + 2));
				tb.paintComponent(g2);

				offset += textBoxHeight;
			}

		// draw separator
		offset += 10;
		g2.setStroke(new BasicStroke(BORDER_WIDTH));
		g2.setColor(borderColor);
		g2.drawLine(bounds.x, offset, bounds.x + bounds.width, offset);

		// draw methods
		if (displayMethods && methodsView.size() > 0)
			// draw methods
			for (final TextBoxMethod tb : methodsView)
			{
				tb.setBounds(new Rectangle(bounds.x + 8, offset + 2, bounds.width - 15, textBoxHeight + 2));
				tb.paintComponent(g2);

				offset += textBoxHeight;
			}

		// is component selected? -> draw selected style
		if (parent.getSelectedComponents().contains(this))
			drawSelectedStyle(g2);
	}

	/**
	 * Method called before creating a new attribute, if modifications on
	 * attribute is necessary.
	 * 
	 * @param attribute
	 *            the attribute to prepare
	 */
	protected abstract void prepareNewAttribute(Field field);

	/**
	 * Method called before creating a new method, if modifications on method is
	 * necessary.
	 * 
	 * @param method
	 *            the method to prepare
	 */
	protected abstract void prepareNewMethod(Method method);

	/**
	 * Delete all TextBox and regenerate them. !! This method take time !!
	 */
	public void regenerateEntity()
	{
		boolean isStopRepaint = parent.getStopRepaint();
		parent.setStopRepaint(true);

		methodsView.clear();
		attributesView.clear();

		entityName.setText(component.getName());

		for (final Field f : component.getFields())
			addField(f, false);

		if (!isStopRepaint)
			parent.goRepaint();
		
		updateHeight();
	}

	/**
	 * Remove the attribute associated with TextBoxAttribute from model (UML).
	 * 
	 * @param tbAttribute
	 *            the attribute to remove.
	 * @return true if the attribute has been removed; false otherwise
	 */
	public boolean removeAttribute(TextBoxAttribute tbAttribute)
	{
		if (component.removeField((Field) tbAttribute.getAssociedComponent()))
		{
			component.notifyObservers();

			updateHeight();

			return true;
		}

		return false;
	}

	/**
	 * Remove the method associated with TextBoxMethod from model (UML)
	 * 
	 * @param tbMethod
	 *            the method to remove.
	 * @return true if component has been removed; false otherwise.
	 */
	public boolean removeMethod(TextBoxMethod tbMethod)
	{return false; //TODO DELETE
	}

	/**
	 * Generic method for remove the associated component for the given TextBox.
	 * 
	 * @param tb
	 *            the TextBox containing the element to remove.
	 * @return true if component has been removed; false otherwise.
	 */
	public boolean removeTextBox(TextBox tb)
	{
		// Need to find a best way
		if (tb instanceof TextBoxAttribute)

			return removeAttribute((TextBoxAttribute) tb);

		else if (tb instanceof TextBoxMethod)

			return removeMethod((TextBoxMethod) tb);

		return false;
	}

	@Override
	public void repaint()
	{
		parent.getScene().repaint(getBounds());
	}

	@Override
	public void setBounds(Rectangle bounds)
	{
		// Save current bounds, change bounds and repaint old bounds and new bounds.
		final Rectangle repaintBounds = new Rectangle(getBounds());

		final Rectangle newBounds = new Rectangle(ajustOnGrid(bounds.x), ajustOnGrid(bounds.y), ajustOnGrid(bounds.width), bounds.height);

		newBounds.width = newBounds.width < MINIMUM_SIZE.x ? MINIMUM_SIZE.x : newBounds.width;

		this.bounds = newBounds;

		parent.getScene().repaint(repaintBounds);
		parent.getScene().repaint(newBounds);

		// Move graphics elements associated with this component
		leftMovableSquare.setBounds(computeLocationResizer(0));
		rightMovableSquare.setBounds(computeLocationResizer(bounds.width));

		setChanged();
		notifyObservers();
	}

	@Override
	public void setColor(Color color)
	{
		setCurrentColor(color);
		defaultColor = color;
	}

	/**
	 * Set the current color for this entity.
	 * 
	 * @param color
	 *            the current color.
	 */
	public void setCurrentColor(Color color)
	{
		super.setColor(color);
	}

	@Override
	public void setDefaultStyle()
	{
		setCurrentColor(defaultColor);
		repaint();
	}

	/**
	 * Set the display state for attributes.
	 * 
	 * @param display
	 *            the new display state for attributes.
	 */
	public void setDisplayAttributes(boolean display)
	{
		displayAttributes = display;

		updateHeight();
	}

	/**
	 * Set the display state for methods.
	 * 
	 * @param display
	 *            the new display state for methods.
	 */
	public void setDisplayMethods(boolean display)
	{
		displayMethods = display;

		updateHeight();
	}

	@Override
	public void setMouseHoverStyle()
	{
		setCurrentColor(getColor().brighter());
		repaint();
	}

	@Override
	public void setSelected(boolean select)
	{
		super.setSelected(select);

		component.select();

		if (select)
			component.notifyObservers(UpdateMessage.SELECT);
		else
			component.notifyObservers(UpdateMessage.UNSELECT);

		if (!select)
			for (final TextBox t : getAllTextBox())
				t.setSelected(false);

	}

	@Override
	public void setStyleClicked()
	{
		setCurrentColor(getColor().darker());
		repaint();
	}

	@Override
	public String toXML(int depth)
	{
		final String tab = Utility.generateTab(depth);

		String xml = tab + "<componentView componentID=\"" + getAssociedComponent().getId() + "\" color=\"" + getColor().getRGB() + "\">\n";
		xml += Utility.boundsToXML(depth + 1, getBounds(), "geometry");
		xml += tab + "</componentView>\n";

		return xml;
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		boolean enable = false;
		if (arg1 != null && arg1.getClass() == UpdateMessage.class)
			switch ((dbDiagram.IDBDiagramComponent.UpdateMessage) arg1)
			{
				case SELECT:
					super.setSelected(true);
					break;
					
				case UNSELECT:
			super.setSelected(false);
					break;
					
				case ADD_FIELD:
					enable = true;
				case ADD_FIELD_NO_EDIT:
					addField(component.getFields().getLast(), enable);
					break;
			}
		else
			regenerateEntity();
	}

	/**
	 * Udpate the height of the entity and notify all components.
	 */
	public void updateHeight()
	{
		final Rectangle repaintBounds = getBounds();

		parent.getScene().paintImmediately(repaintBounds);

		setBounds(new Rectangle(bounds)); // set new height compute while repainting.

		parent.getScene().repaint(repaintBounds);

		setChanged();
		notifyObservers();
	}
	
	@Override
	public void restore()
	{
		super.restore();
		
		parent.addOthersComponents(leftMovableSquare);
		parent.addOthersComponents(rightMovableSquare);
	}
}
