package entity;

import environment.Model;
import glmodel.GL_Mesh;
import org.lwjgl.util.vector.Vector3f;

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
    public State state;

    public Entity(GL_Mesh mesh) {
        this.model = new Model(mesh);
    }

    public Entity(Model model)
    {
        this.model = model;
        Initialize();
    }

    public Entity()
    {

    }

    public enum State
    {
        Invincible,
        Turning,
        FatalCrash,
        Dead,
        Alive
    }

    public void Render()
    {
        model.transform();
        model.render();
    }

    public void Initialize()
    {
        radius = model.getRadius() * model.getScaleRatio() * 0.4f;
        center = model.getCenter();
        center.scale(model.getScaleRatio());
    }

    public Vector3f getPosition() {
        return model.getPosition();
    }

    abstract public void Update();

}
