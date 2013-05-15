package entity;

import environment.Model;
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
    public Vector3f offset;

    public Entity(Model model)
    {
        center = new Vector3f();
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
        radius = model.getRadius() * model.getScaleRatio();
    }

    public Vector3f getPosition() {
        return model.getPosition();
    }

    abstract public void Update();

}
