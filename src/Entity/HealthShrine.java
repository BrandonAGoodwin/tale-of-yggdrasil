package Entity;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import TileMap.TileMap;

public class HealthShrine extends MapObject {

	private boolean activated;
	private boolean deactivated;
	
	private BufferedImage[] sprites;
	
	public HealthShrine(TileMap tm) {
		
		super(tm);
		
		width = 52;
		height = 43;
		cwidth = 52;
		cheight = 43;
		
		// Load Sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Misc/HealthShrineSprites.gif"
				)
			);
				
			sprites = new BufferedImage[13];
			for(int j = 0; j < 13; j++) {
				sprites[j] = spritesheet.getSubimage(j * 52, 0, 52, 43);
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		AudioPlayer.load("/SFX/HealthShrine.mp3", "HealthShrine");
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
	}
	
	public boolean getActivated() { return activated; }
	
	public void setDeactivated() {
		activated = true;
		deactivated = true;
		animation.setFrame(animation.getNumFrames()-1);
	}
	
	public void activate(Player p) {
		if(!activated) {
			p.setHealth(p.getMaxHealth());
			AudioPlayer.play("HealthShrine");
			activated = true;
		}
	}
	
	public void update() {
		
		// Update position
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(activated && animation.getFrame() == 0 && !deactivated) {
			animation.setDelay(4);
		}
		
		animation.update();
		
		if((animation.getFrame() == animation.getNumFrames() - 1) || deactivated) {
			animation.setDelay(-1);
		}
		
	}

}