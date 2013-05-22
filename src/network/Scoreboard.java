package network;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 5/22/13
 * Time: 9:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class Scoreboard {

    TrueTypeFont scoreFont;
    public final HashMap<Byte, Integer> scores;
    public int myScore;

    public Scoreboard() {
        scores = new HashMap<Byte, Integer>();
        myScore = 0;

        // Create font
        Font font = new Font("Courier New", Font.BOLD, 20);
        glPushAttrib(GL_TEXTURE_BIT);
        scoreFont = new TrueTypeFont(font, true);
        glPopAttrib();
    }

    /**
     * Renders the scoreboard and scores
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

            drawScoreboard(); // Draw the container for chat box
            drawText(); // Draw the text to the screen
        }
        glPopAttrib();
    }

    /**
     * Draw a scoreboard, covering the entire screen
     */
    private void drawScoreboard() {
        glBegin(GL_QUADS);
        {
            glColor4f(0.0f, 0.0f, 0.0f, 0.5f);
            glVertex2f(0.0f, Display.getHeight());
            glVertex2f(0.0f, 0.0f);
            glVertex2f(Display.getWidth(), 0.0f);
            glVertex2f(Display.getWidth(), Display.getHeight());
        }
        glEnd();
    }

    /**
     * Draw the chatlog to the chat
     */
    private void drawText() {
        int i = 1;
        for (byte id : scores.keySet()) {
            scoreFont.drawString(0, i * 22, "Enemy " + id + ": " + scores.get(id), org.newdawn.slick.Color.white);
        }
        scoreFont.drawString(0, 0, "You: " + myScore, org.newdawn.slick.Color.yellow);
    }
}
