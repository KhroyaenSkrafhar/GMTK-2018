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
	
	public Game(Dimension size, int scale) {
		SCALE = scale;
		SIZE = size;
		INPUTS = new ArrayDeque<KeyEvent>();
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
		
	}
	
	public void draw(Graphics2D g) {
		g.setColor(new Color(50, 50, 50));
		g.fillRect(0, 0, SIZE.width * SCALE, SIZE.height * SCALE);
	}
	
	public void addInput(KeyEvent e) {
		synchronized (INPUTS) {
			INPUTS.addLast(e);
		}
	}
	
	public void handleInputs() {
		for (Iterator<KeyEvent> iterator = INPUTS.iterator(); iterator.hasNext();) {
			KeyEvent e = iterator.next();
			switch (e.getKeyCode()) {
			default:
				break;
			}
			iterator.remove();
		}
	}

}
