import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Rectangle2D;

import processing.core.PVector;



public class Food {


	private int size;
	private PVector pos;
	private PVector dF;
	private double sc;

	private Area dBox;

	private int height;
	private int width;
	private Ellipse2D.Double foodShape;
	private Color foodColor;	
	
	public Food(int x, int y, int size){

		this.size=size;
		this.pos=new PVector(x,y);
		this.sc=0.8;
		this.foodColor = Color.red;

		setShapeAttributes();
	}

	protected void setShapeAttributes() {
		
			this.foodShape = new Ellipse2D.Double(-width/2, -height/2, width, height);
	}


		public Shape getBoundary() {
			AffineTransform at = new AffineTransform();		
			at.translate(pos.x, pos.y);
			at.scale(sc, sc);
			return at.createTransformedShape(dBox);
		}


	public void draw(Graphics2D g2) {
		AffineTransform at = g2.getTransform();
		g2.translate(pos.x, pos.y);
		g2.scale(sc, sc);
		g2.setColor(Color.RED);
		g2.fill(foodShape);
		
		g2.setTransform(at);
	}

	public PVector getPos() {
		return pos;
	}

	public float getSize() {
		return (float) sc;
	}

	public void setPos(PVector pos) {
		this.pos = pos;
	}

	public PVector direction() {
		PVector directionFood= dF.copy();
		return directionFood;
	}
	public boolean checkMouseHit(PVector e) {
		return	(Math.abs(e.x - pos.x) < size/2) && (Math.abs(e.y - pos.y) < size/2);
	}
	public void enlarge() {
		sc *= 1.25;
	}

}
