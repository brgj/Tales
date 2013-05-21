package entity;

import environment.Model;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Created with IntelliJ IDEA.
 * User: Owner
 * Date: 02/05/13
 * Time: 10:53 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Entity {

    public Model model;
    public float radius;
    public Vector3f center;
    public Vector3f offset;

    public Entity(Model model)
    {
        offset = new Vector3f();
        this.model = model;
        Initialize();
    }

    public Entity()
    {

    }

    public void Render()
    {
        model.transform();
        model.render();
    }

    public void Initialize()
    {
        radius = model.getRadius() * model.getScaleRatio() * 0.75f;
        center = model.getCenter();
        center.scale(model.getScaleRatio());
    }

    public Vector3f getPosition() {
        return model.getPosition();
    }

    public void setOffset(float yaw, float pitch, float pos) {
        //Account for initial model movement, may have to adjust others values in the future
        Vector4f position = new Vector4f(0, 0, pos, 1);

        Matrix4f rotate = new Matrix4f();
        rotate.setIdentity();

        Matrix4f.rotate((float)Math.toRadians(pitch), new Vector3f(1.0f, 0.0f, 0.0f), rotate, rotate);
        Matrix4f.transform(rotate, position, position);

        rotate.setIdentity();

        Matrix4f.rotate((float) Math.toRadians(yaw), new Vector3f(0.0f, 1.0f, 0.0f), rotate, rotate);
        Matrix4f.transform(rotate, position, position);

        offset = new Vector3f(position.x, position.y, -position.z);
    }

    abstract public void Update();

}
