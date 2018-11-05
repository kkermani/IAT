import javax.swing.JFrame;



public class AnimalApp extends JFrame {

	private static final long serialVersionUID = 6457792220456140992L;

	public AnimalApp(String title) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1060,600);
		
		//instantiating our BallPanel
		AnimalPanel panel = new AnimalPanel(this.getSize());
		
		//adding it to the current frame
		this.add(panel);
		
		//displaying the frame
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new AnimalApp("My Interactive Pacman App");
	}

}
