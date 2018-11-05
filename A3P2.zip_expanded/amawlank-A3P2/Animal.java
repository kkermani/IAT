import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import processing.core.PVector;

public abstract class Animal {
	private Arc2D.Double fov; //field-of-view

	private int size;
	public PVector pos, speed;
	public PVector d;

	public PVector aim;
	public double scale;
	private float maxSpeed;


	public Animal(float x, float  y, int size,float scale) {
		this.size = size;
		this.pos=new PVector(x,y);
		this.scale=scale;
		this.speed=Util.randomPVector(maxSpeed);
		this.d=new PVector(0,0);
		this.aim=d.copy();
	}
	
	public abstract Shape getFOV();
		
	public abstract void  move();

	
}

