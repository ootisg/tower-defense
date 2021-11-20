package enemies;

import resources.Sprite;

public class TestEnemy extends Enemy {
	
	public static Sprite enemySpr = new Sprite ("resources/sprites/test.png");
	
	public TestEnemy () {
		super (10, 10);
		setSprite (enemySpr);
	}
	
	@Override
	public void frameEvent () {
		this.move ();
	}

}
