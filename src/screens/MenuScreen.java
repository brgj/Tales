package screens;

import helpers.Delegate;
import helpers.GLHelper;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 23/04/13
 * Time: 3:31 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MenuScreen extends Screen {
    ArrayList<String> MenuOptions;
    protected int selectedIndex;
    protected TrueTypeFont font, titleFont;
    protected Texture background;

    public MenuScreen(Delegate d) {
        super(d);
        Font awtFont = new Font("Verdana", Font.PLAIN, 24);
        Font fnt =  new Font("Courier New", Font.PLAIN, 40);
        titleFont = new TrueTypeFont(fnt, true);
        font = new TrueTypeFont(awtFont, false);
        selectedIndex = 0;
        background = GLHelper.LoadTexture("jpg", "images/menuScreenBackground.jpg");

    }

    protected void drawBackground(){
        // Set the filter colour to nothing and bind the background texture to GL_TEXTURE_2D
        glColor3f(1.0f,1.0f,1.0f);
        background.bind();
        // Map texture coordinates to 2D vertex coordinates
        glBegin(GL_QUADS);
        {
            glTexCoord2f(0, 0);         glVertex2f(0, 0);
            glTexCoord2f(0.9f, 0);      glVertex2f(800, 0);
            glTexCoord2f(0.9f, 0.5f);   glVertex2f(800, 600);
            glTexCoord2f(0, 0.5f);      glVertex2f(0, 600);
        }
        glEnd();
    }

    protected int updateOptions(){
        // Make sure that there is a new key in the queue, and that it is being pressed rather than released
        if(Keyboard.next() && Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();
            if (key == Keyboard.KEY_RETURN) {
                return selectedIndex;
            } else if (key == Keyboard.KEY_UP) {
                selectedIndex = ((selectedIndex + MenuOptions.size()) - 1) % MenuOptions.size();
            } else if (key == Keyboard.KEY_DOWN) {
                selectedIndex = (selectedIndex + 1) %  MenuOptions.size();
            }
        }
        return -1;
    }

    protected void drawOptions(String title){
        // Draw the menu options and colour the selected index yellow
        glEnable(GL_BLEND);
        {
            Color current;
            titleFont.drawString(99, 79, title, Color.orange);
            titleFont.drawString(100, 80, title, Color.white);
            for (int i = 0; i < this.MenuOptions.size(); i++) {
                if (this.selectedIndex == i)
                    current = Color.yellow;
                else
                    current = Color.gray;

                font.drawString(150f, 150f + (i * 50), MenuOptions.get(i), current);
            }
        }
        glDisable(GL_BLEND);
    }
}
