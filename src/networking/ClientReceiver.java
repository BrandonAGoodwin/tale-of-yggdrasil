package networking;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/** Client thread responsible for receiving messages from the server */
public class ClientReceiver extends Thread {

  public InetAddress address;
  public DatagramSocket socket;
  public InetAddress group;
  public AtomicInteger id;
  public LinkedBlockingQueue<String> receivedMessages;
  private boolean run;

  /**
   * ClientReceiver initialization
   *
   * @throws IOException
   */
  public ClientReceiver() throws IOException {
    address = getIpAddress();
    socket = new DatagramSocket(3001);
    id = new AtomicInteger(0);
    receivedMessages = new LinkedBlockingQueue<String>();
    run = true;
  }

  /**
   * Connects to a multicast UDP group and receives a message from the 'Ping' server thread and
   * stores the address of the received message as the IP of the server
   *
   * @return the IP address of the server
   */
  public InetAddress getIpAddress() {
    try {
      MulticastSocket mSocket = new MulticastSocket(4446);
      group = InetAddress.getByName("239.255.255.255");
      joinGroup(mSocket, group);
      byte[] buf = new byte[256];
      DatagramPacket packet = new DatagramPacket(buf, buf.length);
      //System.out.println("before rec packet");
      mSocket.receive(packet);
      //System.out.println("after rec packet");
      address = packet.getAddress();
      mSocket.close();
      return address;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Sets up the game and loops continuously checking for new incoming messages form the server and
   * receives them
   */
  public void run() {
    gameSetup();
    while (run) {
      try {
        byte[] buf = new byte[1000000];
        //byte[] buf = new byte[16000];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        if (run){
          socket.receive(packet);
        }
        //System.out.println(packet.getData().length);
        String received = new String(packet.getData()).trim();
//        System.out.println(
//            "Client received -> " + received.trim() + " ------ from: " + packet.getAddress());
        receivedMessages.put(received.trim());
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /** Receives and assigns this client an ID from the server and waits for the game to start */
  public void gameSetup() {
    String received = "";
    while (!received.equals("start game")) {
      try {
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        received = new String(packet.getData()).trim();
        System.out.println(received);
        if (id.toString().equals("0") && received.startsWith("ID: ")) {
          if (received.endsWith("a")) {
            id = new AtomicInteger(1);
          } else if (received.endsWith("b")) {
            id = new AtomicInteger(2);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    System.out.println(
        "THIS CLIENT HAS CONNECTED AND JOINED THE GAME, CLIENT ID: " + id.toString());
  }

  /**
   * Joins a multicast group
   *
   * @param socket
   * @param group
   * @throws IOException
   */
  private static void joinGroup(MulticastSocket socket, InetAddress group) throws IOException {
    Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
    while (interfaces.hasMoreElements()) {
      NetworkInterface iface = interfaces.nextElement();
      if (iface.isLoopback() || !iface.isUp()) continue;

      Enumeration<InetAddress> addresses = iface.getInetAddresses();
      while (addresses.hasMoreElements()) {
        InetAddress addr = addresses.nextElement();
        socket.setInterface(addr);
        socket.joinGroup(group);
      }
    }
  }

  public void stopRunning(){
    run = false;
    socket.close();
  }
}
