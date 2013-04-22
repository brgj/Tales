import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: brad
 * Date: 4/22/13
 * Time: 11:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {
        try {
            LibraryLoader.loadNativeLibraries();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Sorry :-(");
            System.exit(0);
        }

        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }

        Display.setTitle("Project");

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, 800, 0, 600, -1, 1);
        glMatrixMode(GL_MODELVIEW);

        while (!Display.isCloseRequested()) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            glColor3f(1.0f, 1.0f, 0.0f);

            glBegin(GL_QUADS);
            {
                glVertex2f(100, 100);
                glVertex2f(100 + 200, 100);
                glVertex2f(100 + 200, 100 + 200);
                glVertex2f(100, 100 + 200);
            }
            glEnd();

            Display.update();
        }

        Display.destroy();

        System.exit(0);
    }

}
