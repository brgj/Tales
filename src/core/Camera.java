package core;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: Owner
 * Date: 23/04/13
 * Time: 9:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class Camera {

    //The position that the camera will be placed in the world
    private Vector3f position;
    //Rotation around Y axis
    private float yaw;
    //Rotation around X axis
    private float pitch;
    //Rotation around Z axis
    private float roll;

    private boolean upsidedown = false;

    //Constructor that takes a vector
    public Camera(Vector3f initialPosition) {
        position = initialPosition;
        yaw = 0.0f;
        pitch = 0.0f;
        roll = 0.0f;
    }

    /*
    TODO: Implement following of the player. This will have to be implemented when we figure out how the camera will transform as it follows the ship
     */

    //Changes the cameras view based on the yaw, and pitch
    public void setCameraView(float mouseSpeed, HUD hud) {
        if (Mouse.isInsideWindow()) {
            Mouse.setGrabbed(true);

            int midX = Display.getWidth() / 2;
            int midY = Display.getHeight() / 2;

            int dX = (midX - Mouse.getX());
            int dY = (midY - Mouse.getY());


            pitch += mouseSpeed * dY;
            pitch %= 360;

            upsidedown = pitch > 90 || pitch < -90;

            yaw -= upsidedown ? -1 * mouseSpeed * dX : mouseSpeed * dX;
            yaw %= 360;

            hud.setCrosshairX(-dX / 5);
            hud.setCrosshairY(dY / 5);

            if(dX < 5 && dY < 5)
                hud.crosshairReset();

            Mouse.setCursorPosition(midX, midY);
        } else {
            Mouse.setGrabbed(false);
        }
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        //Sets the cameras X rotation
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //Sets the cameras Y rotation
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //Sets the cameras Z rotation
        glRotatef(roll, 0.0f, 0.0f, 1.0f);
    }

    public void moveCamera() {
        //Sets the camera to a position
        glTranslatef(position.x, position.y, position.z);
    }

    public void move(float units, int dir) {
        if(dir % 180 == 0 && upsidedown) {
            units = -units;
        }
        double rad = Math.toRadians(yaw + dir);
        position.x -= units * (float) Math.sin(rad);
        position.z += units * (float) Math.cos(rad);
    }

    public void moveUp(float units, float dir) {
        double rad = Math.toRadians(pitch + dir);
        position.y += units * (float) Math.sin(rad);
    }


    //region Getters and Setters

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void setY(float units) {
        this.position.y += units;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw += yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch += pitch;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll += roll;
    }

    //endregion
}
