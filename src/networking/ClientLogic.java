package networking;

import networking.utilities.MessageMaker;
import networking.utilities.Serialization;

import java.io.IOException;

public class ClientLogic extends Thread {

  private enum Logic {
    ENCOUNTER_LOGIC
  }

  private Logic currentLogic;

  private ClientReceiver clientReceiver;
  private Communicator communicator;
  private boolean run;

  public ClientLogic(ClientReceiver clientReceiver, Communicator communicator) {
    this.clientReceiver = clientReceiver;
    this.communicator = communicator;
    this.run = true;
    currentLogic = null;
  }

  /** Deals with game logic tasks of the incoming messages */
  public void run() {
    while (run) {
      try {
        String message = clientReceiver.receivedMessages.take();

        if (currentLogic == null) {
          System.err.println("[ERROR] No client logic has been set.");
          continue;
        }

        if (currentLogic == Logic.ENCOUNTER_LOGIC)      encounterLogic(message);

      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }
  }


  private void encounterLogic(String message) throws IOException {
    int pointer;
    if (message.startsWith(MessageMaker.OBJECT_UPDATE_HEADER)) {
      pointer = MessageMaker.OBJECT_UPDATE_HEADER.length();
      String json = message.substring(pointer);
      CopyOnWriteArrayList<BasicObject> objects = Serialization.deserialize(json);
      communicator.populateObjectList(objects);

    } else if (message.startsWith(MessageMaker.POINTS_HEADER)) {
      int topPlayerPoints;
      int bottomPlayerPoints;
      pointer = MessageMaker.POINTS_HEADER.length();

      String topPointsString = message.substring(pointer, pointer + MessageMaker.POINTS_PADDING);
      topPlayerPoints = Integer.parseInt(topPointsString);
      pointer += MessageMaker.POINTS_PADDING + 1;

      String bottomPointsString = message.substring(pointer, pointer + MessageMaker.POINTS_PADDING);
      bottomPlayerPoints = Integer.parseInt(bottomPointsString);

      communicator.setScoreTop(topPlayerPoints);
      communicator.setScoreBottom(bottomPlayerPoints);

    }
  }

  public void setEncounterLogic() {
    currentLogic = Logic.ENCOUNTER_LOGIC;
  }

  public void stopRunning() {
    run = false;
  }
}