package Entity;

import java.awt.Graphics2D;
import java.util.ArrayList;

import TileMap.TileMap;

public abstract class EnemyProjectile extends MapObject {
	
	protected boolean hit;
	protected boolean remove;
	protected boolean interactable;
	protected int damage;
	
	public EnemyProjectile(TileMap tm) {
		super(tm);
	}
	
	public int getDamage() { return damage; }
	public boolean shouldRemove() { return remove; }
	
	public abstract void setHit();
	
	public void checkDoors(ArrayList<Door> doors, ArrayList<EnemyProjectile> eProjectiles) {
		if(interactable) {
			super.checkDoors(doors);
		}
		
		for(int i = 0; i < doors.size(); i++) {
			
			Door d = doors.get(i);
			
			// Make projectiles collide with doors
			for(int j = 0; j < eProjectiles.size(); j++) {
				if(eProjectiles.get(j).intersects(d)) {
					eProjectiles.get(j).setHit();
				}
			}
			
		}
	}
	
	public abstract void update();
	
	public void draw(Graphics2D g) {
		super.draw(g);
	}
	
}
 
