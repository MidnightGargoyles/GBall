package shared;

import java.io.Serializable;

public class MsgData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7883236279128653501L;
	/* 
	 
	private static final long serialVersionUID = 1L;

	public Vector2D m_position;
	public Vector2D m_initialPosition;
	public Vector2D m_initialDirection;
	public Vector2D m_speed;
	public Vector2D m_direction; // Should always be unit vector; determines the
									// object's facing

	public MsgData() {

		m_position = new Vector2D();
		m_initialPosition = new Vector2D();
		m_initialDirection = new Vector2D();
		m_speed = new Vector2D();
		m_direction = new Vector2D();
	}

	public MsgData(Vector2D position, Vector2D initialPosition,
			Vector2D initialDirection, Vector2D speed, Vector2D direction) {

		m_position = position;
		m_initialPosition = initialPosition;
		m_initialDirection = initialDirection;
		m_speed = speed;
		m_direction = direction;
	}
	*/
	public static final int KEYFRAME = 0;
	public static final int SUBFRAME = 1;
	public static final int CONNECTION = 3;
	public static final int PACKAGE = 4;
	
	protected int type;
	
	public MsgData(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}

}
