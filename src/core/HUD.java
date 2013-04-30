package core;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

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

    public HUD()
    {
        crosshairPos = new Vector2f(0, 0);
    }

    public void render()
    {
        glMatrixMode (GL_PROJECTION);
        //Saves any perspective that may already be in place (camera)
        glPushMatrix();
        glLoadIdentity ();
        //Setup 2d display
        gluOrtho2D(0, Display.getWidth(), Display.getHeight(), 0);

        glMatrixMode(GL_MODELVIEW);
        //Saves any matrix transformations we may have on 3d objects
        glPushMatrix();
        glLoadIdentity();
        glTranslatef(0.375f, 0.375f, 0.0f);

        //Disable depth, texture, and lighting. Lighting is not needed on HUD. Depth will prevent HUD clipping. Texture will incorporate any current textures into the HUD.
        glDisable(GL_DEPTH_TEST);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_LIGHTING);

        //Call HUD functions
        drawCrossHair();

        //Re-enable lighting, texture, and depth for 3D models
        glEnable(GL_LIGHTING);
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_DEPTH_TEST);

        //Reload matrix and view transformations
        glMatrixMode (GL_PROJECTION);
        glPopMatrix();
        glMatrixMode(GL_MODELVIEW);
        glPopMatrix();
    }

    public void drawCrossHair()
    {
        //Sets the line width of the crosshair
        glLineWidth(5);
        glColor3f(0.0f, 1.0f, 0.0f);
        //Draw crosshair
        glBegin(GL_LINES);

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

        glEnd();
    }

    //Resets the crosshair gradually to the center.
    public void crosshairReset()
    {
        float slope;
        if(crosshairPos.y == 0 || crosshairPos.x == 0)
        {
            slope = 1;
        }
        else
        {
            slope = Math.abs(crosshairPos.y / crosshairPos.x);
        }
        if(crosshairPos.x != 0)
        {
            crosshairPos.x = (crosshairPos.x >= 1) ? crosshairPos.x - 5 : crosshairPos.x + 5;
        }
        if(crosshairPos.y != 0)
        {
            crosshairPos.y = (crosshairPos.y >= 1) ? crosshairPos.y - 5 * slope : crosshairPos.y + 5 * slope;
        }
    }

    //Sets the crosshair value, cannot go outside screen range
    public void setCrosshairX(int i)
    {
        if(crosshairPos.x <= Display.getWidth() / 2 - TARGETX - BUFFER && crosshairPos.x >= -Display.getWidth() / 2 + TARGETX + BUFFER)
        {
            crosshairPos.x += i * 5;
        }
    }

    public void setCrosshairY(int i)
    {
        if(crosshairPos.y <= Display.getHeight() / 2 - TARGETY - BUFFER && crosshairPos.y >= -Display.getHeight() / 2 + TARGETY + BUFFER)
        {
            crosshairPos.y += i * 5;
        }
    }
}
