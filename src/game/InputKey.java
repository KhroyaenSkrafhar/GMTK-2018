package game;

public enum InputKey {
	LEFT(0),
	RIGHT(1),
	UP(2),
	DOWN(3);
	
	private int value;
	
	private InputKey(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
}
