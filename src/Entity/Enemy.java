package Entity;

import TileMap.TileMap;

public class Enemy extends MapObject {
	
	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	protected boolean remove;
	
	protected boolean ranged;
	
	protected boolean slowed;
	protected int slowTick;
	protected int slowTimer;
	
	protected boolean pierced = false;
	protected Shot tempShot;
	
	protected int dropRate;
	protected int dropRarityMin;
	protected int dropRarityMax;
	
	protected boolean flinching;
	protected long flinchTimer;
	
	protected double tempMoveSpeed;
	protected double tempMaxSpeed;
	
	public Enemy(TileMap tm){
		super(tm);
		remove = false;
	}
	
	public boolean isDead() { return dead; }
	
	public int getDamage() { return damage; }
	
	public int getHealth() { return health; }
	public void setHealth(int health) { this.health = health; }
	public int getMaxHealth() { return maxHealth; }
	public void setMaxHealth(int maxHealth) { this.maxHealth = maxHealth; } 
	
	public void hit(int damage, boolean hit, boolean slow, double slowPercent, Shot tempShot) {
		
		if(hit) return;
		this.tempShot = tempShot;
		if(tempShot.getPierced() == true) return;
		/*if(piercingShot) {
			pierced = true;
		}*/
		
		health -= damage;
		System.out.println("Health: " + health + " / " + maxHealth);
		System.out.println(damage + " Damage Taken");
		if(slow) setSlowed(slowPercent);
		if(health < 0)health = 0;
		if(health == 0) dead = true;
		if(dead) remove = true;
	}
	
	public void hit(int damage) {
		health -= damage;
		if(health < 0)health = 0;
		if(health == 0) dead = true;
		if(dead) remove = true;
	}
	
	public void setDropRate(int dropRate) {
		if(dropRate <= 100) {
			this.dropRate = dropRate;
		}
		else {
			dropRate = 100;
		}
	}
	
	public int getDropRate() { return dropRate; }
	
	public int getDropRarityMin() {
		return dropRarityMin;
	}
	
	public int getDropRarityMax() {
		return dropRarityMax;
	}
	
	public void setDropRarity(int dropRarityMin, int dropRarityMax) {
		this.dropRarityMin = dropRarityMin;
		this.dropRarityMax = dropRarityMax;
	}
	
	public void setSlowed(double slowPercent) {
		System.out.println("Here");
		if(!slowed) {
			System.out.println("There");
			slowed = true;
			slowTick = 0;
			slowTimer = 200;
			tempMoveSpeed = moveSpeed;
			tempMaxSpeed = maxSpeed;
			moveSpeed = moveSpeed * ((100 - slowPercent) / 100);
			maxSpeed = maxSpeed * ((100 - slowPercent) / 100);
		}
	}
	
	public void checkSlow() {
		if(slowed) {
			slowTick++;
			if(slowTick >= slowTimer) {
				moveSpeed =  tempMoveSpeed;
				maxSpeed = tempMaxSpeed;
				slowed = false;
			}
		}
	}
	
	public boolean getPierced() { return pierced; }
	public Shot getTempShot() { return tempShot; }
	
	public void update() {}
	
}
