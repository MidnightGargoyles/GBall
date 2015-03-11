package shared;

import java.util.LinkedList;

import GBall.EntityManager;
import GBall.GameEntity;
import GBall.Vector2D;

public class Subframe extends MsgData {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -623392811321334565L;
	//private EntityTransformation[] entities = new EntityTransformation[5];
	private EntityTransformation entity_0;
	private EntityTransformation entity_1;
	private EntityTransformation entity_2;
	private EntityTransformation entity_3;
	private EntityTransformation entity_4;
	private Vector2D score;
	
	
	public Subframe(EntityManager man) {
		super(MsgData.SUBFRAME);
		entity_0 = new EntityTransformation(man.getState().get(0));
		entity_1 = new EntityTransformation(man.getState().get(1));
		entity_2 = new EntityTransformation(man.getState().get(2));
		entity_3 = new EntityTransformation(man.getState().get(3));
		entity_4 = new EntityTransformation(man.getState().get(4));
		score = man.getScoreAsVector();
		/*LinkedList<GameEntity> ge =  man.getState();
		for(int i = 0; i < 5; i++) {
			entities[i] = new EntityTransformation(ge.get(i));
		}*/
	}
	
	public int size() {
		return 5;
	}
	
	public EntityTransformation get(int index) {
		switch(index) {
		case 4:
			return entity_4;
		case 3:
			return entity_3;
		case 2:
			return entity_2;
		case 1:
			return entity_1;
		case 0:
			return entity_0;
		default:
			return null;
		}
	}

	public Vector2D getScoreAsVector() {
		return score;
	}
}
