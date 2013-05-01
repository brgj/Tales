package core;

import screens.Screen;
import screens.ScreenManager;
import helpers.Delegate;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

/**
 * Created with IntelliJ IDEA.
 * User: brad
 * Date: 4/22/13
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    private static boolean isChanged = true;
    private static ScreenManager SM;

    public static void init() {
        // Load OpenGL libraries
        try {
            LibraryLoader.loadNativeLibraries();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Sorry :-(");
            System.exit(0);
        }

        // Setup Display
        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        Display.setTitle("Project");

        // Initialize 2D Projection and switch to ModelView mode
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        gluOrtho2D(0, Display.getWidth(), Display.getHeight(), 0);
        glMatrixMode(GL_MODELVIEW);

        // Create ScreenManager with delegate for communicating with the game loop
        Delegate d = new Delegate() {
            @Override
            public void change(int val) {
                isChanged = val != 0;
            }
        };
        SM = new ScreenManager(d);

    }

    public static void main(String[] args) {
        init();

        // Game loop
        while (!Display.isCloseRequested()) {
            if (isChanged) {
                SM.Initialize();
                isChanged = false;
            }
            SM.Render();
            SM.Update();
            Display.update();
            Display.sync(80);
        }

        Screen.killAudio();
        Display.destroy();
        System.exit(0);
    }

}