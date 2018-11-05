import java.awt.Color;

import processing.core.PVector;

public class Util {
	public static double random(double min, double max) {
		return Math.random()*(max-min)+min;
	}
	
	public static double random(double max) {
		return Math.random()*max;
	}

	public static Color randomColor() { 	//-------BODY SHELLS COLOR
		int r = (int) random(255);
		int g = (int) random(255);
		int b = (int) random(255);
		
		return new Color(r,g,b);
	}
	public static Color randomFinsColor() { 	//-------FINS COLOR
		int r = (int) random(255);
		int g = (int) random(255);
		int b = (int) random(255);
		
		return new Color(r,g,b);
	}
	public static PVector randomPVector(float magnitude) {
		return PVector.random2D().mult(magnitude);
	}
	
}
