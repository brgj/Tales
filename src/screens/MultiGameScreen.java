package screens;

import environment.Model;
import helpers.Delegate;
import network.Chat;
import network.Client;
import network.Server;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import java.io.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static org.lwjgl.opengl.GL11.glPopAttrib;

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
    HashMap<Byte, Model> enemies = new HashMap<Byte, Model>();
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
            try {
                ip.val = Inet4Address.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return;
            }
            new Thread(server).start();
            // TODO: get rid of this
            enemies.put((byte) 1, model);
        } else {
            enemies.put((byte) 0, model);
        }

        client = new Client(ip.val, PORT);
        chat = new Chat(client);
    }

    @Override
    public void Render() {
        super.Render();

        if (chatting)
            chat.render();
        glPopAttrib();
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

    @Override
    protected void move(float units, int dir) {
        super.move(units, dir);

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        DataOutputStream dataStream = new DataOutputStream(byteStream);
        Vector3f vec = cam.getPosition();
        try {
            dataStream.writeFloat(-vec.getX());
            dataStream.writeFloat(-vec.getY());
            dataStream.writeFloat(-vec.getZ());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        byte[] data = new byte[3 * 4 + 2];
        // Set the option byte to 1
        data[0] = 1 << 4;
        // 12 bytes are sent for updating position
        data[1] = 3 * 4;

        System.arraycopy(byteStream.toByteArray(), 0, data, 2, data.length - 2);

        client.sendData(data);
    }

    private void updateEnemies(Set<Map.Entry<Byte, byte[]>> actionSet) {
        for (Map.Entry<Byte, byte[]> entry : actionSet) {
            switch (entry.getKey() >> 4 & 0x0F) {
                // Movement
                case 1:
                    moveEnemy((byte) (entry.getKey() & 0x0F), entry.getValue());
            }
        }
    }

    private void moveEnemy(byte id, byte[] data) {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        DataInputStream dataStream = new DataInputStream(byteStream);
        float[] fArr = new float[data.length / 4];  // 4 bytes per float
        for (int i = 0; i < fArr.length; i++)
        {
            try {
                fArr[i] = dataStream.readFloat();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        enemies.get(id).updatePosition(fArr[0], fArr[1], fArr[2]);
    }

    @Override
    protected void Exit() {
        chat.disconnect();
        chat = null;
        delegate.change(0);
    }


}
