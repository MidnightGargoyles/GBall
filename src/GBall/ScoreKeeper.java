package GBall;

import java.awt.Graphics;

import GBall.Const;

public class ScoreKeeper {


	private int m_team1Score;
	private int m_team2Score;

	public void changeScores(int deltaTeam1, int deltaTeam2) {
		m_team1Score += deltaTeam1;
		m_team2Score += deltaTeam2;
	}

	public ScoreKeeper() {
		m_team1Score = 0;
		m_team2Score = 0;
	}

	public void render(java.awt.Graphics g) {
		g.setFont(Const.SCORE_FONT);
		g.setColor(Const.TEAM1_COLOR);
		g.drawString(new Integer(m_team1Score).toString(),
				(int) Const.TEAM1_SCORE_TEXT_POSITION.getX(),
				(int) Const.TEAM1_SCORE_TEXT_POSITION.getY());

		g.setColor(Const.TEAM2_COLOR);
		g.drawString(new Integer(m_team2Score).toString(),
				(int) Const.TEAM2_SCORE_TEXT_POSITION.getX(),
				(int) Const.TEAM2_SCORE_TEXT_POSITION.getY());
	}
	
	public Vector2D getScoreAsVector() {
		Vector2D v = new Vector2D();
		v.setX(m_team1Score);
		v.setY(m_team2Score);
		return v;
	}
	
	public void setScore(Vector2D v) {
		m_team1Score = (int) v.getX();
		m_team2Score = (int) v.getY();
	}
}