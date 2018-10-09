package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Entity.Animation;
import Entity.Enemy;
import Entity.EnemyProjectile;
import Entity.Player;
import TileMap.TileMap;

public class PossessedArmour extends Enemy {
	
	boolean attacking;
	int attackCooldownTick = 0;
	int attackCooldownTimer = 60;
	int attackTick = 0;
	int attackTimer = 30;
	
	private ArrayList<BufferedImage[]> sprites;
	
	private final int[] numFrames = {
		4, 4, 4, 6, 6, 6, 5, 5, 5
	};
	private final int[] frameWidths = {
		50, 50, 50, 50, 50, 50, 50, 50, 50
	};
	private final int[] frameHeights = {
		50, 50, 50, 50, 50, 50, 50, 50, 50
	};
	
	private final static int IDLE_DOWN = 0;
	private final static int IDLE_UP = 1;
	private final static int IDLE_HORIZONTAL = 2;
	private final static int WALKING_DOWN = 3;
	private final static int WALKING_UP = 4;
	private final static int WALKING_HORIZONTAL = 5;
	private final static int ATTACKING_DOWN = 6;
	private final static int ATTACKING_UP = 7;
	private final static int ATTACKING_HORIZONTAL = 8;
	
	private Player p;
	private ArrayList<EnemyProjectile> eProjectiles;
	
	public PossessedArmour(TileMap tm, Player p, ArrayList<EnemyProjectile> eProjectiles) {
		
		super(tm);
		
		this.p = p;
		this.eProjectiles = eProjectiles;
		
		moveSpeed = 0.2;
		maxSpeed = 1.0;
		
		width = 50;
		height = 50;
		cwidth = 32;
		cheight = 34;
		
		dropRate = 10;
		dropRarityMin = 0;
		dropRarityMax = 3;
		
		setAllUp();
		
		health = maxHealth = 24;
		damage = 1;
		
		// Load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
						"/Sprites/Enemies/PossessedArmorSprites0.gif"
					)
				);
				
				int count = 0;
				sprites = new ArrayList<BufferedImage[]>();
				for(int i = 0; i < numFrames.length; i++) {
					BufferedImage[] bi = new BufferedImage[numFrames[i]];
					for(int j = 0; j < numFrames[i]; j++) {
						bi[j] = spritesheet.getSubimage(
							j * frameWidths[i],
							count,
							frameWidths[i],
							frameHeights[i]
						);
					}
					sprites.add(bi);
					count += frameHeights[i];
				}
				
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE_DOWN;
		animation.setFrames(sprites.get(IDLE_DOWN));
		animation.setDelay(7);
	}
	
	private void playerDetect(Player p) {
		/*if(!attacking && attackCooldownTick >= attackCooldownTimer) {
			if(p.gety() < y + (height / 2) && p.gety() + p.getHeight() > y + (height / 2)) {
				// Player is to the left of the enemy
				if(p.getx() < x + (width / 2)) {
					setOnlyFalse();
					setFacingLeft();
					setAttacking(true);
				}
				// Player is to the right of the enemy
				else {
					setOnlyFalse();
					setFacingRight();
					setAttacking(true);
				}
			}
			else if(p.getx() < x + (width / 2) && p.getx() + p.getWidth() > x + (width / 2)) {
				// Player is above the enemy
				if(p.gety() < y + (height / 2)) {
					setOnlyFalse();
					setFacingUp();
					setAttacking(true);
				}
				// Player is below the enemy
				else {
					setOnlyFalse();
					setFacingDown();
					setAttacking(true);
				}	
			}
		}*/
		
		if(!attacking && attackCooldownTick >= attackCooldownTimer) {
			if(Math.abs(p.getx() - x) > Math.abs(p.gety() - y)) {
				// Player is to the left of the enemy
				if(p.getx() < x + (width / 2)) {
					setOnlyFalse();
					setFacingLeft();
					setAttacking(true);
				}
				// Player is to the right of the enemy
				else {
					setOnlyFalse();
					setFacingRight();
					setAttacking(true);
				}
			}
			else {
				// Player is above the enemy
				if(p.gety() < y + (height / 2)) {
					setOnlyFalse();
					setFacingUp();
					setAttacking(true);
				}
				// Player is below the enemy
				else {
					setOnlyFalse();
					setFacingDown();
					setAttacking(true);
				}	
			}
		}
		
	}
	
	private void setAttacking(boolean b) {
		attacking = b;
	}
	
	public int getAttackCooldown(int i) { return attackCooldownTimer; }
	public void setAttackCooldown(int i) { attackCooldownTimer = i; }
	
	public ArrayList<EnemyProjectile> getEProjectiles() { return eProjectiles; }
	
	private void setAnimation(int i) {
		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
		//animation.setDelay(spriteDelays[currentAction]);
		width = frameWidths[currentAction];
		height = frameHeights[currentAction];
	/*	cwidth = frameCWidths[currentAction];
		cheight = frameCHeights[currentAction];*/
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
	}
	
	public void update() {
		
		/*// Update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);*/
		
		playerDetect(p);
		
		attackCooldownTick++;
		
		if(attacking) {
			if(facingUp) {
				if(currentAction != ATTACKING_UP) {
					setAnimation(ATTACKING_UP);
					animation.setDelay(10);
				}
			}
			else if(facingDown) {
				if(currentAction != ATTACKING_DOWN) {
					setAnimation(ATTACKING_DOWN);
					animation.setDelay(10);
				}
			}
			else if(facingLeft || facingRight) {
				if(currentAction != ATTACKING_HORIZONTAL) {
					setAnimation(ATTACKING_HORIZONTAL);
					animation.setDelay(10);
				}
			}
			
			if(animation.getFrame() == 4 && animation.getCount() == 0) {
				
				EnergyOrbs eo = new EnergyOrbs(tileMap, 1);
				
				int eoX = 0;
				int eoY = 0;
				
				switch(randomInt(1,2)) {
					case 1:
						eoX = p.getx() - randomInt(5,10);
						break;
					case 2:
						eoX = p.getx() + p.getCWidth() + randomInt(5,10);
						break;
				}
				
				eoY = p.gety() + randomInt(-10, p.getCHeight() + 10);
				
				eo.setPosition(eoX, eoY);
			
				eProjectiles.add(eo);
			}
			
			if(animation.getFrame() == animation.getNumFrames() - 1/*animation.hasPlayedOnce()*/) {
				setAttacking(false);
				attackCooldownTick = 0;
				attackTick = 0;
				
				int nextDirection = randomInt(1,4);
				
				switch(nextDirection) {
					case 1: {
						setAllUp();
						break;
					}
					case 2: {
						setAllDown();
						break;
					}
					case 3: {
						setAllLeft();
						break;
					}
					case 4: {
						setAllRight();
						break;
					}
				}
			}
		}
		else {
			if(facingRight || facingLeft) {
				if(dx == 0) {
					if(currentAction != IDLE_HORIZONTAL) {
						setAnimation(IDLE_HORIZONTAL);
						animation.setDelay(7);
					}
				}
				else {
					if(currentAction != WALKING_HORIZONTAL) {
						setAnimation(WALKING_HORIZONTAL);
						animation.setDelay(7);
					}
				}
			}
			else if(facingUp) {
				if(dy == 0) {
					if(currentAction != IDLE_UP) {
						setAnimation(IDLE_UP);
						animation.setDelay(7);
					}
				}
				else {
					if(currentAction != WALKING_UP) {
						setAnimation(WALKING_UP);
						animation.setDelay(7);
					}
				}
			}
			else if(facingDown) {
				if(dy == 0) {
					if(currentAction != IDLE_DOWN) {
						setAnimation(IDLE_DOWN);
						animation.setDelay(7);
					}
				}
				else {
					if(currentAction != WALKING_DOWN) {
						setAnimation(WALKING_DOWN);
						animation.setDelay(7);
					}
				}
			}
		}
		
		if(!attacking) {
			if(up && dy == 0) {
				setAllDown();
			}
			else if(down && dy == 0) {
				setAllUp();
			}
			else if(left && dx == 0) {
				setAllRight();
			}
			else if(right && dx == 0) {
				setAllLeft();
			}
		}
		
		// Update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// Update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g){
		
		//if(notOnScreen()) return;
		
		setMapPosition();
		
		if(facingLeft){
			g.drawImage(animation.getImage(), (int)(x + xmap - frameWidths[currentAction] / 2 + frameWidths[currentAction]), (int)(y + ymap - frameHeights[currentAction] / 2), -frameWidths[currentAction], frameHeights[currentAction], null);
		}
		else{
			g.drawImage(animation.getImage(), (int)(x + xmap - frameWidths[currentAction] / 2), (int)(y + ymap - frameHeights[currentAction] / 2), null);
		}
		
		/*// Draw collision box
		Rectangle r = getRectangle();
		r.x += xmap;
		r.y += ymap;
		g.draw(r);*/
	}

}