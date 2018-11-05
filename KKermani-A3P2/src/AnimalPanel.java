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

public class AnimalPanel extends JPanel implements ActionListener {

	private Fish fish;
	private Food food;
	private Timer t;

	private int FoNum=3;
	private int FiNum=2;
	private int PNum=1;
	private Dimension pnlSize;
	private boolean shiftKey;


	private ArrayList <Food> FoList;
	private ArrayList<Fish> FiList;
	private ArrayList<Predator>PList;
	private float sc;
	private int count=0;

	public class MyKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SHIFT && e.getKeyCode() == KeyEvent.VK_D)
				for(int i=0;i<FiList.size();i++) {
					Fish currFish= FiList.get(i);
					currFish.select();
				}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SHIFT && e.getKeyCode() == KeyEvent.VK_D)
				fish.deselect();	
		}
	}

	public AnimalPanel(Dimension initialSize) {
		super();
		this.pnlSize = initialSize;
		

		//--------------------------------------------------> FOOD CREATION	
		this.FoList= new ArrayList<>();
		while (FoList.size() < FoNum) {
			FoList.add(new Food((int)Util.random(100, 700),(int)Util.random(100, 500),200));
		}
		//--------------------------------------------------> FISH CREATION	
		// Fish(float x, float  y, float w, float h, int size , float sc ) 
		this.FiList= new ArrayList<>();
		for (int i=0;i<FiNum;i++) {
			sc=(float)Util.random(0.2,0.3);
			FiList.add(generateNewFish()); //scale
		}
		//--------------------------------------------------> PREDATOR CREATION	
		this.PList= new ArrayList<>();
		//  Predator(float x, float  y, float w, float h, int size , float sc ) 
		for (int i=0;i<PNum;i++) {
			sc=(float)Util.random(0.4,0.5);
			PList.add(generateNewPred());
		}
		
		addMouseListener(new MyMouseAdapter());

		t = new Timer(16, this);
		t.start();
		
		MyKeyListener mkl = new MyKeyListener();
		this.addKeyListener(mkl);
		this.setFocusable(true);
	}


	private Fish generateNewFish() {
		// TODO Auto-generated method stub
		return new Fish((float)Util.random(100, pnlSize.width-100), //random x coord
				(float)Util.random(100, pnlSize.height-100) //random y coord
				,100.0f, // width
				180.0f, // height
				20, //size
				sc);
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

		for (Food food : FoList) {
			food.draw(g2);
		}
		for (Fish f:FiList) 
			f.draw(g2);
		for (Predator p:PList) 
			p.draw(g2);			
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub		
		for (int p=0;p<PList.size();p++) {
			Predator currPredator=PList.get(p);
			currPredator.checkCollision(this.getSize()); //------------Detect Wall Predator
			currPredator.move();
			currPredator.decreaseEnergy();
			
			if(currPredator.currHealth()<0 ) {
				PList.remove(currPredator);
				sc=(float)Util.random(0.4,0.5);
				PList.add(generateNewPred());
			}
		
			for (int k=0;k<FiList.size();k++) {
				//		for (Fish currFish:FiList ) {
				Fish currFish=FiList.get(k);
				currFish.checkCollision(this.getSize()); //------------Detect Wall Fish
				currFish.move();
				
				currPredator.traceClosestFish(FiList);
				for (int m=0;m< PList.size();m++) {
					if(currPredator!=PList.get(m) && currPredator.detectCollision(PList.get(m)))					{
						if (currPredator.getSize() < PList.get(m).getSize()) { 
							currPredator.distractedBy(PList.get(m));	
						}
						else 
							PList.get(m).distractedBy(currPredator);
					}	
				}
				if(sc>0.4)							
					currFish.energyDecreaseWithSize();	//------------Decrease Health by 0.07
				currFish.energyDecrease();				//------------Decrease Health by 0.05
				

				if(currFish.inViewOfPredator(currPredator)) {		//------------Fish Escapes Predator
					System.out.println("Fish : I need to Run");
					currFish.distractedBy(currPredator);	
				}
				
				if(currFish.energy()<0) {
					FiList.remove(currFish);
					sc=(float)Util.random(0.2,0.3);
					FiList.add(generateNewFish());
				}
				else {
					currFish.traceClosestFood(FoList);
					
					if(currFish.energy()<3) currFish.speedInHalf();
					for (int i=0;i< FiList.size();i++) {
						if(currFish!=FiList.get(i) && currFish.detectCollision(FiList.get(i)))					{
							if (currFish.getSize() < FiList.get(i).getSize()) { 
								currFish.distractedBy(FiList.get(i));	
							}
							else 
								FiList.get(i).distractedBy(currFish);
						}	
					}
					
					
					if (currPredator.detectCollision(FiList.get(k))) {			//--Predator detects collision with 
						//--Fish
						FiList.remove(k);	
						sc=(float)Util.random(0.2,0.3);
						FiList.add(generateNewFish());
						float val= FiList.get(k).getSize()*10;
						currPredator.increaseEnergy(val);
							
						}
					
					for (int i = 0; i < FoList.size(); i++) {
						if (currFish.detectCollision(FoList.get(i))) {
							FoList.remove(i);
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
			FoList.add(new Food((int)mousePosition.x,(int)mousePosition.y,100));
			for (int i=0;i<FoList.size();i++) {
				Food fishFood=FoList.get(i);
				if (fishFood.checkMouseHit(mousePosition) && isControlDown) {
					FoList.remove(i);
				}
				else if (fishFood.checkMouseHit(mousePosition)) {
					fishFood.enlarge();
				}
			} 
			
		}
		while (FoList.size() < FoNum) 
			FoList.add(new Food((int)Util.random(100, 700),(int)Util.random(100, 500),190));
		
		
			
		int foSize = FoList.size();

		repaint();									// method of JPanel
	}



	private Predator generateNewPred() {
		return new Predator((int)Util.random(100, pnlSize.width-100),
				(float)Util.random(100, pnlSize.height-100),
				100.0f, // width
				180.0f, // height
				2, //size
				sc);
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
			for(int i=0; i<FiList.size();i++) {
				Fish currFish=FiList.get(i);
				if(currFish.checkMouseHit(e) && e.isShiftDown())
					currFish.select();
				else if(e.isShiftDown())
					currFish.deselect();
			}
			for(int i=0; i<PList.size();i++) {
				Predator currPredator=PList.get(i);
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




