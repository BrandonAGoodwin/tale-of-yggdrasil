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

public class EnergyOrbs extends EnemyProjectile {
	
	private BufferedImage[] sprites;
	
	private int type;
	
	private int detonationTick;
	
	public EnergyOrbs (TileMap tm, int type) {
		
		super(tm);
		
		this.type = type;
		
		width = 52;
		height = 52;
		cwidth = 14;
		cheight = 14;
		
		detonationTick = 0;
		
		damage = 1;
		
		interactable = false;
		
		// Load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Resources/Sprites/Enemies/EnergyOrbs0.gif"));
				
			sprites = new BufferedImage[8];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * width, 0, width, height);
			}
				
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		if(type == 0) {
			animation.setFrames(sprites);
			animation.setDelay(9);
		}
		else if(type == 1) {
			animation.setReverseFrames(sprites);
			animation.setFrame(2);
			animation.setDelay(5);
		}
	}
	
	public void setHit() {}
	
	public void update() {
		
		// Update position
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		switch(type) {
			case 0:
				if (animation.getFrame() == 1) { cwidth = cheight = 24; }
				else if (animation.getFrame() == 2) { cwidth = cheight = 34; }
				else if (animation.getFrame() == 3) { cwidth = cheight = 40; }
				else if (animation.getFrame() == 4) { cwidth = cheight = 50; }
				else if (animation.getFrame() == 5) { cwidth = cheight = 52; }
				if(animation.hasPlayedOnce()) {
					remove = true;
				}
				
				break;
				
			case 1:
				detonationTick++;
				if(detonationTick == 1) {
					damage = 0;
				}
				else if(animation.getFrame() == 0 && detonationTick < 20) {
					animation.setFrames(sprites);
					animation.setDelay(-1);
				}
				else if(detonationTick == 30) {
					damage = 1;
					animation.setDelay(6);
				}
				else if (animation.getFrame() == 1) { cwidth = cheight = 24; }
				else if (animation.getFrame() == 2) { cwidth = cheight = 34; }
				else if (animation.getFrame() == 3) { cwidth = cheight = 40; }
				else if (animation.getFrame() == 4) { cwidth = cheight = 50; }
				else if (animation.getFrame() == 5) { cwidth = cheight = 52; }
				
				if((animation.hasPlayedOnce() && detonationTick > 30 ) || animation.getFrame() == 6) {
					remove = true;
				}

				break;
		}
		
		// Update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g) {
		
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