package network;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 4/30/13
 * Time: 12:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class Chat {

    private final float chatTop = 2.0f * Display.getHeight() / 3;
    TrueTypeFont chatFont;
    private List<String> log;
    private StringBuffer messageToSend;
    private Client client;

    public Chat() {
        log = new ArrayList<String>();
        int clientPort = 7777;
        String host = "localhost";

        client = new Client(host, clientPort);

        log.add("Client connecting to " + host + " using port " + clientPort);

        Font font = new Font("Courier New", Font.PLAIN, 13);
        glPushAttrib(GL_TEXTURE_BIT);
        chatFont = new TrueTypeFont(font, true);
        glPopAttrib();
        messageToSend = new StringBuffer();
    }

    public void render() {
        glMatrixMode(GL_PROJECTION);
        //Saves any perspective that may already be in place (camera)
        glPushMatrix();
        {
            glLoadIdentity();
            //Setup 2d display
            gluOrtho2D(0, Display.getWidth(), Display.getHeight(), 0);

            glMatrixMode(GL_MODELVIEW);
            //Saves any matrix transformations we may have on 3d objects
            glPushMatrix();
            {
                glLoadIdentity();
                glTranslatef(0.375f, 0.375f, 0.0f);

                // Push currently enabled flags
                glPushAttrib(GL_ENABLE_BIT);
                {
                    //Disable depth, texture, and lighting. Lighting is not needed . Depth will prevent clipping. Texture will incorporate any current textures.
                    // Blend is needed for transparency with game
                    glDisable(GL_DEPTH_TEST);
                    glDisable(GL_TEXTURE_2D);
                    glDisable(GL_LIGHTING);
                    glEnable(GL_BLEND);
                    // Blending eq: (A * Src) + ((1 - A) * Dst)
                    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

                    //Draw the chat box
                    drawChatBox();
                    getMessageFromClient();
                    drawText();

                }
                glPopAttrib();

                //Reload matrix and view transformations
                glMatrixMode(GL_PROJECTION);
            }
            glPopMatrix();
            glMatrixMode(GL_MODELVIEW);
        }
        glPopMatrix();
    }

    private void drawChatBox() {
        glBegin(GL_QUADS);
        {
            glColor4f(0.0f, 0.0f, 0.0f, 0.5f);
            glVertex2f(0.0f, Display.getHeight());
            glVertex2f(0.0f, chatTop);
            glVertex2f(Display.getWidth(), chatTop);
            glVertex2f(Display.getWidth(), Display.getHeight());
        }
        glEnd();
    }

    public void addChar(char c) {
        messageToSend.append(c);
    }

    public void removeChar() {
        if (messageToSend.length() > 0)
            messageToSend.deleteCharAt(messageToSend.length() - 1);
    }

    public boolean sendMessage() {
        if (messageToSend.length() == 0)
            return false;
        client.sendMessage(messageToSend.toString());
        addMessageToLog("You: " + messageToSend.toString());
        messageToSend.delete(0, messageToSend.length());
        return true;
    }

    private void getMessageFromClient() {
        String rec = client.receiveMessage();

        if (rec != null) {
            addMessageToLog("Stranger: " + rec);
        }
    }

    private void drawText() {
        for (int i = 0; i < log.size(); i++) {
            chatFont.drawString(0, i * 15 + chatTop, log.get(i), Color.white);
        }
        chatFont.drawString(0, 12 * 15 + chatTop, messageToSend.toString(), Color.green);
    }

    private void addMessageToLog(String message) {
        log.add(message);
        if (log.size() > 12) {
            log.remove(0);
        }
    }
}