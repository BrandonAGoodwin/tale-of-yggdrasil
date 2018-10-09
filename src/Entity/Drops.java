package Entity;

import java.util.ArrayList;
import java.util.Random;

import Entity.Items.*;
import TileMap.TileMap;

public class Drops {

	private TileMap tm;
	private ArrayList<Item> items = new ArrayList<Item>();
	
	public Drops(TileMap tm) { this.tm = tm; }
	
	// This is used to determine whether or not an item should be dropped
	public boolean dropRoll(Enemy e) {
		boolean drop = randomInt(0, 100) <= e.getDropRate();
		return drop;
	}
	
	public Item dropItem(Enemy e) {
		return dropItem(e.getDropRarityMin(), e.getDropRarityMax());
	}
	
	private Item getItem() {
		int itemNo = randomInt(0, 10);
		Heart heart = new Heart(tm);
		Item it = heart;
		
		// The list of items that random item is picked from
		switch(itemNo) {
		case 0:
			Heart hrt = new Heart(tm);
			it = hrt;
			break;
		case 1:
			ShardOfGioll sg = new ShardOfGioll(tm);
			it = sg;
			break;
		case 2:
			Dainsleif da = new Dainsleif(tm);
			it = da;
			break;
		case 3:
			FireGiantsEye fg = new FireGiantsEye(tm);
			it = fg;
			break;
		case 4:
			ScaleOfTheSerpent ss = new ScaleOfTheSerpent(tm);
			it = ss;
			break;
		case 5:
			IceGiantHeart ig = new IceGiantHeart(tm);
			it = ig;
			break;
		case 6:
			VolundHammer vh = new VolundHammer(tm);
			it = vh;
			break;
		case 7:
			OdinsSpear os = new OdinsSpear(tm);
			it = os;
			break;
		case 8:
			VengefulTalisman vt = new VengefulTalisman(tm);
			it = vt;
			break;
		case 9:
			GemOfPower gp = new GemOfPower(tm);
			it = gp;
			break;
		case 10: 
			HuntersHeaddress hh = new HuntersHeaddress(tm);
			it = hh;
		}
		return it;
	}
	
	// Returns a random item when called, that has a rarity within the given range
	public Item dropItem(int dropRarityMin, int dropRarityMax) {
		boolean itemFound = false;
		Item it;
		System.out.println("RMin: " + dropRarityMin + " | DRMax: " + dropRarityMax);
		// Returns a random item 
		it = getItem();
		while(!itemFound) {
			System.out.println("Item Rarity: " + it.getItemRarity());
			// Checks if the item rarity is within the range
			if(it.getItemRarity() >= dropRarityMin && it.getItemRarity() <= dropRarityMax) {
				itemFound = true;
				System.out.println("Item Found");
				break;
			}
			else {
				it = getItem();
			}
		}
		return it;
	}
	
	// A function to produce a random integer
	private static int randomInt(int min, int max) {

	    Random rand = new Random();

	    int randomNum = rand.nextInt((max - min) + 1) + min;

	    return randomNum;
	}
	
}
