import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: brad
 * Date: 4/24/13
 * Time: 12:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class Server {
    private static HashSet<Integer> portSet = new HashSet<Integer>();

    public static void main(String[] args) throws IOException {
        int serverPort = 7777;

        if (args.length > 0) {
            serverPort = Integer.valueOf(args[0]);
        }
        System.out.println("Server using port " + serverPort);

        DatagramSocket serverSocket = new DatagramSocket(serverPort);

        System.out.println("Server start");

        while(true) {
            // 1KB messages
            byte[] receiveData = new byte[1024];

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            // Blocks until a packet is received
            serverSocket.receive(receivePacket);

            // Create string out of received byte array
            String clientMessage = new String(receivePacket.getData()).trim();

            System.out.println("Message received from socket address " + receivePacket.getSocketAddress());
            System.out.println("Message: " + clientMessage);

            InetAddress clientIP = receivePacket.getAddress();

            System.out.println("Client IP = " + clientIP);
            System.out.println("Hostname = " + clientIP.getHostName());

            // Port number that the connection came from
            int clientPort = receivePacket.getPort();
            System.out.println("Adding " + clientPort + " to list of connected client ports");
            portSet.add(clientPort);

            // Send message to other conversation participants

            byte[] sendData = clientMessage.getBytes();

            for(Integer port : portSet) {
                if(port != clientPort) {
                    // Create DatagramPacket to send to other client
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientIP, port);
                    System.out.println("Sending message to " + port);
                    serverSocket.send(sendPacket);
                }
            }
            System.out.println();
        }
    }
}
