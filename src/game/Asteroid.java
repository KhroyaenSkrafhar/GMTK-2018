package game;

import java.awt.Color;
import java.awt.Polygon;

public class Asteroid {
	
	public final Color color = new Color(200, 200, 200);
	private final float maxSpeed = 1f;
	
	private boolean alive;
	private float speedX, speedY, x, y, angle, radius;
	
	public Asteroid (float x, float y) {
		this.setX(x);
		this.setY(y);
		speedX = (float) (Math.random()*2*maxSpeed)-maxSpeed;
		speedY = (float) (Math.random()*2*maxSpeed)-maxSpeed;
		angle = (float) Math.random()* (float) Math.PI *2;
		setRadius((float) Math.random() * 10 + 10);
	}
	
	public void move() {		
		setX(getX() + speedX);
		setY(getY() + speedY);
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
}
