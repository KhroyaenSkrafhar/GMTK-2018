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
	private boolean exit = false;
	private boolean pause = false;
	
	private long startTime;
	private long currentTime;
	private int score;
	
	
	public Game(Dimension size, int scale) {
		SCALE = scale;
		SIZE = size;
		INPUTS = new ArrayDeque<KeyEvent>();
		Init();
	}
	
	public void Init() {
		player = new Player(SIZE.width / 2, SIZE.height / 2);
		foes = new ArrayList<Foe>();
		foes.add(new Foe(100f, 100f, 20f));
		asteroids = new ArrayList<Asteroid>();
		
		Date date= new Date();
		startTime = date. getTime();
		currentTime = 0;
		
		score = 0;
		
		endGame = false;
		exit = false;
		pause = false;
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
			if(exit) running = false;
		}	
		System.exit(0);
	}
	
	private void update() {
		handleInputs();
		if (!pause && !endGame) {
			synchronized (player) {
	//			System.out.println(player.getAccelerationX()+" ; "+player.getAccelerationY()+" ; "+player.getSpeedX()+" ; "+player.getSpeedY()
	//			+" || "+player.getX()+" ; "+player.getY());
				player.update();
			}
			synchronized (asteroids) {
				ArrayList<Asteroid> toRemove = new ArrayList();
				int range = 5+(int) Math.floor(currentTime/2000);
				//int range = 2;
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
	}
	
	public void draw(Graphics2D g) {
		g.setColor(new Color(50, 50, 50));
		g.fillRect(0, 0, SIZE.width * SCALE, SIZE.height * SCALE);
		
		synchronized (player) {
			g.setColor(player.color);
			g.fillPolygon(player.getDrawable(SCALE));
		}
		synchronized(asteroids) {
			for (Asteroid asteroid : asteroids) {
				g.setColor(asteroid.color);
				//g.fillArc((int) (asteroid.getX() - asteroid.getRadius()), (int) (asteroid.getY() - asteroid.getRadius()),
				//		(int) asteroid.getRadius() * 2, (int) asteroid.getRadius() * 2,
				//		0, 360);
				g.fillPolygon(asteroid.getDrawable());
			}
		}
		
		g.setFont(new Font("TimesRoman", Font.BOLD, 30));
		
		g.setColor(new Color(200, 200, 200));
		g.fillRoundRect(SIZE.width*SCALE-120, 20, 80, 40, 20, 20);
		String min = String.valueOf((int) Math.floor(currentTime/60000));
		String sec = String.valueOf((int) Math.floor(currentTime/1000)%60);
		if (min.length()==1) min = "0"+min;
		if (sec.length()==1) sec = "0"+sec;
		g.drawString("Time :", SIZE.width*SCALE-220, 50);
		g.setColor(new Color(50, 50, 50));
		g.drawString(min+":"+sec, SIZE.width*SCALE-115, 50);

		g.setColor(new Color(200, 200, 200));
		g.fillRoundRect(SIZE.width*SCALE-120, 90, 80, 40, 20, 20);
		String score = String.valueOf(this.score);
		while(score.length()<4)score = "0"+score;
		g.drawString("Score :", SIZE.width*SCALE-220, 120);
		g.setColor(new Color(50, 50, 50));
		g.drawString(score, SIZE.width*SCALE-110, 120);
		
		if(endGame) {
			g.setColor(new Color(50, 50, 50, 127));
			g.fillRect(0, 0, SIZE.width * SCALE, SIZE.height * SCALE);
			
			g.setFont(new Font("TimesRoman", Font.BOLD, 55));
			g.setColor(new Color(200, 200, 200));
			g.fillRoundRect(SIZE.width*SCALE/2-200, SIZE.height*SCALE/2-150, 400, 200, 50, 50);
			
			g.setColor(new Color(50, 50, 50));
			g.drawString("GAME OVER", SIZE.width*SCALE/2-175, SIZE.height*SCALE/2-70);
			
			g.setFont(new Font("TimesRoman", Font.BOLD, 55));
			g.drawString("Score : "+score, SIZE.width*SCALE/2-145, SIZE.height*SCALE/2+10);
			
			g.setFont(new Font("TimesRoman", Font.BOLD, 30));
			g.setColor(new Color(200, 200, 200, 127));
			g.drawString("Press ENTER to play again", SIZE.width*SCALE/2-175, SIZE.height*SCALE/2+100);
			
			g.setFont(new Font("TimesRoman", Font.BOLD, 30));
			g.setColor(new Color(200, 200, 200, 127));
			g.drawString("Press ESCAPE to quit", SIZE.width*SCALE/2-145, SIZE.height*SCALE/2+150);
		}
		
		if (pause) {
			g.setColor(new Color(50, 50, 50, 127));
			g.fillRect(0, 0, SIZE.width * SCALE, SIZE.height * SCALE);
			
			g.setFont(new Font("TimesRoman", Font.BOLD, 55));
			g.setColor(new Color(200, 200, 200, 127));
			g.drawString("PAUSE", SIZE.width*SCALE/2-90, SIZE.height*SCALE/2-30);
			
			g.setFont(new Font("TimesRoman", Font.BOLD, 30));
			g.setColor(new Color(200, 200, 200, 127));
			g.drawString("Press ESCAPE to quit ...", SIZE.width*SCALE/2-150, SIZE.height*SCALE/2+70);
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
			case KeyEvent.VK_SPACE:
				if (e.getID() == e.KEY_PRESSED && !endGame) pause = !pause;
				break;
			case KeyEvent.VK_ESCAPE:
				if (e.getID() == e.KEY_PRESSED && (pause || endGame)) exit = true;
				break;
			case KeyEvent.VK_ENTER:
				if (e.getID() == e.KEY_PRESSED && endGame) Init();
			}
			
			
			iterator.remove();
		}
	}
	
	public void collision() {
		Rectangle playerBox = player.getDrawable(SCALE).getBounds();
		for (Asteroid asteroid : asteroids) {
			Rectangle asteroidBox = asteroid.getHitBox();
			if (collide(asteroid.getAbsoluteVertices(), player.getAbsoluteVertices(SCALE))) {
					    endGame = true;
			}
		}
	}
	
	public float crossProduct(float[] AB, float[] AC) {
		return AB[0] * AC[1] - AB[1] * AC[0];
	}
	
	public boolean sameSide(float[] p1, float[] p2, float[] a, float[] b) {
		float cp1 = crossProduct(getEdgeOf(a, b), getEdgeOf(a, p1));
		float cp2 = crossProduct(getEdgeOf(a, b), getEdgeOf(a, p2));
		
		return (cp1 * cp2 >= 0);
	}
	
	public float[] getEdgeOf(float[] A, float[] B) {
		return new float[] {
				B[0] - A[0],
				B[1] - A[1]
		};
	}
	
	public boolean collide(float[][] A, float[][] B) {
		for (int i = 0; i < 3; i++) {
			if (!sameSide(A[i], B[0], A[(i + 1) % 3], A[(i + 2) % 3])
					&& sameSide(B[0], B[1], A[(i + 1) % 3], A[(i + 2) % 3])
					&& sameSide(B[1], B[2], A[(i + 1) % 3], A[(i + 2) % 3]))
				return false;
		}
		for (int i = 0; i < 3; i++) {
			if (!sameSide(B[i], A[0], B[(i + 1) % 3], B[(i + 2) % 3])
					&& sameSide(A[0], A[1], B[(i + 1) % 3], B[(i + 2) % 3])
					&& sameSide(A[1], A[2], B[(i + 1) % 3], B[(i + 2) % 3]))
				return false;
		}
		return true;
	}
	
}
