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

//import iat265.lab.w7.Util;
import processing.core.PVector;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.awt.*;

public class Animal {

	protected PVector pos, speed;
	public float maxSpeed;
	private Dimension dimension;
	protected float scale;
	private Color color;
	private float energy;
	private PVector sickSpeed;
	private PVector initSpeed;
	
	private Arc2D.Double body;
	private Ellipse2D.Double eye;
	private Polygon tail;
	private Double pupil;
	private Polygon fins;
	
	private final int SIZE_X = 200;
	private final int SIZE_Y = 110;
	private final int INIT_ENERGY = 100;
	
	public Animal(float x, float y, PVector pos, float size) {
	
		this.pos = new PVector(x, y);
		this.scale = size;
		
		//this.maxSpeed = 3;
		//beta
		this.maxSpeed = 0;
		while (this.maxSpeed == 0) {
			this.maxSpeed = Util.random(3,5) / (1 + scale);
			this.speed.limit(maxSpeed);
			//beta ends
			
			this.speed = new PVector(maxSpeed,0);		
			this.dimension = new Dimension(SIZE_X, SIZE_Y);		
			this.color = Util.randomColor();
			setShapeAttributes();
		}
	
}
