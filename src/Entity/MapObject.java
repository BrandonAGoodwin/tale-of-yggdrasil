package Entity;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import Main.GamePanel;
import TileMap.Tile;
import TileMap.TileMap;

public abstract class MapObject {
	
	// Tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	// Position and vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
	// Dimensions
	protected int width;
	protected int height;
	
	// Collision box
	protected int cwidth;
	protected int cheight;
	
	// Collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	protected boolean collideable;
	
	// Animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingUp;
	protected boolean facingDown;
	protected boolean facingLeft;
	protected boolean facingRight;
	
	// Movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	
	// Movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	
	// Constructor
	public MapObject(TileMap tm){
		tileMap = tm;
		tileSize = tm.getTileSize();
	}
	
	public void setTileMap(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize();
	}
	// A form of proximity detection which checks if one map objects
	// Collision box intersects another and returns a boolean value
	public boolean intersects(MapObject o){
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	// Similar to the intersects() method but the object
	// Must be within the collision box  
	public boolean contains(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.contains(r2);
	}
	
	// How the borders of each object is defined
	public Rectangle getRectangle() {
		return new Rectangle((int)x - cwidth / 2, (int)y - cheight / 2, cwidth, cheight);
	}
	
	public void calculateCorners(double x, double y) {
		
		// Surrounding tiles
		
		int leftTile = (int)(x - cwidth / 2) / tileSize;
		int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
		int topTile = (int)(y - cheight / 2) / tileSize;
		int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
		
		if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
			leftTile < 0 || rightTile >= tileMap.getNumCols()) {
			topLeft = topRight = bottomLeft = bottomRight = false;
			return;
		}
		
		// Get tile type
		int tl = tileMap.getType(topTile, leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);
		
		// Return which directions are blocked
		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
		
	}
	
	public void checkTileMapCollision(){
		
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		// Using the previous calculate corners method so that if an entity is about to collide with
		// an impassable block the the speed of the entity is set to 0 (it stops moving)
		calculateCorners(x, ydest);
		if(dy < 0){
			if(topLeft || topRight){
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0){
			if(bottomLeft || bottomRight){
				dy = 0;
				ytemp = (currRow + 1) * tileSize - cheight / 2; 
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0){
			if(topLeft || bottomLeft){
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else{
				xtemp += dx;
			}
		}
		if(dx > 0){
			if(topRight || bottomRight){
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else{
				xtemp += dx;
			}
		}
		
	}
	
	public int getx() { return (int)x; }
	public int gety() { return (int)y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCWidth() { return cwidth; }
	public int getCHeight() { return cheight; }
	
	public boolean getCollideable() { return collideable; }
	
	public void checkDoors(ArrayList<Door> doors) {
		
		for(int i = 0; i < doors.size(); i++) {
			
			Door dr = doors.get(i);
			
			if(intersects(dr) && !dr.getOpen()) {
				if(dr.direction == "up") {
					y = dr.gety() + dr.getHeight() - ((dr.getHeight() - dr.getCHeight()) / 2) - ((height - cheight) / 2);
					dy = 0;
				}
				else if(dr.direction == "down") {
					y = dr.gety() + 1 - ((cheight + height) / 2);
					dy = 0;
				}
				else if(dr.direction == "left") {
					x = dr.getx() + dr.getCWidth() - ((width - cwidth) / 2);
					dx = 0;
				}
				else if(dr.direction == "right") {
					x = dr.getx() + 1 - ((cwidth + width) / 2);
					dx = 0;
				}
			}
		}
	}
	
	public void increaseSize(double percentageIncrease) {
		height = (int)((double)height * (1 + (percentageIncrease / 100)));
		width = (int)((double)width * (1 + (percentageIncrease / 100)));
		cheight = (int)((double)cheight * (1 + (percentageIncrease / 100)));
		cwidth = (int)((double)cwidth * (1 + (percentageIncrease / 100)));
	}
	
	public void setPosition(double x, double y){
		this.x = x;
		this.y = y;
	}
	public void setVector(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
	}
	
	public static int randomInt(int min, int max) {

	    // Usually this can be a field rather than a method variable
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	
	public void setMoveSpeed(double d) {
		moveSpeed = d;
	}
	
	public void setMaxSpeed(double d) {
		maxSpeed = d;
	}
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	
	public void setOnlyLeft() { 
		left = true;
		right = false;
		up = false;
		down = false;
	}
	public void setOnlyRight() { 
		left = false;
		right = true;
		up = false;
		down = false;
	}
	public void setOnlyUp() { 
		left = false;
		right = false;
		up = true;
		down = false;
	}
	public void setOnlyDown() { 
		left = false;
		right = false;
		up = false;
		down = true;
	}
	public void setOnlyFalse() {
		left = false;
		right = false;
		up = false;
		down = false;
	}
	
	public void setFacingUp() {
		facingUp = true;
		facingDown = false;
		facingLeft = false;
		facingRight = false;
	}
	
	public void setFacingDown() {
		facingUp = false;
		facingDown = true;
		facingLeft = false;
		facingRight = false;
	}
	
	public void setFacingLeft() {
		facingUp = false;
		facingDown = false;
		facingLeft = true;
		facingRight = false;
	}
	
	public void setFacingRight() {
		facingUp = false;
		facingDown = false;
		facingLeft = false;
		facingRight = true;
	}
	
	public void setAllUp() {
		setOnlyUp();
		setFacingUp();
	}
	
	public void setAllDown() {
		setOnlyDown();
		setFacingDown();
	}
	
	public void setAllLeft() {
		setOnlyLeft();
		setFacingLeft();
	}
	
	public void setAllRight() {
		setOnlyRight();
		setFacingRight();
	}
	
	public double getMoveSpeed() { return moveSpeed; }
	public double getMaxSpeed() { return maxSpeed; }
	
	public boolean getFacingUp() { return facingUp; }
	public boolean getFacingDown() { return facingDown; }
	public boolean getFacingLeft() { return facingLeft; }
	public boolean getFacingRight() { return facingRight; }
	
	/*
	public boolean notOnScreen() {
		return x + xmap + width < 0 ||
			x + xmap - width > GamePanel.WIDTH ||
			y + ymap + height < 0 ||
			y + ymap - height > GamePanel.HEIGHT;
	}*/
	
	public boolean notOnScreen() {
		return x < 0 ||
			x > GamePanel.WIDTH ||
			y < 0 ||
			y + height > GamePanel.HEIGHT;
	}
	
	public void draw(java.awt.Graphics2D g) {
		if(facingRight){
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2), (int)(y + ymap - height / 2), null);
		}
		else{
			g.drawImage(animation.getImage(), (int)(x + xmap - width / 2 + width), (int)(y + ymap - height / 2), -width, height, null);
		}
		
		/*// Draw collision box
		Rectangle r = getRectangle();
		r.x += xmap;
		r.y += ymap;
		g.draw(r);*/
	}
	
}











