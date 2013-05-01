package screens;

import helpers.Delegate;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 23/04/13
 * Time: 2:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainMenuScreen extends MenuScreen {

    public MainMenuScreen(Delegate d) {
        super(d);
    }

    public void Initialize() {
        this.MenuOptions = new ArrayList<String>();
        this.MenuOptions.add("Settings");
        this.MenuOptions.add("Single Player");
        this.MenuOptions.add("Multi Player");

        //Start Menu screen music
        killAudio();
        try {
            audio = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("music/02_TitleScreen.wav"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        audio.playAsMusic(1.0f, 1.0f, true);

        // Setup blending function
        // Blending eq: (A * Src) + ((1 - A) * Dst)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    }

    public void Render() {
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();

        // Push currently enabled flags
        glPushAttrib(GL_ENABLE_BIT);
        {
            glDisable(GL_DEPTH_TEST); // Depth is disabled to prevent clipping
            glDisable(GL_TEXTURE_2D); // Texture 2D is disabled so no textures are incorporated
            glDisable(GL_LIGHTING); // Lighting is turned off for 2D
            glEnable(GL_BLEND); // Blend is needed for text transparency

            drawMenu(); // Draw the menu
        }
        glPopAttrib();
    }

    public void Update() {
        if (updateOptions() != -1)
            delegate.change(super.selectedIndex + 1);
    }

    private void drawMenu() {
        drawBackground();
        drawOptions("Main Menu");
    }

}
