package GameState;

import java.awt.*;

import javax.imageio.ImageIO;

import Handlers.Keys;

import Main.GamePanel;

public class PauseState extends GameState {
	
	private Image helpScreen;
	private boolean helpVisible;
	
	private int currentChoice = 0;
	private String[] options = {
			"Resume",
			"Help",
			"Back to Menu",
			"Quit"
	};
	
	private Font menuFont;
	private Font font;
	
	public PauseState(GameStateManager gsm) {
		
		super(gsm);
		
		helpVisible = false;
		
		try {
			helpScreen = ImageIO.read(getClass().getResourceAsStream("/Resources/Backgrounds/HelpScreen.gif"));
			font = new Font ("Century Gothic", Font.PLAIN, 18);
			menuFont = new Font("Century Gothic", Font.PLAIN, 14);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void init () {}
	
	public void update() {
		// Check keys
		handleInput();
	}
	
	public void draw(Graphics2D g) {
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString("Game Paused", 100, 90);
		
		// Draw menu options
		g.setFont(menuFont);
		int j = 0;
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.RED);
				j = 5;
			}
			else {
				g.setColor(Color.WHITE);
				j = 0;
			}
			g.drawString(options[i], 135 + j, 140 + i * 15);
		}
		
		if(helpVisible) {
			g.drawImage(helpScreen, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
		}
		
	}
	
	private void select() {
		if(currentChoice == 0) {
			gsm.setPaused(false);
		}
		if(currentChoice == 1) {
			helpVisible = true;
		}
		if(currentChoice == 2) {
			gsm.setPaused(false);
			gsm.setState(GameStateManager.MENUSTATE);
		}
		if(currentChoice == 3) {
			System.exit(0);
		}
	}
	
	public void handleInput() {
		if(!helpVisible) {
			if(Keys.isPressed(Keys.ESCAPE)) gsm.setPaused(false);
			if(Keys.isPressed(Keys.ENTER)) select();
			if(Keys.isPressed(Keys.UP) || Keys.isPressed(Keys.W)) {
				if(currentChoice > 0) {
					currentChoice--;
				}
			}
			if(Keys.isPressed(Keys.DOWN) || Keys.isPressed(Keys.S)) {
				if(currentChoice < options.length - 1) {
					currentChoice++;
				}
			}
		}
		else {
			if(Keys.isPressed(Keys.ENTER)) {
				helpVisible = false;
			}
		}
	}
}
