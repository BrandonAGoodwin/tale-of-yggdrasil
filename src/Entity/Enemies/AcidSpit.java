package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.EnemyProjectile;
import Entity.Player;
import TileMap.TileMap;

public class AcidSpit extends EnemyProjectile {
	
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;
	
	public AcidSpit (TileMap tm) {
		
		super(tm);
		
		moveSpeed = 0.3;
		maxSpeed = 1.5;
		
		width = 16;
		height = 16;
		cwidth = 14;
		cheight = 14;
		
		damage = 1;
		
		interactable = true;
		
		// Load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Resources/Sprites/Enemies/AcidSpit.gif"));
				
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
			
			hitSprites = new BufferedImage[3];
			for(int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = spritesheet.getSubimage(i * width, height, width, height);
			}
				
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(5);
	}
	
	public void setHit() {
		if(hit) return;
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(6);
		dx = 0;
		dy = 0;
	}
	
	public void update() {
		
		// Update position
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if((dx == 0  && dy == 0) && !hit) {
			setHit();
		}
		
		if(hit && animation.hasPlayedOnce()) {
			remove = true;
		}
		
		// Update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g){
		
		//if(notOnScreen()) return;
		
		setMapPosition();
		
		if(facingLeft){
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2 + width), (int)(y + ymap - height / 2), -width, height, null);
		}
		else{
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), null);
		}
		
		/*// Draw collision box
		Rectangle r = getRectangle();
		r.x += xmap;
		r.y += ymap;
		g.draw(r);*/
	}

}