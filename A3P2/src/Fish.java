import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import processing.core.PVector;

public class Fish extends Animal {

	private Color color;						//featured color
	private Arc2D.Double body;				//the original pacman body
	private Ellipse2D.Double eye;	//the eye
	private Ellipse2D.Double pupil;
	private Polygon fins;
	private Dimension dimension;
	
	public Fish(float x, float y, float size) {
		super(x, y, 200, 100, size);
		this.color = Util.randomColor();
		//this.dimension = new Dimension(200,100);
}

	
	protected void setShapeAttributes() {
		// TODO Auto-generated method stub
		body = new Arc2D.Double( -width/2, -height/2, width, height, 25, 330, Arc2D.PIE);
		eye = new Ellipse2D.Double(width/3, -height/3, width/15, width/15);
		pupil = new Ellipse2D.Double(5 + width/3, 5 - height/3, width/30, width/30);
		
		int[] tx = {(int) width/4, -(int) width/2, - (int) width/5, -(int) width/2};
		int[] ty = {20, - (int) height, 0,(int) height};
		fins = new Polygon(tx, ty, tx.length);
	}
	@Override
	public void draw(Graphics2D g) {	
		//transformation
		AffineTransform at = g.getTransform();		
		g.translate(position.x, position.y);
		g.rotate(speed.heading());
		g.scale(size, size);
		if (speed.x < 0) g.scale(1, -1);
		
		//tail
		g.setColor(color.darker());
		g.fill(fins);
		
		//body
		g.setColor(color);
		g.fill(body);
		
		//eye
		g.setColor(Color.black);
		if (energy < FULL_ENERGY*.3f) g.setColor(Util.randomColor());
		g.fill(eye);
		
		g.setTransform(at);
	}
	
	@Override
	protected void traceBestFood(ArrayList<SimulationObject> fList) {	
		if (fList.size()>0) {	
			// find 1st target
			SimulationObject target = fList.get(0);
			float distToTarget = PVector.dist(position, target.getPos());
			
			// find the closer one
			for (SimulationObject f:fList) if (PVector.dist(position, f.getPos()) < distToTarget) {
				target = f;
				distToTarget = PVector.dist(position, target.getPos());
			}
			
			// make animal follow this target
			this.attractedBy(target);
		}	
	}

	@Override
	protected void setBoundingBox() {
		boundingBox = new Area(body);
		boundingBox.add(new Area(fins));	
	}

	@Override
	protected AffineTransform getAffineTransform() {
		AffineTransform at = new AffineTransform();		
		at.translate(position.x, position.y);
		at.rotate(speed.heading());
		at.scale(size, size);
		return at;
	}

	@Override
	protected boolean eatable(SimulationObject food) {
		return (food instanceof Food);
	}
	
}
