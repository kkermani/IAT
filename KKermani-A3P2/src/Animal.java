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
	private int size;
	public PVector pos, spd;
	public PVector dim;
	protected float width, height;
	public PVector target;
	public double sc;
	private float maxSpeed;


	public Animal(float x, float  y, float w, float h, int size,float scale) {
		this.size = size;
		this.pos = new PVector(x,y);
		this.spd = Util.randomPVector(maxSpeed);
		this.dim = new PVector(0,0);
		this.sc = sc;
		this.width = w;
		this.height = h;
		this.dim = new PVector(0,0);
		this.target = dim.copy();
		
	}
	
	public abstract Shape getFOV();
	public abstract void  move();
	
}

