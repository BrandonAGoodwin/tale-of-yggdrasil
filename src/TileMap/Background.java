package TileMap;

import Main.GamePanel;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Background {

	private BufferedImage image;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	public Background(String s, double ms) {
		
		// This gets image from resources
		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
			moveScale = ms;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// Moves the background by a set amount determined by the move scale
	public void setPosition(double x , double y) {
		this.x = (x * moveScale) % GamePanel.WIDTH; 
		this.y = (y * moveScale) % GamePanel.HEIGHT;
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
		
		// If the background has moved past twice then it's
		// Position is moved back one
		if(x > GamePanel.WIDTH) {
			x = x - GamePanel.WIDTH;
		}
		else if(x < -GamePanel.WIDTH) {
			x = x + GamePanel.WIDTH;
		}
	}
	
	// Draws the initial background and the one before or after
	// Depending on which direction the background is moving
	public void draw(Graphics2D g) {
		g.drawImage(image, (int)x, (int) y, null);
		if(x < 0) {
			g.drawImage(image, (int)x + GamePanel.WIDTH, (int)y, null); 
		}
		if(x > 0) {
			g.drawImage(image, (int)x - GamePanel.WIDTH, (int)y, null);
		}
		
	}
	
}
