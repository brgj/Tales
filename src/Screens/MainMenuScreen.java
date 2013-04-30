package Screens;

import helpers.Delegate;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;
/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 23/04/13
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainMenuScreen extends MenuScreen {

    private Audio wavEffect;

    public MainMenuScreen(Delegate d) {
        super(d);
    }

    public void Initialize() {
        this.MenuOptions = new ArrayList<String>();
        this.MenuOptions.add("Settings");
        this.MenuOptions.add("Single Player");
        this.MenuOptions.add("Multi Player");

        //Start Menu screen music
        try {
            wavEffect = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("music/02_TitleScreen.wav"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        wavEffect.playAsMusic(1.0f, 1.0f, true);

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
                    drawMenu();

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
        if(updateOptions() != -1)
            delegate.change(super.selectedIndex + 1);
    }

    private void drawMenu(){
        drawBackground();
        drawOptions("Main Menu");
    }

}
