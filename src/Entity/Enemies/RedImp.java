package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import Entity.Player;
import TileMap.TileMap;

public class RedImp extends Enemy {
	
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
		6,6,6	
	};
	
	private final static int WALKING_HORIZONTAL = 0;
	private final static int WALKING_DOWN = 1;
	private final static int WALKING_UP = 2;
	
	private Player p;
	
	private int turnTick;
	private int turnTimer;
	
	public RedImp (TileMap tm, Player p) {
		
		super(tm);
		
		this.p = p;
		
		moveSpeed = 0.2;
		maxSpeed = 1.4;
		
		width = 32;
		height = 32;
		cwidth = 18;
		cheight = 30;
		
		dropRate = 7;
		dropRarityMin = 0;
		dropRarityMax = 3;
		
		turnTimer = 30;
		turnTick = 0;
		
		setOnlyDown();
		setFacingDown();
		
		health = maxHealth = 18;
		damage = 1;
		
		// load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Resources/Sprites/Enemies/RedImpSprites1.gif"));
			
			sprites = new ArrayList<BufferedImage[]>(); 
			for(int i = 0; i < 3; i++){
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++){
					bi[j] = spritesheet.getSubimage(j * width, i * height, width, height);
				}
				
				sprites.add(bi);
				
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = WALKING_HORIZONTAL;
		animation.setFrames(sprites.get(WALKING_HORIZONTAL));
		animation.setDelay(5);
	}
	
	private void playerDetect(Player p) {
		
		turnTick++;
		
		if(turnTick >= turnTimer) {
			/*if(p.gety() < y + (height / 2) && p.gety() + p.getHeight() > y + (height / 2)) {
				// player is to the left of the enemy
				if(p.getx() < x + (width / 2)) {
					setOnlyFalse();
					setOnlyLeft();
					setFacingLeft();
				}
				else {
					setOnlyFalse();
					setOnlyRight();
					setFacingRight();
				}
				
			}
			else if(p.getx() < x + (width / 2) && p.getx() + p.getWidth() > x + (width / 2))  {
				
				if(p.gety() < y + (height / 2)) {
					setOnlyFalse();
					setOnlyUp();
					setFacingUp();
				}
				else {
					setOnlyFalse();
					setOnlyDown();
					setFacingDown();
				}
			}*/
			if(Math.abs(p.getx() - x) > Math.abs(p.gety() - y)) {
				// Player is to the left of the enemy
				if(p.getx() < x + (width / 2)) {
					setAllLeft();
				}
				// Player is to the right of the enemy
				else {
					setAllRight();
				}
			}
			else {
				// Player is above the enemy
				if(p.gety() < y + (height / 2)) {
					setAllUp();
				}
				// Player is below the enemy
				else {
					setAllDown();
				}	
			}
			turnTick = 0;
		}
	}
	
	
	private void getNextPosition() {
		
		// movement
		if(left) {
			dx = -maxSpeed;
		}
		else if(right){
			dx = maxSpeed;
		}
		else {
			dx = 0;
		}
		
		if(up) {
			dy = -maxSpeed;
		}
		else if(down) {
			dy = maxSpeed;
		}
		else {
			dy = 0;
		}
		
		/*
		if(falling) {
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		*/
		
		
	}
	
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		/*// check flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;;
			}
		}*/
		
		/*
		// if it hits a wall, go other direction
		if(right && dx == 0) {
			setOnlyFalse();
			dy = 1;
			dx = -1;
			left = true;
			setFacingDown();
		}
		else if(left && dx == 0) {
			setOnlyFalse();
			dx = 1;
			dy = -1;
			right = true;
			setFacingUp();
		}
		
		if(down && dy == 0) {
			setOnlyFalse();
			dy = -1;
			dx = -1;
			up = true;
			setFacingLeft();
		}
		else if(up && dy == 0) {
			setOnlyFalse();
			dx = 1;
			dy = 1;
			down = true;
			setFacingRight();
		}
		*/
		
		playerDetect(p);
		
		if(facingRight || facingLeft) {
			if(currentAction != WALKING_HORIZONTAL) {
				currentAction = WALKING_HORIZONTAL;
				animation.setFrames(sprites.get(WALKING_HORIZONTAL));
				animation.setDelay(5);
				width = 32;
			}
		}
		else if(facingUp) {
			if(currentAction != WALKING_UP) {
				currentAction = WALKING_UP;
				animation.setFrames(sprites.get(WALKING_UP));
				animation.setDelay(5);
				width = 32;
			}
		}
		else if(facingDown) {
			if(currentAction != WALKING_DOWN) {
				currentAction = WALKING_DOWN;
				animation.setFrames(sprites.get(WALKING_DOWN));
				animation.setDelay(5);
				width = 32;
			}
		}
		
		// update animation
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
		
		//g.drawRect((int)x, (int)y, cwidth, cheight);
		//g.drawRect((int)x - cwidth, (int)y - cheight, cwidth, cheight);
		
		/*// draw collision box
		Rectangle r = getRectangle();
		r.x += xmap;
		r.y += ymap;
		g.draw(r);*/
	}

}