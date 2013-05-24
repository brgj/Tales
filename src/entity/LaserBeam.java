package entity;

import display.Camera;
import display.HUD;
import glapp.GLApp;
import helpers.GLHelper;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
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
    static public Texture material = GLHelper.LoadTexture("png", "images/glow/laser3.png");
    public float movement;
    public float speed;
    public float yaw;
    public float pitch;
    public float initial;
    public int timecounter;
    public byte ownerID;
    public boolean isExpired;
    private int multiplier;

    public LaserBeam(Camera cam, Vector3f offset, HUD hud) {
        origin = new Vector3f();
        Vector3f.sub(cam.getPosition(), offset, origin);
        movement = 0.0f;
        initial = Sys.getTime();
        timecounter = 0;
        radius = 1f;
        speed = radius * 2;
        yaw = cam.getYaw();
        pitch = cam.getPitch();
        position = new Vector3f(origin);
        isExpired = false;
        multiplier = 1;
        Initialize(hud.crosshairPos);
    }

    public LaserBeam(Vector3f origin, float yaw, float pitch, byte id, int multiplier) {
        this.origin = origin;
        movement = 0.0f;
        initial = Sys.getTime();
        timecounter = 0;
        radius = 1f;
        speed = radius * 2;
        this.yaw = yaw;
        this.pitch = pitch;
        position = new Vector3f(origin);
        isExpired = false;
        ownerID = id;
        this.multiplier = multiplier;
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
        return new Vector3f(-position.x, -position.y, -position.z);
    }

    public void setPosition(Vector3f position) {
        this.origin = position;
    }

    public void Render() {
        //renderLeft();
        //renderRight();
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
        position.z += (speed * (float) Math.cos(rad)) * multiplier;
        rad = Math.toRadians(pitch);
        position.y += speed * (float) Math.sin(rad);

    }

    public void renderLeft() {
        float x = -this.position.getX();
        float y = -this.position.getY();
        float z = -this.position.getZ();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        material.bind();
        movement += 3.0f * GLApp.getSecondsPerFrame();
        Vector4f[] rot = rotate(x,z,-1.0f,-2.0f);
        glBegin(GL_QUADS);
        {
            /*glTexCoord2f(1, 0);
            glVertex3f(x, y+0.1f, z);

            glTexCoord2f(0, 0);
            glVertex3f(x, y+0.1f, z);

            glTexCoord2f(0, 1);
            glVertex3f(x, y+0.1f, z - 2.0f);

            glTexCoord2f(1, 1);
            glVertex3f(x, y+0.1f, z - 2.0f);  */
            glTexCoord2f(1, 0);
            glVertex3f(rot[0].x, y , -rot[0].z );

            glTexCoord2f(0, 0);
            glVertex3f(rot[1].x, y , -rot[1].z );

            glTexCoord2f(0, 1);
            glVertex3f(rot[2].x, y , -rot[2].z);

            glTexCoord2f(1, 1);
            glVertex3f(rot[3].x, y , -rot[3].z);

        }
        glColor4f(0.0f, 1.0f, 0.0f, 0.8f);
        glEnd();
        glDisable(GL_BLEND);
    }

    public void renderRight() {

        float x = -this.position.getX();
        float y = -this.position.getY();
        float z = -this.position.getZ();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        material.bind();
        movement += 3.0f * GLApp.getSecondsPerFrame();
        Vector4f[] rot = rotate(x,z,1.0f,-2.0f);
        glBegin(GL_QUADS);
        {
            /*glTexCoord2f(1, 0);
            glVertex3f(x + 0.1f, y + 0.1f, z );

            glTexCoord2f(0, 0);
            glVertex3f(x, y + 0.1f, z );

            glTexCoord2f(0, 1);
            glVertex3f(x, y + 0.1f, z - 2.0f);

            glTexCoord2f(1, 1);
            glVertex3f(x + 0.1f, y + 0.1f, z  - 2.0f);*/
            glTexCoord2f(1, 0);
            glVertex3f(rot[0].x, y , -rot[0].z );

            glTexCoord2f(0, 0);
            glVertex3f(rot[1].x, y , -rot[1].z );

            glTexCoord2f(0, 1);
            glVertex3f(rot[2].x, y  , -rot[2].z);

            glTexCoord2f(1, 1);
            glVertex3f(rot[3].x, y , -rot[3].z);

        }

        glColor4f(0.0f, 1.0f, 0.0f, 0.8f);
        glEnd();
        glDisable(GL_BLEND);
    }

    public Vector4f[] rotate(float x,float z,float offsetx,float offsetz)
    {
        float x2 = x+offsetx;
        float z2 = z-offsetz;
        Vector4f point1 = new Vector4f(x2,0,z, 1);
        Vector4f point2 = new Vector4f(x,0,z, 1);
        Vector4f point3 = new Vector4f(x,0,z2, 1);
        Vector4f point4 = new Vector4f(x2,0,z2, 1);

        Matrix4f trans1 = new Matrix4f();
        trans1.setIdentity();
        Matrix4f.translate(new Vector3f(-(x), -position.y, -(z )), trans1, trans1);
        Matrix4f.transform(trans1, point1, point1);

        trans1.setIdentity();
        Matrix4f.translate(new Vector3f(-(x), -position.y, -(z)), trans1, trans1);
        Matrix4f.transform(trans1, point2, point2);

        trans1.setIdentity();
        Matrix4f.translate(new Vector3f(-(x ), -position.y, -(z)), trans1, trans1);
        Matrix4f.transform(trans1, point3, point3);

        trans1.setIdentity();
        Matrix4f.translate(new Vector3f(-(x ), -position.y, -(z )), trans1, trans1);
        Matrix4f.transform(trans1, point4, point4);


        Matrix4f rot = new Matrix4f();
        rot.setIdentity();
        Matrix4f.rotate((float)Math.toRadians(-yaw), new Vector3f(0.0f, 1.0f, 0.0f), rot, rot);
        Matrix4f.transform(rot, point1, point1);

        rot.setIdentity();
        Matrix4f.rotate((float)Math.toRadians(-yaw), new Vector3f(0.0f, 1.0f, 0.0f), rot, rot);
        Matrix4f.transform(rot, point2, point2);

        rot.setIdentity();
        Matrix4f.rotate((float)Math.toRadians(-yaw), new Vector3f(0.0f, 1.0f, 0.0f), rot, rot);
        Matrix4f.transform(rot, point3, point3);

        rot.setIdentity();
        Matrix4f.rotate((float)Math.toRadians(-yaw), new Vector3f(0.0f, 1.0f, 0.0f), rot, rot);
        Matrix4f.transform(rot, point4, point4);



        /*rot.setIdentity();
        Matrix4f.rotate((float)Math.toRadians(-pitch), new Vector3f(0.0f, 0.0f, 1.0f), rot, rot);
        Matrix4f.transform(rot, point1, point1);

        rot.setIdentity();
        Matrix4f.rotate((float)Math.toRadians(-pitch), new Vector3f(0.0f, 0.0f, 1.0f), rot, rot);
        Matrix4f.transform(rot, point2, point2);

        rot.setIdentity();
        Matrix4f.rotate((float)Math.toRadians(-pitch), new Vector3f(0.0f, 0.0f, 1.0f), rot, rot);
        Matrix4f.transform(rot, point3, point3);

        rot.setIdentity();
        Matrix4f.rotate((float)Math.toRadians(-pitch), new Vector3f(0.0f, 0.0f, 1.0f), rot, rot);
        Matrix4f.transform(rot, point4, point4);       */


        Matrix4f trans2 = new Matrix4f();
        trans2.setIdentity();
        Matrix4f.translate(new Vector3f((x), position.y, (z)), trans2, trans2);
        Matrix4f.transform(trans2, point1, point1);

        trans2.setIdentity();
        Matrix4f.translate(new Vector3f((x), position.y, (z)), trans2, trans2);
        Matrix4f.transform(trans2, point2, point2);

        trans2.setIdentity();
        Matrix4f.translate(new Vector3f((x), position.y, (z)), trans2, trans2);
        Matrix4f.transform(trans2, point3, point3);

        trans2.setIdentity();
        Matrix4f.translate(new Vector3f((x), position.y, (z)), trans2, trans2);
        Matrix4f.transform(trans2, point4, point4);



        //System.out.println("after " + x+" " + z + "p1 "+ point1.x +" " + point1.z + " " +
        //       "p2 "+ point2.x +" " + point2.z + " " +
        //       "p3 "+ point3.x +" " + point3.z + " " +
        //       "p4 "+ point3.x +" " + point3.z + " " );
        //System.out.println(trans1.m13 + " " + trans1.m23);
        //System.out.println(point1.x + " after " + point1.z);

        Vector4f[] result = new Vector4f[4];
        result[0] = point1;
        result[1] = point2;
        result[2] = point3;
        result[3] = point4;
        return result;

    }

}
