package map;

import main.GameObject;
import resources.Sprite;

public class Map extends GameObject {

	private Sprite bgSprite;
	
	public Map (String path) {
		bgSprite = new Sprite (path + "bg.png");
		setSprite (bgSprite);
	}
	
}
