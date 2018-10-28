import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.awt.*;
import processing.core.PVector;

public class Animal {

	private PVector pos, spd;
	private float maxSpeed;
	private Dimension dim;
	private float scale;
	private Color color;
	private Arc2D.Double body;
	private Ellipse2D.Double eye;
	private float energy;
	private PVector sickSpeed;
	private Area box;
	private PVector initSpeed;
	
	private final int SIZE_X = 200;
	private final int SIZE_Y = 110;
	
	private final int INIT_ENERGY = 100;
	private Polygon tail;
	
	public Animal(PVector pos, float size) {
		this.pos = new PVector(pos.x, pos.y);
		this.scale = size;
		this.maxSpeed = 0;
		while (this.maxSpeed == 0) {
			this.maxSpeed = Util.random(3,5) / (1 + scale);
		}
		
		//TODO: play around with lower and upper bound
		energy = (float) INIT_ENERGY;
		this.spd = (Util.randomPVector(maxSpeed));
		this.spd.limit(maxSpeed);
		this.dim = new Dimension(SIZE_X, SIZE_Y);
		this.color = Util.randomColor();
		
		this.initSpeed = this.spd.copy();
		this.sickSpeed = this.spd.mult((float)0.5);
		
		setAnimalAttributes();
	}

	
	
	public void setAnimalAttributes() {
		body = new Arc2D.Double(- dim.getWidth() / 2, - dim.getHeight() / 2, (double) dim.getWidth(), (double) dim.getHeight() , (double) 25, (double) 310, Arc2D.PIE);
		eye = new Ellipse2D.Double(3/5 * dim.getWidth()/2, -dim.getHeight()/3,20,20);
		int [] xCoords = {0, (int) (-3/2 * dim.getWidth()/2)  , (int) (-4/3 * dim.getWidth()/2)  , (int) (-3/2 * dim.getWidth()/2) , (int) (-4/3 * dim.getWidth()/2) ,(int) (-3/2 * dim.getWidth()/2)};
		int [] yCoords = {0, (int) (-2/3 * dim.getHeight()/2) , (int) (-3/4 * dim.getHeight()/2) ,                    0            , (int) (3/4 * dim.getHeight()/2) , (int) (2/3 * dim.getHeight()/2)};
		
		tail = new Polygon(xCoords,yCoords,xCoords.length);
		
		box = new Area(body);
		
		
	}
	
	public void move() {
		pos = pos.add(spd);
		energy = energy - (float) 0.002;
		spd = spd.mult((float)((float)energy/(float)INIT_ENERGY));
		spd.limit(maxSpeed);
	}
	
	public Dimension getDimension() {
		return new Dimension((int) (scale * dim.getHeight()), (int) (scale * dim.getHeight()));
	}
	
	public float getArea() {
		return (float) (getDimension().getHeight() * getDimension().getWidth());
	}

	public void setSpeed(PVector s) {
		this.spd = s.copy();
	}
	
	public PVector getSpeed() {
		return this.spd;
	}
	
	public boolean isHalfSpeed() {
		return initSpeed.x * 0.5 > spd.x &&
				initSpeed.y * 0.5 > spd.y;
	}

	public void draw(Graphics2D g) {
		//transformation
		
		if (isHalfSpeed()) {
			drawSick(g);
			return;
		}
		
		AffineTransform at = g.getTransform();		
		g.translate((int)pos.x, (int)pos.y);
		g.rotate(spd.heading());
		g.scale(this.scale, this.scale);
		if (spd.x < 0) {
			g.scale(1, -1);
		}
		
		g.setColor(color.darker());
		g.fill(body);
		g.setColor(color.WHITE);
		g.fill(eye);
		
		g.setColor(color.RED);
		g.fill(tail);
		
		
		g.setTransform(at);
	}
	
	public void drawSick(Graphics2D g) {
		AffineTransform at = g.getTransform();		
		g.translate((int)pos.x, (int)pos.y);
		g.rotate(spd.heading());
		g.scale(this.scale, this.scale);
		
		g.setColor(color.RED);
		g.fill(body);
		
		
		
		
		g.setTransform(at);
	}
	
	public Shape getBoundary() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.rotate(pos.x,pos.y);
		at.scale(scale, scale);
		return at.createTransformedShape(box);
	}
	
	public void checkBoundary() {
		if (pos.x + getDimension().getWidth() > AnimalApp.PANEL_WIDTH || pos.x - getDimension().getWidth() < 0 ) {
			spd.x = -spd.x;
		}
		
		if (pos.y + getDimension().getHeight() > AnimalApp.PANEL_HEIGHT || pos.y - getDimension().getHeight() < 0 ) {
			spd.y = -spd.y;
		}
	}
	
	public boolean animalCollided(Animal other) {
		return getBoundary().intersects(other.getBoundary().getBounds2D()) && 
			other.getBoundary().intersects(getBoundary().getBounds2D());
	}
	
	public boolean collidesFood(Food food) {
		return getBoundary().intersects(food.getBoundary().getBounds2D()) && 
			food.getBoundary().intersects(getBoundary().getBounds2D());
	}
	
	public void increaseEnergy(float amount) {
		energy += amount;
	}



	public void goAfterNextFood(ArrayList<Food> foodList) {
		try {
			Food bestOption = foodList.get(0);
			float bestAF = 0;
			for (Food f : foodList) {
				float dist = PVector.dist(f.getPos(), pos);
				float attractionForce = f.getScale()/dist;
				if (attractionForce > bestAF) {
					bestAF = attractionForce;
					bestOption = f;
				}
			}
			float oldMag = this.spd.mag();
			this.setSpeed((new PVector(bestOption.getPos().x - this.pos.x,bestOption.getPos().y - this.pos.y)).setMag(oldMag));
			
		} catch(NullPointerException e) {
			//nothing
		}
	}
	
}
