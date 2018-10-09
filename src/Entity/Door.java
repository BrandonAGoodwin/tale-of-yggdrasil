package Entity;

import GameState.GameState;
import TileMap.TileMap;

public class Door extends MapObject{

	protected boolean open;
	protected String direction;
	
	public Door(TileMap tm, String direction) {
		super(tm);
		this.direction = direction;
	}
	
	// Set whether or not map objects can collide with the door
	public void setOpen() {
		open = true;
		collideable = false;
	}
	
	public void setClosed() {
		open = false;
		collideable = true;
	}
	
	public boolean getOpen() { 
		return open;
	}
	
	public String getDirection() {
		return direction;
	}
	
	public void update() {}

}
