package iat265.lab.w7;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import processing.core.PVector;

public class SmartPacwoman extends Pacwoman{
	
	private Image badge; 

	public SmartPacwoman(float x, float y, float size) {
		super(x, y, size);
	}
	
	@Override
	protected void setShapeAttributes() {
		super.setShapeAttributes();
		
		try {
		    badge = ImageIO.read(new File("asset/starbadge.png"));
		} catch (IOException e) {
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		AffineTransform at = g.getTransform();		
		g.translate(pos.x, pos.y);
		g.rotate(speed.heading());
		g.scale(scale, scale);
		if (speed.x < 0) g.scale(1, -1);
		
		//draw badge
		g.drawImage(badge, dimension.width/20, 0, dimension.height/3, dimension.height/3, null);
		
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
