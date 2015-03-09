package shared;

import java.io.Serializable;

import GBall.GameEntity;
import GBall.Vector2D;

public class EntityTransformation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1363193922628191136L;
	public final Vector2D pos;
	public final Vector2D dir;
	public final int id;
	
	public EntityTransformation(Vector2D pos, Vector2D dir, int id) {
		this.pos = pos;
		this.dir = dir;
		this.id = id;
	}
	
	public EntityTransformation(GameEntity entity) {
		this.pos = entity.getPosition();
		this.dir = entity.getDirection();
		this.id = entity.id;
		
	}
}
