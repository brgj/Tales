package Screens;

import helpers.Delegate;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 23/04/13
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class SettingsScreen extends MenuScreen {

    public SettingsScreen(Delegate d) {
        super(d);
    }

    public void Initialize() {
        this.MenuOptions = new ArrayList<String>();
        this.MenuOptions.add("Return to Main Menu");
        this.MenuOptions.add("Option 1");
        this.MenuOptions.add("Option 2");
        this.MenuOptions.add("Option 2");

    }

    public void Render() {
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

                    //Draw the menu
                    drawBackground();
                    drawOptions("Settings");

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

    public void Update() {
        if(updateOptions() == 0)
            delegate.change(0);
    }
}
