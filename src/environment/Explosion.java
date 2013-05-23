package environment;

import helpers.GLHelper;
import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

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
    float speed;
    Vector3f position;
    static Texture texture = GLHelper.LoadTexture("png", "images/explosion.png");
    public long initial;

    public Explosion(float scale, float speed, Vector3f position) {
        alpha = 1.0f;
        this.speed = speed;
        this.scale = scale;
        this.position = new Vector3f(position);
        initial = Sys.getTime();
        Initialize();
    }

    public void Initialize() {

    }

    public void reset() {
        this.alpha = 1.0f;
        this.scale = 0.5f;
    }

    public float getAlpha()
    {
        return alpha;
    }

    public void resetTime() {
        this.initial = Sys.getTime();
    }

    public int getTimepassed() {
        return (int) ((Sys.getTime() - this.initial) / 1000);
    }

    public void render() {
        glPushAttrib(GL_ENABLE_BIT);
        {
            glDisable(GL_CULL_FACE);
            glEnable(GL_BLEND);
            //Adding this fixes other clipping, but not the player because its a layer above
            //glDisable(GL_DEPTH_TEST);
            //This enables the material to be colored. Defaulted to be disabled
            glEnable(GL_COLOR_MATERIAL);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            texture.bind();
            if (alpha >= 0) {
                glPushMatrix();
                alpha -= .01f;
                scale += speed;

                glColor4f(alpha, alpha, alpha, alpha);
                glScalef(scale, scale, scale);

                glTranslatef(position.getX() / scale,
                        position.getY() / scale,
                        position.getZ() / scale);

                glBegin(GL_QUADS);
                {
                    glTexCoord2f(0, 0);
                    glVertex3f(1.0f, 1.0f, 1.0f);
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
