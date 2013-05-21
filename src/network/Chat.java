package network;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 4/30/13
 * Time: 12:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class Chat {

    // Defines to what position on the screen the chat will reach from the bottom
    private final float ChatTop = 2.0f * Display.getHeight() / 3;
    TrueTypeFont chatFont;
    private List<String> log;
    private StringBuffer messageToSend;
    private Client client;

    public Chat(Client c) {
        log = new ArrayList<String>();
        messageToSend = new StringBuffer();

        client = c;

        // First message on screen lets client know where they've connected
        log.add("Client connected to " + client.host + " using port " + client.port);

        // Create font, push Texture to avoid messing with model texture
        Font font = new Font("Courier New", Font.PLAIN, 13);
        glPushAttrib(GL_TEXTURE_BIT);
        chatFont = new TrueTypeFont(font, true);
        glPopAttrib();
    }

    /**
     * Renders the chatbox and messages
     */
    public void render() {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // Push currently enabled flags
        glPushAttrib(GL_ENABLE_BIT);
        {
            glDisable(GL_DEPTH_TEST); // Depth is disabled to prevent clipping
            glDisable(GL_TEXTURE_2D); // Texture 2D is disabled so no textures are incorporated
            glDisable(GL_LIGHTING); // Lighting is turned off for 2D
            glEnable(GL_BLEND); // Blend is needed for transparency with game

            drawChatBox(); // Draw the container for chat box
            getMessageFromClient(); // Poll for message from client receiver
            drawText(); // Draw the text to the screen
        }
        glPopAttrib();
    }

    /**
     * Draw a chatbox from the bottom of the screen to ChatTop, and fill the screen horizontally
     */
    private void drawChatBox() {
        glBegin(GL_QUADS);
        {
            glColor4f(0.0f, 0.0f, 0.0f, 0.5f);
            glVertex2f(0.0f, Display.getHeight());
            glVertex2f(0.0f, ChatTop);
            glVertex2f(Display.getWidth(), ChatTop);
            glVertex2f(Display.getWidth(), Display.getHeight());
        }
        glEnd();
    }

    /**
     * Add a character to the message buffer
     *
     * @param c
     */
    public void addChar(char c) {
        if (messageToSend.length() < 255)
            messageToSend.append(c);
    }

    /**
     * Backspace a character from the message buffer
     */
    public void removeChar() {
        if (messageToSend.length() > 0)
            messageToSend.deleteCharAt(messageToSend.length() - 1);
    }

    /**
     * Send a message to the client
     *
     * @return true if message is not empty
     */
    public boolean sendMessage() {
        if (messageToSend.length() == 0)
            return false;
        client.sendMessage(messageToSend.toString());
        addMessageToLog("You: " + messageToSend.toString());
        messageToSend.delete(0, messageToSend.length());
        return true;
    }

    /**
     * Poll the client for a message, add it to the log if not null
     */
    private void getMessageFromClient() {
        String rec = client.receiveMessage();

        if (rec != null) {
            addMessageToLog(rec);
        }
    }

    /**
     * Draw the chatlog to the chat
     */
    private void drawText() {
        for (int i = 0; i < log.size(); i++) {
            chatFont.drawString(0, i * 15 + ChatTop, log.get(i), Color.white);
        }
        chatFont.drawString(0, 12 * 15 + ChatTop, messageToSend.toString(), Color.green);
    }

    /**
     * Add a message to the log
     *
     * @param message
     */
    private void addMessageToLog(String message) {
        log.add(message);
        if (log.size() > 12) {
            log.remove(0);
        }
    }

    /**
     * Disconnect from the server
     */
    public void disconnect() {
        client.sendMessage("Disconnected");
        client = null;
    }
}