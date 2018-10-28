package iat265.lab.w7;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.util.ArrayList;

import processing.core.PVector;

public class Pacwoman {
	
	protected PVector pos, speed;				//position and speed
	private float maxSpeed;					//speed limit
	protected Dimension dimension;				//dimension, includes width and height
	protected float scale;						//size
	private Color color;						//main color
	private Arc2D.Double head;				//the original pacman body
	private Ellipse2D.Double eye;			//the eye
	private Polygon fins;					//tail
	private Area bBox;						//bounding box
	private Double pupil;
	private Polygon tail;
	
	
	public Pacwoman(float x, float y, float size) {
		
		// given attributes
		this.pos = new PVector(x, y);
		this.scale = size;
		
		// attributes set by default
		this.maxSpeed = 3;
		this.speed = new PVector(maxSpeed,0);		//random direction of the speed 5 pixels/frame
		this.dimension = new Dimension(200,100);		//default size = 200 x 100
		this.color = Util.randomColor();				//random color
		setShapeAttributes();
	}

	protected void setShapeAttributes() {
		head = new Arc2D.Double( -dimension.width/2, -dimension.height/2, dimension.width, dimension.height, 25, 330, Arc2D.PIE);
		eye = new Ellipse2D.Double(dimension.width/3, -dimension.height/3, dimension.width/15, dimension.width/15);
		
		pupil = new Ellipse2D.Double(5 + dimension.width/3, 5 - dimension.height/3, dimension.width/30, dimension.width/30);
		
		
		
		int[] tx = {dimension.width/4, -dimension.width/2, -dimension.width/5, -dimension.width/2};
		int[] ty = {20, - dimension.height, 0, dimension.height};
		fins = new Polygon(tx, ty, tx.length);
		
		bBox = new Area(head);
		//bBox.add(new Area(tail));
	}

	public void draw(Graphics2D g) {	
		//transformation
		AffineTransform at = g.getTransform();		
		g.translate(pos.x, pos.y);
		g.rotate(speed.heading());
		g.scale(scale, scale);
		if (speed.x < 0) g.scale(1, -1);
		
		//tail
		g.setColor(color);
		g.fill(fins);
		
		
		
		//body
		g.setColor(color);
		g.fill(head);
		
		//eye
		g.setColor(Color.white);
		g.fill(eye);
		
		g.setColor(Color.black);
		g.fill(pupil);
		
		
		g.setTransform(at);
	}
	
	protected void move() {
		speed.normalize().mult(maxSpeed);
		pos.add(speed);
	}

	public Shape getBoundary() {
		AffineTransform at = new AffineTransform();		
		at.translate(pos.x, pos.y);
		at.rotate(speed.heading());
		at.scale(scale, scale);
		return at.createTransformedShape(bBox);
	}

	protected void attractedBy(Food target) {
		float coef = .2f;	// coefficient of acceleration relative to maxSpeed
		PVector direction = PVector.sub(target.getPos(), pos).normalize();
		PVector acceleration = PVector.mult(direction, maxSpeed*coef);
		speed.add(acceleration);
	}

	protected boolean collides(Food food) {
		return (getBoundary().intersects(food.getBoundary().getBounds2D()) &&
				food.getBoundary().intersects(getBoundary().getBounds2D()) );
	}
	
	protected void traceBestFood(ArrayList<Food> fList) {	
		if (fList.size()>0) {
			
			// set the 1st item as default target
			Food target = fList.get(0);
			float distToTarget = PVector.dist(pos, target.getPos());
			
			// find the closer one
			for (Food f:fList) if (PVector.dist(pos, f.getPos()) < distToTarget) {
				target = f;
				distToTarget = PVector.dist(pos, target.getPos());
			}
			
			// make animal follow this target
			this.attractedBy(target);
		}	
	}

	public void hunt(ArrayList<Food> fList) {
		traceBestFood(fList);
		move();
		for (int i = 0; i < fList.size(); i++) 
			if (collides(fList.get(i))) {
				scale += fList.get(i).getSize()*0.01*scale;
				
				fList.remove(i);
			}
	}

	public PVector getPos() {
		return pos;
	}

	protected void tracefish(ArrayList<Pacwoman> pList) {
		// TODO Auto-generated method stub
		
	}
}
