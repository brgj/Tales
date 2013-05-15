package network;

import java.io.IOException;
import java.net.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 4/30/13
 * Time: 1:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class Client {
    public final String host;
    public final int port;
    // Strictly deals with messages sent between players
    private final Queue<String> messageQueue;
    /**
     * Maps specific actions and their owners to an array of bytes representing details of those actions
     * 0: Chat message
     * 1: ID message
     */
    private final Map<Byte, byte[]> actionMap;
    private Sender sender;
    private ReceiverThread receiver;

    public Client(String h, int p) {
        messageQueue = new ArrayDeque<String>();
        actionMap = new HashMap<Byte, byte[]>();
        host = h;
        port = p;
        try {
            // Get IP of host according to hostname
            InetAddress ia = InetAddress.getByName(host);
            // Creates a Sender object based on IP and port
            sender = new Sender(ia, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }
        // Creates a Receiver object based on the DatagramSocket created in the sender
        receiver = new ReceiverThread(sender.getSocket(), messageQueue, actionMap);
        // Sends an initial message to the server announcing client's arrival
        sendMessage("A new player has entered the room");
        // Start polling the server for messages
        receiver.start();
    }

    /**
     * Checks the message queue for any unread messages
     *
     * @return
     */
    public String receiveMessage() {
        return messageQueue.poll();
    }

    /**
     * Checks the actionMap for any changes in other players state
     *
     * @return
     */
    public Set<Map.Entry<Byte, byte[]>> receiveActions() {
        synchronized (actionMap) {
            Set<Map.Entry<Byte, byte[]>> actions = new HashSet<Map.Entry<Byte, byte[]>>(actionMap.entrySet());
            actionMap.clear();
            return actions;
        }
    }

    /**
     * Sends a text message to the server
     *
     * @param message
     */
    public void sendMessage(String message) {
        sender.sendMessage(message);
    }

    public void sendData(byte[] data) throws IOException {
        sender.sendData(data);
    }

    /**
     * Stops all client activity and disconnects from the server
     */
    public void disconnect() {
        receiver.halt();
        try {
            receiver.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
        sender.disconnect();
    }
}

class Sender {
    private InetAddress serverIPAddress;
    private DatagramSocket clientSocket;
    private int serverPort;

    public Sender(InetAddress address, int port) throws SocketException {
        serverIPAddress = address;
        serverPort = port;

        // Create a DatagramSocket for sending and receiving messages based on IP and port
        clientSocket = new DatagramSocket();
        clientSocket.connect(serverIPAddress, port);
    }

    /**
     * Returns the DatagramSocket used for sending and receiving messages
     *
     * @return
     */
    public DatagramSocket getSocket() {
        return clientSocket;
    }

    /**
     * Disconnects the clientSocket from the server
     */
    public void disconnect() {
        clientSocket.disconnect();
    }

    /**
     * Sends a packet to the server based on the string passed in
     *
     * @param clientMessage
     */
    public void sendMessage(String clientMessage) {
        try {
            byte[] data = new byte[clientMessage.length() + 1];
            // Encode as a text message
            data[0] = 0;
            // Copy data from string to byte array
            System.arraycopy(clientMessage.getBytes("UTF-8"), 0, data, 1, data.length - 1);
            sendData(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(byte[] data) throws IOException {
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, serverIPAddress, serverPort);
        clientSocket.send(sendPacket);
    }
}

class ReceiverThread extends Thread {
    private final Map<Byte, byte[]> actionMap;
    private DatagramSocket clientSocket;
    private Queue<String> messageQueue;

    public ReceiverThread(DatagramSocket ds, Queue<String> queue, Map<Byte, byte[]> map) {
        clientSocket = ds;
        messageQueue = queue;
        actionMap = map;
    }

    /**
     * Closes the DatagramSocket and interrupts and receiving that the socket was attempting
     */
    public void halt() {
        clientSocket.close();
    }

    /**
     * Constantly polls the server, requesting more messages, and adds them to the messageQueue
     */
    public void run() {
        byte[] receiveData = new byte[256];
        // Loops until a problem occurs or halt is called
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            try {
                // Blocks until message received or interrupt issued
                clientSocket.receive(receivePacket);

                int length = receivePacket.getLength();

                // If the message is a chat message, add to message queue
                if ((receiveData[0] & 0xF0) == 0) {
                    String serverReply = (receiveData[0] & 0x0F) + ": " + new String(receiveData, 1, length - 1);
                    messageQueue.add(serverReply);
                } else {
                    byte[] temp = new byte[length - 1];
                    System.arraycopy(receiveData, 1, temp, 0, length - 1);
                    synchronized (actionMap) {
                        actionMap.put(receiveData[0], temp);
                    }
                }

                Thread.yield();
            } catch (SocketException e) {
                System.err.println("Socket closed! Killing receiver");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}
