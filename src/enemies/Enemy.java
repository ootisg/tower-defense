package enemies;

import main.GameObject;
import map.Map.PathNode;
import vec.Vector2D;

public abstract class Enemy extends GameObject {

	private double baseSpeed;
	private double baseHealth;
	private double health;
	
	private double progress;
	private double localProgress;
	
	private PathNode curr;
	private PathNode next;
	
	protected Enemy (double baseSpeed, double baseHealth) {
		this.baseSpeed = baseSpeed;
		this.baseHealth = baseHealth;
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
	
	public double getProgress () {
		return progress;
	}
	
	public void setHealth (double health) {
		this.health = health;
	}
	
	public void startAt (PathNode start) {
		curr = start;
		next = start.getNext ();
		setPosition (curr.getPosition ().x, curr.getPosition ().y);
	}
	
	public PathNode getCurrNode () {
		return curr;
	}
	
	public PathNode getNextNode () {
		return next;
	}
	
	public void move () {
		Vector2D basePos = new Vector2D (curr.getPosition ().x, curr.getPosition ().y);
		if (next != null) {
			localProgress += baseSpeed;
			Vector2D offs = new Vector2D (next.getPosition ().x, next.getPosition ().y);
			offs.diff (basePos);
			double len = offs.getLength ();
			while (localProgress > len) { //There might be a bug here when skipping multiple nodes
				localProgress -= len;
				if (localProgress <= 0) {
					localProgress += len;
					break;
				}
				curr = next;
				next = curr.getNext ();
				if (next == null) {
					//TODO damage the player
					forget ();
					return;
				}
				basePos = new Vector2D (curr.getPosition ().x, curr.getPosition ().y);
				offs = new Vector2D (next.getPosition ().x, next.getPosition ().y);
				offs.diff (basePos);
				len = offs.getLength ();
			}
			offs.normalize ();
			offs.scale (localProgress);
			basePos.add (offs);
			setPosition (basePos.x, basePos.y);
		} else {
			//TODO damage the player
			forget ();
		}
	}
	
	@Override
	public void draw () {
		int offsX = this.getSprite ().getWidth () / 2;
		int offsY = this.getSprite ().getHeight () / 2;
		setX (getX () - offsX);
		setY (getY () - offsY);
		super.draw ();
		setX (getX () + offsX);
		setY (getY () + offsY);
	}
	
}
