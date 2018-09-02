package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java. util. Date;
import java. sql. Timestamp;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

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
	private long startTime;
	private long currentTime;
	private int score;
	
	public Game(Dimension size, int scale) {
		SCALE = scale;
		SIZE = size;
		INPUTS = new ArrayDeque<KeyEvent>();
		
		player = new Player(SIZE.width / 2, SIZE.height / 2);
		foes = new ArrayList<Foe>();
		foes.add(new Foe(100f, 100f, 20f));
		asteroids = new ArrayList<Asteroid>();
		
		Date date= new Date();
		startTime = date. getTime();
		currentTime = 0;
		
		score = 0;
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
			ArrayList<Asteroid> toRemove = new ArrayList();
			int range = 5+(int) Math.floor(currentTime/2000);
			
			if ( Math.random()*100 <range) {
				asteroids.add(new Asteroid(SIZE.width*SCALE,SIZE.height*SCALE));
			}
			for (Asteroid asteroid : asteroids) {
				asteroid.move(currentTime);
				if(asteroid.outSide(SIZE.width*SCALE, SIZE.height*SCALE)) {
					toRemove.add(asteroid);
				}
			}
			
			for (Asteroid asteroid : toRemove) {
				asteroids.remove(asteroid);
				score += 1;
			}
		}
		
		Date date= new Date();
		currentTime = date. getTime() - startTime;	
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
			}
		}
		
		g.setFont(new Font("TimesRoman", Font.BOLD, 30));
		
		g.setColor(new Color(200, 200, 200));
		g.fillRoundRect(SIZE.width*SCALE-100, 20, 80, 40, 20, 20);
		String min = String.valueOf((int) Math.floor(currentTime/60000));
		String sec = String.valueOf((int) Math.floor(currentTime/1000)%60);
		if (min.length()==1) min = "0"+min;
		if (sec.length()==1) sec = "0"+sec;
		g.drawString("Time :", SIZE.width*SCALE-200, 50);
		g.setColor(new Color(50, 50, 50));
		g.drawString(min+":"+sec, SIZE.width*SCALE-95, 50);

		g.setColor(new Color(200, 200, 200));
		g.fillRoundRect(SIZE.width*SCALE-100, 90, 80, 40, 20, 20);
		String score = String.valueOf(this.score);
		while(score.length()<4)score = "0"+score;
		g.drawString("Score :", SIZE.width*SCALE-200, 120);
		g.setColor(new Color(50, 50, 50));
		g.drawString(score, SIZE.width*SCALE-90, 120);
		
		if(endGame) {
			g.setFont(new Font("TimesRoman", Font.BOLD, 55));
			g.setColor(new Color(200, 200, 200));
			g.fillRoundRect(SIZE.width*SCALE/2-200, SIZE.height*SCALE/2-100, 400, 200, 50, 50);
			g.setColor(new Color(50, 50, 50));
			g.drawString("GAME OVER", SIZE.width*SCALE/2-175, SIZE.height*SCALE/2-20);
			g.setFont(new Font("TimesRoman", Font.BOLD, 55));
			g.drawString("Score : "+score, SIZE.width*SCALE/2-145, SIZE.height*SCALE/2+60);
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
