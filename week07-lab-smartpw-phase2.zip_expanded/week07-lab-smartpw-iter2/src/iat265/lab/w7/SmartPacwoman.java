package iat265.lab.w7;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import processing.core.PVector;

public class SmartPacwoman extends Pacwoman{
	
	private Image badge;
	private Polygon tail; 
	private Color color;
	private Ellipse2D.Double eye;
	public SmartPacwoman(float x, float y, float size) {
		super(x, y, size);
	}
	
	@Override
	protected void setShapeAttributes() {
		super.setShapeAttributes();
		
		
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		AffineTransform at = g.getTransform();		
		g.translate(pos.x, pos.y);
		g.rotate(speed.heading());
		g.scale(scale, scale);
		if (speed.x < 0) g.scale(1, -1);
		
		int[] px = {- dimension.width/4, dimension.width/2, dimension.width/20, -dimension.width/20};
		int[] py = {-5,  dimension.height/4, 0, dimension.height};
		tail = new Polygon(px, py, px.length);
		g.setColor(color.black);
		g.fill(tail);
		
		//draw badge
		eye = new Ellipse2D.Double(dimension.width/3, -dimension.height/3, dimension.width/15, dimension.width/15);
		g.setColor(Color.red);
		g.fill(eye);
		
		
		
		g.setTransform(at);
	}
	
	@Override
	protected void traceBestFood(ArrayList<Food> fList) {	
		if (fList.size()>0) {
			
			// set the 1st item as default target
			Food target = fList.get(0);
			float targetAttraction = this.getAttraction(target);
			
			// find the closer one
			for (Food f:fList) if (this.getAttraction(f) > targetAttraction) {
				target = f;
				targetAttraction = this.getAttraction(target);
			}
			
			// make animal follow this target
			this.attractedBy(target);
		}	
	}
	
	protected float getAttraction(Food f) {
		return f.getSize()*10/PVector.dist(pos, f.getPos());
	}

}
