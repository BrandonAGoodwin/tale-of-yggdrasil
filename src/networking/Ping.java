package networking;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;

/** Server Pinging thread that deals with notifying newly connected clients of the Server's IP */
public class Ping extends Thread {
  private MulticastSocket socket;
  private boolean run;

  public Ping(){
    run = true;
  }

  public void run() {
    try {
      socket = new MulticastSocket(4445);
      InetAddress group = InetAddress.getByName("239.255.255.255");

      byte[] buf;
      String message = "Pinging";
      buf = message.getBytes();
      DatagramPacket sending = new DatagramPacket(buf, 0, buf.length, group, 4446);

      while (run) {
        Enumeration<NetworkInterface> faces = NetworkInterface.getNetworkInterfaces();
        while (faces.hasMoreElements()) {
          NetworkInterface iface = faces.nextElement();
          if (iface.isLoopback() || !iface.isUp()) continue;

          Enumeration<InetAddress> addresses = iface.getInetAddresses();
          while (addresses.hasMoreElements()) {
            InetAddress addr = addresses.nextElement();
            socket.setInterface(addr);
            socket.send(sending);
            // System.out.println("Sent message");
          }
        }
        Thread.sleep(100);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void stopRunning() {
    run = false;
    socket.close();
  }
}
