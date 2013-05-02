package network;

import java.io.IOException;
import java.net.*;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 4/30/13
 * Time: 1:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class Client {
    InetAddress ia;
    Sender sender;
    ReceiverThread receiver;
    Queue<String> messageQueue;

    public Client(String host, int clientPort) {
        messageQueue = new ArrayDeque<String>();
        try {
            // Get IP of host according to hostname
            ia = InetAddress.getByName(host);
            // Creates a Sender object based on IP and port
            sender = new Sender(ia, clientPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }
        // Creates a Receiver object based on the DatagramSocket created in the sender
        receiver = new ReceiverThread(sender.getSocket(), messageQueue);
        // Sends an initial message to the server announcing client's arrival
        sendMessage("A new player has entered the room");
        // Start polling the server for messages
        receiver.start();
    }

    /**
     * Checks the message queue for any unread messages
     * @return
     */
    public String receiveMessage() {
        return messageQueue.poll();
    }

    /**
     * Sends a message to the server
     * @param message
     */
    public void sendMessage(String message) {
        sender.sendMessage(message);
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
     * @param clientMessage
     */
    public void sendMessage(String clientMessage) {
        try {
            byte[] data = clientMessage.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, serverIPAddress, serverPort);
            clientSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ReceiverThread extends Thread {
    private DatagramSocket clientSocket;
    private Queue<String> messageQueue;

    public ReceiverThread(DatagramSocket ds, Queue<String> queue) {
        clientSocket = ds;
        messageQueue = queue;
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
        byte[] receiveData = new byte[1024];
        // Loops until a problem occurs or halt is called
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            try {
                // Blocks until message received or interrupt issued
                clientSocket.receive(receivePacket);

                String serverReply = new String(receivePacket.getData(), 0, receivePacket.getLength());

                messageQueue.add(serverReply);
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
