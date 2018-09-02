package game;

import java.awt.Color;
import java.awt.Polygon;

public class Player {

	public final Color color = new Color(200, 200, 200);
	private float x, y, accelerationX, accelerationY, speedX, speedY, angle;
	
	private final float maxSpeed = 1f;
	private final float maxAcceleration = 1f;
	
	private float rotationSpeed;
	
	private final boolean[] state = new boolean[4];
	
	private float[][] vertices;
	
	public Player (int x, int y) {
		setAngle(0);
		setAccelerationX(0);
		setAccelerationY(0);
		setSpeedX(0);
		setSpeedY(0);
		
		this.vertices = new float[][] {
			{5f, 0f},
			{-5f, -5f},
			{-5f, 5f}
		};
		
		this.setX(x);
		this.setY(y);
	}
	
	public void update() {
		if (state[InputKey.RIGHT.getValue()]) rotate(0.1f);
		if (state[InputKey.LEFT.getValue()]) rotate(-0.1f);
		if (state[InputKey.UP.getValue()]) accelerate(0.12f);
		if (state[InputKey.DOWN.getValue()]) slow(0.3f);
		slow(0.02f);
		move();
	}
	
	public void rotate(float rotationSpeed) {
		
		angle += rotationSpeed;
		
		for (float[] vertex : vertices) {
			float x2 = vertex[0] * (float) Math.cos(rotationSpeed) - vertex[1] * (float) Math.sin(rotationSpeed);
			float y2 = vertex[0] * (float) Math.sin(rotationSpeed) + vertex[1] * (float) Math.cos(rotationSpeed);
			vertex[0] = x2;
			vertex[1] = y2;
		}
	}
	
	public void accelerate(float acceleration) {
		
		float newAccelerationX = acceleration * (float) Math.cos(angle) + accelerationX;
		float newAccelerationY = acceleration *(float) Math.sin(angle) + accelerationY;
		float length = (float) Math.sqrt(newAccelerationX * newAccelerationX + newAccelerationY * newAccelerationY);
		if (length < maxAcceleration) {
			accelerationX += acceleration * (float) Math.cos(angle);
			accelerationY += acceleration * (float) Math.sin(angle);
		}
		
		
//		if (accelerationX > maxAcceleration) accelerationX = maxAcceleration;
//		if (accelerationY > maxAcceleration) accelerationY = maxAcceleration;
//
//		if (accelerationX < -maxAcceleration) accelerationX = -maxAcceleration;
//		if (accelerationY < -maxAcceleration) accelerationY = -maxAcceleration;
		
//		float newSpeedX = speedX + accelerationX;
//		float newSpeedY = speedY + accelerationY;
//		
//		length = (float) Math.sqrt(newSpeedX * newSpeedX + newSpeedY * newSpeedY);
//		if (length < maxSpeed) {
//			speedX += accelerationX;
//			speedY += accelerationY;
//		}
		
		if (speedX < accelerationX) {
			speedX += 0.2f;
			if(speedX >= accelerationX) speedX = accelerationX;
		}
		else {
			speedX -= 0.2f;
			if(speedX <= accelerationX) speedX = accelerationX;		
		}
		
		if (speedY < accelerationY) {
			speedY += 0.2f;
			if(speedY >= accelerationY) speedY = accelerationY;
		}
		else {
			speedY -= 0.2f;
			if(speedY <= accelerationY) speedY = accelerationY;		
		}

//		float length = (float) Math.sqrt(speedX * speedX + speedY * speedY);
//		if (length > maxSpeed) {
//			if (speedX < 0) speedX = -maxSpeed;
//			else if (speedX > 0) speedX = maxSpeed;
//			
//			if (speedY < 0) speedY = -maxSpeed;
//			else if (speedY > 0) speedY = maxSpeed;			
//		}
	}
	
	public void slow(float deceleration) {
		
		float decSpeedX, decSpeedY, decAccelX, decAccelY;

		float absSpeedX = Math.abs(speedX);
		float absSpeedY = Math.abs(speedY);
		float absAccelX = Math.abs(accelerationX);
		float absAccelY = Math.abs(accelerationY);
		
		if(absSpeedX > absSpeedY) {
			decSpeedX = deceleration;
			decSpeedY = (absSpeedY/absSpeedX)*deceleration;
		} else {
			decSpeedX = (absSpeedX/absSpeedY)*deceleration;
			decSpeedY = deceleration;			
		}
		
		if(absAccelX > absAccelY) {
			decAccelX = deceleration;
			decAccelY = (absAccelY/absAccelX)*deceleration;
		} else {
			decAccelX = (absAccelX/absAccelY)*deceleration;
			decAccelY = deceleration;
		}		
		
		if (speedX > -decSpeedX && speedX < decSpeedX) speedX = 0;
		else {
			if (speedX > 0) speedX -= decSpeedX;
			if (speedX < 0) speedX += decSpeedX;
		}
		
		if (speedY > -decSpeedY && speedY < decSpeedY) speedY = 0;
		else {
			if (speedY > 0) speedY -= decSpeedY;
			if (speedY < 0) speedY += decSpeedY;
		}
		
		if (accelerationX > -decAccelX && accelerationX < decAccelX) accelerationX = 0;
		else {
			if (accelerationX > 0) accelerationX -= decAccelX;
			if (accelerationX < 0) accelerationX += decAccelX;
		}
		
		if (accelerationY > -decAccelY && accelerationY < decAccelY) accelerationY = 0;
		else {
			if (accelerationY > 0) accelerationY -= decAccelY;
			if (accelerationY < 0) accelerationY += decAccelY;
		}
	}
	
	public void move() {
//		if (speedX > maxSpeed) speedX = maxSpeed;
//		if (speedY > maxSpeed) speedY = maxSpeed;
//		if (speedX < -maxSpeed) speedX = -maxSpeed;
//		if (speedY < -maxSpeed) speedY = -maxSpeed;
	
		
		x += speedX;
		y += speedY;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
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

	public boolean[] getState() {
		return state;
	}
	
	public void setState(int index, boolean value) {
		this.state[index] = value;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getAccelerationX() {
		return accelerationX;
	}

	public void setAccelerationX(float accelerationX) {
		this.accelerationX = accelerationX;
	}

	public float getAccelerationY() {
		return accelerationY;
	}

	public void setAccelerationY(float accelerationY) {
		this.accelerationY = accelerationY;
	}

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}
}
