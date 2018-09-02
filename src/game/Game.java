package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

import java.awt.Toolkit;

public class Game implements Runnable {

	private final int UPS = 30; // Updates Per Second (game logic)
	private final int BLANK_TIME = 1000 / UPS; // Time to wait between updates
	
	private final Dimension SIZE;
	private final int SCALE;
	private final ArrayDeque<KeyEvent> INPUTS;
	
	private Thread thread;
	
	// Game elements
	private Player player;
	private ArrayList<Foe> foes;
	private ArrayList<Asteroid> asteroids;
	private boolean endGame = false;
	
	public Game(Dimension size, int scale) {
		SCALE = scale;
		SIZE = size;
		INPUTS = new ArrayDeque<KeyEvent>();
		
		player = new Player(SIZE.width / 2, SIZE.height / 2);
		foes = new ArrayList<Foe>();
		foes.add(new Foe(100f, 100f, 20f));
		asteroids = new ArrayList<Asteroid>();
		
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
				if(!endGame) update();
				nextUpdate = System.currentTimeMillis() + BLANK_TIME;
			}
			// render handled in rendering thread
		}
	}
	
	private void update() {
		handleInputs();
		
		synchronized (player) {
//			System.out.println(player.getAccelerationX()+" ; "+player.getAccelerationY()+" ; "+player.getSpeedX()+" ; "+player.getSpeedY()
//			+" || "+player.getX()+" ; "+player.getY());
			player.update();
		}
		synchronized (foes) {
			for (Foe foe : foes) {
				foe.update();
			}
		}
		synchronized (asteroids) {
			if ( Math.random()*100 <5) {
				asteroids.add(new Asteroid(SIZE.width*SCALE,SIZE.height*SCALE));
			}
			for (Asteroid asteroid : asteroids) {
				asteroid.move();
			}
		}
		collision();
	}
	
	public void draw(Graphics2D g) {
		g.setColor(new Color(50, 50, 50));
		g.fillRect(0, 0, SIZE.width * SCALE, SIZE.height * SCALE);
		
		synchronized (player) {
			g.setColor(player.color);
			g.fillPolygon(player.getDrawable(SCALE));
		}
		synchronized(foes) {
			for (Foe foe : foes) {
				g.setColor(Color.getHSBColor(foe.getAge(), 0.8f, 0.8f));
				g.fillArc((int) (foe.getX() - foe.getRadius()), (int) (foe.getY() - foe.getRadius()),
						(int) foe.getRadius() * 2, (int) foe.getRadius() * 2,
						0, 360);
			}
		}
		synchronized(asteroids) {
			for (Asteroid asteroid : asteroids) {
				g.setColor(asteroid.color);
				g.fillArc((int) (asteroid.getX() - asteroid.getRadius()), (int) (asteroid.getY() - asteroid.getRadius()),
						(int) asteroid.getRadius() * 2, (int) asteroid.getRadius() * 2,
						0, 360);
				g.fill(asteroid.getHitBox());
			}
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
				player.setState(InputKey.RIGHT.getValue(), (e.getID() == e.KEY_PRESSED));
				break;
			case KeyEvent.VK_LEFT:
				player.setState(InputKey.LEFT.getValue(), (e.getID() == e.KEY_PRESSED));
				break;
			case KeyEvent.VK_UP:
				player.setState(InputKey.UP.getValue(), (e.getID() == e.KEY_PRESSED));
				break;
			case KeyEvent.VK_DOWN:
				player.setState(InputKey.DOWN.getValue(), (e.getID() == e.KEY_PRESSED));
				break;
			}
			
			iterator.remove();
		}
	}
	
	public void collision() {
		Rectangle playerBox = player.getDrawable(SCALE).getBounds();
		for (Asteroid asteroid : asteroids) {
			Rectangle asteroidBox = asteroid.getHitBox();
			if (playerBox.x < asteroidBox.x + asteroidBox.width &&
					playerBox.x + playerBox.width > asteroidBox.x &&
					playerBox.y < asteroidBox.y + asteroidBox.height &&
					playerBox.height + playerBox.y > asteroidBox.y) {
					    endGame = true;
		}
		}
	}
}
