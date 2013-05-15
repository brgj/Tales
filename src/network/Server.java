package network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
//TODO: delete entry from addressMap on disconnect and free up ID
public class Server implements Runnable {
    private HashMap<Integer, InetAddress> addressMap;
    private HashMap<Integer, Byte> idMap;
    private int serverPort;

    public Server(int port) {
        addressMap = new HashMap<Integer, InetAddress>();
        idMap = new HashMap<Integer, Byte>();
        serverPort = port;
    }

    @Override
    public void run() {
        System.out.println("Server using port " + serverPort);

        DatagramSocket serverSocket;
        try {
            serverSocket = new DatagramSocket(serverPort);
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Server start");

        while (true) {
            //0.25KB messages
            byte[] receiveData = new byte[256];

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            // Blocks until a packet is received
            try {
                serverSocket.receive(receivePacket);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            InetAddress clientIP = receivePacket.getAddress();

//            System.out.println("Client IP = " + clientIP);
//            System.out.println("Hostname = " + clientIP.getHostName());

            // Port number that the connection came from
            int clientPort = receivePacket.getPort();
            if (!addressMap.containsKey(clientPort)) {
                addressMap.put(clientPort, clientIP);
                byte id = 0;
                while(idMap.containsValue(id))
                    id++;
                idMap.put(clientPort, id);
            }

            byte option = receiveData[0];
            byte[] sendData;

            // If the packet is not a text message
            if (option != 0) {
                // Second byte in receiveData is packet length (0-254)
                sendData = new byte[receiveData[1] + 1];
                // Create the identifier for which option/id this packet represents
                sendData[0] = (byte) ((option & 0xF0) | (idMap.get(clientPort) & 0x0F));
                // Copy the rest of the data in
                System.arraycopy(receiveData, 2, sendData, 1, sendData.length-1);

            } else {
                // Create string out of received byte array and trim it down
                String clientMessage;
                try {
                    clientMessage = new String(receiveData, "UTF-8").trim();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    return;
                }

                // System.out.println("Message received from socket address " + receivePacket.getSocketAddress());
                // System.out.println("Message: " + clientMessage);

                // Create a byte buffer of trimmed message size + 1
                sendData = new byte[clientMessage.length() + 1];
                // Put the option/ID byte at the start of the buffer
                sendData[0] = (byte) ((option & 0xF0) | (idMap.get(clientPort) & 0x0F));

                try {
                    // Copy byte data from message to buffer
                    System.arraycopy(clientMessage.getBytes("UTF-8"), 0, sendData, 1, sendData.length - 1);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    return;
                }
            }

            // Send packet to other players
            for (Integer port : addressMap.keySet()) {
                if (port != clientPort) {
                    InetAddress ip = addressMap.get(port);
                    // Create DatagramPacket to send to other client
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip, port);
                    try {
                        serverSocket.send(sendPacket);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
    }
}
