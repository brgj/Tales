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

import java.io.IOException;
import java.util.ArrayList;
import glapp.*;
import glmodel.*;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

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
    GLModel object;
    public GLImage sky;
    float lightDirection[]= { -2f, 2f, 2f, 0f };
    Background background;
    private Audio wavEffect;

    public GameplayScreen(Delegate d) {
        super(d);
        cam = new Camera(new Vector3f(0, 0, -10));
    }

    public void Initialize() {

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glClearColor(.5f,.6f,.9f,1f);
        // Create a light
        GLApp.setLight( GL11.GL_LIGHT1,
                new float[] { 1f,  1f,  1f,  1f },    // diffuse color
                new float[] { .6f, .6f, .9f, 1f },    // ambient
                new float[] { 1f,  1f,  1f,  1f },    // specular
                lightDirection );                     // direction/position

        GLApp.setAmbientLight(new float[] { .6f, .6f, .9f, 1f });
        //This code resets the camera view and the ModelView to initial view and identity respectively
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(50.0f, (float) Display.getWidth() / (float) Display.getHeight(), 1f, 5000f);
        glMatrixMode(GL_MODELVIEW);

        //Start Gamescreen music
        try {
            wavEffect = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("music/36-VersusMode.wav"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        wavEffect.playAsMusic(1.0f, 1.0f, true);

        //Create Background
        //background = new Background();

        //background.createBackground();
        sky = GLApp.loadImage("images/sky.jpg");
        //load the model
        object = new GLModel("data/chopper/chopper.obj");
       object.regenerateNormals();

    }

    public void Render() {

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glColor3f(0.0f, 1.0f, 1.0f);

        //glRotatef(0.0f, 0.0f, 0.0f, 1.0f);



        glBegin(GL_QUADS);
        GLApp.drawImageFullScreen(sky);
        object.render();
       /* glColor3d(1, 0, 0);
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
        glVertex3i(50, -50, -50);*/
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
