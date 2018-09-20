import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JarGUI {

	private final int WIDTH = 200, HEIGHT = 100;
	private JFrame frame = new JFrame("Mega Charizard X Discord Bot");
	private JLabel label = new JLabel("Mega Charizard Running..");
	private JPanel panel = new JPanel();
	
	public JarGUI() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	}
	
	public void setup() {
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.setBackground(Color.WHITE);
		
		panel.add(label);
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
