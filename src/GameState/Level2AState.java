package GameState;

import Entity.*;
import Entity.Doors.GrassyStoneDoor;
import Entity.Enemies.*;
import Entity.Items.Dainsleif;
import Entity.Items.FireGiantsEye;
import Entity.Items.ShardOfGioll;
import Entity.Items.ScaleOfTheSerpent;
import Main.GamePanel;
import TileMap.*;

import java.awt.*;
import java.util.ArrayList;

import Handlers.Keys;

import Audio.AudioPlayer;

public class Level2AState extends GameState {
	
	private TileMap tileMap;
	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<EnemyProjectile> eProjectiles;
	private ArrayList<Explosion> explosions;
	
	private ArrayList<HealthShrine> healthShrines;
	
	private ArrayList<Item> items;
	private Drops drops;
	
	private ArrayList<Door> doors;
	private Door currentDoor;
	
	private String currentRoom ="2-7";
	private int currentRoomX = 2;
	private int currentRoomY = 7;
	
	private boolean a27,a26,a16,a15,a05,a04,a03,a13,a23,a36,a35,a45,a44,a43,a33,a24,a22,a21 = false;
	private boolean a22hs,a26hs = false;
	
	private HUD hud;
	
	private Font font = new Font("Arial", Font.PLAIN, 14);
	
	private Emblem emblem;
	
	// Events
	private int eventCount = 0;
	private boolean eventStart;
	private ArrayList<Rectangle> tb;
	private boolean eventRoom = false;
	private boolean eventDead;
	
	private boolean keysLocked = false;
	
	public Level2AState(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	public void init() {

		tileMap = new TileMap(32);
		tileMap.loadTiles("/Resources/Tilesets/SandyTileset2.png");
		tileMap.loadMap("/Resources/Maps/Level2A/2-7.map");
		tileMap.setPosition(0, 0);
		
		bg = new Background("/Resources/Backgrounds/grassbg1.gif", 0.5);
		
		emblem = new Emblem("/Resources/Transitions/Level2ATransition.gif", 256);
		emblem.setPosition((GamePanel.WIDTH / 2) - (emblem.width / 2), (GamePanel.HEIGHT / 2) - (emblem.height / 2));
		
		player = PlayerSave.importPlayer(tileMap);
		player.setPosition(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2);
		
		drops = new Drops(tileMap);
		
		enemies = new ArrayList<Enemy>();
		eProjectiles = new ArrayList<EnemyProjectile>();
		items = new ArrayList<Item>();
		healthShrines = new ArrayList<HealthShrine>();
		doors = new ArrayList<Door>();
		populate(enemies, items, healthShrines, doors, eProjectiles);
			
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
		
		AudioPlayer.load("/Resources/Music/Lost Island Looping.mp3", "lvl1Music");
		AudioPlayer.load("/Resources/SFX/levelStart.mp3", "levelStart");
		AudioPlayer.load("/Resources/SFX/HealthShrine.mp3", "healthShrine");
		
		// Start event
		eventStart = true;
		tb = new ArrayList<Rectangle>();
		eventStart();
	}
	
	private void populate(ArrayList<Enemy> enemies, ArrayList<Item> items, ArrayList<HealthShrine> healthShrines, ArrayList<Door> doors, ArrayList<EnemyProjectile> eProjectiles) {
		
		currentRoom = (currentRoomX + "-" + currentRoomY);
		
		// Enemies
		Slugger s;
		Imp im;
		HauntedMask hm;
		AngryMask am;
		PossessedArmour pa;
		RedImp ri;
		Stinger si;
		SluggerFast sf;
		AcidBug ab;
		VengefulMask vm;
		
		// Doors
		GrassyStoneDoor gd;
		
		/*im = new Imp(tileMap, player);
		im.setPosition(200, 100);
		enemies.add(im);*/
		
		/*s = new Slugger(tileMap);
		s.setPosition(200, 120);
		enemies.add(s);*/
		
		/*hm = new HauntedMask(tileMap, player, eProjectiles);
		hm.setPosition(200, 200);
		enemies.add(hm);*/
		
		/*am = new AngryMask(tileMap, player);
		am.setPosition(200, 200);
		enemies.add(am);*/
		
		/*pa = new PossessedArmour(tileMap, player, eProjectiles);
		pa.setPosition(200, 50);
		enemies.add(pa);*/
		
		tileMap.loadMap("/Resources/Maps/Level2A/" + currentRoom + ".map");
		
		switch(currentRoom) {
		
			case "2-7": { // Done
				if(!a27) { a27 = true; }
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"up");
				gd.setPosition(160, 16);
				doors.add(gd);
				
				break;
			}
		
			case "2-6": { // Done	
				if(!a26) { a26 = true;	}
				HealthShrine hs = new HealthShrine(tileMap);
				hs.setPosition(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2);
				if(a26hs) {	hs.setDeactivated(); }
				healthShrines.add(hs);
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"left");
				gd.setPosition(16, 128);
				doors.add(gd);
				
				gd = new GrassyStoneDoor(tileMap,"right");
				gd.setPosition(304, 128);
				doors.add(gd);
				
				gd = new GrassyStoneDoor(tileMap,"down");
				gd.setPosition(160, 240);
				doors.add(gd);
				
				break;
			}
			
			case "1-6": { // Done

				if(!a16) {
					///////////////////////Enemies///////////////////////
					
					sf = new SluggerFast(tileMap);
					sf.setPosition(100, 70);
					sf.setAllRight();
					enemies.add(sf);
					
					sf = new SluggerFast(tileMap);
					sf.setPosition(100, 185);
					sf.setAllLeft();
					enemies.add(sf);
					
					hm = new HauntedMask(tileMap, player, eProjectiles);
					hm.setPosition(60, 135);
					hm.setAllRight();
					enemies.add(hm);

					a16 = true;
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
			
			case "1-5": {
	
				if(!a15) {
					///////////////////////Enemies///////////////////////
					
					
					
					///////////////////////Items/////////////////////////
					
					
					a15 = true;
				}
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"down");
				gd.setPosition(160, 240);
				doors.add(gd);
				
				gd = new GrassyStoneDoor(tileMap,"left");
				gd.setPosition(16, 128);
				doors.add(gd);
				
				break;
			}
			
			case "0-5": {
				
				if(!a05) {
					///////////////////////Enemies///////////////////////
					
					s = new Slugger(tileMap);
					s.setPosition(190, 65);
					enemies.add(s);
					
					s = new Slugger(tileMap);
					s.setPosition(110, 165);
					enemies.add(s);
					
					am = new AngryMask(tileMap, player);
					am.setPosition(190, 90);
					am.setAllDown();
					enemies.add(am);
					
					///////////////////////Items/////////////////////////
					
					
					a05 = true;
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
			
			case "0-4": { // Done
				
				if(!a04) {
					
					ab = new AcidBug(tileMap, player, eProjectiles);
					ab.setPosition(100, 130);
					enemies.add(ab);
					ab = new AcidBug(tileMap, player, eProjectiles);
					ab.setPosition(100, 130);
					ab.setDegrees(120);
					enemies.add(ab);
					ab = new AcidBug(tileMap, player, eProjectiles);
					ab.setPosition(100, 130);
					ab.setDegrees(240);
					enemies.add(ab);
					
					a04 = true;
					}
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"up");
				gd.setPosition(160, 16);
				doors.add(gd);
				
				gd = new GrassyStoneDoor(tileMap,"down");
				gd.setPosition(160, 240);
				doors.add(gd);
			
				break;
			}
			
			case "0-3": { // Health Shrine Room
				
				if(!a03) { a03 = true; }
				
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"down");
				gd.setPosition(160, 240);
				doors.add(gd);
				
				gd = new GrassyStoneDoor(tileMap,"right");
				gd.setPosition(304, 128);
				doors.add(gd);
				
				break;
			}
			case "1-3": { // Done
				
				if(!a13) {
					///////////////////////Enemies///////////////////////
					
					hm = new HauntedMask(tileMap, player, eProjectiles);
					hm.setPosition(260, 70);
					enemies.add(hm);

					hm = new HauntedMask(tileMap, player, eProjectiles);
					hm.setPosition(260, 130);
					enemies.add(hm);

					hm = new HauntedMask(tileMap, player, eProjectiles);
					hm.setPosition(260, 160);
					enemies.add(hm);
					
				/*	si = new Stinger(tileMap, player);
					si.setPosition(100, 100);
					enemies.add(si);
					si = new Stinger(tileMap, player);
					si.setPosition(100, 100);
					si.setDegrees(120);
					enemies.add(si);
					si = new Stinger(tileMap, player);
					si.setPosition(100, 100);
					si.setDegrees(240);
					enemies.add(si);
					
					si = new Stinger(tileMap, player);
					si.setPosition(65, 200);
					enemies.add(si);
					
					si = new Stinger(tileMap, player);
					si.setPosition(230, 200);
					enemies.add(si);*/
					
					///////////////////////Items/////////////////////////
					
					
					a13 = true;
					
				}
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"right");
				gd.setPosition(304, 128);
				doors.add(gd);
				
				gd = new GrassyStoneDoor(tileMap,"left");
				gd.setPosition(16, 128);
				doors.add(gd);
			
				break;
			}
			
			case "2-3": { // Done

				if(!a23) { a23 = true; }
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"up");
				gd.setPosition(160, 16);
				doors.add(gd);
				
				gd = new GrassyStoneDoor(tileMap,"down");
				gd.setPosition(160, 240);
				doors.add(gd);

				gd = new GrassyStoneDoor(tileMap,"right");
				gd.setPosition(304, 128);
				doors.add(gd);
				
				gd = new GrassyStoneDoor(tileMap,"left");
				gd.setPosition(16, 128);
				doors.add(gd);
				
				break;
			}
			
			case "3-6": { // Done
				
				if(!a36) {
					///////////////////////Enemies///////////////////////
					
					ri = new RedImp(tileMap, player);
					ri.setPosition(260, 70);
					enemies.add(ri);

					hm = new HauntedMask(tileMap, player, eProjectiles);
					hm.setPosition(260, 130);
					enemies.add(hm);

					ri = new RedImp(tileMap, player);
					ri.setPosition(260, 160);
					enemies.add(ri);
					
					a36 = true;
				}
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"left");
				gd.setPosition(16, 128);
				doors.add(gd);
				
				gd = new GrassyStoneDoor(tileMap,"up");
				gd.setPosition(160, 16);
				doors.add(gd);
				
				break;
			}
			
			case "3-5": { // Done
				
				if(!a35) {
					
					si = new Stinger(tileMap, player);
					si.setPosition(240, 90);
					si.setMaxHealth(si.getMaxHealth() + 6);
					si.setHealth(si.getMaxHealth());
					enemies.add(si);
					si = new Stinger(tileMap, player);
					si.setPosition(70, 90);
					si.setMaxHealth(si.getMaxHealth() + 6);
					si.setHealth(si.getMaxHealth());
					si.setDegrees(120);
					enemies.add(si);
					si = new Stinger(tileMap, player);
					si.setPosition(GamePanel.WIDTH / 2, 100);
					si.setMaxHealth(si.getMaxHealth() + 6);
					si.setHealth(si.getMaxHealth());
					si.setDegrees(240);
					enemies.add(si);
					
					a35 = true;
				}
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"right");
				gd.setPosition(304, 128);
				doors.add(gd);
				
				gd = new GrassyStoneDoor(tileMap,"down");
				gd.setPosition(160, 240);
				doors.add(gd);
				
				break;
			}
			
			case "4-5": {

				if(!a45) { // Done
					///////////////////////Enemies///////////////////////
					
					/*ri = new RedImp(tileMap, player);
					ri.setPosition(230, 130);
					enemies.add(ri);*/
					
					hm = new HauntedMask(tileMap, player, eProjectiles);
					hm.setPosition(230, 200);
					hm.setAllLeft();
					enemies.add(hm);
					
					am = new AngryMask(tileMap, player);
					am.setPosition(230, 90);
					am.setAllLeft();
					enemies.add(am);
					
					///////////////////////Items/////////////////////////
					
					
					a45 = true;
				}
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"left");
				gd.setPosition(16, 128);
				doors.add(gd);
				
				gd = new GrassyStoneDoor(tileMap,"up");
				gd.setPosition(160, 16);
				doors.add(gd);
				
				break;
			}
			
			case "4-4": { // Done

				if(!a44) {
					///////////////////////Enemies///////////////////////
					
					pa = new PossessedArmour(tileMap, player, eProjectiles);
					pa.setPosition(GamePanel.WIDTH / 2, 40);
					enemies.add(pa);
					
					a44 = true;
				}
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"up");
				gd.setPosition(160, 16);
				doors.add(gd);
				
				gd = new GrassyStoneDoor(tileMap,"down");
				gd.setPosition(160, 240);
				doors.add(gd);
				
				break;
			}
			case "4-3": { // Done
				
				if(!a43){ a43=true;	}
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"down");
				gd.setPosition(160, 240);
				doors.add(gd);
				
				gd = new GrassyStoneDoor(tileMap,"left");
				gd.setPosition(16, 128);
				doors.add(gd);
				
				break;
			}
			case "3-3": { // Done
				
				if(!a33){
					///////////////////////Enemies///////////////////////
					
					am = new AngryMask(tileMap, player);
					am.setPosition(260, 70);
					enemies.add(am);

					am = new AngryMask(tileMap, player);
					am.setPosition(260, 130);
					enemies.add(am);

					am = new AngryMask(tileMap, player);
					am.setPosition(260, 160);
					enemies.add(am);
					
					a33=true;
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
			case "2-2": { // Done
				
				if(!a22) { a22 = true;	}
				
				HealthShrine hs = new HealthShrine(tileMap);
				hs.setPosition(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2);
				if(a22hs) {	
					hs.setDeactivated(); 
				}
				healthShrines.add(hs);
				
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"up");
				gd.setPosition(160, 16);
				doors.add(gd);
				
				gd = new GrassyStoneDoor(tileMap,"down");
				gd.setPosition(160, 240);
				doors.add(gd);
				
				break;
			}
			case "2-1": { // Done
				
				if(!a21){
					///////////////////////Enemies///////////////////////
					
					vm = new VengefulMask(tileMap, player, eProjectiles);
					vm.setPosition(GamePanel.WIDTH / 2, 80);
					vm.setDropRate(100);
					enemies.add(vm);

					a21=true;
				}
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"up");
				gd.setPosition(160, 16);
				doors.add(gd);
				
				gd = new GrassyStoneDoor(tileMap,"down");
				gd.setPosition(160, 240);
				doors.add(gd);
				
				
				break;
			}
			case "2-4": { // Done
				
				if(!a24){
					///////////////////////Items/////////////////////////
					
					// Drops a random item within a certain item rarity range
					Item it = drops.dropItem(2,3);
					it.setPosition((GamePanel.WIDTH / 2), (GamePanel.HEIGHT / 2));
					items.add(it);
					
					a24=true;
				}
				///////////////////////Doors/////////////////////////
				
				gd = new GrassyStoneDoor(tileMap,"up");
				gd.setPosition(160, 16);
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
		if(player.getHealth() == 0) {
			eventDead = true;
		}
		
		if(eventStart) eventStart();
		if(eventDead) eventDead();
		if(eventRoom) eventRoom(currentDoor);
		
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
			//e.checkDoors(doors);
			if(e.isDead()) {
				if(drops.dropRoll(e)) {
					Item it = drops.dropItem(e);
					it.setPosition(e.getx(), e.gety());
					items.add(it);
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
					items.remove(i);
					i--;
				}
			}
		}
		
		// Update Health Shrines
		for(int i = 0; i < healthShrines.size(); i++) {
			HealthShrine hs = healthShrines.get(i);
			hs.update();
			if(player.intersects(hs) && !hs.getActivated()) {
				hs.activate(player);
				AudioPlayer.play("healthShrine");
				if(currentRoomX == 2 && currentRoomY == 2) {
					a22hs = true;
				}
				else if(currentRoomX == 2 && currentRoomY == 6) {
					a26hs = true;
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
		for(int i = 0; i < healthShrines.size(); i++) {
			healthShrines.get(i).draw(g);
		}
		
		// Draw player
		player.draw(g);
		
		// Draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}
		
		// Draw doors
		for(int i = 0; i < doors.size(); i++) {
			doors.get(i).draw(g);
		}
		
		// Draw enemy projectiles
		for(int i = 0; i < eProjectiles.size(); i++) {
			eProjectiles.get(i).draw(g);
		}
		
		// Draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).setMapPosition(tileMap.getx(), tileMap.gety());
			explosions.get(i).draw(g);
		}
	
		// Draw items
		for(int i = 0; i < items.size(); i++) {
			items.get(i).draw(g);
		}
		
		// Draw HUD
		hud.draw(g);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString(currentRoom, GamePanel.WIDTH - 30, 20);
		
		
		// Draw transition boxes
		g.setColor(java.awt.Color.BLACK);
		for(int i = 0; i < tb.size(); i++) {
			g.fill(tb.get(i));
		}
		
		if(eventStart == true && eventCount > 1 && eventCount < 60) {
			emblem.draw(g);
		}
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
	
	private void reset() {
		
		//player.reset();
		player.setPosition(300, 161);
		eventCount = 0;
		eventStart = true;
		eventStart();
	}
	
	// Level started
	private void eventStart() {
		
		eventCount++;
		
		// Transition for the start of the level
		if(eventCount == 1) {
			
			keysLocked = true;
			AudioPlayer.play("levelStart");
			tb.clear();
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
			tb.add(new Rectangle(0, GamePanel.HEIGHT / 2, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
			
			
		}
		if(eventCount > 61 && eventCount < 120) {
			tb.get(0).height -= 4;
			tb.get(1).width -= 6;
			tb.get(2).y += 4;
			tb.get(3).x += 6;
		}
		// Ends transition and the event
		if(eventCount == 120) {
			keysLocked = false;
			AudioPlayer.loop("lvl1Music", AudioPlayer.getFrames("lvl1Music"));
			tb.clear();
			eventStart = false;
			eventCount = 0;
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
			
			if(currentRoomX == 2 && currentRoomY == 0) {
				eventRoom = false;
				currentRoomX = 0;
				currentRoomY = 0;
				PlayerSave.exportPlayer(player);
				AudioPlayer.stop("lvl1Music");
				gsm.setState(GameStateManager.MENUSTATE);
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