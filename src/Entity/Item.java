package Entity;

import TileMap.TileMap;

public class Item extends MapObject{
	
	protected String name = "";
	protected String desc = "";

	protected boolean activated = false;
	protected boolean pickedUp = false;
	
	protected int itemRarity;
	
	protected int degrees = 0;
	
	public Item(TileMap tm) {
		super(tm);
	}
	
	public boolean isPickedUp() { return pickedUp; }
	public void setPickedUp() { pickedUp = true; }
	
	public void setActivated() { activated = true; }
	public boolean getActivated() { return activated; }
	
	public int getItemRarity() { return itemRarity; }
	
	public void activate(Player p) {}
	
	public void update() {}

	public String getName() { return name; }
	public String getDesc() { return desc; }

}