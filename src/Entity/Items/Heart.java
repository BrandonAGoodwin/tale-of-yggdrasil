package Entity.Items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import Entity.Animation;
import Entity.Item;
import Entity.Player;
import TileMap.TileMap;

public class Heart extends Item {
	
	private BufferedImage [] sprites;
	
	public Heart(TileMap tm){
		
		super(tm);
		
		width = cwidth = 16;
		height = cheight = 16;
		
		itemRarity = 0;
		
		// Load Sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Items/Heart.gif"));
			
			sprites = new BufferedImage[1];
			sprites[0] = spritesheet.getSubimage(0, 0, width, height);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		AudioPlayer.load("/SFX/HealthUp.mp3", "Heart");
	
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(100);
	}
	
	public void update() {
		
		// Update Position
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		animation.update();
		
	}
	
	public void activate(Player p) {
		if(p.getHealth() < p.getMaxHealth()) {
			p.setHealth(p.getHealth() + 2);
			if(p.getHealth() > p.getMaxHealth()) {
				p.setHealth(p.getMaxHealth());
			}
			AudioPlayer.play("Heart");
			setPickedUp();
		}
		else { activated = false; }
	}
	
	public void draw(Graphics2D g) {

		setMapPosition();
		
		double radians;

		degrees += 10;
		radians  = Math.toRadians(degrees);
		
		g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)((y + ymap - height / 2) + Math.sin(radians)), width, height, null);
		
	}
}
