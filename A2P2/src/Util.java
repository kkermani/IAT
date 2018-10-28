import java.awt.Color;

import processing.core.PVector;

public class Util {

	public static float random(double min, double max) {
		return (float) (Math.random()*(max-min)+min);
	}
	
	public static float random(double max) {
		return (float) (Math.random()*max);
	}
	
	public static Color randomColor() {
		int r = (int) random(255);
		int g = (int) random(255);
		int b = (int) random(255);
		
		return new Color(r,g,b);
	}
	
	public static PVector randomPVector(int maxX, int maxY) {		
		return new PVector((float)random(maxX), (float)random(maxY));	
	}
	
	public static PVector randomPVector(float magnitude) {
		return PVector.random2D().mult(magnitude);
	}
}
