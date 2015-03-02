package shared;

import GBall.Vector2D;

public class EntityTransformation {
	private Vector2D pos;
	private Vector2D dir;
	private int id;
	
	public EntityTransformation(Vector2D pos, Vector2D dir, int id) {
		this.pos = pos;
		this.dir = dir;
		this.id = id;
	}
}
