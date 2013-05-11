package environment;

import display.Camera;
import glapp.GLApp;
import helpers.GLHelper;
import org.lwjgl.Sys;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tammy
 * Date: 5/8/13
 * Time: 9:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class LaserBeam {public Vector3f position;

    public Texture material  = GLHelper.LoadTexture("png", "images/glow/laser.png");
    public float movement;
    public float initial;
    public int timecounter;
    public LaserBeam(Camera cam)
    {
        this.position = cam.getPosition();
        movement = 0.0f;
        initial = Sys.getTime();
        timecounter = 0;

    }
    public void resetTime()
    {
        this.initial = Sys.getTime();
        //this.movement = 0.0f;

    }
    public void getTimepassed()
    {
        this.timecounter = (int)((Sys.getTime()-this.initial)/1000);
    }
    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
    public void renderLeft()
    {

        float x = this.position.getX();
        float y = this.position.getY();
        float z = this.position.getZ();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        material.bind();
        movement+=10.0f * GLApp.getSecondsPerFrame();
        glBegin(GL_QUADS);
        {

                glTexCoord2f(1,0);
                glVertex3f(x,y-0.1f,-movement);

                glTexCoord2f(0,0);
                glVertex3f(x-0.1f,y-0.1f,-movement);

                glTexCoord2f(0,1);
                glVertex3f(x-0.1f,y-0.1f,1.0f-movement);

                glTexCoord2f(1,1);
                glVertex3f(x,y-0.1f,1.0f-movement);





        }
        glColor4f(0.0f, 1.0f, 0.0f, 0.8f);
        glEnd();
        glDisable(GL_BLEND);
    }
    public void renderRight()
    {

        float x = this.position.getX();
        float y = this.position.getY();
        float z = this.position.getZ();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        material.bind();
        movement+=3.0f * GLApp.getSecondsPerFrame();
        glBegin(GL_QUADS);
        {

            glTexCoord2f(1,0);
            glVertex3f(x+0.1f,y-0.1f,-movement);

            glTexCoord2f(0,0);
            glVertex3f(x,y-0.1f,-movement);

            glTexCoord2f(0,1);
            glVertex3f(x,y-0.1f,1.0f-movement);

            glTexCoord2f(1,1);
            glVertex3f(x+0.1f,y-0.1f,1.0f-movement);





        }
        glColor4f(0.0f, 1.0f, 0.0f, 0.8f);
        glEnd();
        glDisable(GL_BLEND);
    }
}
