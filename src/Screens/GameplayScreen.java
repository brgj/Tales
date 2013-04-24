package Screens;

import helpers.Delegate;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 23/04/13
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameplayScreen extends Screen
{
    public GameplayScreen(Delegate d) {
        super(d);
    }

    public void Initialize(){

    }

    public void Render(){

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glColor3f(0.0f, 1.0f, 1.0f);

        glBegin(GL_QUADS);

        glVertex2f(100,100);
        glVertex2f(100+600,100);
        glVertex2f(100+600,100+400);
        glVertex2f(100,100+400);

        glEnd();

    }

    public void Update(){

    }
}
