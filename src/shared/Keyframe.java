package shared;

import GBall.GameEntity;
import GBall.Vector2D;

public class Keyframe extends MsgData {
	private GameEntity[] entities;
	private int team0_score;
	private int team1_score;
	public Keyframe(GameEntity[] entities, int team0_score, int team1_score) {
		super(MsgData.KEYFRAME);
		this.entities = entities;
		this.team0_score = team0_score;
		this.team1_score = team1_score;
	}
	
}
