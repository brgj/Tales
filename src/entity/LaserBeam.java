package entity;

import display.Camera;
import entity.Entity;
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
public class LaserBeam extends Entity {
    public Vector3f origin;
    public Vector3f initialDir = new Vector3f(0.0f, 0.0f, 1.0f);
    public Vector3f position;
    public Texture material = GLHelper.LoadTexture("png", "images/glow/laser.png");
    public float movement;
    public float initial;
    public int timecounter;

    public LaserBeam(Camera cam) {
        this.origin = new Vector3f(cam.getPosition());
        movement = 0.0f;
        initial = Sys.getTime();
        timecounter = 0;
        radius = 1.0f;
        position = new Vector3f(origin);
    }

    public void resetTime() {
        this.initial = Sys.getTime();
        //this.movement = 0.0f;
    }

    public void getTimepassed() {
        this.timecounter = (int) ((Sys.getTime() - this.initial) / 1000);
    }

    public Vector3f getPosition() {
        return origin;
    }

    public void setPosition(Vector3f position) {
        this.origin = position;
    }

    public void Render() {
        renderLeft();
        renderRight();
    }

    public void Update() {
        movement += 10.0f * GLApp.getSecondsPerFrame();
        //Updates the position of the laser
        position.z += -movement;
    }

    /*public void renderLeft() {

        float x = -this.origin.getX();
        float y = -this.origin.getY();
        float z = -this.origin.getZ();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        material.bind();
        glBegin(GL_QUADS);
        {
            glTexCoord2f(1, 0);
            glVertex3f(x, y + 0.2f - 0.1f, z - movement);

            glTexCoord2f(0, 0);
            glVertex3f(x - 0.1f, y + 0.2f - 0.1f, z - movement);

            glTexCoord2f(0, 1);
            glVertex3f(x - 0.1f, y + 0.2f - 0.1f, z - 1.0f - movement);

            glTexCoord2f(1, 1);
            glVertex3f(x, y + 0.2f - 0.1f, z - 1.0f - movement);
        }
        glColor4f(0.0f, 1.0f, 0.0f, 0.8f);
        glEnd();
        glDisable(GL_BLEND);
    }

    public void renderRight() {

        float x = -this.origin.getX();
        float y = -this.origin.getY();
        float z = -this.origin.getZ();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        material.bind();
        movement += 3.0f * GLApp.getSecondsPerFrame();
        glBegin(GL_QUADS);
        {
            glTexCoord2f(1, 0);
            glVertex3f(x + 0.1f, y + 0.2f - 0.1f, z - movement);

            glTexCoord2f(0, 0);
            glVertex3f(x, y + 0.2f - 0.1f, z - movement);

            glTexCoord2f(0, 1);
            glVertex3f(x, y + 0.2f - 0.1f, z - 1.0f - movement);

            glTexCoord2f(1, 1);
            glVertex3f(x + 0.1f, y + 0.2f - 0.1f, z - 1.0f - movement);

        }
        glColor4f(0.0f, 1.0f, 0.0f, 0.8f);
        glEnd();
        glDisable(GL_BLEND);
    }

    public void calDir(Camera cam) {
        float x = cam.getPosition().getX();
        float y = cam.getPosition().getY();
        float z = cam.getPosition().getZ() + 5.0f;
        Matrix4f dir;
        Matrix4f temp;
        if (cam.getYaw() != 0) {
        }
    }     */
    public void renderLeft()
    {
        //glLoadIdentity();

        //this.origin = cam.getPosition();
        float x = -this.origin.getX();
        float y = -this.origin.getY();
        float z = -this.origin.getZ();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        material.bind();
        movement+=3.0f * GLApp.getSecondsPerFrame();
        glBegin(GL_QUADS);
        {
            /*glTexCoord2f(1,0);
            glVertex3f(x,y+0.2f-0.1f,-z-20-movement);

            glTexCoord2f(0,0);
            glVertex3f(x-0.2f,y+0.2f-0.1f,-z-20-movement);

            glTexCoord2f(0,1);
            glVertex3f(x-0.2f,y+0.2f-0.1f,-z-20-2.0f-movement);

            glTexCoord2f(1,1);
            glVertex3f(x,y+0.2f-0.1f,-z-20-2.0f-movement);

            */glTexCoord2f(1,0);
            glVertex3f(x,y-0.1f,z-0.5f-movement);

            glTexCoord2f(0,0);
            glVertex3f(x-0.1f,y-0.1f,z-0.5f-movement);

            glTexCoord2f(0,1);
            glVertex3f(x-0.1f,y-0.1f,z-0.5f-2.0f-movement);

            glTexCoord2f(1,1);
            glVertex3f(x,y-0.1f,z-0.5f-2.0f-movement);





        }
        glColor4f(0.0f, 1.0f, 0.0f, 0.8f);
        glEnd();
        glDisable(GL_BLEND);
    }
    public void renderRight()
    {
        //glLoadIdentity();
        //this.origin = cam.getPosition();
        float x = -this.origin.getX();
        float y = -this.origin.getY();
        float z = -this.origin.getZ();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        material.bind();
        movement+=3.0f * GLApp.getSecondsPerFrame();
        glBegin(GL_QUADS);
        {
            /*glTexCoord2f(1,0);
            glVertex3f(x+0.2f,y+0.2f-0.1f,-z-20-movement);

            glTexCoord2f(0,0);
            glVertex3f(x,y+0.2f-0.1f,-z-20-movement);

            glTexCoord2f(0,1);
            glVertex3f(x,y+0.2f-0.1f,-z-20-2.0f-movement);

            glTexCoord2f(1,1);
            glVertex3f(x+0.2f,y+0.2f-0.1f,-z-20-2.0f-movement);
            */glTexCoord2f(1,0);
            glVertex3f(x+0.1f,y-0.1f,z-0.5f-movement);

            glTexCoord2f(0,0);
            glVertex3f(x,y-0.1f,z-0.5f-movement);

            glTexCoord2f(0,1);
            glVertex3f(x,y-0.1f,z-0.5f-2.0f-movement);

            glTexCoord2f(1,1);
            glVertex3f(x+0.1f,y-0.1f,z-0.5f-2.0f-movement);
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
        Matrix4f dir1 = new Matrix4f();
        dir1.setZero();

        Matrix4f dir ;
        Matrix4f temp;
        if(cam.getYaw()!=0)
        {
            Matrix4f yaw = new Matrix4f();
            yaw.setIdentity();
            //yaw.

        }

    }
    public void getcurPosition(Camera cam)
    {
        //glLoadIdentity();
        float x = cam.getPosition().getY();
        float y = cam.getPosition().getY();
        float z = cam.getPosition().getZ();
        Matrix4f p1 = new Matrix4f();
        p1.setZero();
        p1.m00 = x;
        p1.m01 = y;
        p1.m02 = z;
        p1.m03 = 1;
        System.out.println("origin "+x+" "+y+" "+z);
        Matrix4f pitch = new Matrix4f();
        //pitch.setIdentity();
        pitch = p1.rotate((float)Math.toRadians(cam.getPitch()),new Vector3f(1.0f,0.0f,0.0f),pitch);
        Matrix4f yaw = new Matrix4f();
        //yaw.setIdentity();
        yaw = p1.rotate((float)Math.toRadians(cam.getYaw()),new Vector3f(0.0f,1.0f,0.0f),yaw);

        //this.origin.setX(yaw.m00);
        //this.origin.setY(yaw.m01);
        //this.origin.setZ(yaw.m02);
        this.origin = new Vector3f(yaw.m00,yaw.m01,yaw.m02);
        //System.out.println(cam.getPitch());
        System.out.println("pitch"+ yaw.m00+" "+yaw.m01+" "+yaw.m02);
        //System.out.println(origin.getX()+" "+origin.getY()+" "+origin.getZ());
        /*Matrix4f yaw = pitch.rotate((float)Math.toRadians(cam.getYaw()),new Vector3f(0.0f,1.0f,0.0f));

        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //Sets the cameras Y rotation
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //Sets the cameras Z rotation
        glRotatef(roll, 0.0f, 0.0f, 1.0f);    */
    }

}
