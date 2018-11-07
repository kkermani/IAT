import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Arc2D.Double;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;


import processing.core.PVector;


public class Predator extends Animal{
	
	private int size;
	public PVector pos; 
	public PVector spd;
	public PVector dim;
	public PVector aim;
	public double sc;
	private float life;
	private float maxSpeed;
	private boolean selected;
	
	private Color colour;
	private Color Pcolor;
	public Double body;
	private Ellipse2D.Double eye;	//the eye
	private Ellipse2D.Double pupil;
	private Polygon fins;
	private float FoSize;
	
	private Arc2D.Double FOV; 
	private Arc2D.Double FOVToHunt;
	private Area Box;
	private float val;
	private float FULL_ENERGY=1000;
	
	public Predator(float x, float  y, float w, float h, int size , float sc ) {
		super( x , y , w , h , size , sc );
		this.size = size;
		this.pos = new PVector(x,y);
		this.spd = Util.randomPVector(maxSpeed);
		this.sc = sc;
		this.dim = new PVector(0,0);
		this.aim =dim.copy();
		this.colour = Util.randomColour();
		this.Pcolor = Util.randomPColour();
		this.selected=false;
		life = FULL_ENERGY;
		
		
		if (sc<0.4) 
			this.maxSpeed = (float) 0.9;		

		else 
			this.maxSpeed= (float) .4;					
	
				
		
		setShapeAttributes();
		
		}

	private void setShapeAttributes() {
		// TODO Auto-generated method stub
		body = new Arc2D.Double( -width/2, -height/2, width, height, 25, 330, Arc2D.PIE);
		eye = new Ellipse2D.Double(width/3, -height/3, width/15, width/15);
		pupil = new Ellipse2D.Double(5 + width/3, 5 - height/3, width/30, width/30);
		
		int[] tx = {(int) width/4, -(int) width/2, - (int) width/5, -(int) width/2};
		int[] ty = {20, - (int) height, 0,(int) height};
		fins = new Polygon(tx, ty, tx.length);
		
		float sight = size * 2.5f * 2.00f;
		FOV = new Arc2D.Double(-sight/1, -sight, sight*2, sight*2, 2, 175, Arc2D.PIE);
		float vision = size * 2.5f * .75f;
		FOVToHunt = new Arc2D.Double(-vision/1, -vision, vision*2, vision*2, -50, 295, Arc2D.PIE);
		
		Box = new Area();
		Box.add(new Area(body));
		Box.add(new Area(fins));
		

	}
	
	public void checkCollision(Dimension panelSize) { 		
		Rectangle2D.Double top = new Rectangle2D.Double(0, -10, panelSize.width, 10);
		Rectangle2D.Double bottom = new Rectangle2D.Double(0, panelSize.height, panelSize.width, 10);
		Rectangle2D.Double left = new Rectangle2D.Double(-10, 0, 10, panelSize.height);
		Rectangle2D.Double right = new Rectangle2D.Double(panelSize.width, 0, 10, panelSize.height);
		float coef = .15f;
		PVector acceleration = new PVector();
		if (getFOV().intersects(left)) acceleration.add(1,0);
		else if (getFOV().intersects(right)) acceleration.add(-1,0);
		else if (getFOV().intersects(top)) acceleration.add(0,1);
		else if (getFOV().intersects(bottom)) acceleration.add(0,-1);
		acceleration.mult(3f*coef);
		dim.add(acceleration);
	}
	
	public Shape getBoundary() {
		AffineTransform at = new AffineTransform();		
		at.translate(pos.x, pos.y);
		at.rotate(dim.heading());
		at.rotate(Math.toRadians(90));
		at.scale(sc, sc);
		return at.createTransformedShape(Box);
	}

	public Shape getFOV() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(dim.heading());
		at.rotate(Math.toRadians(90));
		at.scale(sc, sc);
		return at.createTransformedShape(FOV);
	}

	public Shape FOVToHunt() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(dim.heading());
		at.rotate(Math.toRadians(90));
		at.scale(sc, sc);
		return at.createTransformedShape(FOVToHunt);
	}
	


	public void draw(Graphics2D g2) {
		if(life > 2) {
			AffineTransform tf = g2.getTransform();
			g2.translate(pos.x, pos.y);
			g2.rotate(dim.heading());
			g2.rotate(Math.toRadians(90));
			g2.scale(sc,sc);
			
			if (spd.x < 0)  g2.scale(-1, 1);
			
			
			g2.setColor(colour.white.darker());
			g2.fill(fins);
			
			//body
			g2.setColor(colour.white);
			g2.fill(body);
			
			//eye
			g2.setColor(Color.red);
			
			g2.fill(eye);
			
			g2.setColor(Color.red);
			g2.setTransform(tf);
			drawInfo(g2);
}
	}


public void drawInfo(Graphics2D g) {
	AffineTransform at = g.getTransform();
	g.translate(pos.x, pos.y);
	Font f = new Font("Arial", Font.BOLD, 12); 
	g.setFont(f); 
	String st = String.format("%.2f Speed ", val); 
	String sv = String.format("%.2f Energy ", life); 

	//		String st = String.format("%d", size); 
	FontMetrics metrics = g.getFontMetrics(f); 
	g.drawString(st, -metrics.stringWidth(st)/2, -size*25.5f); 
	g.drawString(sv, -metrics.stringWidth(sv)/2, -size*17.0f); 

	g.setTransform(at);
}


@Override
public void move(){
	PVector direction=dim.copy();
	val = 1;
	if (life>500) {
		val = (float) (0.1f + 1.8f*sc);
		//			System.out.print("\nspeed "+ val);
	}
	else {
		val = (float) (1f - 1.8f*(sc-0.5f))/10;
		//			System.out.print("\nspeed decreased"+ val);

	}
	pos.add(direction.mult((float)val));			//Correct			
	dim.lerp(aim,0.005f);
	dim.normalize();
}

public PVector getPos() {
	return pos;
}



public boolean detectCollision(Predator p) {		//------------>detect FISH collision
	return (getBoundary().intersects(p.getBoundary().getBounds2D()) &&
			p.getBoundary().intersects(getBoundary().getBounds2D()) );
}

public boolean detectCollision(Fish f) {		//------------>detect FOOD collision
	return (getBoundary().intersects(f.getBoundary().getBounds2D()) &&
			f.getBoundary().intersects(getBoundary().getBounds2D()) );
}



public void traceClosestFish(ArrayList<Fish> fishList) {	
	if (fishList.size()>0) {
		// set the 1st item as default target
		Fish target = fishList.get(0);
		float distToTarget = PVector.dist(pos, target.getPos());
		// find the closer one
		for (Fish f:fishList) if (PVector.dist(pos, f.getPos()) < distToTarget) {
			target = f;
			distToTarget = PVector.dist(pos, target.getPos());
		}
		// make animal follow this target
		this.attractedBy(target);
	}	
}





public void attractedBy(Fish target) {	//---------> Atraction Force for food will Increase 
	//												 according to the size of the Food.

	//		float coef = 2f;	// coefficient of acceleration relative to maxSpeed
	PVector direction = PVector.sub(target.getPos(), pos).normalize();
	FoSize= (float) (target.getSize()/0.2);
	direction.normalize();
	dim.add(PVector.div(direction,FoSize));			// now correct

}

public void distractedBy(Predator p) {
	PVector path = PVector.sub(pos, p.pos);
	dim.add(path.normalize().mult(2f)).limit(2.0f);
	//		d.add(path.normalize().mult(maxSpeed*0.02f)).limit(maxSpeed);
}

public void increaseEnergy(float increaseSize) {
	life += increaseSize*4;
}
public void decreaseEnergy() {
	life-= FULL_ENERGY/(30*20); //30fps x 20sec living duration
}

public void resolveCollision() {
	// TODO Auto-generated method stub
	if (sc<0.6)
		sc*=1.1;
}

public double getSize() {
	// TODO Auto-generated method stub
	return sc;
}

public void select() {
	this.selected = true;
}

public void deselect() {
	this.selected = false;
}

public float currHealth() {
	// TODO Auto-generated method stub
	return life;
}

public boolean checkMouseHit(MouseEvent e) {
	return	(Math.abs(e.getX() - pos.x) < size/2) && 
			(Math.abs(e.getY() - pos.y) < size/2);
}

}


