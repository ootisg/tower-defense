package main;

import map.Map;

public class GameCode extends GameAPI {
	
	Map currMap;
	
	public void initialize () {
		currMap = new Map ("resources/maps/test/");
		currMap.declare (0, 0);
	}
	public void gameLoop () {
		
	}
}