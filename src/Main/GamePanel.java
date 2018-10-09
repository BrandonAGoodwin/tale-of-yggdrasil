package Main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Handlers.Keys;

import GameState.GameStateManager;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {
	
	// Dimensions
	public static GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
	public static final int SCREEN_WIDTH = gd.getDisplayMode().getWidth();
	public static final int SCREEN_HEIGHT = gd.getDisplayMode().getHeight();
	public static final int WIDTH = 320;
	public static final int HEIGHT = 256;
	public static final int SCALE = 3; 	// Scale allows me to change the size of the game and game window easily

	// Fullscreen
	private int fsm;
	
	// Game thread
	private Thread thread;
	private boolean running;
	private int FPS = 60; // The number of frames the game will load per second 
	private long targetTime = 1000 / FPS;

	// Image
	private BufferedImage image;
	private BufferedImage border;
	private Graphics2D g;

	// Game state manager
	private GameStateManager gsm;

	public static boolean GOD_MODE = false;
	
	// Recording
	private boolean recording = false;
	private int recordingCount = 0;
	private boolean screenshot;
	
	
	
	// Constructor
	public GamePanel(int fsm) {
		super();
		this.fsm = fsm;
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}

	private void init() { // Sets the image and image sizes

		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		border = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics2D) image.getGraphics();
		
		g.fillRect(0, 0, (SCREEN_WIDTH - (WIDTH * SCALE)) / 2, SCREEN_HEIGHT);
		
		running = true;

		gsm = new GameStateManager();
	}
 
	public void run() { // What initially occurs when the game runs from frame to frame

		init();

		long start;
		long elapsed;
		long wait;

		// Game loop
		while (running) {

			start = System.nanoTime();

			update();
			draw();
			drawToScreen();

			elapsed = System.nanoTime() - start;
			// This makes it so that once the target time has passed, then the code will continue running
			wait = targetTime - elapsed / 1000000;
			if (wait < 0)
				wait = 5;

			try {
				Thread.sleep(wait);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private void update() { 
		gsm.update();
		Keys.update();
	}

	private void draw() {
		gsm.draw(g);
	}

	// This gets the various images used and draws them all onto the screen as one big picture
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		
		if(fsm == 1) {
			g2.drawImage(image, (SCREEN_WIDTH - (WIDTH * SCALE)) / 2, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
			g2.drawImage(border,0, 0, (SCREEN_WIDTH - (WIDTH * SCALE)) / 2, SCREEN_HEIGHT, null);
			g2.drawImage(border, ((SCREEN_WIDTH - (WIDTH * SCALE)) / 2) + (WIDTH * SCALE), 0, (SCREEN_WIDTH - (WIDTH * SCALE)) / 2, SCREEN_HEIGHT, null);
		}
		else {
			g2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		}
		
		g2.dispose();
		if(screenshot) {
			screenshot = false;
			try {
				java.io.File out = new java.io.File("screenshot " + System.nanoTime() + ".gif");
				javax.imageio.ImageIO.write(image, "gif", out);
			}
			catch(Exception e) {}
		}
		if(!recording) return;
		try {
			java.io.File out = new java.io.File("C:\\out\\frame" + recordingCount + ".gif");
			javax.imageio.ImageIO.write(image, "gif", out);
			recordingCount++;
		}
		catch(Exception e) {}
	}

	// Methods for the Key Listener
	/*public void keyTyped(KeyEvent key) {
	}

	public void keyPressed(KeyEvent key) {
		gsm.keyPressed(key.getKeyCode());
	}

	public void keyReleased(KeyEvent key) {
		gsm.keyReleased(key.getKeyCode());
	}*/
	
	public void keyTyped(KeyEvent key) {}
	public void keyPressed(KeyEvent key) {
		if(key.isControlDown()) {
			if(key.getKeyCode() == KeyEvent.VK_R) {
				recording = !recording;
				return;
			}
			if(key.getKeyCode() == KeyEvent.VK_S) {
				screenshot = true;
				return;
			}
		}
		Keys.keySet(key.getKeyCode(), true);
	}
	public void keyReleased(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), false);
	}
}
