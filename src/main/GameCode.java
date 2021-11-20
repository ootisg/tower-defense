package main;

import enemies.TestEnemy;
import map.Map;

public class GameCode extends GameAPI {
	
	Map currMap;
	
	public void initialize () {
		currMap = new Map ("resources/maps/test/");
		currMap.declare (0, 0);
		TestEnemy e = new TestEnemy ();
		e.declare (0, 0);
		e.startAt (currMap.getStartNode ());
	}
	public void gameLoop () {
		
	}
}