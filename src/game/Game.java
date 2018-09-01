package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Iterator;

public class Game implements Runnable {

	private final int UPS = 30; // Updates Per Second (game logic)
	private final int BLANK_TIME = 1000 / UPS; // Time to wait between updates
	
	private final Dimension SIZE;
	private final int SCALE;
	private final ArrayDeque<KeyEvent> INPUTS;
	
	private Thread thread;
	
	// Game elements
	private Player player;
	
	public Game(Dimension size, int scale) {
		SCALE = scale;
		SIZE = size;
		INPUTS = new ArrayDeque<KeyEvent>();
		
		player = new Player(SIZE.width / 2, SIZE.height / 2, 5);
	}
	
	public synchronized void start() {
		thread = new Thread(this);
		thread.start(); // calls this.run();
	}
	
	@Override
	public void run() {
		boolean running = true;
		long nextUpdate = System.currentTimeMillis();
		
		while (running) {
			// handle UPS
			while (System.currentTimeMillis() > nextUpdate) {
				update();
				nextUpdate = System.currentTimeMillis() + BLANK_TIME;
			}
			// render handled in rendering thread
		}
	}
	
	private void update() {
		handleInputs();
		synchronized (player) {player.rotate(); }
	}
	
	public void draw(Graphics2D g) {
		g.setColor(new Color(50, 50, 50));
		g.fillRect(0, 0, SIZE.width * SCALE, SIZE.height * SCALE);
		
		synchronized (player) {
			
			g.setColor(player.color);
			g.fillPolygon(player.getDrawable(SCALE));
		}
	}
	
	public void addInput(KeyEvent e) {
		synchronized (INPUTS) {
			INPUTS.addLast(e);
		}
	}
	
	public synchronized void handleInputs() {
		for (Iterator<KeyEvent> iterator = INPUTS.iterator(); iterator.hasNext();) {
			KeyEvent e = iterator.next();
			switch (e.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				player.setRotationSpeed(0.1f);
				break;
			case KeyEvent.VK_LEFT:
				player.setRotationSpeed(-0.1f);
				break;
			}
			iterator.remove();
		}
	}

}
