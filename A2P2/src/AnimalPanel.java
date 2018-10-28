import java.awt.event.*;
import java.awt.*;

import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

import processing.core.PVector;


public class AnimalPanel extends JPanel implements ActionListener {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Color BACKGROUND_COLOUR = new Color(72,173,249);
	private Timer t;
	private Dimension panelSize;
	
	private ArrayList<Animal> fishList;
	private ArrayList<Food> foodList;
	
	private final double MARGIN = 85;
	
	public AnimalPanel(Dimension initialSize) {
		super();
		this.panelSize = initialSize;
		
		this.fishList = new ArrayList<>();
		this.foodList = new ArrayList<>();
		
		System.out.println("util test - " + Float.toString(Util.random(10, 100)));
		//generate food
		for(int i = 0; i < 4; ++i) {
			foodList.add(new Food(Util.random(MARGIN,panelSize.width - MARGIN),
					Util.random(MARGIN,panelSize.width - MARGIN),
					Util.random(1,5)));
		}
		
		for (int i = 0; i < 5; ++i) {
			genNewFish(foodList);
		}
		
		addMouseListener(new MyMouseAdapter());
		
		t = new Timer(30,this);
		t.start();
		
	}


	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		panelSize = getSize();
		setBackground(BACKGROUND_COLOUR);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		for (Food f : foodList) {
			f.draw(g2);
		}
		
		for (Animal a : fishList) {
			a.draw(g2);
		}
		
	}

	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		ArrayList<Animal> deleteList = new ArrayList<>();
		
		for (Animal a : fishList) {
			a.move();
			a.checkBoundary();
			
			if (a.isHalfSpeed()) {
				deleteList.add(a);
			}
			
			for (Animal a2 : fishList) {
				//check collision with other fish
				if (a2 != a) {
					if (a.animalCollided(a2)) {
						moveSmallerAnimal(a, a2);
					}
				}
			}
			
			//check food collision
			for (int i = 0; i < foodList.size(); ++i) {
				if (a.collidesFood(foodList.get(i))) {
					a.increaseEnergy(foodList.get(i).getEnergy());
					foodList.add(new Food(Util.random(MARGIN,panelSize.width - MARGIN),
							Util.random(MARGIN,panelSize.width - MARGIN),
							Util.random(3,5)));
					foodList.remove(i);
					
					a.goAfterNextFood(foodList);
					
				}
				
			}
			
			
			
		}
		
		for (Animal a : deleteList) {
			fishList.remove(a);
			genNewFish(foodList);
		}
		
		repaint();
	}

	private void genNewFish(ArrayList<Food> list) {
		Animal newAnimal = new Animal(
				new PVector(Util.random(MARGIN, panelSize.getWidth() - MARGIN),
					    Util.random(MARGIN,panelSize.getHeight() - MARGIN)),
			Util.random(0.1,0.5)
			);
		fishList.add(newAnimal);
		newAnimal.goAfterNextFood(list);
		
	}


	private void moveSmallerAnimal(Animal a1, Animal a2) {
		if (a1.getArea() < a2.getArea()) {
			a1.setSpeed(a1.getSpeed().mult((float)-1));
			return;
		}
		a2.setSpeed(a2.getSpeed().mult((float)-1));
	}
	

	private class MyMouseAdapter extends MouseAdapter {
		
		public void mousePressed(MouseEvent e) {
			
		}
	
	}
}