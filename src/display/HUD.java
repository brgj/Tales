package display;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: Owner
 * Date: 29/04/13
 * Time: 6:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class HUD {

    //Buffer for the side of the screen. Target will be BUFFER away from edge
    private static final int BUFFER = 10;
    //Target X radius
    public static final int TARGETX = 40;
    //Target Y radius
    public static final int TARGETY = 25;
    //Stores the position of the crosshair
    public Vector2f crosshairPos;
    // The font to draw the score with
    TrueTypeFont font;

    public HUD() {
        crosshairPos = new Vector2f(0, 0);

        // Create font
        Font f = new Font("Arial", Font.PLAIN, 25);
        glPushAttrib(GL_TEXTURE_BIT);
        font = new TrueTypeFont(f, true);
        glPopAttrib();
    }

    public void render(boolean enemyInTarget, float health, int score) {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
//        glTranslatef(0.375f, 0.375f, 0.0f);

        // Push currently enabled flags
        glPushAttrib(GL_ENABLE_BIT);
        {
            glDisable(GL_DEPTH_TEST); // Depth is disabled to prevent clipping
            glDisable(GL_TEXTURE_2D); // Texture 2D is disabled so no textures are incorporated
            glDisable(GL_LIGHTING); // Lighting is turned off for 2D
            glEnable(GL_BLEND);

            //Call HUD functions
            drawCrossHair(enemyInTarget);
            drawHealth(health);
            drawScore(score);
        }
        glPopAttrib();
    }

    public void drawCrossHair(boolean enemyInTarget) {
        //Sets the line width of the crosshair
        glLineWidth(5);

        if(enemyInTarget)
            glColor3f(1.0f, 0.0f, 0.0f);
        else
            glColor3f(0.0f, 1.0f, 0.0f);
        //Draw crosshair
        glBegin(GL_LINES);
        {
            //Left side
            glVertex2d(Display.getWidth() / 2 + crosshairPos.x - TARGETX, Display.getHeight() / 2 + crosshairPos.y - TARGETY);
            glVertex2d(Display.getWidth() / 2 + crosshairPos.x - TARGETX, Display.getHeight() / 2 + crosshairPos.y + TARGETY);

            //Right side40
            glVertex2d(Display.getWidth() / 2 + crosshairPos.x + TARGETX, Display.getHeight() / 2 + crosshairPos.y - TARGETY);
            glVertex2d(Display.getWidth() / 2 + crosshairPos.x + TARGETX, Display.getHeight() / 2 + crosshairPos.y + TARGETY);

            //Top side40
            glVertex2d(Display.getWidth() / 2 + crosshairPos.x + TARGETX, Display.getHeight() / 2 + crosshairPos.y + TARGETY);
            glVertex2d(Display.getWidth() / 2 + crosshairPos.x - TARGETX, Display.getHeight() / 2 + crosshairPos.y + TARGETY);

            //Bottom side40
            glVertex2d(Display.getWidth() / 2 + crosshairPos.x + TARGETX, Display.getHeight() / 2 + crosshairPos.y - TARGETY);
            glVertex2d(Display.getWidth() / 2 + crosshairPos.x - TARGETX, Display.getHeight() / 2 + crosshairPos.y - TARGETY);

            //glVertex2d(Display.getWidth() / 2 + crosshairPos.x -40, Display.getHeight() / 2 + crosshairPos.y - 25);
            //glVertex2d(Display.getWidth() / 2 + crosshairPos.x - 15, Display.getHeight() / 2 );
        }
        glEnd();
    }

    public void drawHealth(float health)
    {
        glBegin(GL_QUADS);
        glColor3f(1 - health, health, .2f);
        glVertex2d(BUFFER, BUFFER);
        glVertex2d(BUFFER + health * 500, BUFFER);
        glVertex2d(BUFFER + health * 500, BUFFER + 50);
        glVertex2d(BUFFER, BUFFER + 50);
        glEnd();
    }

    public void drawScore(int score) {
        font.drawString(Display.getWidth() - 30, 0, Integer.toString(score), org.newdawn.slick.Color.yellow);

    }

    //Resets the crosshair gradually to the center.
    public void crosshairReset() {
        float slope;
        if (crosshairPos.y == 0 || crosshairPos.x == 0) {
            slope = 1;
        } else {
            slope = Math.abs(crosshairPos.y / crosshairPos.x);
        }
        if (crosshairPos.x != 0) {
            crosshairPos.x = (crosshairPos.x >= 1) ? crosshairPos.x - 5 : crosshairPos.x + 5;
        }
        if (crosshairPos.y != 0) {
            crosshairPos.y = (crosshairPos.y >= 1) ? crosshairPos.y - 5 * slope : crosshairPos.y + 5 * slope;
        }
    }

    //Sets the crosshair value, cannot go outside screen range
    public void setCrosshairX(int i) {
        if (crosshairPos.x <= Display.getWidth() / 2 - TARGETX - BUFFER && crosshairPos.x >= -Display.getWidth() / 2 + TARGETX + BUFFER) {
            crosshairPos.x += i * 5;
        }
        if(crosshairPos.x > Display.getWidth() / 2 - TARGETX - BUFFER)
        {
            crosshairPos.x = Display.getWidth() / 2 - TARGETX - BUFFER;
        }
        else if(crosshairPos.x < -Display.getWidth() / 2 + TARGETX + BUFFER)
        {
            crosshairPos.x = -Display.getWidth() / 2 + TARGETX + BUFFER;
        }
    }

    public void setCrosshairY(int i) {
        if (crosshairPos.y <= Display.getHeight() / 2 - TARGETY - BUFFER && crosshairPos.y >= -Display.getHeight() / 2 + TARGETY + BUFFER) {
            crosshairPos.y += i * 5;
        }
        if(crosshairPos.y > Display.getHeight() / 2 - TARGETY - BUFFER)
        {
            crosshairPos.y = Display.getHeight() / 2 - TARGETY - BUFFER;
        }
        else if(crosshairPos.y < -Display.getHeight() / 2 + TARGETY + BUFFER)
        {
            crosshairPos.y = -Display.getHeight() / 2 + TARGETY + BUFFER;
        }
    }
}
