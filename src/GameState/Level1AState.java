package GameState;

import Entity.*;
import Entity.Doors.GrassyStoneDoor;
import Entity.Enemies.*;
import Entity.Items.Dainsleif;
import Entity.Items.FireGiantsEye;
import Entity.Items.GemOfPower;
import Entity.Items.Heart;
import Entity.Items.HuntersHeaddress;
import Entity.Items.IceGiantHeart;
import Entity.Items.OdinsSpear;
import Entity.Items.ShardOfGioll;
import Entity.Items.VengefulTalisman;
import Entity.Items.VolundHammer;
import Entity.Items.ScaleOfTheSerpent;
import Main.GamePanel;
import TileMap.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import Handlers.Keys;

import Audio.AudioPlayer;

public class Level1AState extends GameState {
	
	private TileMap tileMap;
	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<EnemyProjectile> eProjectiles;
	private ArrayList<Explosion> explosions;
	
	private ArrayList<Item> items;
	private Drops drops;
	
	private ArrayList<HealthShrine> healthShrines;
	
	private ArrayList<Door> doors;
	private Door currentDoor;
	
	private String currentRoom ="0-0";
	private int currentRoomX = 0;
	private int currentRoomY = 0;
	
	private boolean a00, a10, a11, a21, a31, a30 = false;
	
	private HUD hud;
	
	private Font font = new Font("Arial", Font.PLAIN, 14);
	
	// Events
	private int eventCount = 0;
	private boolean eventStart;
	private ArrayList<Rectangle> tb;
	private boolean eventRoom = false;
	private boolean eventDead;
	
	// Used to prevent player input
	private boolean keysLocked = false;
	
	private Description description;
	
	public Level1AState(GameStateManager gsm){
		super(gsm);
		init();
	}
	
	public void init() {

		// Loads in the tile set, map, background, player, HUD, audio samples and map objects
		tileMap = new TileMap(32);
		tileMap.loadTiles("/Tilesets/TileSet4.png");
		tileMap.setPosition(0, 0);
		
		bg = new Background("/Backgrounds/grassbg1.gif", 0.5);
		
		player = new Player(tileMap);	
		player.setPosition(50, 100);
		
		drops = new Drops(tileMap);
		
		// Creates all the map object array lists
		enemies = new ArrayList<Enemy>();
		eProjectiles = new ArrayList<EnemyProjectile>();
		items = new ArrayList<Item>();
		healthShrines = new ArrayList<HealthShrine>();
		doors = new ArrayList<Door>();
		populate(enemies, items, healthShrines, doors, eProjectiles);
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
		
		// Loads in and plays the background music on loop
		AudioPlayer.load("/Music/Lost Island Looping.mp3", "lvl1Music");
		AudioPlayer.loop("lvl1Music", AudioPlayer.getFrames("lvl1Music"));
		AudioPlayer.load("/SFX/levelStart.mp3", "levelStart");

		// Start event
		// Starts the transition for the first level
		eventStart = true;
		tb = new ArrayList<Rectangle>();
		eventStart();
	}
	
	// Populates the contents of each room
	private void populate(ArrayList<Enemy> enemies, ArrayList<Item> items, ArrayList<HealthShrine> healthShrines, ArrayList<Door> doors, ArrayList<EnemyProjectile> eProjectiles) {
		// The current room is a string created by concatenating the X and Y value for each room
		currentRoom = (currentRoomX + "-" + currentRoomY);
		
		// Enemies
		Imp im;
		HauntedMask hm;
		PossessedArmour pa;
		Stinger si;
		
		// Doors
		GrassyStoneDoor gd;
		
		tileMap.loadMap("/Maps/Level1A/" + currentRoom + ".map");
		
		// Each room is labelled depending on its position from the top left point of the whole level
		switch(currentRoom) {
		
			case "0-0": {
				
				if(!a00) {
					
						a00 = true;		
				}
				
				////////////////////////Doors///////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"right");
				doors.add(gd);
				
				break;
			}
			
			case "1-0": {
				
				if(!a10) {
					///////////////////////Enemies///////////////////////
					
					im = new Imp(tileMap, player);
					im.setPosition(230, 68);
					enemies.add(im);
					
					a10 = true;
				}
				
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"down");
				doors.add(gd);
				
				gd = new GrassyStoneDoor(tileMap,"left");
				doors.add(gd);
				
				break;
			}
			case "1-1": {

				if(!a11) {
					///////////////////////Enemies///////////////////////
					
					si = new Stinger(tileMap, player);
					si.setPosition(100, 130);
					enemies.add(si);
					si = new Stinger(tileMap, player);
					si.setPosition(100, 130);
					si.setDegrees(120);
					enemies.add(si);
					si = new Stinger(tileMap, player);
					si.setPosition(100, 130);
					si.setDegrees(240);
					enemies.add(si);
					
					a11 = true;
				}
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"up");
				gd.setPosition(160, 16);
				doors.add(gd);
				gd = new GrassyStoneDoor(tileMap,"right");
				gd.setPosition(304, 128);
				doors.add(gd);
				
				break;
			}
			case "2-1": {
				
				if(!a21) {
					///////////////////////Enemies///////////////////////
					
					im = new Imp(tileMap, player);
					im.setPosition(240, GamePanel.HEIGHT/2);
					im.setFacingLeft();
					enemies.add(im);
					
					im = new Imp(tileMap, player);
					im.setPosition(290, GamePanel.HEIGHT/2);
					im.setFacingLeft();
					enemies.add(im);
					
					si = new Stinger(tileMap, player);
					si.setPosition(265, GamePanel.HEIGHT/2);
					si.setMaxHealth(si.getMaxHealth() * 2);
					si.setHealth(si.getMaxHealth());
					enemies.add(si);				
					
				a21 = true;	
				}
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"left");
				gd.setPosition(16, 128);
				doors.add(gd);
				gd = new GrassyStoneDoor(tileMap,"right");
				gd.setPosition(304, 128);
				doors.add(gd);
				
				break;
			}
			case "3-1": {
				
				if(!a31) {
					///////////////////////Enemies///////////////////////
					
					hm = new HauntedMask(tileMap, player, eProjectiles);
					hm.setPosition(260, 70);
					hm.setMaxHealth(hm.getMaxHealth() + 7);
					enemies.add(hm);
					
					a31 = true;
				}
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"up");
				gd.setPosition(160, 16);
				doors.add(gd);
				gd = new GrassyStoneDoor(tileMap,"left");
				gd.setPosition(16, 128);
				doors.add(gd);
				
				break;
			}
			
			case "3-0": {
				
				if(!a30) {
					///////////////////////Enemies///////////////////////
					
					pa = new PossessedArmour(tileMap, player, eProjectiles);
					pa.setPosition(GamePanel.WIDTH/2, 50);
					pa.setMaxHealth(pa.getMaxHealth() + 10);
					pa.setHealth(pa.getHealth() + 10);
					pa.setAttackCooldown(45);
					pa.setMaxSpeed(pa.getMaxSpeed() * 1.3);
					pa.setDropRate(100);
					System.out.println(pa.getDropRate() +"  " + pa.getDropRarityMin() +" " + pa.getDropRarityMax());
					enemies.add(pa);
				
					a30 = true;
				}
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"down");
				gd.setPosition(160, 240);
				doors.add(gd);
				gd = new GrassyStoneDoor(tileMap,"right");
				gd.setPosition(304, 128);
				doors.add(gd);
				
				break;
			}
			
		}
		
	}

	public void update() {
		
		// Check keys
		handleInput();
		
		if(!gsm.getPaused() && player.getHealth() > 0 && !eventStart) {
			AudioPlayer.resume("lvl1Music");
		}
		
		// Set player as dead if health = 0
		if(player.getHealth() == 0) { eventDead = true; }
		
		if(eventStart) eventStart();
		if(eventDead) eventDead();
		if(eventRoom) eventRoom(currentDoor);
		
		if(description != null) {
			description.update();
			if(description.shouldRemove()) description = null;
		}
		
		// Update player
		player.update();
		
		// Set map
		tileMap.setPosition(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2);

		// Set background 
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		// Attack enemies
		player.checkAttack(enemies, eProjectiles);
		
		// Check items
		player.checkItem(items);
		
		// Check doors
		player.checkDoors(doors);
		
		// Update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.checkDoors(doors);
			e.update();
			if(e.isDead()) {
				if(drops.dropRoll(e)) {
					System.out.println(e);
					Item it = drops.dropItem(e);
					it.setPosition(e.getx(), e.gety());
					items.add(it);
					//System.out.println("Items: " + items); 
				}
				enemies.remove(i);
				i--;	
				explosions.add(new Explosion(e.getx(), e.gety()));
			}
		}
		
		// Update all enemy projectiles
		for(int i = 0; i < eProjectiles.size(); i++) {
			EnemyProjectile ep = eProjectiles.get(i);
			ep.update();
			ep.checkDoors(doors, eProjectiles);
			if(ep.shouldRemove()) {
				eProjectiles.remove(i);
				i--;
			}
		}
		
		// Update all doors
		for(int i = 0; i < doors.size(); i++) {
			Door d = doors.get(i);
			d.update();
			if(enemies.size() == 0) {
				d.setOpen();
			}
			// If the player walks into a door while it is open eventRoom begins
			if(player.intersects(d) && d.getOpen()) {
				if(!eventRoom) {
					currentDoor = d;
					eventRoom = true;
				}
			}
		}
		
		// Update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()){
				explosions.remove(i);
				i--;
			}
		}
		
		// Update all items
		for(int i = 0; i < items.size(); i++) {
			Item it = items.get(i);
			it.update();
			if(it.getActivated()) {
				it.activate(player);
				if(it.isPickedUp()) {
					it.getDesc();
					items.remove(i);
					i--;
				}
			}
		}
		
	}

	public void draw(Graphics2D g) {
		
		// Draw bg
		bg.draw(g);
		
		// Draw tilemap
		tileMap.draw(g);
		
		// Draw Health Shrines
		for(int i = 0; i < healthShrines.size(); i++) { healthShrines.get(i).draw(g); }
		
		// Draw player
		player.draw(g);
		
		// Draw enemies
		for(int i = 0; i < enemies.size(); i++) { enemies.get(i).draw(g); }
		
		// Draw doors
		for(int i = 0; i < doors.size(); i++) { doors.get(i).draw(g); }
		
		// Draw enemy projectiles
		for(int i = 0; i < eProjectiles.size(); i++) { eProjectiles.get(i).draw(g); }
		
		// Draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition(tileMap.getx(), tileMap.gety());
			explosions.get(i).draw(g);
		}
	
		// Draw items
		for(int i = 0; i < items.size(); i++) { items.get(i).draw(g); }
		
		// Draw HUD
		hud.draw(g);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString(currentRoom, GamePanel.WIDTH - 30, 20);
		
		// Draw transition boxes
		g.setColor(java.awt.Color.BLACK);
		for(int i = 0; i < tb.size(); i++) { g.fill(tb.get(i)); }	
	
	}
	
	public void handleInput() {
		if(Keys.isPressed(Keys.ESCAPE)) {
			AudioPlayer.stop("lvl1Music");
			gsm.setPaused(true);
		}
		if(keysLocked || player.getHealth() == 0) return;
		player.setUp(Keys.keyState[Keys.W]);
		player.setDown(Keys.keyState[Keys.S]);
		player.setLeft(Keys.keyState[Keys.A]);
		player.setRight(Keys.keyState[Keys.D]);
		// The shooting keys
		if(Keys.keyState[Keys.UP] || Keys.keyState[Keys.DOWN] || Keys.keyState[Keys.LEFT] || Keys.keyState[Keys.RIGHT]) {
			if(Keys.keyState[Keys.UP]) player.setShooting(true, false, true);
			if(Keys.keyState[Keys.DOWN]) player.setShooting(true, false, false);
			if(Keys.keyState[Keys.LEFT]) player.setShooting(true, true, false);
			if(Keys.keyState[Keys.RIGHT]) player.setShooting(true, true, true);
		}
		else {
			player.setShooting(false, false, false);
		}
	}
	
	// Level started
	private void eventStart() {
		
		eventCount++;
		
		// Transition for the start of the level
		if(eventCount == 1) {
			tb.clear();
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
			tb.add(new Rectangle(0, GamePanel.HEIGHT / 2, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
		}
		if(eventCount > 1 && eventCount < 60) {
			tb.get(0).height -= 4;
			tb.get(1).width -= 6;
			tb.get(2).y += 4;
			tb.get(3).x += 6;
		}
		// Ends transition and the event
		if(eventCount == 60) {
			eventStart = false;
			eventCount = 0;
			tb.clear();
		}
	}
	
	// Player has died
	private void eventDead() {
		
		eventCount++;
		
		if(eventCount == 1) {
			AudioPlayer.stop("lvl1Music");
			player.setDead();
			player.stop();
		}
		// Death transition
		if(eventCount == 60) {
			tb.clear();
			tb.add(new Rectangle(
				GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		}
		else if(eventCount > 60) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
		}
		// Returns to the menu screen
		if(eventCount >= 120) {
			gsm.setState(GameStateManager.MENUSTATE);
		}
	}
	
	// Room transition
	private void eventRoom(Door currentDoor) {
		
		eventCount++;
		// The current door is saved to be used
		currentDoor = this.currentDoor;
		
		// The player continues walking through the door way automatically
		if(eventCount == 1) {
			keysLocked = true; // Stops the player from using the character
			player.setShooting(false, true, true); // Stops the player shooting (if they are)
		} 
		else if(eventCount < 50) {
			if(currentDoor.getDirection() == "up") {
				player.setOnlyUp();
			}
			else if(currentDoor.getDirection() == "down") {
				player.setOnlyDown();
			}
			else if(currentDoor.getDirection() == "left") {
				player.setOnlyLeft();
			}
			else if(currentDoor.getDirection() == "right") {
				player.setOnlyRight();
			}
		}
		
		// After a short period of time the transition effect occurs
		else if(eventCount == 50) {
			tb.clear();
			tb.add(new Rectangle(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
			player.stop();
			
		}
		else if(eventCount > 50 && eventCount < 85) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
		}
		// While the effect transition is happening
		else if(eventCount == 85) {
			
			items.clear();
			doors.clear();
			healthShrines.clear();
			
			// Room is changed and new player position is set
			if(currentDoor.getDirection() == "up") {
				currentRoomY --;
				player.setPosition(GamePanel.WIDTH / 2, GamePanel.HEIGHT - currentDoor.getHeight()); 
			}
			else if(currentDoor.getDirection() == "down") {
				currentRoomY ++;
				player.setPosition(GamePanel.WIDTH / 2, currentDoor.getHeight() + (player.getCHeight() / 2));
			}
			else if(currentDoor.getDirection() == "left") {
				currentRoomX --;
				player.setPosition(GamePanel.WIDTH - currentDoor.getWidth() - (player.getCWidth() / 2), GamePanel.HEIGHT / 2);
			}
			else if(currentDoor.getDirection() == "right") {
				currentRoomX ++;
				player.setPosition(currentDoor.getWidth() , GamePanel.HEIGHT / 2);
			}
			
			if(currentRoomX == 4 && currentRoomY == 0) {
				eventRoom = false;
				currentRoomX = 0;
				currentRoomY = 0;
				PlayerSave.exportPlayer(player);
				AudioPlayer.stop("lvl1Music");
				gsm.setState(GameStateManager.LEVEL1BSTATE);
				return;
			}
			populate(enemies, items, healthShrines, doors, eProjectiles);
		}
		
		// Ends the transition
		else if(eventCount > 90 && !(tb.get(0).width == 0)) {
			tb.get(0).x += 6;
			tb.get(0).y += 4;
			tb.get(0).width -= 12;
			tb.get(0).height -= 8;
		}
		// If there are enemies in the room the doors close behind the player
		else if(eventCount == 125) {
			player.stop();
			keysLocked = false;
			if(enemies.size() != 0){
				for(int i = 0; i < doors.size(); i++) {
					doors.get(i).setClosed();
				}
			}
			eventRoom = false;
			eventCount = 0;
		}	
	}
	
}