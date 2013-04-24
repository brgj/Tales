package Screens;

import helpers.Delegate;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

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

        glVertex2f(100,100);
        glVertex2f(100+600,100);
        glVertex2f(100+600,100+400);
        glVertex2f(100,100+400);

        glEnd();
    }

    public void Update(){
        if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)) {
            //Hard coded, change later..
            delegate.change(2);
        }
    }

}
