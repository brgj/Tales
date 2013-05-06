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

    Vector3f position;
    Model model;

    public Entity(Model model)
    {
        this.model = model;
    }

    public void Render()
    {
        model.render();
    }

    public void Update()
    {

    }

    public void Initialize()
    {

    }




}
