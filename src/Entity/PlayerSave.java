package Entity;

public class PlayerSave {

	private static Player savedPlayer;
	
	public static void exportPlayer(Player player) {
		savedPlayer = player;
	}
	
	public static Player importPlayer(TileMap.TileMap tm) {
		savedPlayer.setTileMap(tm);
		return savedPlayer; 
	} 
	
}