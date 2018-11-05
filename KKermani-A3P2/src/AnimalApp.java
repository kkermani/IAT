import javax.swing.JFrame;

public class AnimalApp extends JFrame {

	public AnimalApp(String title) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800,600);
		
		//instantiating our BallPanel
		AnimalPanel panel = new AnimalPanel(this.getSize());
		
		//adding it to the current frame
		this.add(panel);
		
		//displaying the frame
		this.setVisible(true);
	}

	public static void main(String[] args) {
		AnimalApp app = new AnimalApp("Kian Kermani's SeaWorld");
	}

}
