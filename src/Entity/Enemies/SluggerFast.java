package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import TileMap.TileMap;

public class SluggerFast extends Enemy {
	
	private BufferedImage[] sprites;
	
	public SluggerFast (TileMap tm) {
		
		super(tm);
		
		moveSpeed = 1;
		maxSpeed = 1.8;
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		dropRate = 8;
		dropRarityMin = 0;
		dropRarityMax = 0;
		
		health = maxHealth = 18; //10
		damage = 1;
		
		// Load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Resources/Sprites/Enemies/SluggerFast.gif"));
			
			sprites = new BufferedImage[3];
			for(int i = 0; i < sprites.length; i++){
				sprites[i] = spritesheet.getSubimage(width * i, 0, width, height);
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(9);
		
		right = true;
		facingRight = true;
		
	}
	
	private void getNextPosition() {
		
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed){
				dx = -maxSpeed;
			}
		}
		/*else if(right){
			dx += moveSpeed;
			if(dx > maxSpeed){
				dx = maxSpeed;
			}
		}*/
		else if(right){

			if(dx >= maxSpeed){
				if(dx - moveSpeed < maxSpeed) {
					dx = maxSpeed;
				}
				else {
					dx -= moveSpeed;
				}
			}
			else {
				dx += moveSpeed;
			}
		}
		
	}
	
	public void update() {
		
		// if it hits a wall, go other direction
		if(right && dx == 0) {
			right = false;
			left = true;
			facingRight = false;
		}
		else if(left && dx == 0) {
			right = true;
			left = false;
			facingRight = true;
		}
		
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g){
		
		//if(notOnScreen()) return;
		
		setMapPosition();
		
		if(flinching){
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) { return; }
		}
		
		super.draw(g);
		

		
		/*// Draw collision box
		Rectangle r = getRectangle();
		r.x += xmap;
		r.y += ymap;
		g.draw(r);*/
		
	}

}
