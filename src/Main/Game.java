package Main;

import java.awt.*;
import java.net.URL;

public class Game {

	public static void main(String[] args) {
		// Creating the window
		GameWindow window = new GameWindow("Tale of Yggdrasil");
		window.setFullscreen(1);
		window.setContentPane(new GamePanel(window.getFullscreenMode())); // Loading the GamePanel class as the content pane for the window
		window.pack();
		window.setVisible(true);
		
		try {
			URL Icon = ClassLoader.getSystemResource("Sprites/Misc/TOY Icon.png");
			Toolkit kit = Toolkit.getDefaultToolkit();
			Image img = kit.createImage(Icon);
			window.setIconImage(img);
		} catch(Exception e) { e.printStackTrace(); }
	}
}
