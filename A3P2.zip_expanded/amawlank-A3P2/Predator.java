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
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

//import iat265.lab.w7.Food;
import processing.core.PVector;

public class Predator extends Animal{

	private int size;
	public PVector pos, speed;
	public PVector d;

	public PVector aim;
	public float scale;
	public Ellipse2D.Double body,dot,dot1,dot2;
	private Arc2D.Double finL,finR;			// Fins
	public Ellipse2D.Double leftEye,leftPupil,leftDot,rightEye,rightDot,rightPupil;
	public Rectangle2D.Double nose;
	private Polygon tail,arrow;					//tail
	//private Color color,color1;
	private float foodSize;
	private float maxSpeed;
	private Arc2D.Double fov; //field-of-view
	private  Arc2D.Double fovToCatchFish;
	private Area bBox;
	public float health;
	private float FULL_ENERGY=1000;
	private boolean selected;
	
	private float val; // associated to the speed value.


	public Predator(float x, float  y, int size,float scale) {
		super(x,y,size,scale);

		this.size = size;
		this.pos=new PVector(x,y);
		this.scale=scale;
		health = FULL_ENERGY;
		
		if (scale<0.4) 
			this.maxSpeed = (float) 0.9;		// Predator is Smaller as it is scaled smaller
		// speed is faster
		else 
			this.maxSpeed= (float) .4;					// Fish is Larger as it is scaled smaller
		// speed is slower

		this.speed=Util.randomPVector(maxSpeed);
		this.d=new PVector(0,0);

		this.aim=d.copy();
		body = new Ellipse2D.Double();
		dot = new Ellipse2D.Double();
		dot1 = new Ellipse2D.Double();
		dot2 = new Ellipse2D.Double();		

		finL= new Arc2D.Double();
		finR= new Arc2D.Double();
		leftEye= new Ellipse2D.Double();
		leftDot= new Ellipse2D.Double();
		leftPupil = new Ellipse2D.Double();
		rightEye= new Ellipse2D.Double();
		rightDot= new Ellipse2D.Double();
		rightPupil= new Ellipse2D.Double();
		nose= new Rectangle2D.Double();
		this.selected=false;

		setShapeAttributes();

	}

	private void setShapeAttributes() {
		setBodyAttributes();
		//		setTailFinAttributes();
		setFinAttributes();
		setEyeAttributes();	

		float sight = size * 2.5f * 1.20f;
		fov = new Arc2D.Double(-sight/1, -sight, sight*2, sight*2, 2, 175, Arc2D.PIE);

		float pVision = size * 2.5f * .85f;
		fovToCatchFish = new Arc2D.Double(-pVision/1, -pVision, pVision*2, pVision*2, 55, 65, Arc2D.PIE);

		int[] px = {(int) (200/4), (int) (-200/2), (int) (-200/5), (int) (-200/2)};
		int[] py = {0, (int) (-100/2), 0, (int) (100/2)};
		tail = new Polygon(px, py, px.length);
		int[] pq = {(int) (150/4), (int) (-150/2), (int) (-150/5), (int) (-150/2)};
		int[] pr = {0, (int) (-150/2), 0, (int) (150/2)};
		arrow = new Polygon(pq, pr, px.length);

		bBox = new Area();
		bBox.add(new Area(body));
		bBox.add(new Area(finL));
		bBox.add(new Area(finR));
		bBox.add(new Area(tail));
		bBox.add(new Area(arrow));

	}

	public void checkCollision(Dimension panelSize) { 		//--------WALLS

		Rectangle2D.Double top = new Rectangle2D.Double(0, -10, panelSize.width, 10);
		Rectangle2D.Double bottom = new Rectangle2D.Double(0, panelSize.height-50, panelSize.width, 10);
		Rectangle2D.Double left = new Rectangle2D.Double(-10, 0, 10, panelSize.height);
		Rectangle2D.Double right = new Rectangle2D.Double(panelSize.width, 0, 10, panelSize.height);

		float coef = .15f;
		PVector acceleration = new PVector();
		if (getFOV().intersects(left)) acceleration.add(1,0);
		else if (getFOV().intersects(right)) acceleration.add(-1,0);
		else if (getFOV().intersects(top)) acceleration.add(0,1);
		else if (getFOV().intersects(bottom)) acceleration.add(0,-1);

		acceleration.mult(1.5f*coef);
		d.add(acceleration);
	}
	
	

	private Shape getBoundary() {
		AffineTransform at = new AffineTransform();		
		at.translate(pos.x, pos.y);
		at.rotate(d.heading());
		at.rotate(Math.toRadians(90));
		at.scale(scale, scale);
		return at.createTransformedShape(bBox);
	}
	@Override
	public Shape getFOV() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(d.heading());
		at.rotate(Math.toRadians(90));
		at.scale(scale, scale);
		return at.createTransformedShape(fov);
	}

	public Shape fovToCatchFish() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(d.heading());
		at.rotate(Math.toRadians(90));
		at.scale(scale, scale);
		return at.createTransformedShape(fovToCatchFish);
	}


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
		//		nose.setFrame(-size/1-5,-size/2,50,100);
	}


	public void draw(Graphics2D g2) {
		if(health>500) {
			AffineTransform tra = g2.getTransform();
			g2.translate(pos.x, pos.y);
			g2.rotate(d.heading());
			g2.rotate(Math.toRadians(90));
			g2.scale(scale,scale);

			if (speed.x < 0)
				g2.scale(-1, 1);
			//-------------------------------------> Fins
			g2.setColor(Color.GRAY);
			g2.fillOval(-size/2+16,-size/2-5,20,10);
			g2.setColor(Color.darkGray);
			g2.fill(finR);		
			g2.fill(finL);
			//-------------------------------------> Body
			g2.setColor(Color.GRAY);
			g2.fill(body);
			g2.setColor(Color.BLACK);
			g2.draw(body);


			//-------------------------------------> Tail
			AffineTransform transform = g2.getTransform();
			g2.setColor(Color.LIGHT_GRAY);
			g2.translate(-size/2+18,-size/5+140);
			g2.rotate(Math.PI*3/2);
			g2.fill(tail);
			g2.setTransform(transform);
			//-------------------------------------> Arrow Head
			AffineTransform trans = g2.getTransform();
			g2.setColor(Color.GRAY);
			g2.translate(-size/2+25,-size/5-36);
			g2.rotate(Math.PI*3/2);
			g2.fill(arrow);
			g2.setTransform(trans);
			//-------------------------------------> Body yellow shells
			g2.setColor(Color.LIGHT_GRAY);
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
			g2.setColor(Color.RED);
			g2.draw(fovToCatchFish);
			g2.setColor(Color.BLACK);
			g2.draw(fov);
			//before setTransform
			g2.setTransform(tra);
			//-----------------------------------------------------> Draw scale on Screen
			//			int fishSize = (int) (scale);
			//			int displaySizeWidth = g2.getFontMetrics().stringWidth(Integer.toString(fishSize));
			//			g2.setColor(Color.BLACK);
			//			g2.drawString(Integer.toString((int) (150*scale)), 
			//					-displaySizeWidth/2, -150/2-5 );
			//			g2.setColor(Color.BLACK);
			//			g2.draw(getBoundary().getBounds2D());
			if(selected)
				drawInfo(g2);
		}
		else {
			AffineTransform tra = g2.getTransform();
			g2.translate(pos.x, pos.y);
			g2.rotate(d.heading());
			g2.rotate(Math.toRadians(90));
			g2.scale(scale,scale);

			if (speed.x < 0)
				g2.scale(-1, 1);
			//-------------------------------------> Fins
			g2.setColor(Color.GRAY);
			g2.fillOval(-size/2+16,-size/2-5,20,10);
			g2.setColor(Color.darkGray);
			g2.fill(finR);		
			g2.fill(finL);
			//-------------------------------------> Body
			g2.setColor(Color.GRAY);
			g2.fill(body);
			g2.setColor(Color.BLACK);
			g2.draw(body);


			//-------------------------------------> Tail
			AffineTransform transform = g2.getTransform();
			g2.setColor(Color.LIGHT_GRAY);
			g2.translate(-size/2+18,-size/5+140);
			g2.rotate(Math.PI*3/2);
			g2.fill(tail);
			g2.setTransform(transform);
			//-------------------------------------> Arrow Head
			AffineTransform trans = g2.getTransform();
			g2.setColor(Color.GRAY);
			g2.translate(-size/2+25,-size/5-36);
			g2.rotate(Math.PI*3/2);
			g2.fill(arrow);
			g2.setTransform(trans);
			//-------------------------------------> Body yellow shells
			g2.setColor(Color.WHITE);
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
			g2.setColor(Color.BLACK);
			g2.draw(fovToCatchFish);
			//before setTransform
			g2.setTransform(tra);
			//			g2.setColor(Color.BLACK);
			//			g2.draw(getBoundary().getBounds2D());
			if(selected)
				drawInfo(g2);
		}



	}
	public void drawInfo(Graphics2D g) {
		AffineTransform at = g.getTransform();
		g.translate(pos.x, pos.y);
		Font f = new Font("Arial", Font.BOLD, 12); 
		g.setFont(f); 
		String st = String.format("%.2f Speed ", val); 
		String sv = String.format("%.2f Energy ", health); 

		//		String st = String.format("%d", size); 
		FontMetrics metrics = g.getFontMetrics(f); 
		g.drawString(st, -metrics.stringWidth(st)/2, -size*1.5f); 
		g.drawString(sv, -metrics.stringWidth(sv)/2, -size*1.7f); 

		g.setTransform(at);
	}


	@Override
	public void move(){
		PVector direction=d.copy();
		val = 1;
		if (health>500) {
			val = (float) (0.1f + 1.8f*scale);
			//			System.out.print("\nspeed "+ val);
		}
		else {
			val = (float) (1f - 1.8f*(scale-0.5f))/10;
			//			System.out.print("\nspeed decreased"+ val);

		}
		pos.add(direction.mult((float)val));			//Correct			
		d.lerp(aim,0.005f);
		d.normalize();
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
		foodSize= (float) (target.getSize()/0.2);
		direction.normalize();
		d.add(PVector.div(direction,foodSize));			// now correct

	}

	public void distractedBy(Predator p) {
		PVector path = PVector.sub(pos, p.pos);
		d.add(path.normalize().mult(2f)).limit(2.0f);
		//		d.add(path.normalize().mult(maxSpeed*0.02f)).limit(maxSpeed);
	}

	public void increaseEnergy(float increaseSize) {
		health += increaseSize*4;
	}
	public void decreaseEnergy() {
		health-= FULL_ENERGY/(30*20); //30fps x 20sec living duration
	}

	public void resolveCollision() {
		// TODO Auto-generated method stub
		if (scale<0.6)
			scale*=1.1;
	}

	public double getSize() {
		// TODO Auto-generated method stub
		return scale;
	}
	
	public void select() {
		this.selected = true;
	}
	
	public void deselect() {
		this.selected = false;
	}

	public float currHealth() {
		// TODO Auto-generated method stub
		return health;
	}
	
	public boolean checkMouseHit(MouseEvent e) {
		return	(Math.abs(e.getX() - pos.x) < size/2) && 
				(Math.abs(e.getY() - pos.y) < size/2);
	}

}


