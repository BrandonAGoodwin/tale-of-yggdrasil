package GameState;

import java.awt.Graphics2D;

import Entity.Player;

public abstract class GameState {

	protected GameStateManager gsm;
	private Player player;
	
	public GameState(GameStateManager gsm) {
		this.gsm = gsm;
	}
	
	public abstract void init();
	public abstract void update();
	public abstract void draw(Graphics2D g);
	public abstract void handleInput();

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() { return player; }
}
