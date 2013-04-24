package Screens;

import helpers.Delegate;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
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

    TrueTypeFont font;
    private int selectedIndex;
    private int lastKeyPressed;

    public MainMenuScreen(Delegate d) {
        super(d);

    }

    public  void Initialize(){
        this.MenuOptions = new ArrayList<String>();
        this.MenuOptions.add("Settings");
        this.MenuOptions.add("Single Player");
        this.MenuOptions.add("Multi Player");

    }

    public void Render(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glColor3f(1.0f, 1.0f, 0.0f);

        glBegin(GL_QUADS);

        glVertex2f(100, 100);
        glVertex2f(100+600,100);
        glVertex2f(100+600,100+400);
        glVertex2f(100,100+400);

        glEnd();

        GL11.glEnable(GL11.GL_BLEND);
        Color current;
        for(int i = 0; i < this.MenuOptions.size(); i++)
        {
            if(selectedIndex == i)
                current = Color.darkGray;
            else
                current = Color.gray;

            super.font.drawString(150f, 150f +(i*50), MenuOptions.get(i), current);
        }
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void Update(){
        if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
            //Hard coded, change later..
            delegate.change(selectedIndex +1);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            if(lastKeyPressed != Keyboard.KEY_UP)
                selectedIndex = ((selectedIndex + 3)-1)%3;
            lastKeyPressed = Keyboard.KEY_UP;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            if(lastKeyPressed != Keyboard.KEY_DOWN)
                selectedIndex = (selectedIndex+1)%3;
            lastKeyPressed = Keyboard.KEY_DOWN;
        } else {
            lastKeyPressed = -1;
        }
    }

}
