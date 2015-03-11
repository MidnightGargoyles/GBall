package shared;

import java.io.Serializable;

import GBall.GameEntity;
import GBall.Ship;
import GBall.Vector2D;

/**
 * Represents the most releveant transformation properties
 * of an entity.
 * @author Niklas
 * @author Victor
 */
public class EntityTransformation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1363193922628191136L;
	public final Vector2D pos;
	public final Vector2D dir;
	public final Vector2D vel;
	public final Vector2D curPeak;
	public final int id;
	public final int rotation;
	
	public EntityTransformation(Vector2D pos, Vector2D dir, Vector2D vel, Vector2D curPeak, int id, int rotation) {
		this.pos = pos;
		this.dir = dir;
		this.vel = vel;
		this.id = id;
		this.rotation = rotation;
		this.curPeak = curPeak;
	}
	
	public EntityTransformation(GameEntity entity) {
		this.pos = entity.getPosition();
		this.dir = entity.getDirection();
		this.id = entity.id;
		this.vel = entity.getSpeed();
		this.curPeak = entity.curPeak;
		if(entity.getClass().equals(Ship.class)) {
			this.rotation = ((Ship)entity).getRotation();
		} else {
			this.rotation = 0;
		}
	}
}
