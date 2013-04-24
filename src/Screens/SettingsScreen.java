package Screens;

import helpers.Delegate;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 23/04/13
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class SettingsScreen extends MenuScreen{

    public SettingsScreen(Delegate d) {
        super(d);
    }

    public void Initialize(){
        this.MenuOptions = new ArrayList<String>();
        this.MenuOptions.add("Return to Main Menu");
        this.MenuOptions.add("Option 1");
        this.MenuOptions.add("Option 2");
        this.MenuOptions.add("Option 2");

    }

    public void Render(){

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glColor3f(1.0f, 1.0f, 1.0f);

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
        glDisable(GL11.GL_BLEND);
    }

    public void Update(){
        if(Keyboard.isKeyDown(Keyboard.KEY_RETURN) ) {
            if(selectedIndex == 0 && lastKeyPressed != Keyboard.KEY_RETURN)
                delegate.change(0);
            lastKeyPressed = Keyboard.KEY_RETURN;
        }
        else if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            if(lastKeyPressed != Keyboard.KEY_UP)
                selectedIndex = ((selectedIndex + 4)-1)%4;
            lastKeyPressed = Keyboard.KEY_UP;
        }else if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            if(lastKeyPressed != Keyboard.KEY_DOWN)
                selectedIndex = (selectedIndex+1)%4;
            lastKeyPressed = Keyboard.KEY_DOWN;
        } else {
            lastKeyPressed = -1;
        }
    }
}
