package Entity.Doors;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Door;
import Entity.MapObject;
import Entity.Player;
import GameState.GameState;
import TileMap.TileMap;

public class GrassyStoneDoor extends Door {
	
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
		9,9
	};
	
	private static final int CLOSING_DOOR = 0;
	private static final int OPENING_DOOR = 1;
	
	public GrassyStoneDoor(TileMap tm, String direction) {
		
		super(tm, direction);
		
		if(direction == "left" || direction == "right") {
			if(direction == "left") {
				setFacingLeft();
				x = 16;
				y = 128;
			}
			else {
				x = 304;
				y = 128;
				setFacingRight();
			}
			width = 32;
			cwidth = 30;
			height = cheight = 64;
		}
		else {
			if(direction == "up") {
				x = 160;
				y = 16;
			}
			else {
				x = 160;
				y = 240;
			}
			setFacingRight();
			width = cwidth = 64;
			height = 32;
			cheight = 30;
		}
		
		open = false;
		collideable = true;	
		
		try {
			
			BufferedImage spritesheet = null;
			
			if(direction == "right" || direction == "left") {
				spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Doors/Double Doors Animation Horizontal.gif"));
			}
			else if(direction == "up") {
				spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Doors/Double Doors Animation Up.gif"));
			}
			else if(direction == "down") {
				spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Doors/Double Doors Animation Down.gif"));
			}
				
			sprites = new ArrayList<BufferedImage[]>(); 
			for(int i = 0; i < 2; i++){
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				if(i == 0) {
					for(int j = 0; j < numFrames[i]; j++) {
						bi[j] = spritesheet.getSubimage(j * width, 0, width, height);
					}
				}
				else {
					for(int j = 0; j < numFrames[i]; j++) {
						bi[numFrames[i]- j - 1] = spritesheet.getSubimage(j * width, 0, width, height);
					}
				}
				
				sprites.add(bi);
				
			}
			
			animation = new Animation();
			currentAction = CLOSING_DOOR;
			animation.setFrames(sprites.get(CLOSING_DOOR));
			animation.setDelay(5);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		
		// set the animation to only play once then freeze on the last frame
		
		if(!open) {
			if(currentAction != CLOSING_DOOR) {
				currentAction = CLOSING_DOOR;
				animation.setFrames(sprites.get(CLOSING_DOOR));
				animation.setDelay(5);
			}
			
			if (animation.getFrame() == 8) {
			animation.setDelay(-1);
			}
		}
		else {
			if(currentAction != OPENING_DOOR) {
				currentAction = OPENING_DOOR;
				animation.setFrames(sprites.get(OPENING_DOOR));
				animation.setDelay(5);
			}
			
			if (animation.getFrame() == 8) {
			animation.setDelay(-1);
			}
		}
		
		animation.update();
		
	}
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	
}
