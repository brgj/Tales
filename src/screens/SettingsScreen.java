package screens;

import helpers.Delegate;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

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
        MenuOptions = new ArrayList<String>();
        MenuOptions.add("Return to Main Menu");
        MenuOptions.add("Music On/Off");
        MenuOptions.add("Option 2");
        MenuOptions.add("Option 3");
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

            drawSettings(); // Draw Settings Menu
        }
        glPopAttrib();
    }

    public void Update() {
        int option = updateOptions();
        if(option == 0)
            delegate.change(0);
        else if(option == 1) {
            audioOn = !audioOn;
            if(audioOn)
                audio.playAsMusic(1.0f, 1.0f, true);
            else
                audio.stop();
        }
    }

    private void drawSettings() {
        drawBackground();
        drawOptions("Settings");
    }
}
