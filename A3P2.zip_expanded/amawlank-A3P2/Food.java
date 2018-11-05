import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import processing.core.PVector;

public class Food {


	private int size;
	private PVector pos;
	private PVector dF;
	private double scale;
	private Rectangle2D.Double r1,r2;
	private Area dBox;

	public Food(int x, int y, int size){

		this.size=size;
		this.pos=new PVector(x,y);
		this.scale=0.8;


		setFoodAttributes();
	}

	private void setFoodAttributes() {
		// TODO Auto-generated method stub

		r1 = new Rectangle2D.Double(-2, -1, size, size);
		r2 = new Rectangle2D.Double(2,1, size, size);
		dBox = new Area(r1);
		dBox.add(new Area(r2));
		//		bBox.add(new Area(r2));
	}


	public Shape getBoundary() {
		AffineTransform at = new AffineTransform();		
		at.translate(pos.x, pos.y);
		at.scale(scale, scale);
		return at.createTransformedShape(dBox);
	}


	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		AffineTransform trans = g2.getTransform();
		g2.translate(pos.x, pos.y);
		g2.scale(scale, scale);
		g2.setColor(Color.RED);
		g2.fill(r1);
		g2.setColor(Color.ORANGE);
		g2.fill(r2);
		g2.setTransform(trans);
	}

	public PVector getPos() {
		return pos;
	}

	public float getSize() {
		return (float) scale;
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
		scale *= 1.1;
	}

}
