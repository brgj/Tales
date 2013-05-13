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

    public Vector3f position;
    public Model model;
    public float radius;
    public Vector3f center;

    public Entity(Model model)
    {
        position = new Vector3f();
        center = new Vector3f();
        this.model = model;
        Initialize();
    }

    public void Initialize()
    {
        radius = model.getRadius() * model.getScaleRatio();
    }

    abstract void Render();

    abstract void Update();

}
