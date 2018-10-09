package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.DebugGraphics;

import Entity.Animation;
import Entity.Enemy;
import Entity.Player;
import TileMap.TileMap;

public class Stinger extends Enemy {
	
	private BufferedImage[] sprites;
	
	private int xcentre;
	private int ycentre;
	private double degrees = 0;
	private Player p;
	private int turnTick = 0;
	private int turnTimer = 20;
	
	public Stinger (TileMap tm, Player p) {
		
		super(tm);
		
		moveSpeed = 0.9;
		maxSpeed = 1.75;
		
		this.p = p;
		width = 30;
		height = 25;
		cwidth = 20;
		cheight = 20;
		
		dropRate = 7;
		dropRarityMin = 0;
		dropRarityMax = 2;
		
		health = maxHealth = 8; //10
		damage = 1;
		
		// Load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Enemies/Bug.gif"));
			
			sprites = new BufferedImage[4];
			for(int i = 0; i < sprites.length; i++){
				sprites[i] = spritesheet.getSubimage(width * i, 0, width, height);
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(7);
		
		right = true;
		facingRight = true;
		
	}
	
	public void setDegrees(double degrees) { this.degrees = degrees; }
	
	private void playerDetect(Player p) {
		
		turnTick++;
		
		if(turnTick >= turnTimer) {
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
		
		/*// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed){
				dx = -maxSpeed;
			}
		}
		else if(right){
			dx += moveSpeed;
			if(dx > maxSpeed){
				dx = maxSpeed;
			}
		}
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
		}*/
		
		double radians;
		double savedDx;
		double savedDy;
		
		degrees += 10;
		radians = Math.toRadians(degrees);
		
		savedDx = dx;
		savedDy = dy;
		
		dy = Math.cos(radians) * 2;
		dx = Math.sin(radians) * 2;
		
		/*if(horizontal) {
			degrees = degrees + 10;
			radians  = Math.toRadians(degrees);
			dy = savedDy + Math.sin(radians);
		}
		else {
			degrees = degrees + 10;
			radians  = Math.toRadians(degrees);
			dx = savedDx + Math.sin(radians);
		}*/
		
		// Movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed){
				dx = -maxSpeed;
			}
		}
		else if(right){
			dx += moveSpeed;
			if(dx > maxSpeed){
				dx = maxSpeed;
			}
		}
		
		if(up) {
			dy -= moveSpeed;
			if(dy < -maxSpeed){
				dy = -maxSpeed;
			}
		}
		else if(down){
			dy += moveSpeed;
			if(dy > maxSpeed){
				dy = maxSpeed;
			}
		}
	}
	
	public void update() {
		checkSlow();
		
		/*// if it hits a wall, go other direction
		if(right && dx == 0) {
			setAllLeft();
		}
		else if(left && dx == 0) {
			setAllRight();
		}
		
		if(up && dy == 0) {
			setAllDown();
		}
		else if(down && dy == 0) {
			setAllUp();
		}*/
		
		//playerDetect(p);
		
		getNextPosition();
		playerDetect(p);
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
