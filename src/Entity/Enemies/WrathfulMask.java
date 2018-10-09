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

public class WrathfulMask extends Enemy {
	
	boolean charging = false;
	boolean shooting = false;
	int chargeTimer = 20;
	int chargeTick = 0;
	int chargeCooldownTimer = 55;
	int chargeCooldownTick = 0;
	int shootTimer = 70;
	int shootTick = 0;
	
	boolean attacking;
	int attackCooldownTick = 0;
	int attackCooldownTimer = 70;
	int attackTick = 0;
	int attackTimer = 100;
	int attackCount = 0;
	int attackMax = 8;
	
	private ArrayList<BufferedImage[]> sprites;
	
	private final int[] numFrames = {
		4, 4, 4, 6, 6, 6, 7, 7, 7	
	};
	private final int[] frameWidths = {
		32, 32, 32, 32, 32, 45, 32, 32, 43
	};
	private final int[] frameHeights = {
		36, 32, 36, 47, 34, 42, 47, 39, 44
	};
	
	private final static int DOWN = 0;
	private final static int UP = 1;
	private final static int HORIZONTAL = 2;
	private final static int CHARGING_DOWN = 3;
	private final static int CHARGING_UP = 4;
	private final static int CHARGING_HORIZONTAL = 5;
	private final static int SHOOTING_DOWN = 6;
	private final static int SHOOTING_UP = 7;
	private final static int SHOOTING_HORIZONTAL = 8;
	
	private Player p;
	private ArrayList<EnemyProjectile> eProjectiles;
	
	public WrathfulMask (TileMap tm, Player p, ArrayList<EnemyProjectile> eProjectiles) {
		
		super(tm);
		
		this.p = p;
		this.eProjectiles = eProjectiles;
		
		moveSpeed = 0.5;
		maxSpeed = 1.4;
		
		width = 32;
		height = 32;
		cwidth = 32;
		cheight = 64;
		
		dropRate = 5;
		dropRarityMin = 0;
		dropRarityMax = 2;
		
		setAllUp();
		
		health = maxHealth = 70;
		damage = 1;
		
		// Load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
						"/Sprites/Enemies/WrathfulMaskSprites0.gif"
					)
				);
				
				int count = 4;
				sprites = new ArrayList<BufferedImage[]>();
				for(int i = 0; i < numFrames.length; i++) {
					BufferedImage[] bi = new BufferedImage[numFrames[i]];
					for(int j = 0; j < numFrames[i]; j++) {
						bi[j] = spritesheet.getSubimage(
							4 + (j * frameWidths[i]) + (j * 8),
							count,
							frameWidths[i],
							frameHeights[i]
						);
					}
					sprites.add(bi);
					count += frameHeights[i] + 8;
				}
				
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = DOWN;
		animation.setFrames(sprites.get(DOWN));
		animation.setDelay(7);
	}
	
	private void playerDetect(Player p) {
		
		if(attackCooldownTick >= attackCooldownTimer && !attacking) {
			int roll = randomInt(1, 2);
			if(shootTick >= shootTimer) {
				//System.out.println(roll);
				if(roll == 1) {
					//System.out.println("Shoot");
					if(p.gety() < y + (height / 2) && p.gety() + p.getHeight() > y + (height / 2)) {
						// Player is to the left of the enemy
						if(p.getx() < x + (width / 2)) {
							setOnlyFalse();
							setFacingLeft();
							setShooting(true);
						}
						// Player is to the right of the enemy
						else {
							setOnlyFalse();
							setFacingRight();
							setShooting(true);
						}
					}
					else if(p.getx() < x + (width / 2) && p.getx() + p.getWidth() > x + (width / 2)) {
						// Player is above the enemy
						if(p.gety() < y + (height / 2)) {
							setOnlyFalse();
							setFacingUp();
							setShooting(true);
						}
						// Player is below the enemy
						else {
							setOnlyFalse();
							setFacingDown();
							setShooting(true);
						}	
					}
				}
			}
			
			if(!charging && chargeCooldownTick >= chargeCooldownTimer) {
				if(roll == 2) {
					System.out.println("Charge");
					if(p.gety() < y + (height / 2) && p.gety() + p.getHeight() > y + (height / 2)) {
						// player is to the left of the enemy
						if(p.getx() < x + (width / 2)) {
							setOnlyFalse();
							setFacingLeft();
							setCharging(true);
						}
						else {
							setOnlyFalse();
							setFacingRight();
							setCharging(true);
						}
						
					}
					
					else if(p.getx() < x + (width / 2) && p.getx() + p.getWidth() > x + (width / 2))  {
						
						if(p.gety() < y + (height / 2)) {
							setOnlyFalse();
							setFacingUp();
							setCharging(true);
						}
						else {
							setOnlyFalse();
							setFacingDown();
							setCharging(true);
						}
					}
				}
			}
		}
	}
	
	//private void setAttacking(boolean b) { attacking = b; }
	
	private void setCharging(boolean b) { 
		charging = b;
		attacking = b;
	}
	
	private void setShooting(boolean b) {
		shooting = b;
		attacking = b;
		System.out.println("Shooting: " + shooting + attacking);
	}
	
	public ArrayList<EnemyProjectile> getEProjectiles() { return eProjectiles; }
	
	private void setAnimation(int i) {
		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
		//animation.setDelay(spriteDelays[currentAction]);
		width = frameWidths[currentAction];
		height = frameHeights[currentAction];
	/*	cwidth = frameCWidths[currentAction];
		cheight = frameCHeights[currentAction];*/
		System.out.println("Change");
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
		
		shootTick++;
		attackCooldownTick++;
		chargeCooldownTick++;
		
		//shootTick++;
		// Sets shooting animations
		if(shooting && !charging) {
			if(facingUp) {
				if(currentAction != SHOOTING_UP) {
					setAnimation(SHOOTING_UP);
					animation.setDelay(4);
				}
			}
			else if(facingDown) {
				if(currentAction != SHOOTING_DOWN) {
					setAnimation(SHOOTING_DOWN);
					animation.setDelay(4);
				}
			}
			else if(facingLeft || facingRight) {
				if(currentAction != SHOOTING_HORIZONTAL) {
					setAnimation(SHOOTING_HORIZONTAL);
					animation.setDelay(4);
				}
			}
			
			
			// Shoots the fireball on the 5th frame of the animation
			if(animation.getFrame() == 5 && animation.getCount() == 0) {
				System.out.println("FRAME");
				
				CursedFireball cf = new CursedFireball(tileMap, 2);
				cf.setPosition(x, y);
				
				if(facingUp) {
					cf.setVector(0, -2);
				}
				if(facingDown) {
					cf.setVector(0, 2);
				}
				if(facingLeft) {
					cf.setVector(-2, 0);
				}
				if(facingRight) {
					cf.setVector(2, 0);
				}
				eProjectiles.add(cf);
			}
			
			if(animation.hasPlayedOnce()) {
				setShooting(false);
				shootTick = 0;
				attackCooldownTick = 0;
				
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
/*		else {
			if(facingRight || facingLeft) {
				if(currentAction != HORIZONTAL) {
					setAnimation(HORIZONTAL);
					animation.setDelay(7);
				}
			}
			else if(facingUp) {
				if(currentAction != UP) {
					setAnimation(UP);
					animation.setDelay(7);
				}
			}
			else if(facingDown) {
				if(currentAction != DOWN) {
					setAnimation(DOWN);
					animation.setDelay(7);
				}
			}
		
		}*/
		
		if(charging && !shooting) {
			
			chargeTick++;
			if(chargeTick < chargeTimer) {}
			else {
				moveSpeed = 1;
				maxSpeed = 3.7;
				if(facingRight || facingLeft) {
					if(currentAction != CHARGING_HORIZONTAL) {
						currentAction = CHARGING_HORIZONTAL;
						animation.setFrames(sprites.get(CHARGING_HORIZONTAL));
						animation.setDelay(8);
					}
					if(facingRight) {
						setOnlyRight();
					}
					else {
						setOnlyLeft();
					}
				}
				else if(facingUp) {
					if(currentAction != CHARGING_UP) {
						currentAction = CHARGING_UP;
						animation.setFrames(sprites.get(CHARGING_UP));
						animation.setDelay(8);
						setOnlyUp();
					}
				}
				else if(facingDown) {
					if(currentAction != CHARGING_DOWN) {
						currentAction = CHARGING_DOWN;
						animation.setFrames(sprites.get(CHARGING_DOWN));
						animation.setDelay(8);
						setOnlyDown();
					}
				}
			}
			
			if(dy == 0 && dx == 0 && chargeTick > chargeTimer + 1) {
				moveSpeed = 0.5;
				maxSpeed = 1.4;
				setCharging(false);
				chargeTick = 0;
				chargeCooldownTick = 0;
				attackCooldownTick = 0;
			}
		}
		
		if(!attacking /*|| chargeTick < chargeTimer*/) {
			
			if(facingRight || facingLeft) {
				if(currentAction != HORIZONTAL) {
					setAnimation(HORIZONTAL);
					animation.setDelay(7);
				}
			}
			else if(facingUp) {
				if(currentAction != UP) {
					setAnimation(UP);
					animation.setDelay(7);
				}
			}
			else if(facingDown) {
				if(currentAction != DOWN) {
					setAnimation(DOWN);
					animation.setDelay(7);
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
		
		// Update animation
		animation.update();
		
		// Update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
	}
	
	public void draw(Graphics2D g){
		
		//if(notOnScreen()) return;
		
		setMapPosition();
		
		if(facingLeft){
			g.drawImage(animation.getImage(), (int)(x + xmap - (frameWidths[currentAction] * 2) / 2 + (frameWidths[currentAction] * 2)), (int)(y + ymap - (frameHeights[currentAction] * 2) / 2), -(frameWidths[currentAction] * 2), (frameHeights[currentAction] * 2), null);
		}
		else{
			g.drawImage(animation.getImage(), (int)(x + xmap - (frameWidths[currentAction] * 2) / 2), (int)(y + ymap - (frameHeights[currentAction] * 2) / 2), (frameWidths[currentAction] * 2), (frameHeights[currentAction] * 2), null);
		}
		
		/*// Draw collision box
		Rectangle r = getRectangle();
		r.x += xmap;
		r.y += ymap;
		g.draw(r);*/
	}

}