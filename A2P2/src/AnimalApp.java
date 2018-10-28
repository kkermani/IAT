import javax.swing.JFrame;

public class AnimalApp extends JFrame {

	/**
	 * 
	 */
	
	public static final int PANEL_WIDTH = 800;
	public static final int PANEL_HEIGHT = 600;
	
	private static final long serialVersionUID = 1L;

	public AnimalApp(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(PANEL_WIDTH, PANEL_HEIGHT);
		AnimalPanel panel = new AnimalPanel(this.getSize());
		this.add(panel);
		this.setVisible(true);
		
	}

	public static void main(String[] args) {
		AnimalApp app = new AnimalApp("The fish tank");
	}

}
