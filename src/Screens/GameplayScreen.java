package Screens;

import core.Background;
import core.Camera;
import helpers.Delegate;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.opengl.GL11;
import java.util.ArrayList;
import glapp.*;
import glmodel.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 23/04/13
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class GameplayScreen extends Screen {
    //Camera for single player
    Camera cam;
//    GLModel object;
    Background background;

    public GameplayScreen(Delegate d) {
        super(d);
        cam = new Camera(new Vector3f(0, 0, -1000));
    }

    public void Initialize() {

        //This code resets the camera view and the ModelView to initial view and identity respectively
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(50.0f, (float) Display.getWidth() / (float) Display.getHeight(), 1f, 5000f);
        glMatrixMode(GL_MODELVIEW);

        //Create Background
        background = new Background();

        //background.createBackground();
        //load the model
//        object = new GLModel("data/chopper/Chopper.obj");
//        object.regenerateNormals();

    }

    public void Render() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glColor3f(0.0f, 1.0f, 1.0f);

        //glRotatef(0.0f, 0.0f, 0.0f, 1.0f);




        glBegin(GL_QUADS);
        GL11.glPushMatrix();
        {
            //GL11.glRotatef(rotation, 0, 1, 0);  // turn it
            GL11.glScalef(0.01f,0.01f,0.01f);
//            object.render();
        }

        glColor3d(1, 0, 0);
        glVertex3i(50, 50, 50);
        glVertex3i(50, -50, 50);
        glVertex3i(-50, -50, 50);
        glVertex3i(-50, 50, 50);


        glColor3d(0, 1, 0);
        glVertex3i(50, 50, -50);
        glVertex3i(50, -50, -50);
        glVertex3i(-50, -50, -50);
        glVertex3i(-50, 50, -50);


        glColor3d(0, 0, 1);
        glVertex3i(50, 50, 50);
        glVertex3i(50, -50, 50);
        glVertex3i(50, -50, -50);
        glVertex3i(50, 50, -50);


        glColor3d(0, 1, 1);
        glVertex3i(-50, 50, 50);
        glVertex3i(-50, -50, 50);
        glVertex3i(-50, -50, -50);
        glVertex3i(-50, 50, -50);


        glColor3d(1, 0, 0);
        glVertex3i(-50, 50, -50);
        glVertex3i(-50, 50, 50);
        glVertex3i(50, 50, 50);
        glVertex3i(50, 50, -50);


        glColor3d(1, 1, 0);
        glVertex3i(-50, -50, -50);
        glVertex3i(-50, -50, 50);
        glVertex3i(50, -50, 50);
        glVertex3i(50, -50, -50);
background.createBackground();
        glEnd();
        cam.setCameraView();

    }

    public void Update() {

        //Temporary controls for the camera
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            cam.walk(1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            cam.walk(-1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            cam.setYaw(1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            cam.setYaw(-1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            cam.setPitch(1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            cam.setPitch(-1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            cam.strafe(1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            cam.strafe(-1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
            cam.setRoll(1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
            cam.setRoll(-1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            cam.setY(1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            cam.setY(-1);
        }
    }
}
