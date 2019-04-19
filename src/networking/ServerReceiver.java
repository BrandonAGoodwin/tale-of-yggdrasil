package networking;

import networking.constants.GameType;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.LinkedBlockingQueue;

/** Server thread that is responsible for receiving messages from all connected clients */
public class ServerReceiver extends Thread {
  public DatagramSocket socket;
  public ServerSender serverSender;
  public LinkedBlockingQueue<String> receivedMessages;
  public GameType type;
  public int numberOfClients;
  private boolean run;

  /**
   * ServerReceiver initialization
   *
   * @param serverSender the ServerSender thread of the same Server
   * @param type the type of the game: either "singleplayer" or "multiplayer"
   * @throws SocketException
   */
  public ServerReceiver(ServerSender serverSender, GameType type) throws SocketException {
    socket = new DatagramSocket(3000);
    this.serverSender = serverSender;
    receivedMessages = new LinkedBlockingQueue<String>();
    this.type = type;
    numberOfClients = 0;
    run = true;
  }

  /** A method that continuously checks for incoming messages */
  public void run() {
    gameSetup();
    try{
      while (run) {
        try {
          byte[] buf = new byte[1024];
          DatagramPacket packet = new DatagramPacket(buf, 1024);
          if (run){
            socket.receive(packet);
          }
          String receivedMessage = new String(packet.getData(), 0, packet.getLength());
//          System.out.println(
//              "Server received -> " + receivedMessage + "------- from: " + packet.getAddress());
          receivedMessages.put(receivedMessage.trim());
        } catch (IOException | InterruptedException e) {
          e.printStackTrace();
        }
      }
    } finally{
//      socket.close();
    }
  }

  /** A method that deals with receiving and storing client ID and waiting for the game to start */
  public void gameSetup() {
    while (run) {
      try {
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, 1024);
        socket.receive(packet);
        String receivedMessage = new String(packet.getData(), 0, packet.getLength());
        System.out.println(
            "SETTING UP THE GAME: Server received -> "
                + receivedMessage
                + "------- from: "
                + packet.getAddress());
        if (!serverSender.connectedClients.contains(packet.getAddress())) {
          serverSender.connectedClients.add(packet.getAddress());
        }

        if (receivedMessage.trim().equals("connected")
            && type == GameType.SINGLE_PLAYER
            && numberOfClients == 0) {
          serverSender.sendMessage("ID: a");
          serverSender.sendMessage("start game");
          numberOfClients++;
          break;
        } else if (receivedMessage.trim().equals("connected") && type == GameType.MULTIPLAYER_HOST) {
          if (numberOfClients == 0) {
            serverSender.sendMessage("ID: a");
            numberOfClients++;
          } else if (numberOfClients == 1) {
            serverSender.sendMessage("ID: b");
            serverSender.sendMessage("start game");
            numberOfClients++;
            break;
          }
        }
      } catch (SocketException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  public void stopRunning(){
    run = false;
//    Timer.startTimer(100);
//    if (socket != null){
      socket.close();
//    }
  }
}
