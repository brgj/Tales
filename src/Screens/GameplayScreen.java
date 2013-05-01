package Screens;

import core.*;
import glapp.GLApp;
import helpers.Delegate;
import network.Chat;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 23/04/13
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
/*public class GameplayScreen extends Screen {

    float lightDirection[]= { -2f, 2f, 2f, 0f };//direction , position
    float diffuse[] = { 1f,  1f,  1f,  1f };  // diffuse color
    float ambient[] = { .6f, .6f, .9f, 1f };    // ambient
    float specular[]= { 1f,  1f,  1f,  1f };
    Light l = new Light(lightDirection,diffuse,ambient,specular);
    Model model;//=new Model("data/DarkFighter/dark_fighter.obj",1.0f,0.0f,0.0f,0.0f,-10.0f,10.0f,0.0f);
    public GameplayScreen(Delegate d) {
        super(d);

    }

    public void Initialize() {
        model = new Model("data/DarkFighter/dark_fighter.obj",5.0f,0.0f,0.0f,0.0f,-6.0f,60.0f,16.0f)   ;
        //Terrain.setUpDisplay();
        l.setLight();
        Terrain.setUpStates();
        Terrain.setUpHeightmap();
        Terrain.setUpShaders();
        Terrain.setUpMatrices();
    }

    public void Render() {
        Terrain.render();
        //glClear( GL_DEPTH_BUFFER_BIT);
        //glColor3f(0.0f, 1.0f, 1.0f);
        //glBegin(GL_QUADS);
        glEnable( GL_TEXTURE_2D );
        model.render();
        // glEnd();
    }

    public void Update() {
        Terrain.input();
    }
}*/

public class GameplayScreen extends Screen {
    //Camera for single player
    Camera cam;
    Model model;
    HUD hud;
    Chat chat;
    Background background;
    private Audio wavEffect;
    float lightDirection[] = {-2f, 2f, 2f, 0f};//direction , position
    float diffuse[] = {1f, 1f, 1f, 1f};  // diffuse color
    float ambient[] = {.6f, .6f, .9f, 1f};    // ambient
    float specular[] = {1f, 1f, 1f, 1f};
    Light l = new Light(lightDirection, diffuse, ambient, specular);
    float objrot = 0.0f;
    private boolean chatting = false;

    public GameplayScreen(Delegate d) {
        super(d);
        cam = new Camera(new Vector3f(0, 0, -10));
    }

    public void Initialize() {

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glClearColor(.5f, .6f, .9f, 1f);
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
        background = new Background();
        //load the model
        model = new Model("data/Arwing/finalarwing.obj", 0.5f, 0.0f, 0.0f, 0.0f, -10.0f, -10.0f, 0.0f);

        //TODO: implement huds for individual players
        chat = new Chat();
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

        cam.setCameraView(0.2f, hud);
        // Push the Texture bit so the stupid model doesn't throw a hissy-fit
        glPushAttrib(GL_TEXTURE_BIT);
        {
            background.drawSkybox(50.0f);
            cam.moveCamera();
        }
        glPopAttrib();
        model.render();
        glPushAttrib(GL_TEXTURE_BIT);
        {
            if (chatting)
                chat.render();
            else
                hud.render();
        }
        glPopAttrib();
    }

    public void Update() {

        //Temporary controls for the camera and target
        boolean keyPressed = Keyboard.next();
        if (!chatting) {
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                cam.move(0.2f, 90);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                cam.move(0.2f, 270);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                cam.move(0.2f, 0);
                cam.moveUp(0.2f, 0);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                cam.move(0.2f, 180);
                cam.moveUp(0.2f, 180);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
                chatting = true;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                delegate.change(0);
            }
        } else if (keyPressed && Keyboard.getEventKeyState()) {
            int key = Keyboard.getEventKey();
            if (key == Keyboard.KEY_RETURN) {
                if (!chat.sendMessage())
                    chatting = false;
            } else if (key == Keyboard.KEY_BACK) {
                chat.removeChar();
            } else {
                char c = Keyboard.getEventCharacter();
                if (Character.isLetterOrDigit(c) || c == ' ')
                    chat.addChar(c);
            }
        }


//        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
//            cam.walk(.1f);
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
//            cam.walk(-.1f);
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
//            cam.strafe(.1f);
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
//            cam.strafe(-.1f);
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
//            cam.setRoll(1);
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
//            cam.setRoll(-1);
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
//            cam.setY(.1f);
//        }
//        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
//            cam.setY(-.1f);
//        }
        //If not movement keys are pressed
//        if (!Keyboard.isKeyDown(Keyboard.KEY_D) && !Keyboard.isKeyDown(Keyboard.KEY_A) &&
//                !Keyboard.isKeyDown(Keyboard.KEY_W )&& !Keyboard.isKeyDown(Keyboard.KEY_S))
//        {
//            hud.crosshairReset();
//        }
    }
}