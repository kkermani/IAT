//	The longer you keep the mouse button pressed on the food, the larger the food portion	
//	Concept of an ‘ideal weight' the larger the size of the fish the more its Health is reduced


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.swing.Timer;

import processing.core.PVector;

public class FishPanel extends JPanel implements ActionListener {

	private Fish fish;
	private Food food;
	private Timer t;
//	private PVector positionFood;
	private static int NUM_FOOD=7;
	private static int NUM_FISH=2;
	private static int NUM_PREDATOR=1;
	private Dimension pnlSize;
	private boolean shiftKey;
//	private int foodEaten=0;

	private ArrayList <Food> fList;
	private ArrayList<Fish> fishList;
	private ArrayList<Predator>predatorList;
	private float scale;
	private int count=0;
	
	public class MyKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SHIFT && e.getKeyCode() == KeyEvent.VK_D)
				for(int i=0;i<fishList.size();i++) {
					Fish currFish= fishList.get(i);
					currFish.select();
				}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SHIFT && e.getKeyCode() == KeyEvent.VK_D)
				fish.deselect();	
		}
	}


	public FishPanel(Dimension initialSize) {
		super();
		this.pnlSize = initialSize;
		

		//--------------------------------------------------> FOOD CREATION	
		this.fList= new ArrayList<>();
		while (fList.size() < NUM_FOOD)
			fList.add(new Food((int)Util.random(100, 700),(int)Util.random(100, 500),10));
		
		//--------------------------------------------------> FISH CREATION	

		this.fishList= new ArrayList<>();
		for (int i=0;i<NUM_FISH;i++) {
			scale=(float)Util.random(0.2,0.3);
			fishList.add(new Fish((int)Util.random(100, pnlSize.width-100),
					(int)Util.random(100, pnlSize.height-100)
					,Math.min(initialSize.width,initialSize.height)/10
					,scale));
		}
		//--------------------------------------------------> PREDATOR CREATION	
		this.predatorList= new ArrayList<>();
		for (int i=0;i<NUM_PREDATOR;i++) {
			scale=(float)Util.random(0.4,0.5);
			predatorList.add(new Predator((int)Util.random(100, pnlSize.width-100),
					(int)Util.random(100, pnlSize.height-100)
					,Math.min(initialSize.width,initialSize.height)/10
					,scale));
		}
		
		addMouseListener(new MyMouseAdapter());

		t = new Timer(16, this);
		t.start();
		
		MyKeyListener mkl = new MyKeyListener();
		this.addKeyListener(mkl);
		this.setFocusable(true);
	}


	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2= (Graphics2D) g;
		Color myBlue = new Color(77, 184, 255);
		Color gound =new Color(255, 204, 102);
		setBackground(myBlue);
		g2.setColor(gound);
		g2.fillRect(0, 550, pnlSize.width, pnlSize.height);

		for (int i=0;i<fList.size();i++) {
			fList.get(i).draw(g2);
		}
		for (Fish f:fishList) 
			f.draw(g2);
		for (Predator p:predatorList) 
			p.draw(g2);			
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub		
		for (int p=0;p<predatorList.size();p++) {
			Predator currPredator=predatorList.get(p);
			currPredator.checkCollision(this.getSize()); //------------Detect Wall Predator
			currPredator.move();
			currPredator.decreaseEnergy();
			
			if(currPredator.currHealth()<0 ) {
				predatorList.remove(currPredator);
				scale=(float)Util.random(0.4,0.5);
				predatorList.add(new Predator((int)Util.random(100, pnlSize.width-100),
						(int)Util.random(100, pnlSize.height-100)
						,Math.min(pnlSize.width,pnlSize.height)/10
						,scale));
			}
		
			for (int k=0;k<fishList.size();k++) {
				//		for (Fish currFish:fishList ) {
				Fish currFish=fishList.get(k);
				currFish.checkCollision(this.getSize()); //------------Detect Wall Fish
				currFish.move();
				
				currPredator.traceClosestFish(fishList);
				for (int m=0;m< predatorList.size();m++) {
					if(currPredator!=predatorList.get(m) && currPredator.detectCollision(predatorList.get(m)))					{
						if (currPredator.getSize() < predatorList.get(m).getSize()) { 
							currPredator.distractedBy(predatorList.get(m));	
						}
						else 
							predatorList.get(m).distractedBy(currPredator);
					}	
				}
				if(scale>0.4)							
					currFish.energyDecreaseWithSize();	//------------Decrease Health by 0.07
				currFish.energyDecrease();				//------------Decrease Health by 0.05
				

				if(currFish.inViewOfPredator(currPredator)) {		//------------Fish Escapes Predator
					System.out.println("Fish : I need to Run");
					currFish.distractedBy(currPredator);	
				}
				
				if(currFish.energy()<0) {
					fishList.remove(currFish);
					scale=(float)Util.random(0.2,0.3);
					fishList.add(new Fish((int)Util.random(100, pnlSize.width-100),
							(int)Util.random(100, pnlSize.height-100)
							,Math.min(800,600)/10
							,scale));
				}
				else {
					currFish.traceClosestFood(fList);
					
					if(currFish.energy()<3) currFish.speedInHalf();
					for (int i=0;i< fishList.size();i++) {
						if(currFish!=fishList.get(i) && currFish.detectCollision(fishList.get(i)))					{
							if (currFish.getSize() < fishList.get(i).getSize()) { 
								currFish.distractedBy(fishList.get(i));	
							}
							else 
								fishList.get(i).distractedBy(currFish);
						}	
					}
					
					
					if (currPredator.detectCollision(fishList.get(k))) {			//--Predator detects collision with 
						//--Fish
						fishList.remove(k);	
						scale=(float)Util.random(0.2,0.3);
						fishList.add(new Fish((int)Util.random(100, pnlSize.width-100),
								(int)Util.random(100, pnlSize.height-100)
								,Math.min(800,600)/10
								,scale));
						float val= fishList.get(k).getSize()*10;
						currPredator.increaseEnergy(val);
							
						}
					
					for (int i = 0; i < fList.size(); i++) {
						if (currFish.detectCollision(fList.get(i))) {
							fList.remove(i);
							currFish.resolveCollision();
							currFish.energyIncrease();
							currFish.decreaseSpeed();
						}
						else  {
							currFish.increaseSpeed();
						}
					}		
				}
			}
		}
		if(mouseButtonState)
		{
			fList.add(new Food((int)mousePosition.x,(int)mousePosition.y,10));
			for (int i=0;i<fList.size();i++) {
				Food fishFood=fList.get(i);
				if (fishFood.checkMouseHit(mousePosition) && isControlDown) {
					fList.remove(i);
				}
				else if (fishFood.checkMouseHit(mousePosition)) {
					fishFood.enlarge();
				}
			} 
			
		}
		while (fList.size() < NUM_FOOD) 
			fList.add(new Food((int)Util.random(100, 700),(int)Util.random(100, 500),10));
		
		
			
			

		repaint();									// method of JPanel
	}



	boolean mouseButtonState = false;
	PVector mousePosition = new PVector(0,0);
	boolean isControlDown = false;

	private class MyMouseAdapter extends MouseAdapter {

		public void  mousePressed(MouseEvent e) {
			mouseButtonState = true;
			isControlDown = e.isControlDown();
			mousePosition.x = e.getX();
			mousePosition.y = e.getY();
			for(int i=0; i<fishList.size();i++) {
				Fish currFish=fishList.get(i);
				if(currFish.checkMouseHit(e) && e.isShiftDown())
					currFish.select();
				else if(e.isShiftDown())
					currFish.deselect();
			}
			for(int i=0; i<fishList.size();i++) {
				Predator currPredator=predatorList.get(i);
				if(currPredator.checkMouseHit(e) && e.isShiftDown())
					currPredator.select();
				else if(e.isShiftDown())
					currPredator.deselect();
			}
			
		}

		public void mouseReleased(MouseEvent e) {
			mouseButtonState = false;
		}
	}



}
