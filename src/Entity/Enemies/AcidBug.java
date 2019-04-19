package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.DebugGraphics;

import Entity.Animation;
import Entity.Enemy;
import Entity.EnemyProjectile;
import Entity.Player;
import TileMap.TileMap;

public class AcidBug extends Enemy {
	
	private BufferedImage[] sprites;
	
	private int xcentre;
	private int ycentre;
	private double degrees = 0;
	private double spinRate = 5;

	private int turnTick = 0;
	private int turnTimer = 1;
	
	private boolean shooting = false;
	private int shootTimer = 130;
	private int shootTick = 0;
	private boolean pUp;
	private boolean pDown;
	private boolean pLeft;
	private boolean pRight;
	
	private Player p;
	private ArrayList<EnemyProjectile> eProjectiles;
	
	public AcidBug (TileMap tm, Player p, ArrayList<EnemyProjectile> eProjectiles) {
		
		super(tm);
		
		this.p = p;
		this.eProjectiles = eProjectiles;
		
		moveSpeed = 0.5;
		maxSpeed = 1.25;
		
		width = 30;
		height = 25;
		cwidth = 20;
		cheight = 20;
		
		dropRate = 10;
		dropRarityMin = 0;
		dropRarityMax = 0;
		
		health = maxHealth = 8; //10
		damage = 1;
		
		// Load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Resources/Sprites/Enemies/AcidBug.gif"));
			
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
	public void setSpinRate(double spinRate) { this.spinRate = spinRate; }
	public void setShooting(boolean b) { shooting = b; }
	
	public void setPUp() {
		pUp = true;
		pDown = false;
		pLeft = false;
		pRight = false;
	}
	
	public void setPDown() {
		pUp = false;
		pDown = true;
		pLeft = false;
		pRight = false;
	}
	
	public void setPLeft() {
		pUp = false;
		pDown = false;
		pLeft = true;
		pRight = false;
	}
	
	public void setPRight() {
		pUp = false;
		pDown = false;
		pLeft = false;
		pRight = true;
	}
	
	private void playerDetect(Player p) {
		
		if(shootTick >= shootTimer && !shooting) {
			if(p.gety() < y + (height / 2) && p.gety() + p.getHeight() > y + (height / 2)) {
				// Player is to the left of the enemy
				if(p.getx() < x + (width / 2)) {
					setPLeft();
					setShooting(true);
				}
				// Player is to the right of the enemy
				else {
					setPRight();
					setShooting(true);
				}
			}
			else if(p.getx() < x + (width / 2) && p.getx() + p.getWidth() > x + (width / 2)) {
				// Player is above the enemy
				if(p.gety() < y + (height / 2)) {
					setPUp();
					setShooting(true);
				}
				// Player is below the enemy
				else {
					setPDown();
					setShooting(true);
				}	
			}
		}
		
		turnTick++;
		
		if(turnTick >= turnTimer && !shooting) {
			System.out.println("Life");
			if(Math.abs(p.getx() - x) > Math.abs(p.gety() - y)) {
				// Player is to the left of the enemy
				if(p.getx() < x + (width / 2)) {
					setOnlyRight();
					System.out.println("Left");
				}
				// Player is to the right of the enemy
				else {
					setOnlyLeft();
					System.out.println("Right");
				}
			}
			else {
				// Player is above the enemy
				if(p.gety() < y + (height / 2)) {
					setOnlyDown();
					System.out.println("Up");
				}
				// Player is below the enemy
				else {
					setOnlyUp();
					System.out.println("Down");
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
		
		degrees += spinRate;
		radians = Math.toRadians(degrees);
		
		savedDx = dx;
		savedDy = dy;
		
		dy = Math.cos(radians) * 3;
		dx = Math.sin(radians) * 3;
		
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
		
		shootTick++;
		
		if(shooting) {
			
			AcidSpit as = new AcidSpit(tileMap);
			as.setPosition(x, y);
			
			if(pUp) {
				as.setVector(0, -2);
			}
			if(pDown) {
				as.setVector(0, 2);
			}
			if(pLeft) {
				as.setVector(-2, 0);
			}
			if(pRight) {
				as.setVector(2, 0);
			}
			eProjectiles.add(as);
			
			shootTick = 0;
			setShooting(false);
			
		}
		
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
		
	/*	// Draw collision box
		Rectangle r = getRectangle();
		r.x += xmap;
		r.y += ymap;
		g.draw(r);*/
		
	}

}
