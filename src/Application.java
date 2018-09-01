import java.awt.Dimension;

import javax.swing.JFrame;

import game.Game;
import rendering.RenderingPanel;

public class Application {

	public static void main(String[] args) {
		
		int scale = 10;
		Dimension gameSize = new Dimension(50, 50);
		Dimension windowSize = new Dimension(gameSize.width * scale, gameSize.height * scale);
		
		Game game = new Game(gameSize, scale);
		RenderingPanel panel = new RenderingPanel(windowSize, game);
		
		JFrame window = new JFrame();
		window.setTitle("GMTK Game Jam 2018");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setContentPane(panel);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		game.start();
	}

}
