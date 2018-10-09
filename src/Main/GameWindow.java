package Main;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class GameWindow extends JFrame {
	
	boolean fse = false; // Fullscreen Enabled
	int fsm = 0; // Fullscreen mode
	GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];

	public GameWindow(String title) {
		setTitle(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
	}

	private void setfullscreen() {
		switch (fsm) {
		case 0:
			setUndecorated(false);
			break;
		case 1:
			setUndecorated(true);
			setExtendedState(JFrame.MAXIMIZED_BOTH);
			break;
		case 2:
			setUndecorated(true);
			device.setFullScreenWindow(this);
			break;
		}
	}

	public void setFullscreen(int fsnm) {
		fse = true;
		if (fsm <= 2) {
			this.fsm = fsnm;
			setfullscreen();
		} else {
			System.err.println("Error " + fsnm + " is not supported");
		}

	}
	
	public int getFullscreenMode() { return fsm; }

}
