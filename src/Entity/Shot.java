package Entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Entity.Items.ScaleOfTheSerpent;
import TileMap.TileMap;

import java.util.ArrayList;

public class Shot extends MapObject{
	
	private double savedDx;
	private double savedDy;
	
	private boolean hit;
	private boolean remove;
	
	private double knockback;
	private double mass;
	
	private boolean snakingShot;
	double degrees = 0;
	private boolean slowShot;
	private boolean slow;
	private boolean speedShot;
	private Animation animationSpeedShot;
	private int shotSize;
	
	private boolean piercingShot;
	private boolean pierced;
	
	private ArrayList<BufferedImage[]> sprites;
	private BufferedImage[] hitSprites;
	private final int[] numFrames = {
			4,4,4,4
	};
	private ArrayList<BufferedImage[]> speedShotSprites;
	private final int[] numFramesSpeedShot = {
			4, 4, 4, 4
	};
	
	private boolean horizontal;
	
	private static final int BASICSHOT_HORIZONTAL = 0;
	private static final int BASICSHOT_VERTICAL = 1;
	private static final int SPEEDSHOT_HORIZONTAL = 2;
	private static final int SPEEDSHOT_VERTICAL = 3;
	
	private int currentActionSpeedShot;
	
	private static final int SPEED_EFFECT_HORIZONTAL = 0;
	private static final int SPEED_EFFECT_VERTICAL = 1;

	public Shot(TileMap tm,
			boolean horizontal, 
			boolean rightOrUp, 
			String shotType, 
			double shotSpeed, 
			double playerDx, 
			double playerDy, 
			boolean snakingShot, 
			boolean slowShot, 
			double slowPercent, 
			int shotSize, 
			boolean piercingShot) {
		
		super(tm);
		
		this.horizontal = horizontal;
		
		facingRight = (horizontal && rightOrUp);
		facingDown = (!horizontal && !rightOrUp);
		facingUp = (!horizontal && rightOrUp);
		facingLeft = (horizontal && !rightOrUp);
		
		moveSpeed = shotSpeed;
		
		this.shotSize = shotSize; 
		
		speedShot = shotType == "speedShot";
		this.snakingShot = snakingShot;
		this.slowShot = slowShot;
		this.piercingShot = piercingShot;
		
		if(horizontal) {
			if(rightOrUp) dx = moveSpeed;
			else dx = -moveSpeed;
			
			dy = playerDy * 0.25;
		}
		else {
			if(rightOrUp) dy = -moveSpeed;
			else dy = moveSpeed;
			
			dx = playerDx * 0.25;
		}
		
		savedDx = dx;
		savedDy = dy;
		
		width = 32;
		height = 32;
		cwidth = 16 * shotSize;
		cheight = 16 * shotSize;
		
		int roll = randomInt(1, 15);
		if(!hit && slowShot && roll == 1) slow = true;
		else slow = false;
		
		
		
		// Load Sprites
		try{
			BufferedImage spritesheet1 = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/shotSprites.gif"));
			
			sprites = new ArrayList<BufferedImage[]>(); 
			for(int i = 0; i < 4; i++){
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++){
					bi[j] = spritesheet1.getSubimage(j * width, i * height, width, height);
				}
				
				sprites.add(bi);
				
			}
			
			BufferedImage spritesheet2 = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/fireball.gif"));
			
			hitSprites = new BufferedImage[3];
			for(int i = 0; i < hitSprites.length; i++){
				hitSprites[i] = spritesheet2.getSubimage(i * 30, 30, 30, 30);
			}
			
			animationSpeedShot = new Animation();
			if(speedShot) {
				BufferedImage spritesheet3 = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/speedShot.gif"));
				
				speedShotSprites = new ArrayList<BufferedImage[]>(); 
				for(int i = 0; i < 4; i++){
					BufferedImage[] bi = new BufferedImage[numFrames[i]];
					for(int j = 0; j < numFrames[i]; j++){
						bi[j] = spritesheet3.getSubimage(j * width, i * height, width, height);
					}
					
					speedShotSprites.add(bi);
					
					
				}
				//Animation animationSpeedShot = new Animation();
			}
			
			animation = new Animation();
			
			if(slow) {
				mass = 0.5;
				if(horizontal) {
					currentAction = SPEEDSHOT_HORIZONTAL;
					animation.setFrames(sprites.get(SPEEDSHOT_HORIZONTAL));
					animation.setDelay(5);
				}
				else {
					currentAction = SPEEDSHOT_VERTICAL;
					animation.setFrames(sprites.get(SPEEDSHOT_VERTICAL));
					animation.setDelay(5);
				}
				
			}
			else {
				mass = 0.3;
				if(horizontal) {
					currentAction = BASICSHOT_HORIZONTAL;
					animation.setFrames(sprites.get(BASICSHOT_HORIZONTAL));
					animation.setDelay(5);
				}
				else {
					currentAction = BASICSHOT_VERTICAL;
					animation.setFrames(sprites.get(BASICSHOT_VERTICAL));
					animation.setDelay(5);
				}
				
			}
			
			if(speedShot) {
				if(horizontal) {
					currentActionSpeedShot = SPEED_EFFECT_HORIZONTAL;
					animationSpeedShot.setFrames(speedShotSprites.get(SPEED_EFFECT_HORIZONTAL));
					animationSpeedShot.setDelay(5);
				}
				else {
					currentActionSpeedShot = SPEED_EFFECT_VERTICAL;
					animationSpeedShot.setFrames(speedShotSprites.get(SPEED_EFFECT_VERTICAL));
					animationSpeedShot.setDelay(5);
				}
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void setShotType(String shotType, boolean horizontal) {
		
		if(shotType == "speedShot") {
			mass = 0.5;
			if(horizontal) {
				currentAction = SPEEDSHOT_HORIZONTAL;
				animation.setFrames(sprites.get(SPEEDSHOT_HORIZONTAL));
				animation.setDelay(5);
			}
			else {
				currentAction = SPEEDSHOT_VERTICAL;
				animation.setFrames(sprites.get(SPEEDSHOT_VERTICAL));
				animation.setDelay(5);
			}
			
		}
		else {
			mass = 0.3;
			if(horizontal) {
				currentAction = BASICSHOT_HORIZONTAL;
				animation.setFrames(sprites.get(BASICSHOT_HORIZONTAL));
				animation.setDelay(5);
			}
			else {
				currentAction = BASICSHOT_VERTICAL;
				animation.setFrames(sprites.get(BASICSHOT_VERTICAL));
				animation.setDelay(5);
			}
			
		}
		
	}
	
	public void setHit() {
		if(hit) return;
		if(!pierced && piercingShot) {
			pierced = true;
		}
		else {
		hit = true;
		animation.setFrames(hitSprites);
		animation.setDelay(6);
		speedShot = false;
		dx = 0;
		dy = 0;
		}
	}
	
	public void setSpeedShot() {
		if(currentAction != SPEEDSHOT_HORIZONTAL) {
			currentAction = SPEEDSHOT_HORIZONTAL;
			animation.setFrames(sprites.get(SPEEDSHOT_HORIZONTAL));
			animation.setDelay(5);
		}
	}
	
	public boolean getHit() {
		return hit;
	}
	public boolean getSlowing() {
		return slow;
	}
	public boolean getPierced() { return pierced; }
	public boolean getPiercingShot() { return piercingShot; }
	public double getKnockback() {
		knockback = moveSpeed * mass;
		return knockback;
	}
	
	public boolean shouldRemove() { return remove; }
	
	private void getNextPosition() {
		
		double radians;
		
		if(snakingShot && !hit) {
			
			if(horizontal) {
				degrees = degrees + 10;
				radians  = Math.toRadians(degrees);
				dy = savedDy + Math.sin(radians);
			}
			else {
				degrees = degrees + 10;
				radians  = Math.toRadians(degrees);
				dx = savedDx + Math.sin(radians);
			}
			
		}
		
	}
	
	public void update() {
		
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(((dx == 0 && horizontal) || (dy == 0 && !horizontal)) && !hit) {
			setHit();
		}
		
		animation.update();
		if(speedShot) {
			animationSpeedShot.update();
		}
		if(hit && animation.hasPlayedOnce()){
			remove = true;
		}
		
	}
	
	public void draw (Graphics2D g) {
		
		setMapPosition();
		
		if(facingRight){
			g.drawImage(animation.getImage(), (int)(x + xmap - (width * shotSize) / 2), (int)(y + ymap - (height * shotSize) / 2), width * shotSize, height * shotSize, null);
			if(speedShot)g.drawImage(animationSpeedShot.getImage(), (int)(x + xmap - (width * shotSize) / 2), (int)(y + ymap - (height * shotSize) / 2), width * shotSize, height * shotSize, null);
		}
		else if(facingDown){
			g.drawImage(animation.getImage(), (int)(x + xmap - (width * shotSize) / 2), (int)(y + ymap - (height * shotSize) / 2 + (height * shotSize)), width * shotSize, -height * shotSize, null);
			if(speedShot)g.drawImage(animationSpeedShot.getImage(), (int)(x + xmap - (width * shotSize) / 2), (int)(y + ymap - (height * shotSize) / 2 + (height * shotSize)), width * shotSize, -height * shotSize, null);
		}
		else{
			g.drawImage(animation.getImage(), (int)(x + xmap - (width * shotSize) / 2 + (width * shotSize)), (int)(y + ymap - (height * shotSize) / 2), -width * shotSize, height * shotSize, null);
			if(speedShot)g.drawImage(animationSpeedShot.getImage(), (int)(x + xmap - (width * shotSize) / 2 + (width * shotSize)), (int)(y + ymap - (height * shotSize) / 2), -width * shotSize, height * shotSize, null);
		}
		
		/*// draw collision box
		Rectangle r = getRectangle();
		r.x += xmap;
		r.y += ymap;
		g.draw(r);*/
	}
	
}
