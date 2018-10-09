package GameState;

import TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.imageio.ImageIO;

import Handlers.Keys;
import Main.GamePanel;
import Audio.AudioPlayer;

public class MenuState extends GameState{
	
	private Background bg;
	
	private Image helpScreen;
	private boolean helpVisible;
	
	private int currentChoice = 0;
	private String[] options = {
			"Start",
			"Help",
			"Quit",
			"God Mode"
	};
	
	private Color titleColor;
	private Font titleFont;
	private Font font;
	
	public MenuState(GameStateManager gsm) {
		
		super(gsm);
		
		try{
			
			
			bg = new Background("/Backgrounds/Grass&Sky.gif", 0.5);
			helpScreen = ImageIO.read(getClass().getResourceAsStream("/Backgrounds/HelpScreen.gif"));
			
			// Move speed of the background
			bg.setVector(-0.5, 0);
			
			titleColor = new Color(128, 0, 0);
			titleFont = new Font("Century Gothic", Font.PLAIN, 28);
			
			font = new Font ("Arial", Font.PLAIN, 12);
			
			// Background music + menu sound effects 
			AudioPlayer.load("/SFX/beep.mp3", "beep");
			AudioPlayer.load("/Music/Moonlit Secrets Looping.mp3", "bgMusic");
			AudioPlayer.loop("bgMusic", AudioPlayer.getFrames("bgMusic"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void init () {}
	
	public void update() {
		// Check keys
		handleInput();
		bg.update();
	}
	
	public void draw(Graphics2D g) {
		
		// Draw background
		bg.draw(g);
		
		/// Draw title
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Tale of Yggdrasil",50, 70);
		
		// Draw menu options
		g.setFont(font);
		int j = 0;
		for(int i = 0; i < options.length; i++) {
			if(i == currentChoice) {
				g.setColor(Color.RED);
				j = 5;
			}
			else {
				g.setColor(Color.BLACK);
				j = 0;
			}
			if(i != options.length - 1 || i == currentChoice ) {
				g.drawString(options[i], 145 + j, 140 + i * 15);
			}
		}
		if(helpVisible) {
			g.drawImage(helpScreen, 0, 0, GamePanel.WIDTH, GamePanel.HEIGHT, null);
		}
	}
	
	// The three menu options
	private void select() {
		if(currentChoice == 0) {
			AudioPlayer.stop("bgMusic");
			// Starts the game (Level 1)
			gsm.setState(GameStateManager.LEVEL1ASTATE);
		}
		if(currentChoice == 1) {
			helpVisible = true;
		}
		if(currentChoice == 2) {
			System.exit(0);
		}
		if(currentChoice == 3) {
			GamePanel.GOD_MODE = true;
		}
	}
	
	public void handleInput() {
		if(!helpVisible) {
			if(Keys.isPressed(Keys.ENTER)) {
				AudioPlayer.play("beep");
				select();
			}
			if(Keys.isPressed(Keys.UP) || Keys.isPressed(Keys.W)) {
				if(currentChoice > 0) {
					AudioPlayer.play("beep");
					currentChoice--;
				}
			}
			if(Keys.isPressed(Keys.DOWN) || Keys.isPressed(Keys.S)) {
				if(currentChoice < options.length - 1) {
					AudioPlayer.play("beep");
					currentChoice++;
				}
			}
		}
		else {
			if(Keys.isPressed(Keys.ENTER)) {
				AudioPlayer.play("beep");
				helpVisible = false;
			}
		}
	}
	
	/*public void keyPressed(KeyEvent key) {
		if(helpVisible) {
			if(key.getKeyCode() == KeyEvent.VK_ENTER) {
				helpVisible = false;
				return;
			}
		}
		Keys.keySet(key.getKeyCode(), true);
	}*/
}
