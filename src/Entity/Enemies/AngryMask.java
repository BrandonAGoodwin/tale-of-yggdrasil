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

public class AngryMask extends Enemy {
	
	boolean charging = false;
	boolean shooting = false;
	int chargeTimer = 45;
	int chargeTick = 0;
	int chargeCooldownTimer = 55;
	int chargeCooldownTick = 0;
	int shootTimer = 70;
	int shootTick = 0;
	
	private ArrayList<BufferedImage[]> sprites;
	
	private final int[] numFrames = {
		4, 4, 4, 6, 6, 6
	};
	private final int[] frameWidths = {
			32, 32, 32, 32, 32, 45
	};
	private final int[] frameHeights = {
			36, 32, 36, 47, 34, 42
	};
	
	private final static int DOWN = 0;
	private final static int UP = 1;
	private final static int HORIZONTAL = 2;
	private final static int CHARGING_DOWN = 3;
	private final static int CHARGING_UP = 4;
	private final static int CHARGING_HORIZONTAL = 5;
	
	private Player p;
	
	public AngryMask (TileMap tm, Player p) {
		
		super(tm);
		
		this.p = p;
		
		moveSpeed = 0.6;
		maxSpeed = 1.7;
		
		width = 32;
		height = 32;
		cwidth = 32;
		cheight = 32;
		
		dropRate = 5;
		dropRarityMin = 0;
		dropRarityMax = 2;
		
		setAllUp();
		
		health = maxHealth = 21;
		damage = 1;
		
		// Load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(
					getClass().getResourceAsStream(
						"Resources/Sprites/Enemies/AngryMaskSprites0.gif"
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

		if(!charging && chargeCooldownTick >= chargeCooldownTimer) {
			if(p.gety() < y + (height / 2) && p.gety() + p.getHeight() > y + (height / 2)) {
				// player is to the left of the enemy
				if(p.getx() < x + (width / 2)) {
					setOnlyFalse();
					setFacingLeft();
					setCharging(true);
				}
				// Player is the the right of the enemy
				else {
					setOnlyFalse();
					setFacingRight();
					setCharging(true);
				}	
			}
			else if(p.getx() < x + (width / 2) && p.getx() + p.getWidth() > x + (width / 2)) {
				// Player is above the enemy
				if(p.gety() < y + (height / 2)) {
					setOnlyFalse();
					setFacingUp();
					setCharging(true);
				}
				// Player is below the enemy
				else {
					setOnlyFalse();
					setFacingDown();
					setCharging(true);
				}
			}
		}
	}

	private void setCharging(boolean b) {
		charging = b;
	}
		
	private void setAnimation(int i) {
		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
		//animation.setDelay(spriteDelays[currentAction]);
		width = frameWidths[currentAction];
		height = frameHeights[currentAction];
		/*cwidth = frameCWidths[currentAction];
		cheight = frameCHeights[currentAction];*/
	}

	private void getNextPosition() {
		
		// Movement
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
		
		chargeCooldownTick++;
		
		if(charging) {
			
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
				moveSpeed = 0.2;
				maxSpeed = 1;
				setCharging(false);
				chargeTick = 0;
				chargeCooldownTick = 0;
			}
		}
		
		if(!charging || chargeTick < chargeTimer) {

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
			
		if(animation.hasPlayed(2) && !charging) {
		
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
		
		if(!charging) {
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
		
		// update animation
		animation.update();
		
	}
	
	public void draw(Graphics2D g) {
		
		//if(notOnScreen()) return;
		
		setMapPosition();
		
		if(facingLeft) {
			g.drawImage(animation.getImage(), (int)(x + xmap - frameWidths[currentAction] / 2 + frameWidths[currentAction]), (int)(y + ymap - frameHeights[currentAction] / 2), -frameWidths[currentAction], frameHeights[currentAction], null);
		}
		else {
			g.drawImage(animation.getImage(), (int)(x + xmap - frameWidths[currentAction] / 2), (int)(y + ymap - frameHeights[currentAction] / 2), null);
		}
		
		/*// Draw collision box
		Rectangle r = getRectangle();
		r.x += xmap;
		r.y += ymap;
		g.draw(r);*/
		
	}
}