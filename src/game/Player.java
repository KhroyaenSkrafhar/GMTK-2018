package game;

import java.awt.Color;
import java.awt.Polygon;

public class Player {

	public final Color color = new Color(200, 200, 200);
	private float x, y, acceleration, speed, radius;
	
	private float rotationSpeed;
	
	private float[][] vertices;
	
	public Player (int x, int y, int radius) {
		setAcceleration(0);
		setSpeed(0);
		setRadius(radius);
		
		this.vertices = new float[][] {
			{5f, 0f},
			{-5f, -5f},
			{-5f, 5f}
		};
		
		this.setX(x);
		this.setY(y);
	}
	
	public void rotate() {
		for (float[] vertex : vertices) {
			float x2 = vertex[0] * (float) Math.cos(rotationSpeed) - vertex[1] * (float) Math.sin(rotationSpeed);
			float y2 = vertex[0] * (float) Math.sin(rotationSpeed) + vertex[1] * (float) Math.cos(rotationSpeed);
			vertex[0] = x2;
			vertex[1] = y2;
		}
	}

	public float getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(float acceleration) {
		this.acceleration = acceleration;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float[][] getVertices() {
		return vertices;
	}
	
	public Polygon getDrawable(int scale) {
		int[] xArray = new int[vertices.length];
		int[] yArray = new int[vertices.length];
		
		for (int i = 0; i < vertices.length; i++) {
			xArray[i] = (int) ((vertices[i][0] + x) * scale);
			yArray[i] = (int) ((vertices[i][1] + y) * scale);
		}
		
		Polygon drawable = new Polygon(xArray, yArray, vertices.length);
		return drawable;
	}

	public void setVertices(float[][] vertices) {
		this.vertices = vertices;
	}

	public float getRotationSpeed() {
		return rotationSpeed;
	}

	public void setRotationSpeed(float rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}
}
