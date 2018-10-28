import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

import processing.core.PVector;

public class Food {
	
	private PVector pos;						//position
	private float diameter;					//diameter
	private float scale;						//food size
	private Ellipse2D.Double foodShape;		//geometric shape
	private Color foodColor;					//shape color
	private Area bBox;
	
	public Food(float x, float y, float size) {

		// given attributes
		this.pos = new PVector(x, y);
		this.scale = size;
		// attributes set by default
		this.diameter = 10;
		this.foodColor = Color.red;
		setShapeAttributes();
	}
	
	public float getEnergy() {
		return scale;
	}

	public void draw(Graphics2D g) {
		AffineTransform at = g.getTransform();
		
		g.translate(pos.x, pos.y);
		g.scale(scale, scale);
		
		//draw food
		g.setColor(foodColor);
		g.fill(foodShape);
		
		g.setTransform(at);
	}
	
	private void setShapeAttributes() {
		this.foodShape = new Ellipse2D.Double(-diameter/2, -diameter/2, diameter, diameter);
		bBox = new Area(foodShape);
	}
	
	public Shape getBoundary() {
		AffineTransform at = new AffineTransform();		
		at.translate(pos.x, pos.y);
		at.scale(scale, scale);
		return at.createTransformedShape(bBox);
	}

	public boolean checkMouseHit(MouseEvent e) {
//		PVector mousePos = new PVector(e.getX(), e.getY());
//		float distance = PVector.dist(mousePos, pos);
//		return (distance <= diameter/2*scale);
		return getBoundary().contains(e.getX(), e.getY());
	}

	public void setColor(Color color) {
		this.foodColor = color;
	}

	public PVector getPos() {
		return pos;
	}

	public float getScale() {
		return scale;
	}

}
