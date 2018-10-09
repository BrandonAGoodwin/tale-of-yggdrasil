package GameState;

import Audio.AudioPlayer;

public class GameStateManager {

	private GameState[] gameStates;
	private int currentState;
	
	public static final int NUMGAMESTATES = 4;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1ASTATE = 1;
	public static final int LEVEL1BSTATE = 2;
	public static final int LEVEL2ASTATE = 3;
	
	private PauseState pauseState;
	private boolean paused;
	
	public GameStateManager() {
		
		AudioPlayer.init();
		
		gameStates = new GameState[NUMGAMESTATES];
		
		pauseState = new PauseState(this);
		paused = false;
		
		currentState = MENUSTATE;
		loadState(currentState);
		
	}
	
	private void loadState(int state) {
		if(state == MENUSTATE)
			gameStates[state] = new MenuState(this);
		if(state == LEVEL1ASTATE)
			gameStates[state] = new Level1AState(this);
		if(state == LEVEL1BSTATE)
			gameStates[state] = new Level1BState(this);
		if(state == LEVEL2ASTATE)
			gameStates[state] = new Level2AState(this);
	}
	
	private void unloadState(int state) {
		gameStates[state] = null;
	}

	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}
	
	public void setPaused(boolean b) { paused = b; }
	public boolean getPaused() { return paused; }
	
	public void update() {
		if(paused) {
			pauseState.update();
			return;
		}
		if(gameStates[currentState] != null) gameStates[currentState].update();
	}
	
	public void draw(java.awt.Graphics2D g) {
		if(paused) {
			pauseState.draw(g);
			return;
		}
		try {
			gameStates[currentState].draw(g);
		}
		catch(Exception e) { }
	}

}
