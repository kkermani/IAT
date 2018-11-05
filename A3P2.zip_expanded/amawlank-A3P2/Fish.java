import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javafx.scene.transform.Affine;
import processing.core.PVector;

public class Fish extends Animal{

	private int size;
	public PVector pos, speed;
	public PVector d;

	public PVector aim;
	public double scale;
	public Ellipse2D.Double body,dot,dot1,dot2;
	private Ellipse2D.Double finTL,finTR;		// Tail Fins
	private Arc2D.Double finL,finR;			// Fins
	public Ellipse2D.Double leftEye,leftPupil,leftDot,rightEye,rightDot,rightPupil;
	private Color color,color1;
	private float foodSize;
	private float maxSpeed;
	private Arc2D.Double fov; //field-of-view
	private Arc2D.Double getFOVtoSurvive; //field-of-view
	private boolean selected;
	private Area bBox; //re

	private float health;

	public Fish(float x, float  y, int size,float scale) {
		super(x,y,size,scale);
		this.size = size;
		this.pos=new PVector(x,y);
		this.scale=scale;

		//		if (scale<0.4) this.maxSpeed = (float) 1.1;
		//		else this.maxSpeed= (float) .9;

		if (scale<0.4) this.maxSpeed = (float) 0.6;		// Fish is Smaller as it is scaled smaller
		// speed is faster
		else this.maxSpeed= (float) .3;					// Fish is Larger as it is scaled smaller
		// speed is slower
		this.health=150;

		this.speed=Util.randomPVector(maxSpeed);
		this.d=new PVector(0,0);

		this.aim=d.copy();
		body = new Ellipse2D.Double();
		dot = new Ellipse2D.Double();
		dot1 = new Ellipse2D.Double();
		dot2 = new Ellipse2D.Double();		
		finTL= new Ellipse2D.Double();
		finTR= new Ellipse2D.Double();
		finL= new Arc2D.Double();
		finR= new Arc2D.Double();
		leftEye= new Ellipse2D.Double();
		leftDot= new Ellipse2D.Double();
		leftPupil = new Ellipse2D.Double();
		rightEye= new Ellipse2D.Double();
		rightDot= new Ellipse2D.Double();
		rightPupil= new Ellipse2D.Double();

		this.color = Util.randomColor();
		this.color1 = Util.randomFinsColor();
		this.selected=false;
		
		setShapeAttributes();
	}

	private void setShapeAttributes() {
		setBodyAttributes();
		setTailFinAttributes();
		setFinAttributes();
		setEyeAttributes();	

		float sight = size * 2.5f * 2.00f;
		fov = new Arc2D.Double(-sight/1, -sight, sight*2, sight*2, 2, 175, Arc2D.PIE);

		float vision = size * 2.5f * .75f;
		getFOVtoSurvive= new Arc2D.Double(-vision/1, -vision, vision*2, vision*2, -50, 295, Arc2D.PIE);

		bBox = new Area();
		bBox.add(new Area(body));
		bBox.add(new Area(finL));
		bBox.add(new Area(finR));
		bBox.add(new Area(finTL));
		bBox.add(new Area(finTR));

	}


	public void checkCollision(Dimension panelSize) { 		//--------WALLS
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
		d.add(acceleration);
	}



	public Shape getBoundary() {
		AffineTransform at = new AffineTransform();		
		at.translate(pos.x, pos.y);
		at.rotate(d.heading());
		at.rotate(Math.toRadians(90));
		at.scale(scale, scale);
		return at.createTransformedShape(bBox);
	}

	public Shape getFOV() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(d.heading());
		at.rotate(Math.toRadians(90));
		at.scale(scale, scale);
		return at.createTransformedShape(fov);
	}

	public Shape getFOVtoSurvive() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(d.heading());
		at.rotate(Math.toRadians(90));
		at.scale(scale, scale);
		return at.createTransformedShape(fov);
	}


	//	
	private void setEyeAttributes() {
		// TODO Auto-generated method stub
		leftEye.setFrame(-size/1, -size/2, size, size);
		leftDot.setFrame(-size/2-10, -size/2+2, size/2, size/2);
		leftPupil.setFrame(-size/3-8, -size/2+4, size/5, size/5);
		rightEye.setFrame(-size/7,-size/2, size, size);
		rightDot.setFrame(-size/7+10, -size/2+2, size/2, size/2);
		rightPupil.setFrame(-size/7+15, -size/2+4, size/5, size/5);
	}

	private void setFinAttributes() {
		// TODO Auto-generated method stub
		finR.setArc(-size/4+25,-size/5+65,80,90,0,90,Arc2D.PIE);		
		finL.setArc(-size/1-40,-size/5+65,80,90,90,90,Arc2D.PIE);
	}

	public void setBodyAttributes() {
		body.setFrame(-size/1-20,-size/2,150,200);
		dot.setFrame(-size/1-20,-size/2,150,200);		
		dot1.setFrame(-size/1-20, (-size/2),150,200);
		dot2.setFrame(-size/1+4,-size/4,100,180);	
	}

	public void setTailFinAttributes() {
		finTL.setFrame(0,0,40,80);
		finTR.setFrame(0,0,40,80);		
	}

	public void draw(Graphics2D g2) {
		if(health>2) {
			AffineTransform tra = g2.getTransform();
			g2.translate(pos.x, pos.y);
			g2.rotate(d.heading());
			g2.rotate(Math.toRadians(90));
			g2.scale(scale,scale);

			if (speed.x < 0)
				g2.scale(-1, 1);
			//-------------------------------------> Fins
			g2.setColor(color1);
			g2.fillOval(-size/2+16,-size/2-5,20,10);
			g2.setColor(color1);
			g2.fill(finR);		
			g2.fill(finL);
			//-------------------------------------> Body
			g2.setColor(color.darker());
			g2.fill(body);
			g2.setColor(Color.BLACK);
			g2.draw(body);

			//-------------------------------------> Tail Fins
			//Left fin
			AffineTransform transform = g2.getTransform();
			g2.setColor(color1.brighter());
			g2.translate(-size/2+40,-size/5+140);
			g2.rotate(Math.PI/4);
			g2.fill(finTL);
			g2.setTransform(transform);
			//Right fin
			AffineTransform trans = g2.getTransform();
			g2.setColor(color1.brighter());
			g2.translate(-size/2+80,-size/5+195);
			g2.rotate(Math.PI/4 *3);
			g2.fill(finTR);
			g2.setTransform(trans);
			//-------------------------------------> Body yellow shells
			g2.setColor(color);
			g2.fill(dot2);
			//-------------------------------------> Eyes
			g2.setColor(Color.BLACK);
			g2.fill(leftEye);
			g2.setColor(Color.WHITE);
			g2.fill(leftDot);
			g2.setColor(Color.BLUE);		
			g2.fill(leftPupil);
			g2.setColor(Color.BLACK);
			g2.fill(rightEye);
			g2.setColor(Color.WHITE);
			g2.fill(rightDot);
			g2.setColor(Color.BLUE);
			g2.fill(rightPupil);

			g2.setColor(Color.red);
			g2.draw(getFOVtoSurvive);
			g2.setColor(Color.BLACK);
			g2.draw(fov);
			//before setTransform
			g2.setTransform(tra);
			g2.setColor(Color.BLACK);
			if(selected) {
				drawInfo(g2);
//				System.out.print(" DRAW INFO ");
			}
		}
		//---------------------------------------------> HEALTH IS LOW SO DRAW DYING FISH
		else if(health<2) {

			AffineTransform tra = g2.getTransform();
			g2.translate(pos.x, pos.y);
			g2.rotate(d.heading());
			g2.rotate(Math.toRadians(90));
			g2.scale(scale-0.1,scale);

			if (speed.x < 0)
				g2.scale(-1, 1);
			//-------------------------------------> Fins
			g2.setColor(color1);
			g2.fillOval(-size/2+16,-size/2-5,20,10);
			g2.setColor(color1);
			g2.fill(finR);		
			g2.fill(finL);
			//-------------------------------------> Body
			g2.setColor(Color.WHITE);
			g2.fill(body);
			g2.setColor(Color.BLACK);
			g2.draw(body);

			//-------------------------------------> Tail Fins
			//Left fin
			AffineTransform transform = g2.getTransform();
			g2.setColor(color1.darker());
			g2.translate(-size/2+40,-size/5+140);
			g2.rotate(Math.PI/4);
			g2.fill(finTL);
			g2.setTransform(transform);
			//Right fin
			AffineTransform trans = g2.getTransform();
			g2.setColor(color1.darker());
			g2.translate(-size/2+80,-size/5+195);
			g2.rotate(Math.PI/4 *3);
			g2.fill(finTR);
			g2.setTransform(trans);
			//-------------------------------------> Body yellow shells
			g2.setColor(Color.white);
			g2.fill(dot2);
			//-------------------------------------> Eyes
			g2.setColor(Color.BLACK);
			g2.fill(leftEye);
			g2.setColor(Color.WHITE);
			g2.fill(leftDot);
			g2.setColor(Color.BLUE);		
			g2.fill(leftPupil);
			g2.setColor(Color.BLACK);
			g2.fill(rightEye);
			g2.setColor(Color.WHITE);
			g2.fill(rightDot);
			g2.setColor(Color.BLUE);
			g2.fill(rightPupil);

			g2.setColor(Color.red);
			g2.draw(getFOVtoSurvive);
			g2.setColor(Color.BLACK);
			g2.draw(fov);
			//before setTransform

			g2.setTransform(tra);
			g2.setColor(Color.BLACK);
			if(selected)
				drawInfo(g2);
			//			g2.setColor(Color.DARK_GRAY);
			//			g2.draw(getBoundary().getBounds2D());
		}
	}

	public void drawInfo(Graphics2D g) {
		AffineTransform at = g.getTransform();
		g.translate(pos.x, pos.y);
		Font f = new Font("Arial", Font.BOLD, 12); 
		g.setFont(f); 
		String st = String.format("%.2f Speed ", maxSpeed); 
		String sv = String.format("%.2f Energy ", health); 

		//		String st = String.format("%d", size); 
		FontMetrics metrics = g.getFontMetrics(f); 
		g.drawString(st, -metrics.stringWidth(st)/2, -size*1.2f); 
		g.drawString(sv, -metrics.stringWidth(sv)/2, -size*1.4f); 

		g.setTransform(at);
	}

	public void move(){
		PVector direction=d.copy();
		float val= maxSpeed/1.5f;
		pos.add(direction.mult((float)val));			//Correct			
		d.lerp(aim,0.4f);
		d.normalize();
	}

	public void checkForFood(Food food) {
		// TODO Auto-generated method 
		PVector foodPos;
		if(food == null)
			foodPos = new PVector(400,300);
		else
			foodPos = food.getPos().copy();	
		PVector direction = foodPos.sub(pos);
		direction.normalize();
		this.aim=direction;
	}

	public PVector getPos() {
		return pos;
	}

	public boolean detectCollision(Food food) {		//------------>detect FOOD collision
		return (getBoundary().intersects(food.getBoundary().getBounds2D()) &&
				food.getBoundary().intersects(getBoundary().getBounds2D()) );
	}

	public boolean detectCollision(Fish f) {		//------------>detect FISH collision
		return (getBoundary().intersects(f.getBoundary().getBounds2D()) &&
				f.getBoundary().intersects(getBoundary().getBounds2D()) );
	}

	public boolean inViewOfPredator(Predator p) {//------------>detect FISH collision
		return (getFOVtoSurvive().intersects(p.fovToCatchFish().getBounds2D()) &&
				p.fovToCatchFish().intersects(getFOVtoSurvive().getBounds2D()) );
	}




	public void traceClosestFood(ArrayList<Food> fList) {	
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


	//	public void attractedBy(Food target) {	//---------> Atraction Force for food will Increase 
	//		//												 according to the size of the Food.
	//		PVector direction = PVector.sub(target.getPos(), pos).normalize();
	//		foodSize= (float) (target.getSize()/0.9);
	//		direction.normalize();
	//		d.add(PVector.div(direction,foodSize));			// now correct
	//	}

	protected void attractedBy(Food target) {
		float coef = .02f;	// coefficient of acceleration relative to maxSpeed
		PVector direction = PVector.sub(target.getPos(), pos).normalize();
		PVector acceleration = PVector.mult(direction, maxSpeed*coef);
		d.add(acceleration);
	}	

	public void energyIncrease() {	 //---------> Energy will Increase according to the
		//										  size of the Food.
		health+=foodSize*0.01*scale;;
	}

	public void energyDecrease() {
		health-=0.10;
	}

	public void energyDecreaseWithSize() {
		health-=0.10;
	}

	public void resolveCollision() {
		// TODO Auto-generated method stub
		if (scale<0.4)
			scale+=foodSize*0.01*scale;
	}

	public void speedInHalf() {		//---------> When Energy is less than 3 animal will move
		//			 at half the speed
		maxSpeed=(float) (maxSpeed/2.0);
	}

	public void decreaseSpeed() {
		// TODO Auto-generated method stub
		if(maxSpeed>=0) 
			maxSpeed-=0.01;
	}

	public void increaseSpeed() {
		// TODO Auto-generated method stub
		maxSpeed+=0.001;
	}

	public void distractedBy(Fish f) {
		PVector path = PVector.sub(pos, f.pos);
		d.add(path.normalize().mult(2f*2f)).limit(2.0f);
		//		d.add(path.normalize().mult(maxSpeed*0.02f)).limit(maxSpeed);
	}

	public float getSize() {
		return (float) scale;
	}

	public float energy() {
		return health;
	}

	public void distractedBy(Predator p) {
		// TODO Auto-generated method stub
		PVector path = PVector.sub(pos, p.pos);
		d.add(path.normalize().mult(0.75f)).limit(2.0f);
	}

	public void select() {
		this.selected = true;
	}
	
	public void deselect() {
		this.selected = false;
	}
	
	public boolean checkMouseHit(MouseEvent e) {
		return	(Math.abs(e.getX() - pos.x) < size/2) && 
				(Math.abs(e.getY() - pos.y) < size/2);
	}

}


