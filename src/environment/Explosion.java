package environment;

import glapp.GLApp;
import helpers.GLHelper;
import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.opengl.Texture;
import org.lwjgl.Sys;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: Owner
 * Date: 13/05/13
 * Time: 9:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class Explosion {

    float alpha;
    float scale;
    Vector3f position;
    Texture texture;
    public long initial;
    public Explosion() {

        texture = GLHelper.LoadTexture("png", "images/explosion.png");
        //texture = GLHelper.LoadTexture("png", "images/glow/laser.png");
        position = new Vector3f();
        Initialize();
        alpha = 1.0f;
        scale = 0.5f;
        initial = Sys.getTime();
    }

    public void Initialize() {
        alpha = 1.0f;
        scale = 0.5f;
    }
    public void reset()
    {
        this.alpha = 1.0f;
        this.scale = 0.5f;
    }
    public void resetTime()
    {
        this.initial = Sys.getTime();
    }
    public int getTimepassed() {
        return (int) ((Sys.getTime() - this.initial) / 1000);
    }
    public void drawExplosion() {
        glPushAttrib(GL_ENABLE_BIT);
        {
            glDisable(GL_CULL_FACE);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            texture.bind();
            if (alpha >= 0) {
                glPushMatrix();
                alpha -= 0.01f;
                scale += 0.03f;

                glColor4f(1, 1, 0, alpha);
                glScalef(scale, scale, scale);

                glBegin(GL_QUADS);
                {
                    glTexCoord2f(0, 0);
                    glVertex3f(1.0f, 1.0f,1.0f);
                    glTexCoord2f(1, 0);
                    glVertex3f(-1.0f, 1.0f, 1.0f);
                    glTexCoord2f(1, 1);
                    glVertex3f(-1.0f, -1.0f, 1.0f);
                    glTexCoord2f(0, 1);
                    glVertex3f(1.0f, -1.0f, 1.0f);


                }
                texture.bind();
                glBegin(GL_QUADS);
                {
                    glTexCoord2f(0, 0);
                    glVertex3f(0, 1.0f, 0);
                    glTexCoord2f(1, 0);
                    glVertex3f(0, 1.0f, 2.0f);
                    glTexCoord2f(1, 1);
                    glVertex3f(0, -1.0f, 2.0f);
                    glTexCoord2f(0, 1);
                    glVertex3f(0, -1.0f, 0);

                }
                glColor4f(0.0f, 1.0f, 0.0f, 0.8f);
                glEnd();
                glPopMatrix();
            }
            glDisable(GL_BLEND);
        }
        glPopAttrib();
        /*glPushAttrib(GL_ENABLE_BIT);
        {
            glEnable(GL_BLEND);
            glEnable(GL_TEXTURE_2D);
            glDisable(GL_DEPTH_TEST);
            glEnable(GL_LIGHTING);
            if (alpha >= 0) {
                glPushMatrix();
                alpha -= 0.01f;
                scale += 0.03f;

                glColor4f(1, 1, 0, alpha);
                glScalef(scale, scale, scale);

//                glTranslatef(position.getX() / scale,
//                        position.getY() / scale,
//                        position.getZ() / scale);

                texture.bind();
                glBegin(GL_QUADS);
                {
                    glTexCoord2f(0, 0);
                    glVertex3f(50, 50, 50);
                    glTexCoord2f(1, 0);
                    glVertex3f(-50, 50, 50);
                    glTexCoord2f(1, 1);
                    glVertex3f(-50, -50, 50);
                    glTexCoord2f(0, 1);
                    glVertex3f(50, -50, 50);
                }
                glEnd();

                glBegin(GL_QUADS);
                {
                    glRotatef(90, 0.0f, 1f, 0.0f);
                    glTexCoord2f(0, 0);
                    glVertex3f(50, 50, 50);
                    glTexCoord2f(1, 0);
                    glVertex3f(-50, 50, 50);
                    glTexCoord2f(1, 1);
                    glVertex3f(-50, -50, 50);
                    glTexCoord2f(0, 1);
                    glVertex3f(50, -50, 50);
                }
                glEnd();

                glPopMatrix();
            }
            glDisable(GL_BLEND);
            glEnable(GL_DEPTH_TEST);
        }
        glPopAttrib();      */
       /*glPushAttrib(GL_ENABLE_BIT);
        {
            glEnable(GL_BLEND);
            glEnable(GL_TEXTURE_2D);
            glDisable(GL_DEPTH_TEST);
            glEnable(GL_LIGHTING);
            if (alpha >= 0) {
                glPushMatrix();
                alpha -= 0.01f;
                scale += 0.03f;

                glColor4f(1, 1, 0, alpha);
                glScalef(scale, scale, scale);

//                glTranslatef(position.getX() / scale,
//                        position.getY() / scale,
//                        position.getZ() / scale);

                texture.bind();
                glBegin(GL_QUADS);
                {
                    glTexCoord2f(0, 0);
                    glVertex3f(50, 50, 50);
                    glTexCoord2f(1, 0);
                    glVertex3f(-50, 50, 50);
                    glTexCoord2f(1, 1);
                    glVertex3f(-50, -50, 50);
                    glTexCoord2f(0, 1);
                    glVertex3f(50, -50, 50);
                }
                glEnd();

                glBegin(GL_QUADS);
                {
                    glRotatef(90, 0.0f, 1f, 0.0f);
                    glTexCoord2f(0, 0);
                    glVertex3f(50, 50, 50);
                    glTexCoord2f(1, 0);
                    glVertex3f(-50, 50, 50);
                    glTexCoord2f(1, 1);
                    glVertex3f(-50, -50, 50);
                    glTexCoord2f(0, 1);
                    glVertex3f(50, -50, 50);
                }
                glEnd();

                glPopMatrix();
            }
            glDisable(GL_BLEND);
            glEnable(GL_DEPTH_TEST);
        }
        glPopAttrib();      */
    }
    //TODO: Displose of explosion properly after use (when alpha < 0)
}
