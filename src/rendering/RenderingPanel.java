package rendering;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import game.Game;

public class RenderingPanel extends JPanel implements KeyListener {
	
	private static final long serialVersionUID = 1L;
	
	private final Dimension SIZE;
	private final Game GAME;
	
	public RenderingPanel(Dimension size, Game game) {
		super();
		SIZE = size;
		GAME = game;
		this.setPreferredSize(SIZE);
		this.setFocusable(true);
		this.addKeyListener(this);
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		GAME.draw(g2D);
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		GAME.addInput(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		GAME.addInput(e);
	}

}
