package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: brad
 * Date: 4/24/13
 * Time: 12:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class Server implements Runnable {
    private static HashMap<Integer, InetAddress> portMap = new HashMap<Integer, InetAddress>();

    @Override
    public void run() {
        int serverPort = 7777;
        System.out.println("Server using port " + serverPort);

        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(serverPort);
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Server start");

        while(true) {
            // 1KB messages
            byte[] receiveData = new byte[1024];

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            // Blocks until a packet is received
            try {
                serverSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            // Create string out of received byte array
            String clientMessage = new String(receivePacket.getData()).trim();

            System.out.println("Message received from socket address " + receivePacket.getSocketAddress());
            System.out.println("Message: " + clientMessage);

            InetAddress clientIP = receivePacket.getAddress();

            System.out.println("Client IP = " + clientIP);
            System.out.println("Hostname = " + clientIP.getHostName());

            // Port number that the connection came from
            int clientPort = receivePacket.getPort();
            portMap.put(clientPort, clientIP);

            // Send message to other conversation participants

            byte[] sendData = clientMessage.getBytes();

            for(Integer port : portMap.keySet()) {
                if(port != clientPort) {
                    InetAddress ip = portMap.get(port);
                    // Create DatagramPacket to send to other client
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
                    System.out.println("Sending message to " + ip + ":" + port);
                    try {
                        serverSocket.send(sendPacket);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
            System.out.println();
        }
    }
}
