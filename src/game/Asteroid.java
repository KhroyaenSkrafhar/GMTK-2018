package game;

import java.awt.Color;
import java.awt.Polygon;

import java.awt.Rectangle;

public class Asteroid {
	
	public final Color color = new Color(200, 200, 200);
	private final float maxSpeed = 2f;
	private final int pointNumber = 8;
	
	private boolean alive;
	private float speedX, speedY, x, y, angle, radius;
	private float[][] vertices = new float[pointNumber][2];
	
	
	public Asteroid (int width, int height) {

		angle = (float) Math.random()* (float) Math.PI *2;
		
		double random = Math.random()*4;
		if (random < 1) {
			this.setX(-5);
			this.setY((float) Math.random()*height);
			speedX = (float) (Math.random()*maxSpeed);
			speedY = (float) (Math.random()*2*maxSpeed)-maxSpeed;
		} else if (random < 2) {
			this.setX((float) Math.random()*width);
			this.setY(-5);			
			speedX = (float) (Math.random()*2*maxSpeed)-maxSpeed;
			speedY = (float) (Math.random()*maxSpeed);
		} else if (random < 3) {
			this.setX(width+5);
			this.setY((float) Math.random()*height);
			speedX = (float) -(Math.random()*maxSpeed);
			speedY = (float) (Math.random()*2*maxSpeed)-maxSpeed;
		} else {
			this.setX((float) Math.random()*width);
			this.setY(height+5);
			speedX = (float) (Math.random()*2*maxSpeed)-maxSpeed;
			speedY = (float) -(Math.random()*maxSpeed);
		}
		setRadius((float) Math.random() * 10 + 10);
		
		for(int i = 0; i<pointNumber; i++) {
			if(i<=pointNumber/4) {
				vertices[i][0] = (radius-10)+ (float) Math.random()*10;
				vertices[i][1] = (radius-10)+ (float) Math.random()*10;			
			} else if(i<=pointNumber/2){
				vertices[i][0] = -1*((radius-10)+ (float) Math.random()*10);
				vertices[i][1] = (radius-10)+ (float) Math.random()*10;				
			} else if(i<=(pointNumber*3)/10) {
				vertices[i][0] = -1*((radius-10)+ (float) Math.random()*10);
				vertices[i][1] = -1*((radius-10)+ (float) Math.random()*10);	
			} else {
				vertices[i][0] = (radius-10)+ (float) Math.random()*10;
				vertices[i][1] = -1*((radius-10)+ (float) Math.random()*10);		
			}
		}	
	}
	
	public void move(long currentTime) {		
		setX(getX() + speedX*(1+0.1f*currentTime/1000));
		setY(getY() + speedY*(1+0.1f*currentTime/1000));
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	public Polygon getDrawable() {
		int[] tempX = new int[pointNumber];
		int[] tempY = new int[pointNumber];
		
		for (int i = 0; i < pointNumber; i++) {
			tempX[i] = (int) ((vertices[i][0] + x));
			tempY[i] = (int) ((vertices[i][1] + y));
		}
		
		System.out.println(tempX[0]+" | "+tempX[1]+" | "+tempX[2]);
		
		Polygon drawable = new Polygon(tempX, tempY, pointNumber);
		
		return drawable;
	}
	
	public Rectangle getHitBox() {
		return new Rectangle((int) Math.floor(-radius) + (int) x+2, (int) Math.floor(-radius) + (int) y+2, (int) Math.floor(radius+radius)-4, (int) Math.floor(radius+radius)-4);
	}
	
	public boolean outSide(int width, int height) {
		boolean outside = false;
		if (x<-40 || x>(width+40) || y<-40 || y>(height+40)) outside = true;
		return outside;
	}
	
}
