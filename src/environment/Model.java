package environment;

import glmodel.GLModel;
import glmodel.GL_Vector;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created with IntelliJ IDEA.
 * User: Tammy
 * Date: 4/26/13
 * Time: 10:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class Model {
    private float scaleRatio = 1.0f;
    public float pitch = 0.0f, yaw = 0.0f, roll = 0.0f;
    public float transX = 0.0f, transY = 0.0f, transZ = 0.0f;
    private GLModel model;

    public Model(String filename) {
        model = new GLModel(filename);
        model.regenerateNormals();
    }

    public Model(String filename, float scaleRatio, float rotX, float rotY, float rotZ,
                 float transX, float transY, float transZ) {
        model = new GLModel(filename);
        this.scaleRatio = scaleRatio;
        this.pitch = rotX;
        this.yaw = rotY;
        this.roll = rotZ;
        this.transX = transX;
        this.transY = transY;
        this.transZ = transZ;
        model.regenerateNormals();
    }

    public void transform() {
        GL11.glTranslatef(transX, transY, transZ);
        if (scaleRatio != 0) {
            GL11.glScalef(scaleRatio, scaleRatio, scaleRatio);
        }
        GL11.glRotatef(pitch, 1, 0, 0);
        GL11.glRotatef(yaw, 0, 1, 0);
        GL11.glRotatef(roll, 0, 0, 1);
    }

    public void render() {
        model.render();
    }

    public void updatePosition(float transX, float transY, float transZ) {
        this.transX = transX;
        this.transY = transY;
        this.transZ = transZ;
    }

    public void updateRotation(float pitch, float yaw, float roll) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.roll = roll;
    }

    public Vector3f getPosition() {
        return new Vector3f(transX, transY, transZ);
    }

    public float getScaleRatio() {
        return scaleRatio;
    }

    public Vector3f getCenter() {
        GL_Vector center = model.mesh.getCenter();

        return new Vector3f(center.x, center.y, center.z);
    }

    public float getRadius() {
        GL_Vector dim = model.mesh.getDimension();

        return (float) Math.sqrt(Math.pow(dim.x / 2, 2) + Math.pow(dim.y / 2, 2) + Math.pow(dim.z / 2, 2));
    }
}
