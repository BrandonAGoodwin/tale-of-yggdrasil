package Entity;

import TileMap.*;

import java.util.ArrayList;

import javax.imageio.ImageIO;

import Audio.AudioPlayer;
import Main.GamePanel;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Player extends MapObject {
	
	// Player stuff
	private int health;
	private int maxHealth;
	private boolean flinching;
	private long flinchTimer;
	private boolean dead;
	
	// Shots
	private boolean shooting;
	private boolean horizontal;
	private boolean rightOrUp;
	private int shotDamage;
	private String shotType;
	private double shotSpeed;
	private ArrayList<Shot> shots;
	private int shotCooldown;
	private int shotTick;
	
	// Shot types
	private String basicShot = "basicShot";
	
	// Shot modifications
	private boolean snakingShot;
	private boolean supportShot;
	private int supportShotTick = 0;
	private int supportShotCooldown = 3;
	private boolean vampiricShot;
	private boolean slowShot;
	private double slowPercent;
	private boolean powerShot;
	private int shotSize;
	private boolean thorns;
	private boolean piercingShot;
	
	// Animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {
		1, 1, 1, 6, 6, 6, 1, 1, 1, 6, 6, 6, 5, 5, 5
	};
	
	// Animation actions
	private static final int IDLE_HORIZONTAL = 0;
	private static final int IDLE_DOWN = 1;
	private static final int IDLE_UP = 2;
	private static final int WALKING_HORIZONTAL = 3;
	private static final int WALKING_DOWN = 4;
	private static final int WALKING_UP = 5;
	private static final int SHOOTING_IDLE_HORIZONTAL = 6;
	private static final int SHOOTING_IDLE_DOWN = 7;
	private static final int SHOOTING_IDLE_UP = 8;
	private static final int SHOOTING_WALKING_HORIZONTAL = 9;
	private static final int SHOOTING_WALKING_DOWN = 10;
	private static final int SHOOTING_WALKING_UP = 11;
	private static final int DYING_HORIZONTAL = 12;
	private static final int DYING_DOWN = 13;
	private static final int DYING_UP = 14;
	
	public Player(TileMap tm) {
		
		super(tm);
		
		width = 32;
		height = 32;
		// Collision box width and height
		cwidth = 17;
		cheight = 26;
		
		collideable = false;
		
		moveSpeed = 0.6;
		maxSpeed = 1.7;
		/*moveSpeed = 3.0;
		maxSpeed = 6.0;*/
		stopSpeed = 0.4;
		
		setFacingDown();
		
		health = maxHealth = 6;
		if(GamePanel.GOD_MODE == true) {
			health = maxHealth = 1000;
		}
		shotDamage = 3;
		/*shotDamage = 100;*/
		shotType = basicShot;
		shotSpeed = 3;
		shots = new ArrayList<Shot>();
		shotCooldown = 30;
		/*shotCooldown = 1;*/
		shotTick = 0;
		shotSize = 1;
		
		// Shot modifications
		snakingShot = false;
		vampiricShot = false;
		powerShot = false;
		
		thorns = false;
		
		// Load sprites
		try{
			
			BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/Sprites/Player/PlayerSpritesV7.gif"));
			
			sprites = new ArrayList<BufferedImage[]>(); 
			for(int i = 0; i < 15; i++){
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
		currentAction = IDLE_DOWN;
		animation.setFrames(sprites.get(IDLE_DOWN));
		animation.setDelay(10);
		
		// Loading in all the player sound effects
		AudioPlayer.load("/SFX/YouDied(FadeOut).mp3", "playerDeath");
		AudioPlayer.load("/SFX/Grunt1.mp3", "grunt1");
		AudioPlayer.load("/SFX/Grunt2.mp3", "grunt2");
		AudioPlayer.load("/SFX/Grunt3.mp3", "grunt3");
		AudioPlayer.load("/SFX/scratch.mp3", "shoot");
		
	}

	public int getHealth() { return health; }
	public int getMaxHealth() { return maxHealth; }
	
	public void setHealth(int health) {
		this.health = health;
	}
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}
	
	public int getCurrentAction() { return currentAction; }
	
	public void setShooting(boolean b, boolean horizontal, boolean rightOrUp) {
		shooting = b;
		this.horizontal = horizontal;
		this.rightOrUp = rightOrUp;
	}
	
	public int getShotDamage() { return shotDamage; }
	public double getShotSpeed() { return shotSpeed; }
	public int getShotCooldown() { return shotCooldown; }
	
	public void setShotDamage(int shotDamage) { this.shotDamage = shotDamage; }
	
	public void setShotType(String shotType) { this.shotType = shotType; }
	
	public void setShotSpeed(double shotSpeed) {  this.shotSpeed = shotSpeed; }
	
	public void setShotCooldown(int shotCooldown) { this.shotCooldown = shotCooldown; }
	
	public void setSnakingShot(boolean b) { snakingShot = b; }
	
	public void setSupportShot(boolean b) { supportShot = b; }
	
	public void setVampiricShot(boolean b) { vampiricShot = b; }
	
	public void setSlowShot(boolean b) {
		slowShot = b;
		slowPercent = 50;
	}
	public boolean getSlowShot() { return slowShot; }
	public void increaseSlowPercentage() {	slowPercent += 10; }
	
	public void setPowerShot() { powerShot = true; }
	public void increaseShotSize() { shotSize = 2; }
	public boolean getPowerShot() { return powerShot; }
	
	public void setThorns(boolean b) { thorns = b; }
	public boolean getThorns() { return thorns; }
	
	public void setPiercingShot(boolean b) { piercingShot = b; }
	public boolean getPiercingShot() { return piercingShot; }
	
	public void setDead() {
		health = 0;
		dead = true;
		// Plays the dying animation
		if(facingRight || facingLeft) {
			if(currentAction != DYING_HORIZONTAL){
				currentAction = DYING_HORIZONTAL;
				animation.setFrames(sprites.get(DYING_HORIZONTAL));
				animation.setDelay(8);
			}
		}
		else if(facingUp) {
			if(currentAction != DYING_UP){
				currentAction = DYING_UP;
				animation.setFrames(sprites.get(DYING_UP));
				animation.setDelay(8);
			}
		}
		else if(facingDown) {
			if(currentAction != DYING_DOWN){
				currentAction = DYING_DOWN;
				animation.setFrames(sprites.get(DYING_DOWN));
				animation.setDelay(8);
			}
		}
		// Player death SFX
		AudioPlayer.play("playerDeath");
		stop();
	}
	
	public void checkAttack(ArrayList<Enemy> enemies, ArrayList<EnemyProjectile> eProjectiles) {
		
		// Goes through each enemy in the enemy array list
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i);
			
			// Shots
			// Checks if any shots intersect any of the enemies
			for(int j = 0; j < shots.size(); j++) {
				if(shots.get(j).intersects(e)/*(!e.getPierced() || shots.get(j).getPiercing())*/) {
					if(!shots.get(j).getPiercingShot()) {
						e.hit(shotDamage, shots.get(j).getHit(), shots.get(j).getSlowing(), slowPercent, shots.get(j));
						shots.get(j).setHit();
					}
					else { 
						 if(e.getTempShot() != shots.get(j)) {
							 e.hit(shotDamage, shots.get(j).getHit(), shots.get(j).getSlowing(), slowPercent, shots.get(j));
							 shots.get(j).setHit();
						 }
					}
				}
			}
			
			// Check for enemy collision
			if(intersects(e)) {
				hit(e);
			}
			
		}
		
		// Checks if any of the enemy projectiles intersect the player
		for(int i = 0; i < eProjectiles.size(); i++) {
			
			EnemyProjectile ep = eProjectiles.get(i);
			
			if(intersects(ep) && !ep.hit) {
				hit(ep.getDamage());
				ep.setHit();
			}
		}
	}
	
	public void checkDoors(ArrayList<Door> doors) {
		
		super.checkDoors(doors);
		
		// Checks if the player shots collide with the doors
		for(int i = 0; i < doors.size(); i++) {
			
			Door d = doors.get(i);
			
			// Make shots collide with doors
			for(int j = 0; j < shots.size(); j++) {
				if(shots.get(j).intersects(d)) {
					shots.get(j).setHit();
				}
			}
		}
	}
	
	// Checks if the player intersects the item
	public void checkItem(ArrayList<Item> items) {
		
		for(int i = 0; i < items.size(); i++) {
			
			Item it = items.get(i);
			// If the player intersects the item,
			// the item is activated
			if(intersects(it)) {
				it.setActivated();
			}
		}
	}
	
	public void hit(Enemy e) {
		if(flinching || e.getDamage() == 0) return;
		if(thorns == true) {
			e.hit(shotDamage);
		}
		hit(e.getDamage());
	}
	
	public void hit(int damage) {
		// If the player is flinching he doesn't take damage
		if(flinching) return;
		if(damage == 0) return;
		// Plays a random grunt SFX
		int grunt = randomInt(1, 3);
		//System.out.println("Grunt is " + grunt);
		
		health -= damage;
		if(health > 0) {
			switch(grunt) {
				case 1: { 
					AudioPlayer.play("grunt1");
					break;
				}
				case 2: {
					AudioPlayer.play("grunt2");
					break;
				}
				case 3: {
					AudioPlayer.play("grunt3");
					break;
				}
			}
		}
		
		if(health < 0) health = 0;
		// Starts the flinching timer
		flinching = true;
		flinchTimer = System.nanoTime();
	}
	
	// Sets the player's health to full and sets the animation to null;
	public void reset() {
		health = maxHealth;
		facingRight = true;
		currentAction = -1;
		stop();
	}
	
	// Stops any player actions (Moving, shooting and flinching)
	public void stop() {
		left = right = up = down = flinching = shooting = false;
	}
	
	private void getNextPosition() {
		
		// Movement
		// Horizontal
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		
		// Vertical
		if(up) {
			dy -= moveSpeed;
			if(dy < -maxSpeed) {
				dy = -maxSpeed;
			}
		}
		else if(down) {
			dy += moveSpeed;
			if(dy > maxSpeed){
				dy = maxSpeed;
			}
		}
		else {
			if(dy > 0) {
				dy -= stopSpeed;
				if(dy < 0) {
					dy = 0;
				}
			}
			else if(dy < 0) {
				dy += stopSpeed;
				if(dy > 0) {
					dy = 0;
				}
			}
		}
	}
	

	public void update() {
		
		// Update Position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		shotTick++;
		
		// The player is shooting
		if(shooting) {
			/*System.out.println("Horizontal: " + horizontal + " | RightOrUp: " + rightOrUp);
			System.out.println("Up: " + up + " | Down: " + down + " | Left: " + left + " | Right: " + right);*/
			if(shotTick > shotCooldown) {
				// Creating a new shot
				Shot fb = new Shot(tileMap, horizontal, rightOrUp, shotType, shotSpeed, dx, dy, snakingShot, slowShot, slowPercent, shotSize, piercingShot);
				fb.setPosition(x, y);
				shots.add(fb);
				AudioPlayer.play("shoot");
				
				shotTick = 0;
				if(supportShot) {
					supportShotTick++;
					if(supportShotTick == supportShotCooldown) {
						Shot sfb = new Shot(tileMap, horizontal, !rightOrUp, shotType, shotSpeed, dx, dy, snakingShot, slowShot, slowPercent, shotSize, piercingShot); // Just import player and use p.dx and p.dy for example?
						sfb.setPosition(x, y);
						shots.add(sfb);
						supportShotTick = 0;
					}
				}
			}
		}
		
		// Update shots
		for(int i = 0; i < shots.size(); i++) {
			shots.get(i).update();
			if(shots.get(i).shouldRemove()) {
				shots.remove(i);
				i--;
			}
		}
		
		// Check done flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 500) {
				flinching = false;
			}
		}
		
		if(!dead) { // Stops another animation playing if the player is dead
			if(shooting) {
				// If the player is shooting, the shooting versions
				// walking and idle animations are used instead
				if(horizontal) {
					if(rightOrUp) {
						if(right || up || down) {
							if(currentAction != SHOOTING_WALKING_HORIZONTAL) {
								currentAction = SHOOTING_WALKING_HORIZONTAL;
								animation.setFrames(sprites.get(SHOOTING_WALKING_HORIZONTAL));
								animation.setDelay(7);
							}
						}
						else if(left) {
							// If the player is walking left and shooting right the horizontal 
							// Walking animation is played in reverse
							if(currentAction != SHOOTING_WALKING_HORIZONTAL) {
								currentAction = SHOOTING_WALKING_HORIZONTAL;
								animation.setReverseFrames(sprites.get(SHOOTING_WALKING_HORIZONTAL));
								animation.setDelay(7);
							}
						}
						else {
							if(currentAction != SHOOTING_IDLE_HORIZONTAL) {
								currentAction = SHOOTING_IDLE_HORIZONTAL;
								animation.setFrames(sprites.get(SHOOTING_IDLE_HORIZONTAL));
								animation.setDelay(7);
							}
						}
						setFacingRight();
					}
					else {
						if(left || up || down) {
							if(currentAction != SHOOTING_WALKING_HORIZONTAL) {
								currentAction = SHOOTING_WALKING_HORIZONTAL;
								animation.setFrames(sprites.get(SHOOTING_WALKING_HORIZONTAL));
								animation.setDelay(7);
							}
						}
						else if(right) {
							// If the player is walking right and shooting left the horizontal 
							// Walking animation is played in reverse
							if(currentAction != SHOOTING_WALKING_HORIZONTAL) {
								currentAction = SHOOTING_WALKING_HORIZONTAL;
								animation.setReverseFrames(sprites.get(SHOOTING_WALKING_HORIZONTAL));
								animation.setDelay(7);
							}
						}
						else {
							if(currentAction != SHOOTING_IDLE_HORIZONTAL) {
								currentAction = SHOOTING_IDLE_HORIZONTAL;
								animation.setFrames(sprites.get(SHOOTING_IDLE_HORIZONTAL));
								animation.setDelay(7);
							}
						}
						setFacingLeft();
					}
				}
				else {
					if(rightOrUp) {
						if(up || right || left) {
							if(currentAction != SHOOTING_WALKING_UP) {
								currentAction = SHOOTING_WALKING_UP;
								animation.setFrames(sprites.get(SHOOTING_WALKING_UP));
								animation.setDelay(7);
							}
						}
						else if(down) {
							// If the player is walking down and shooting up the up
							// Walking animation is played in reverse
							if(currentAction != SHOOTING_WALKING_UP) {
								currentAction = SHOOTING_WALKING_UP;
								animation.setReverseFrames(sprites.get(SHOOTING_WALKING_UP));
								animation.setDelay(7);
							}
						}
						else {
							if(currentAction != SHOOTING_IDLE_UP) {
								currentAction = SHOOTING_IDLE_UP;
								animation.setFrames(sprites.get(SHOOTING_IDLE_UP));
								animation.setDelay(7);
							}
						}
					}
					else {
						if(down || right || left) {
							if(currentAction != SHOOTING_WALKING_DOWN) {
								currentAction = SHOOTING_WALKING_DOWN;
								animation.setFrames(sprites.get(SHOOTING_WALKING_DOWN));
								animation.setDelay(7);
							}
						}
						else if(up) {
							// If the player is walking up and shooting down the down
							// Walking animation is played in reverse
							if(currentAction != SHOOTING_WALKING_DOWN) {
								currentAction = SHOOTING_WALKING_DOWN;
								animation.setReverseFrames(sprites.get(SHOOTING_WALKING_DOWN));
								animation.setDelay(7);
							}
						}
						else {
							if(currentAction != SHOOTING_IDLE_DOWN) {
								currentAction = SHOOTING_IDLE_DOWN;
								animation.setFrames(sprites.get(SHOOTING_IDLE_DOWN));
								animation.setDelay(7);
							}
						}
					}
					
				}
			}
			else if(up) {
				if(currentAction != WALKING_UP) {
					currentAction = WALKING_UP;
					animation.setFrames(sprites.get(WALKING_UP));
					animation.setDelay(7);
				}
			}
			else if(down) {
				if(currentAction != WALKING_DOWN) {
					currentAction = WALKING_DOWN;
					animation.setFrames(sprites.get(WALKING_DOWN));
					animation.setDelay(7);
				}
			}
			else if(left || right){
				if(currentAction != WALKING_HORIZONTAL) {
					currentAction = WALKING_HORIZONTAL;
					animation.setFrames(sprites.get(WALKING_HORIZONTAL));
					animation.setDelay(7);
				}
			}
			else {
				if(facingRight || facingLeft) {
					if(currentAction != IDLE_HORIZONTAL) {
						currentAction = IDLE_HORIZONTAL;
						animation.setFrames(sprites.get(IDLE_HORIZONTAL));
						animation.setDelay(40);
					}
				}
				else if(facingUp) {
					if(currentAction != IDLE_UP) {
						currentAction = IDLE_UP;
						animation.setFrames(sprites.get(IDLE_UP));
						animation.setDelay(40);
					}
				}
				else if(facingDown) {
					if(currentAction != IDLE_DOWN) {
						currentAction = IDLE_DOWN;
						animation.setFrames(sprites.get(IDLE_DOWN));
						animation.setDelay(40);
					}
				}
			}
		}
		else {
			// If the player isn't walking or shooting, then the animation
			// is played and frozen on the last frame
			if(animation.getFrame() == animation.getNumFrames() - 1) {
				animation.setDelay(-1);
			}
		}
			
		animation.update();
		
		// Sets the player direction to the direction of shooting
		if(shooting) {
			if(horizontal) {
				if(rightOrUp) {
					setFacingRight();
				}
				else {
					setFacingLeft();
				}
			}
			else {
				if(rightOrUp) {
					setFacingUp();
				}
				else {
					setFacingDown();
				}
			}
		}
		// If the player isn't shooting,
		// he faces the direction he is walking in
		else {
			if(right) setFacingRight();
			if(left) setFacingLeft();
			if(up) setFacingUp();
			if(down) setFacingDown();
		}
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		// Draw shots
		for(int i = 0; i < shots.size(); i++) {
			shots.get(i).draw(g);
		}
		
		// Draw player
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) return; 
		}
		
		// If the player is facing left then the animation
		// image is flipped horizontally
		if(facingLeft) {
			g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2 + width),
					(int)(y + ymap - height / 2), 
					-width, height,
					null);
		}
		else{
			g.drawImage(
					animation.getImage(),
					(int)(x + xmap - width / 2),
					(int)(y + ymap - height / 2),
					width, height,
					null);
		}
		
		/*// Draw collision box
		Rectangle r = getRectangle();
		r.x += xmap;
		r.y += ymap;
		g.draw(r);*/
	}
	
}