package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class HUD {
	
	private Player player;
	
	private BufferedImage container;
	private BufferedImage containerEnd;
	private BufferedImage fullBar;
	private BufferedImage halfBar;
	private Font font;
	
	public HUD(Player p) {
		player = p;
		try {
			container = ImageIO.read(getClass().getResourceAsStream("/Resources/HUD/Container.gif"));
			containerEnd = ImageIO.read(getClass().getResourceAsStream("/Resources/HUD/ContainerEnd.gif"));
			fullBar = ImageIO.read(getClass().getResourceAsStream("/Resources/HUD/FullBar.gif"));
			halfBar = ImageIO.read(getClass().getResourceAsStream("/Resources/HUD/HalfBar.gif"));
			//font = new Font("Arial", Font.PLAIN, 14);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {
		
		int frames = player.getMaxHealth() / 2;
		for(int i = 0; i < frames; i++) {
			g.drawImage(container, 32 * i, 13, 32, 12, null);
			if(i == frames - 1) {
				g.drawImage(containerEnd, (i * 32) + 32, 13, 32, 12, null);
			}
		}
		for(int i = 0; i < player.getHealth(); i++) {
			if(i % 2 == 1) {
				g.drawImage(fullBar, (16 * i) - 16, 13, 32, 12, null);
			}
			else {
				g.drawImage(halfBar, (16 * i), 13, 32, 12, null);
			}
		}

		//g.drawImage(heart, 2 )
		//g.drawString(player.getHealth() + "/" + player.getMaxHealth(), 20, 35);
		//g.drawString(player.getFire() + "/" + player.getMaxFire(), 20, 55);
	}

}
