package Entity.Items;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Item;
import Entity.Player;
import Entity.Shot;
import TileMap.TileMap;

public class VolundHammer extends Item{
	
	private BufferedImage [] sprites;
	
	public VolundHammer(TileMap tm) {
		
		super(tm);
		
		width = 33;
		height = 33;
		cwidth = 28;
		cheight = 28;
		
		itemRarity = 3;
		
		//load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Items/VolundHammer.gif"));
			
			sprites = new BufferedImage[1];
			sprites[0] = spritesheet.getSubimage(0, 0, width, height);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(100);
	}
	
	public void update() {
		
		// Update position
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		animation.update();
		
	}
	
	public void activate(Player p) {
		setPickedUp();
		p.setShotDamage(p.getShotDamage() + 2);
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		double radians;

		degrees += 10;
		radians  = Math.toRadians(degrees);
		
		g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)((y + ymap - height / 2) + Math.sin(radians)), width, height, null);
		
	}

}
