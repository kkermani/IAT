import javax.swing.JFrame;

public class FishApp extends JFrame {

	public FishApp(String title) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(800,600);
		
		//instantiating our BallPanel
		FishPanel panel = new FishPanel(this.getSize());
		
		//adding it to the current frame
		this.add(panel);
		
		//displaying the frame
		this.setVisible(true);
	}

	public static void main(String[] args) {
		FishApp app = new FishApp("My Aquarium App");
	}

}
