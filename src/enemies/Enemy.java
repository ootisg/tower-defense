package enemies;

import main.GameObject;

public abstract class Enemy extends GameObject {

	private double baseSpeed;
	private double baseHealth;
	private double health;
	
	protected Enemy () {
		
	}
	
	public double getBaseSpeed () {
		return baseSpeed;
	}
	
	public double getBaseHealth () {
		return baseHealth;
	}
	
	public double getHealth () {
		return health;
	}
	
	public double getHealthFill () {
		return health / baseHealth;
	}
	
	public void setHealth (double health) {
		this.health = health;
	}
	
}
