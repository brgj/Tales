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
            ia = InetAddress.getByName(host);
            sender = new Sender(ia, clientPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return;
        } catch (SocketException e) {
            e.printStackTrace();
            return;
        }
        receiver = new ReceiverThread(sender.getSocket(), messageQueue);
        sendMessage("A new player has entered the room");
        receiver.start();
    }

    public String receiveMessage() {
        return messageQueue.poll();
    }

    public void sendMessage(String message) {
        sender.sendMessage(message);
    }
}

class Sender {
    private InetAddress serverIPAddress;
    private DatagramSocket clientSocket;
    private int serverPort;

    public Sender(InetAddress address, int serverPort) throws SocketException {
        this.serverIPAddress = address;
        this.serverPort = serverPort;
        this.clientSocket = new DatagramSocket();
        this.clientSocket.connect(serverIPAddress, serverPort);
    }

    public DatagramSocket getSocket() {
        return clientSocket;
    }

    public void sendMessage(String clientMessage) {
        try {
            byte[] data = clientMessage.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(data, data.length, serverIPAddress, serverPort);
            clientSocket.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}

class ReceiverThread extends Thread {
    private DatagramSocket clientSocket;
    private boolean stopped = false;
    private Queue<String> messageQueue;

    public ReceiverThread(DatagramSocket ds, Queue<String> queue) {
        clientSocket = ds;
        messageQueue = queue;
    }

    public void halt() {
        stopped = true;
    }

    public void run() {
        byte[] receiveData = new byte[1024];
        while (true) {
            if (stopped) {
                return;
            }

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            try {
                clientSocket.receive(receivePacket);

                String serverReply = new String(receivePacket.getData(), 0, receivePacket.getLength());

                messageQueue.add(serverReply);
                Thread.yield();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
