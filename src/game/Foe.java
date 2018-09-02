package game;

public class Foe {
	
	private float x, y, radius;
	private float age;
	
	public Foe(float x, float y, float radius) {
		this.setX(x);
		this.setY(y);
		this.setAge(1);
		this.setRadius(radius);
	}
	
	public void update() {
		age -= 0.01;
		if (age == 0) {
			System.err.println("AGE == 0");
		}
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

	public float getAge() {
		return age;
	}

	public void setAge(float age) {
		this.age = age;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}
	
}
