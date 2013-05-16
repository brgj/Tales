package entity;

import display.Camera;
import display.HUD;
import entity.Entity;
import glapp.GLApp;
import helpers.GLHelper;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
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
    public float speed;
    public float yaw;
    public float pitch;
    public float initial;
    public int timecounter;
    public int ownerID;
    public boolean isExpired;

    public LaserBeam(Camera cam, Vector3f offset, HUD hud) {
        this.origin = (Vector3f.sub(cam.getPosition(), offset, origin));
        movement = 0.0f;
        initial = Sys.getTime();
        timecounter = 0;
        radius = 5f;
        speed = .1f;
        yaw = cam.getYaw();
        pitch = cam.getPitch();
        position = new Vector3f(origin);
        isExpired = false;
        Initialize(hud.crosshairPos);
    }

    public void Initialize(Vector2f chPosition) {
        //Account for the crosshair position
        //y field of view (in degrees)
        float fovy = 45.0f;
        //x field of view (in degrees)
        float fovx = fovy * (float) Display.getWidth() / (float) Display.getHeight();

        yaw = (yaw + (chPosition.getX() / (float) Display.getWidth() / 2) * (fovx / 2)) % 360;
        pitch = (pitch + (chPosition.getY() / (float) Display.getHeight() / 2) * (fovy / 2)) % 360;
    }


    public void resetTime() {
        this.initial = Sys.getTime();
        //this.movement = 0.0f;
    }

    public int getTimepassed() {
        return (int) ((Sys.getTime() - this.initial) / 1000);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.origin = position;
    }

    public void Render() {
        //renderLeft();
        renderRight();
    }

    public void Update() {
        getTimepassed();
        if(getTimepassed() > 5)
        {
            isExpired = true;
        }
        //movement += 10.0f * GLApp.getSecondsPerFrame();
        //Updates the position of the laser
        double rad = Math.toRadians(yaw);
        position.x -= speed * (float) Math.sin(rad);
        position.z += speed * (float) Math.cos(rad);
        rad = Math.toRadians(pitch);
        position.y += speed * (float) Math.sin(rad);
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
    public void renderLeft() {
        //glLoadIdentity();

        //this.origin = cam.getPosition();
        float x = -this.position.getX();
        float y = -this.position.getY();
        float z = -this.position.getZ();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        material.bind();
        movement += 3.0f * GLApp.getSecondsPerFrame();

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

            */
            glTexCoord2f(1, 0);
            glVertex3f(x, y, z);

            glTexCoord2f(0, 0);
            glVertex3f(x, y, z);

            glTexCoord2f(0, 1);
            glVertex3f(x, y, z - 2.0f);

            glTexCoord2f(1, 1);
            glVertex3f(x, y, z - 2.0f);


        }
        glColor4f(0.0f, 1.0f, 0.0f, 0.8f);
        glEnd();
        glDisable(GL_BLEND);
    }

    public void renderRight() {
        //glLoadIdentity();
        //this.origin = cam.getPosition();
        float x = -this.position.getX();
        float y = -this.position.getY();
        float z = -this.position.getZ();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        material.bind();
        movement += 3.0f * GLApp.getSecondsPerFrame();
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
            */
            glTexCoord2f(1, 0);
            glVertex3f(x + 0.1f, y - 0.1f, z - 0.5f);

            glTexCoord2f(0, 0);
            glVertex3f(x, y - 0.1f, z - 0.5f);

            glTexCoord2f(0, 1);
            glVertex3f(x, y - 0.1f, z - 0.5f - 2.0f);

            glTexCoord2f(1, 1);
            glVertex3f(x + 0.1f, y - 0.1f, z - 0.5f - 2.0f);
        }
        glColor4f(0.0f, 1.0f, 0.0f, 0.8f);
        glEnd();
        glDisable(GL_BLEND);
    }

    public void calDir(Camera cam) {

        float x = cam.getPosition().getX();
        float y = cam.getPosition().getY();
        float z = cam.getPosition().getZ() + 5.0f;
        Matrix4f dir1 = new Matrix4f();
        dir1.setZero();

        Matrix4f dir;
        Matrix4f temp;
        if (cam.getYaw() != 0) {
            Matrix4f yaw = new Matrix4f();
            yaw.setIdentity();
            //yaw.

        }

    }

    public void getcurPosition(Camera cam) {
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
        System.out.println("origin " + x + " " + y + " " + z);
        Matrix4f pitch = new Matrix4f();
        //pitch.setIdentity();
        pitch = p1.rotate((float) Math.toRadians(cam.getPitch()), new Vector3f(1.0f, 0.0f, 0.0f), pitch);
        Matrix4f yaw = new Matrix4f();
        //yaw.setIdentity();
        yaw = p1.rotate((float) Math.toRadians(cam.getYaw()), new Vector3f(0.0f, 1.0f, 0.0f), yaw);

        //this.origin.setX(yaw.m00);
        //this.origin.setY(yaw.m01);
        //this.origin.setZ(yaw.m02);
        this.origin = new Vector3f(yaw.m00, yaw.m01, yaw.m02);
        //System.out.println(cam.getPitch());
        System.out.println("pitch" + yaw.m00 + " " + yaw.m01 + " " + yaw.m02);
        //System.out.println(origin.getX()+" "+origin.getY()+" "+origin.getZ());
        /*Matrix4f yaw = pitch.rotate((float)Math.toRadians(cam.getYaw()),new Vector3f(0.0f,1.0f,0.0f));

        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //Sets the cameras Y rotation
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //Sets the cameras Z rotation
        glRotatef(roll, 0.0f, 0.0f, 1.0f);    */
    }

}
