package screens;

import entity.Enemy;
import entity.LaserBeam;
import environment.Model;
import helpers.Delegate;
import network.Chat;
import network.Client;
import network.MessageType;
import network.Server;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 5/2/13
 * Time: 12:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class MultiGameScreen extends GameplayScreen {
    private static final int PORT = 7777;
    IPPlaceHolder ip;
    Server server;
    Client client;
    Chat chat;
    private boolean chatting = false;

    public MultiGameScreen(Delegate d) {
        super(d);
    }

    public MultiGameScreen(Delegate d, IPPlaceHolder ip) {
        super(d);
        this.ip = ip;
    }

    @Override
    public void Initialize() {
        super.Initialize();
        if (ip == null) {
            server = new Server(PORT);
            ip = new IPPlaceHolder();
            ip.val = "127.0.0.1";
            new Thread(server).start();
        }

        client = new Client(ip.val, PORT);
        chat = new Chat(client);
    }

    @Override
    public void Render() {
        super.Render();

        if (chatting)
            chat.render();
    }

    @Override
    public void Update() {
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Exit();
            return;
        }

        boolean keyPressed = Keyboard.next();
        if (!chatting) {
            super.Update();
            broadcastMove();
            if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
                chatting = true;
            }
        } else if (keyPressed && Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();
            if (key == Keyboard.KEY_RETURN) {
                if (!chat.sendMessage())
                    chatting = false;
            } else if (key == Keyboard.KEY_BACK) {
                chat.removeChar();
            } else {
                char c = Keyboard.getEventCharacter();
                if (Pattern.matches("[a-zA-Z0-9\\s\\p{P}]", new String(new char[]{c})))
                    chat.addChar(c);
            }
        }

        // Update other players
        updateEnemies(client.receiveActions());
    }

    private void broadcastMove() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);
        Vector3f vec = new Vector3f();
        Vector3f.sub(player.offset, cam.getPosition(), vec);
        float pitch = cam.getPitch() - player.model.pitch;
        float yaw = cam.getYaw() - player.model.yaw;
        float roll = cam.getRoll() - player.model.roll;

        try {
            dataStream.writeFloat(vec.getX());
            dataStream.writeFloat(vec.getY());
            dataStream.writeFloat(vec.getZ());
            dataStream.writeFloat(-pitch);
            dataStream.writeFloat(-yaw);
            dataStream.writeFloat(-roll);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        byte[] data = new byte[6 * 4 + 2];
        // Set the option byte to Movement
        data[0] = (byte) (MessageType.Movement.ordinal() << 4);
        // 24 bytes are sent for updating position and rotation
        data[1] = 6 * 4;

        System.arraycopy(byteStream.toByteArray(), 0, data, 2, data.length - 2);

        try {
            client.sendData(data);
        } catch (IOException e) {
            e.printStackTrace();
            Exit();
        }
    }

    private void broadcastLaser(LaserBeam l) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);
        Vector3f origin = l.origin;
        float yaw = l.yaw;
        float pitch = l.pitch;

        try {
            dataStream.writeFloat(origin.getX());
            dataStream.writeFloat(origin.getY());
            dataStream.writeFloat(origin.getZ());
            dataStream.writeFloat(yaw);
            dataStream.writeFloat(pitch);
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }
        byte[] data = new byte[5 * 4 + 2];
        // Set the option byte to Laser
        data[0] = (byte) (MessageType.Laser.ordinal() << 4);
        // 20 bytes are sent for start position and rotation
        data[1] = 5 * 4;

        System.arraycopy(byteStream.toByteArray(), 0, data, 2, data.length - 2);

        try {
            client.sendData(data);
        } catch (IOException e) {
            e.printStackTrace();
            Exit();
        }
    }

    private void updateEnemies(Set<Map.Entry<Byte, byte[]>> actionSet) {
        for (Map.Entry<Byte, byte[]> entry : actionSet) {
            byte key = entry.getKey();
            byte id = (byte) (key & 0x0F);
            MessageType option = MessageType.values()[key >> 4 & 0x0F];

            if (!enemies.containsKey(id))
                enemies.put(id, new Enemy(new Model("data/DarkFighter/dark_fighter_2.obj", 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f), new Vector3f(0, 0, -5)));

            switch (option) {
                case Movement:
                    moveEnemy(id, entry.getValue());
                    break;
                case Laser:
                    addLaser(id, entry.getValue());
                    break;
                case Disconnect:
                    enemies.remove(id);
                    break;
            }
        }
    }

    private void moveEnemy(byte id, byte[] data) {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        DataInputStream dataStream = new DataInputStream(byteStream);
        float[] fArr = new float[data.length / 4];  // 4 bytes per float
        for (int i = 0; i < fArr.length; i++) {
            try {
                fArr[i] = dataStream.readFloat();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        enemies.get(id).model.updatePosition(fArr[0], fArr[1], fArr[2]);
        enemies.get(id).model.updateRotation(fArr[3], fArr[4], fArr[5]);
    }

    private void addLaser(byte id, byte[] data) {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        DataInputStream dataStream = new DataInputStream(byteStream);
        float[] fArr = new float[data.length / 4];  // 4 bytes per float

        for (int i = 0; i < fArr.length; i++) {
            try {
                fArr[i] = dataStream.readFloat();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        lasers.add(new LaserBeam(new Vector3f(fArr[0], fArr[1], fArr[2]), fArr[3], fArr[4], id));
    }

    @Override
    protected void shootLaser() {
        LaserBeam temp = new LaserBeam(cam, player.offset, player.hud);
        temp.ownerID = -50;
        lasers.add(temp);
        broadcastLaser(temp);
    }

    @Override
    protected void Exit() {
        chat.disconnect();
        chat = null;
        try {
            client.sendData(new byte[]{(byte) (MessageType.Disconnect.ordinal() << 4)});
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.disconnect();
        client = null;
        delegate.change(0);
    }


}
