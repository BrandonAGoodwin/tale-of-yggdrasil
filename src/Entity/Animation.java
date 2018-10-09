package Entity;

import java.awt.image.BufferedImage;

public class Animation {
	
	private BufferedImage[] frames;
	private int currentFrame;
	private int numFrames;
	
	private int count;
	private int delay;
	
	private int timesPlayed;
	
	private boolean reverseFrames;

	public Animation() {
		timesPlayed = 0;
	}
	
	// Plays the array of frames that is input
	public void setFrames(BufferedImage[] frames){
		reverseFrames = false;
		this.frames = frames;
		currentFrame = 0;
		count = 0;
		timesPlayed = 0;
		delay = 2;
		numFrames = frames.length;
	}
	// Does the same as setFrames but prepares the frames to be
	// Played in reverse
	public void setReverseFrames(BufferedImage[] frames){
		reverseFrames = true;
		this.frames = frames;
		currentFrame = frames.length - 1;
		count = 0;
		timesPlayed = 0;
		delay = 2;
		numFrames = frames.length;
	}
	
	public void setDelay(int i) { delay = i; }
	public void setFrame(int i) { currentFrame = i; }
	public void setNumFrames(int i) { numFrames = i; }
	public void setFreezeFrame(int i) { currentFrame = i; }
	
	public void update() {
		
		// The delay value to get the animation to pause
		if(delay == -1) return;
		
		// Essentially a tick timer (increments each loop of the game)
		count ++;
		
		if(!reverseFrames) {
			if(count == delay) {
				currentFrame++;
				count = 0;
			}
			// Restarts the animation to the first frame
			if(currentFrame == numFrames) {
				currentFrame = 0;
				timesPlayed++;
			}
		}
		else {
			// Once the count is equal to the set delay
			// The frame is iterated
			if(count == delay) {
				currentFrame--;
				count = 0;
			}
			// If at the end of the list of frames
			// The animation is looped
			if(currentFrame == -1) {
				currentFrame = numFrames - 1;
				timesPlayed++;
			}
		}
		
	}
	
	public int getFrame() { return currentFrame; }
	public int getCount() { return count; }
	public int getNumFrames() { return numFrames; }
	public BufferedImage getImage() { return frames[currentFrame]; }
	public boolean hasPlayedOnce() { return timesPlayed > 0; }
	public boolean hasPlayed(int i) { return timesPlayed == i; }
	
}
