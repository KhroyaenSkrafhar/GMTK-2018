import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import javax.swing.event.*;

import game.Game;
import rendering.RenderingPanel;

public class Application {

	public static void main(String[] args) {
		
		JFrame window = new JFrame();
		window.setTitle("GMTK Game Jam 2018");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setExtendedState(JFrame.MAXIMIZED_BOTH);
		window.setUndecorated(true);
		window.setAlwaysOnTop(false);
		
		int scale = 5;
		Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension gameSize = new Dimension(windowSize.width / scale+5, windowSize.height / scale+5);
		
		Game game = new Game(gameSize, scale);
		RenderingPanel panel = new RenderingPanel(windowSize, game);
		
		
		window.setContentPane(panel);
		//window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		game.start();
	}

}
