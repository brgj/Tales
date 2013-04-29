package Screens;

import core.Background;
import core.Camera;
import core.*;
import helpers.Delegate;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
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
    Model model;
    HUD hud;
    public GLImage sky;
    Background background;
    private Audio wavEffect;
    float lightDirection[]= { -2f, 2f, 2f, 0f };//direction , position
    float diffuse[] = { 1f,  1f,  1f,  1f };  // diffuse color
    float ambient[] = { .6f, .6f, .9f, 1f };    // ambient
    float specular[]= { 1f,  1f,  1f,  1f };
    Light l = new Light(lightDirection,diffuse,ambient,specular);
    float objrot = 0.0f;

    public GameplayScreen(Delegate d) {
        super(d);
        cam = new Camera(new Vector3f(0, 0, -10));
    }

    public void Initialize() {

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glClearColor(.5f,.6f,.9f,1f);
        // Create a light
        l.setLight();

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
        model = new Model("data/arwing/finalarwing.obj",0.5f,0.0f,0.0f,0.0f,-10.0f,-10.0f,0.0f);

        //TODO: implement huds for individual players
        hud = new HUD();

    }

    public void Render() {

        glViewport(0, 0, Display.getWidth(), Display.getHeight());

        //This code resets the camera view and the ModelView to initial view and identity respectively
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(45.0f, (float) Display.getWidth() / (float) Display.getHeight(), 1f, 5000f);
        glMatrixMode(GL_MODELVIEW);

        objrot += 25f * GLApp.getSecondsPerFrame();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glColor3f(0.0f, 1.0f, 1.0f);
        glBegin(GL_QUADS);
        //GLApp.drawImageFullScreen(sky);
        //model.updateRotation(objrot,0.0f,0.0f);
        //model.updatePosition(objrot/10,objrot/10-0.5f,0.0f);
        model.render();
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
        hud.render();
    }

    public void Update() {

        //Temporary controls for the camera and target

        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            cam.walk(.1f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            cam.walk(-.1f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            cam.setYaw(1);
            hud.setCrosshairX(10);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            cam.setYaw(-1);
            hud.setCrosshairX(-10);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            cam.setPitch(1);
            hud.setCrosshairY(10);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            cam.setPitch(-1);
            hud.setCrosshairY(-10);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            cam.strafe(.1f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            cam.strafe(-.1f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
            cam.setRoll(1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
            cam.setRoll(-1);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            cam.setY(.1f);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            cam.setY(-.1f);
        }
        //If not movement keys are pressed
        if (!Keyboard.isKeyDown(Keyboard.KEY_D) && !Keyboard.isKeyDown(Keyboard.KEY_A) &&
                !Keyboard.isKeyDown(Keyboard.KEY_W )&& !Keyboard.isKeyDown(Keyboard.KEY_S))
        {
            hud.crosshairReset();
        }
    }
}
