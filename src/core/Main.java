package core;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import screens.MenuManager;
import screens.Screen;

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
    private static MenuManager SM;

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

        SM = new MenuManager();

    }

    public static void main(String[] args) {
        init();

        SM.Initialize();

        // Game loop
        while (!Display.isCloseRequested()) {
            SM.Update();
            SM.Render();
            Display.update();
            Display.sync(30);
        }

        Screen.killAudio();
        Display.destroy();
        System.exit(0);
    }

}