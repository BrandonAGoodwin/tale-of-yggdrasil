package networking;

import main.com.bodyconquest.constants.GameType;
import main.com.bodyconquest.game_logic.Game;
import main.com.bodyconquest.gamestates.EncounterState;

import java.net.SocketException;

/** Server class */
public class Server {
  private ServerSender serverSender;
  private ServerReceiver serverReceiver;
  private ServerLogic serverLogic;
  private Ping ping;
  private boolean gameEnded;


  /**
   * Server initialization: receiver, sender and logic threads are started
   *
   * @param type game type: either "singleplayer" or "mutliplayer"
   * @throws SocketException
   */
  public void startServer(GameType type) throws SocketException {
    gameEnded = false;
    ping = new Ping();
    ping.start();

    serverSender = new ServerSender();
    serverReceiver = new ServerReceiver(serverSender, type);
    serverLogic = new ServerLogic(serverReceiver, serverSender);

    serverSender.start();
    serverReceiver.start();
    serverLogic.start();
  }

  public ServerSender getServerSender() {
    return serverSender;
  }

  public void startEncounterLogic(EncounterState encounterState) {
    serverLogic.setEncounterLogic(encounterState);
  }

  public static void main(String args[]) throws Exception {
    Ping ping = new Ping();
    ping.start();

    ServerSender serverSender = new ServerSender();
    ServerReceiver serverReceiver = new ServerReceiver(serverSender, GameType.MULTIPLAYER_HOST);

    serverSender.start();
    serverReceiver.start();

    serverSender.sendMessage(
        "This is a message from the server sent just after the game has started");
  }

  public void closeEverything() {
    if (serverSender != null) serverSender.stopRunning();
    if (serverReceiver != null) serverReceiver.stopRunning();
    if (serverLogic != null) serverLogic.stopRunning();
    if (ping != null) ping.stopRunning();

    gameEnded = true;
  }

  public boolean isGameEnded() {
    return gameEnded;
  }

  public void startRaceSelectionLogic(Game game) {
    serverLogic.setRaceSelectionLogic(game);
  }

  public void startBodyLogic() { serverLogic.setBodyLogic(); }

}
