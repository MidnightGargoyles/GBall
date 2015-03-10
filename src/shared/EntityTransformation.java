package shared;

import java.io.Serializable;

import GBall.GameEntity;
import GBall.Ship;
import GBall.Vector2D;

public class EntityTransformation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1363193922628191136L;
	public final Vector2D pos;
	public final Vector2D dir;
	public final int id;
	public final int rotation;
	
	public EntityTransformation(Vector2D pos, Vector2D dir, int id, int rotation) {
		this.pos = pos;
		this.dir = dir;
		this.id = id;
		this.rotation = rotation;
	}
	
	public EntityTransformation(GameEntity entity) {
		this.pos = entity.getPosition();
		this.dir = entity.getDirection();
		this.id = entity.id;
		if(entity.getClass().equals(Ship.class)) {
			this.rotation = ((Ship)entity).getRotation();
		} else {
			this.rotation = 0;
		}
		
	}
}
