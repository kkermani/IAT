import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

public class AnimalPanel extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<SimulationObject> objList;
	private Timer t;
	private Dimension pnlSize;
	private int MAX_FOOD  = 3;
	private static String status = "Status";
	
	public AnimalPanel(Dimension initialSize) {
		super();
		this.pnlSize = initialSize;
		
		// create new animals
		this.objList = new ArrayList<>();
		this.objList.add(new Fish(pnlSize.width/2, pnlSize.height/2, .25f));
		this.objList.add(new Predator(pnlSize.width/2, pnlSize.height/2, .3f));
		for (int i = 0; i < MAX_FOOD; i++) objList.add(Util.randomFood(pnlSize));
		
		t = new Timer(33, this);
		t.start();
	}
	
	private void drawStatusBar(Graphics2D g) {
		Font font = new Font("Arial", Font.BOLD, 12);
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics(font);
		
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, pnlSize.height -metrics.getHeight()*2, pnlSize.width, metrics.getHeight()*2);
		
		g.setColor(Color.BLACK);
		g.drawString(status, 24, pnlSize.height - (int)(metrics.getHeight()*.75));
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		pnlSize = getSize();
		setBackground(Color.darkGray);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		//draw objects
		for (SimulationObject obj:objList ) obj.draw(g2);	
		drawStatusBar(g2);
	}
	
	public static void setStatus(String st) {
		status = st;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < objList.size(); i++) objList.get(i).update(objList);	
		if (Util.countFood(objList) < MAX_FOOD) objList.add(Util.randomFood(pnlSize));
		repaint();
	}
	
}
