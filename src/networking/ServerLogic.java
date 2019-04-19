package networking;

/** Server Thread responsible for dealing with game logic based on incoming messages */
public class ServerLogic extends Thread {

  /** Possible Server Logics. */
  public enum LogicType {
    RACE_SELECTION_LOGIC,
    BODY_LOGIC,
    ENCOUNTER_LOGIC
  }

  private LogicType currentLogicType;

  private ServerReceiver serverReceiver;
  private main.com.bodyconquest.networking.ServerSender serverSender;

  private boolean run;

  // Encounter LogicType Variables
  private EncounterState encounterState;

  // Race Selection Variables
  private Game game;
  private boolean topPlayerReady;
  private boolean bottomPlayerReady;

  //private RaceSelectionLogic raceSelectionLogic;

  /**
   * Constructor.
   *
   * @param serverReceiver The ServerReceiver thread of the same Server.
   */
  public ServerLogic(ServerReceiver serverReceiver, ServerSender serverSender) {
    this.serverReceiver = serverReceiver;
    this.serverSender = serverSender;
    this.run = true;
    topPlayerReady = false;
    bottomPlayerReady = false;
    init();
  }

  private void init() {
    currentLogicType = null;
  }

  /** Deals with game logic tasks of the incoming messages */
  public void run() {
    while (run) {
      try {
        String message = serverReceiver.receivedMessages.take();

        if (currentLogicType == null) {
          System.err.println("[ERROR] No server logic has been set.");
          // Right now we throw away messages we don't interpret
          // In future we may want to add them back to the received messages list
          continue;
        }

        if (currentLogicType == LogicType.RACE_SELECTION_LOGIC) raceSelectionLogic(message);
        if (currentLogicType == LogicType.ENCOUNTER_LOGIC) encounterLogic(message);
        if (currentLogicType == LogicType.BODY_LOGIC) bodyLogic(message);

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void bodyLogic(String message) {
    int pointer;

    if(message.startsWith(MessageMaker.CONFIRM_ORGAN_HEADER)) {
      Organ organ;

      pointer = MessageMaker.CONFIRM_ORGAN_HEADER.length();

      String encodedOrgan = message.substring(pointer, pointer + Organ.getEncodedLength());
      organ = Organ.decode(encodedOrgan);

      game.startEncounterState(organ);
      serverSender.sendMessage(MessageMaker.startEncounterMessage(organ));
    }
  }

  private void raceSelectionLogic(String message) {
    int pointer;
    if (message.startsWith(MessageMaker.RACE_HEADER)) {
      Disease disease;
      PlayerType playerType;

      pointer = MessageMaker.RACE_HEADER.length();

      String encodedDisease = message.substring(pointer, pointer + Disease.getEncodedLength());
      disease = Disease.decode(encodedDisease);
      pointer += Disease.getEncodedLength() + 1;

      String encodedPlayerType =
              message.substring(pointer, pointer + PlayerType.getEncodedLength());
      playerType = PlayerType.decode(encodedPlayerType);

      if (playerType == PlayerType.PLAYER_TOP) game.setPlayerTop(disease);
      if (playerType == PlayerType.PLAYER_BOTTOM) game.setPlayerBottom(disease);

      // Response message for the other player to receive so they can update the other player's
      // selection on their screen
      String responseMessage = MessageMaker.diseaseMessage(disease, playerType);
      serverSender.sendMessage(responseMessage);

      // Does nothing for player type AI as of now
      // if(playerType == PlayerType.AI)          playerTop = new Player(playerType, disease);
    } else if (message.startsWith(MessageMaker.CONFIRM_RACE_HEADER)) {
      PlayerType player;

      pointer = MessageMaker.CONFIRM_RACE_HEADER.length();

      String encodedPlayerType = message.substring(pointer, pointer + PlayerType.getEncodedLength());
      player = PlayerType.decode(encodedPlayerType);

      if(player == PlayerType.PLAYER_BOTTOM) {
        bottomPlayerReady = true;
      } else {
        topPlayerReady = true;
      }

      if(game.getGameType() == GameType.SINGLE_PLAYER || bottomPlayerReady && topPlayerReady) {
        game.startBodyState();
        serverSender.sendMessage(MessageMaker.START_BODY);
      }
    }

    else if(message.startsWith(MessageMaker.USERNAME_)){
//      PlayerType player;
//      pointer = MessageMaker.USERNAME_.length();
//
//      String encodedPlayerType = message.substring(pointer,pointer + PlayerType.getEncodedLength());
//      player = PlayerType.decode(encodedPlayerType);
//      pointer +=PlayerType.getEncodedLength() +1;
//
//      String username = message.substring(pointer);

      serverSender.sendMessage(message);
    }

    else {
      System.err.println("[ERROR] This message doesn't conform to the current logic.");
    }
  }

  private void encounterLogic(String message) {
    int pointer;
    if (message.startsWith(MessageMaker.TROOP_SPAWN_HEADER)) {
      pointer = MessageMaker.TROOP_SPAWN_HEADER.length();

      UnitType unit =
          UnitType.decode(message.substring(pointer, pointer + UnitType.getEncodedLength()));
      pointer += UnitType.getEncodedLength() + 1;

      PlayerType playerType =
          PlayerType.decode(message.substring(pointer, pointer + PlayerType.getEncodedLength()));
      pointer += PlayerType.getEncodedLength() + 1;

      Lane lane = Lane.decode(message.substring(pointer, pointer + Lane.getEncodedLength()));
      //System.out.println("TRYING TO SPAWN NOW");

      encounterState.spawnUnit(unit, lane, playerType);
      //System.out.println("FINISHED SPAWNING");

    } else if (message.startsWith(MessageMaker.ABILITY_CAST_HEADER)) {

      AbilityType abilityType;
      PlayerType player;
      int xDest;
      int yDest;

      pointer = MessageMaker.ABILITY_CAST_HEADER.length();

      String encodedAbility = message.substring(pointer, pointer + AbilityType.getEncodedLength());
      abilityType = AbilityType.decode(encodedAbility);
      pointer += AbilityType.getEncodedLength() + 1;

      String encodedPlayerType =
          message.substring(pointer, pointer + PlayerType.getEncodedLength());
      player = PlayerType.decode(encodedPlayerType);
      pointer += PlayerType.getEncodedLength() + 1;

      String xString = message.substring(pointer, pointer + MessageMaker.COORDINATE_PADDING);
      xDest = Integer.parseInt(xString);
      pointer += MessageMaker.COORDINATE_PADDING + 1;

      String yString = message.substring(pointer, pointer + MessageMaker.COORDINATE_PADDING);
      yDest = Integer.parseInt(yString);

      encounterState.castAbility(abilityType, player, xDest, yDest);

    } else if (message.startsWith(MessageMaker.LANE_ABILITY_CAST_HEADER)) {

      AbilityType abilityType;
      PlayerType player;
      Lane lane;

      pointer = MessageMaker.LANE_ABILITY_CAST_HEADER.length();

      String encodedAbility = message.substring(pointer, pointer + AbilityType.getEncodedLength());
      abilityType = AbilityType.decode(encodedAbility);
      pointer += AbilityType.getEncodedLength() + 1;

      String encodedPlayerType =
          message.substring(pointer, pointer + PlayerType.getEncodedLength());
      player = PlayerType.decode(encodedPlayerType);
      pointer += PlayerType.getEncodedLength() + 1;

      String encodedLane = message.substring(pointer, pointer + Lane.getEncodedLength());
      lane = Lane.decode(encodedLane);

      encounterState.castAbility(abilityType, player, lane);

    } else if (message.equals("PAUSE")) {
      // TO DO: pause the game
    } else if (message.equals("EXIT")) {
      // TO DO: exit the game
    }
  }

  public void setEncounterLogic(EncounterState encounterState) {
    this.encounterState = encounterState;
    currentLogicType = LogicType.ENCOUNTER_LOGIC;
  }

  public void setRaceSelectionLogic(Game game) {
    this.game = game;
    currentLogicType = LogicType.RACE_SELECTION_LOGIC;
  }

  public void setBodyLogic() {
    currentLogicType = LogicType.BODY_LOGIC;
  }


  public void stopRunning() {
    run = false;
  }

//  private class RaceSelectionLogic implements Logic{
//
//    private boolean topPlayerConfirmed;
//    private boolean bottomPlayerConfirmed;
//
//    RaceSelectionLogic(){
//      topPlayerConfirmed = false;
//      bottomPlayerConfirmed = false;
//    }
//
//    @Override
//    public void interpret(String message) {
//      if (message.startsWith(MessageMaker.RACE_HEADER)) {
//        Disease disease;
//        PlayerType playerType;
//
//        int pointer = MessageMaker.RACE_HEADER.length();
//
//        String encodedDisease = message.substring(pointer, pointer + Disease.getEncodedLength());
//        disease = Disease.decode(encodedDisease);
//        pointer += Disease.getEncodedLength() + 1;
//
//        String encodedPlayerType =
//                message.substring(pointer, pointer + PlayerType.getEncodedLength());
//        playerType = PlayerType.decode(encodedPlayerType);
//
//        if (playerType == PlayerType.PLAYER_TOP) game.setPlayerTop(disease);
//        if (playerType == PlayerType.PLAYER_BOTTOM) game.setPlayerBottom(disease);
//
//        // Response message for the other player to receive so they can update the other player's
//        // selection on their screen
//        String responseMessage = MessageMaker.diseaseMessage(disease, playerType);
//        serverSender.sendMessage(responseMessage);
//
//        // Does nothing for player type AI as of now
//        // if(playerType == PlayerType.AI)          playerTop = new Player(playerType, disease);
//      } else if (message.equals(MessageMaker.CONFIRM_RACE)) {
//
//      } else {
//        System.err.println("[ERROR] This message doesn't conform to the current logic.");
//      }
//    }
//  }
}
