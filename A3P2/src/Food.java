import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

public class Food extends SimulationObject {
	
	private Ellipse2D.Double foodShape;		//geometric shape
	private Color foodColor;					//shape color
	
	public Food(float x, float y, float size) {
		super(x, y, 10, 10, size);
		this.foodColor = Color.red;
	}

	@Override
	public void draw(Graphics2D g) {
		AffineTransform at = g.getTransform();
		
		g.translate(position.x, position.y);
		g.scale(size, size);
		
		//draw food
		g.setColor(foodColor);
		g.fill(foodShape);
		

		//draw size as text
		//g.drawString(Float.toString(size), 0, -height/2); //iter 1
		
		g.setTransform(at);
		
		drawInfo(g); //iter 2
	}
	
	
	public void drawInfo(Graphics2D g) {
		AffineTransform at = g.getTransform();
		g.translate(position.x, position.y);
		Font f = new Font("Arial", Font.BOLD, 12); //ier 3
		g.setFont(f); //ier 3
		String st = String.format("%.2f", size); //iter 4
		FontMetrics metrics = g.getFontMetrics(f); //iter 5
		g.drawString(st, -metrics.stringWidth(st)/2, -height*size*.75f); //iter 5
		g.setTransform(at);
	}

	@Override
	protected void setShapeAttributes() {
		this.foodShape = new Ellipse2D.Double(-width/2, -height/2, width, height);	
	}

	@Override
	public void setBoundingBox() {
		boundingBox = new Area(foodShape);	
	}

	@Override
	public AffineTransform getAffineTransform() {
		AffineTransform at = new AffineTransform();		
		at.translate(position.x, position.y);
		at.scale(size, size);
		return at;
	}

	@Override
	public void update(ArrayList<SimulationObject> objList) {
		// nothing, food don't need to be updated
	}

}
