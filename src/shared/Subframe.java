package shared;

public class Subframe extends MsgData {
	
	private EntityTransformation[] entities = new EntityTransformation[4];
	
	public Subframe(EntityTransformation e0, EntityTransformation e1, EntityTransformation e2, EntityTransformation e3) {
		super(MsgData.SUBFRAME);
		entities[0] = e0;
		entities[1] = e1;
		entities[2] = e2;
		entities[3] = e3;
	}
	
	public EntityTransformation[] getEntities() {
		return entities;
	}
}
