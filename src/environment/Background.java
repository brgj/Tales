package environment;

import helpers.TextureHelper;
import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: Owner
 * Date: 24/04/13
 * Time: 8:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class Background {
    private final static int SKY_LEFT = 0;
    private final static int SKY_BACK = 1;
    private final static int SKY_RIGHT = 2;
    private final static int SKY_FRONT = 3;
    private final static int SKY_TOP = 4;
    private final static int SKY_BOTTOM = 5;
    public Texture[] skybox;

    public Background() {
        skybox = new Texture[6];
        skybox[SKY_LEFT] = TextureHelper.LoadTexture("png", "images/left.png");
        skybox[SKY_BACK] = TextureHelper.LoadTexture("png", "images/back.png");
        skybox[SKY_RIGHT] = TextureHelper.LoadTexture("png", "images/right.png");
        skybox[SKY_FRONT] = TextureHelper.LoadTexture("png", "images/front.png");
        skybox[SKY_TOP] = TextureHelper.LoadTexture("png", "images/top.png");
        skybox[SKY_BOTTOM] = TextureHelper.LoadTexture("png", "images/bottom.png");
    }

    public void drawSkybox(float size) {
        // Set colour to white
        glColor3f(1.0f, 1.0f, 1.0f);

        // Push currently enabled flags
        glPushAttrib(GL_ENABLE_BIT);
        {
            glDisable(GL_DEPTH_TEST); // Depth is disabled to prevent clipping
            glDisable(GL_LIGHTING); // Lighting is turned off for skybox
            glDisable(GL_BLEND); // Blending is not required for skybox
            glEnable(GL_TEXTURE_2D); // Skybox needs to be textured

            // Front
            skybox[SKY_FRONT].bind();
            glBegin(GL_QUADS);
            {
                glTexCoord2f(0, 0);
                glVertex3f(size / 2, size / 2, size / 2);
                glTexCoord2f(1, 0);
                glVertex3f(-size / 2, size / 2, size / 2);
                glTexCoord2f(1, 1);
                glVertex3f(-size / 2, -size / 2, size / 2);
                glTexCoord2f(0, 1);
                glVertex3f(size / 2, -size / 2, size / 2);
            }
            glEnd();

            // Left
            skybox[SKY_LEFT].bind();
            glBegin(GL_QUADS);
            {
                glTexCoord2f(0, 0);
                glVertex3f(-size / 2, size / 2, size / 2);
                glTexCoord2f(1, 0);
                glVertex3f(-size / 2, size / 2, -size / 2);
                glTexCoord2f(1, 1);
                glVertex3f(-size / 2, -size / 2, -size / 2);
                glTexCoord2f(0, 1);
                glVertex3f(-size / 2, -size / 2, size / 2);
            }
            glEnd();

            // Back
            skybox[SKY_BACK].bind();
            glBegin(GL_QUADS);
            {
                glTexCoord2f(1, 0);
                glVertex3f(size / 2, size / 2, -size / 2);
                glTexCoord2f(0, 0);
                glVertex3f(-size / 2, size / 2, -size / 2);
                glTexCoord2f(0, 1);
                glVertex3f(-size / 2, -size / 2, -size / 2);
                glTexCoord2f(1, 1);
                glVertex3f(size / 2, -size / 2, -size / 2);
            }
            glEnd();

            // Right
            skybox[SKY_RIGHT].bind();
            glBegin(GL_QUADS);
            {
                glTexCoord2f(0, 0);
                glVertex3f(size / 2, size / 2, -size / 2);
                glTexCoord2f(1, 0);
                glVertex3f(size / 2, size / 2, size / 2);
                glTexCoord2f(1, 1);
                glVertex3f(size / 2, -size / 2, size / 2);
                glTexCoord2f(0, 1);
                glVertex3f(size / 2, -size / 2, -size / 2);
            }
            glEnd();

            // Top
            skybox[SKY_TOP].bind();
            glBegin(GL_QUADS);
            {
                glTexCoord2f(1, 0);
                glVertex3f(size / 2, size / 2, size / 2);
                glTexCoord2f(0, 0);
                glVertex3f(-size / 2, size / 2, size / 2);
                glTexCoord2f(0, 1);
                glVertex3f(-size / 2, size / 2, -size / 2);
                glTexCoord2f(1, 1);
                glVertex3f(size / 2, size / 2, -size / 2);
            }
            glEnd();

            // Bottom
            skybox[SKY_BOTTOM].bind();
            glBegin(GL_QUADS);
            {
                glTexCoord2f(1, 1);
                glVertex3f(size / 2, -size / 2, size / 2);
                glTexCoord2f(0, 1);
                glVertex3f(-size / 2, -size / 2, size / 2);
                glTexCoord2f(0, 0);
                glVertex3f(-size / 2, -size / 2, -size / 2);
                glTexCoord2f(1, 0);
                glVertex3f(size / 2, -size / 2, -size / 2);
            }
            glEnd();
        }
        glPopAttrib();
    }
}
