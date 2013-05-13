package environment;

import display.Camera;
import glapp.GLApp;
import helpers.GLHelper;
import org.lwjgl.Sys;
import org.lwjgl.util.vector.Matrix4f;
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
public class LaserBeam {
    public Vector3f origin;
    public Vector3f initialDir = new Vector3f(0.0f,0.0f,1.0f);
    public Texture material  = GLHelper.LoadTexture("png", "images/glow/laser.png");
    public float movement;
    public float initial;
    public int timecounter;
    public LaserBeam(Camera cam)
    {
        this.origin = cam.getPosition();
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
        return origin;
    }

    public void setPosition(Vector3f position) {
        this.origin = position;
    }
    public void renderLeft()
    {

        float x = -this.origin.getX();
        float y = -this.origin.getY();
        float z = -this.origin.getZ();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        material.bind();
        movement+=10.0f * GLApp.getSecondsPerFrame();
        glBegin(GL_QUADS);
        {

            glTexCoord2f(1,0);
            glVertex3f(x,y+0.2f-0.1f,z-movement);

            glTexCoord2f(0,0);
            glVertex3f(x-0.1f,y+0.2f-0.1f,z-movement);

            glTexCoord2f(0,1);
            glVertex3f(x-0.1f,y+0.2f-0.1f,z-1.0f-movement);

            glTexCoord2f(1,1);
            glVertex3f(x,y+0.2f-0.1f,z-1.0f-movement);





        }
        glColor4f(0.0f, 1.0f, 0.0f, 0.8f);
        glEnd();
        glDisable(GL_BLEND);
    }
    public void renderRight()
    {

        float x = -this.origin.getX();
        float y = -this.origin.getY();
        float z = -this.origin.getZ();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        material.bind();
        movement+=3.0f * GLApp.getSecondsPerFrame();
        glBegin(GL_QUADS);
        {

            glTexCoord2f(1,0);
            glVertex3f(x+0.1f,y+0.2f-0.1f,z-movement);

            glTexCoord2f(0,0);
            glVertex3f(x,y+0.2f-0.1f,z-movement);

            glTexCoord2f(0,1);
            glVertex3f(x,y+0.2f-0.1f,z-1.0f-movement);

            glTexCoord2f(1,1);
            glVertex3f(x+0.1f,y+0.2f-0.1f,z-1.0f-movement);





        }
        glColor4f(0.0f, 1.0f, 0.0f, 0.8f);
        glEnd();
        glDisable(GL_BLEND);
    }
    public void calDir(Camera cam)
    {
        float x = cam.getPosition().getX();
        float y = cam.getPosition().getY();
        float z = cam.getPosition().getZ()+5.0f;
        Matrix4f dir ;
        Matrix4f temp;
        if(cam.getYaw()!=0)
        {
        }

    }
}
