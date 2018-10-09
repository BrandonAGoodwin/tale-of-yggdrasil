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

public class CursedFireball extends EnemyProjectile {
	
	private BufferedImage[] sprites;
	private BufferedImage[] hitSprites;
	private int type;
	
	public CursedFireball (TileMap tm, int type) {
		
		super(tm);
		System.out.println("CF");
		
		this.type = type;
		moveSpeed = 0.6;
		maxSpeed = 2;
		
		
		width = 60;
		height = 60;
		cwidth = 40;
		cheight = 40;
		/*if(type == 1) {
			width = 60;
			height = 60;
			cwidth = 48;
			cheight = 48;
		}
		else if(type == 2) {
			width = 30;
			height = 30;
			cwidth = 24;
			cheight = 24;
		}*/
		
		damage = 1;
		
		interactable = true;
		
		// Load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/CursedFireball.gif"));
			if(type == 2) {
				spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/fireball.gif"));
			}
			
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(i * 30, 0, 30, 30);
			}
			
			hitSprites = new BufferedImage[3];
			for(int i = 0; i < hitSprites.length; i++) {
				hitSprites[i] = spritesheet.getSubimage(i * 30, 30, 30, 30);
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
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), width, height, null);
		}
		
		/*// Draw collision box
		Rectangle r = getRectangle();
		r.x += xmap;
		r.y += ymap;
		g.draw(r);*/
	}

}