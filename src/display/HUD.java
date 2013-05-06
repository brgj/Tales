package display;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

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
    private static final int TARGETX = 40;
    //Target Y radius
    private static final int TARGETY = 25;
    //Stores the position of the crosshair
    public Vector2f crosshairPos;

    public HUD() {
        crosshairPos = new Vector2f(0, 0);
    }

    public void render() {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
//        glTranslatef(0.375f, 0.375f, 0.0f);

        // Push currently enabled flags
        glPushAttrib(GL_ENABLE_BIT);
        {
            glDisable(GL_DEPTH_TEST); // Depth is disabled to prevent clipping
            glDisable(GL_TEXTURE_2D); // Texture 2D is disabled so no textures are incorporated
            glDisable(GL_LIGHTING); // Lighting is turned off for 2D

            //Call HUD functions
            drawCrossHair();
        }
        glPopAttrib();
    }

    public void drawCrossHair() {
        //Sets the line width of the crosshair
        glLineWidth(5);
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
