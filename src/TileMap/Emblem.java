package TileMap;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Emblem {

	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	public int width, height;
	
	public Emblem(String s,int widthAndHeight) {
		
		// This gets image from resources
		try {
			width = height = widthAndHeight;
			
			image = ImageIO.read(getClass().getResourceAsStream(s));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	// Moves the background by a set amount determined by the move scale
	public void setPosition(double x , double y) {
		this.x = x;
		this.y = y;
	}
	
	// Sets the rate at which the background moves
	public void setVector(double dx, double dy){
		this.dx = dx;
		this.dy = dy;
	}
	
	// Moves the background
	public void update() {
		x += dx;
		y += dy;
	}
	
	// Draws the initial background and the one before or after
	// Depending on which direction the background is moving
	public void draw(Graphics2D g) {
		g.drawImage(image, (int)x, (int) y, null);
	}
	
}
