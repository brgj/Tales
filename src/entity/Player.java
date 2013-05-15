package entity;

import display.HUD;
import environment.Model;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Created with IntelliJ IDEA.
 * User: Owner
 * Date: 02/05/13
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class Player extends Entity {

    public HUD hud;

    public Player(Model model)
    {
        super(model);
        hud = new HUD();
    }

    public void Render()
    {
        super.Render();
    }

    public void Update()
    {
        model.updateRotation(-hud.crosshairPos.y * .1f, -hud.crosshairPos.x * .1f, -hud.crosshairPos.x * .1f);
    }

    public void Initialize()
    {
        super.Initialize();
    }

    public void setPlayerOffset(float yaw, float pitch) {
        //Account for initial model movement, may have to adjust others values in the future
        Vector4f position = new Vector4f(0, 0, -model.getPosition().z, 1);

        Matrix4f rotate = new Matrix4f();
        rotate.setIdentity();

        Matrix4f.rotate((float)Math.toRadians(pitch), new Vector3f(1.0f, 0.0f, 0.0f), rotate, rotate);
        Matrix4f.transform(rotate, position, position);

        rotate.setIdentity();

        Matrix4f.rotate((float) Math.toRadians(yaw), new Vector3f(0.0f, 1.0f, 0.0f), rotate, rotate);
        Matrix4f.transform(rotate, position, position);

        offset = new Vector3f(position.x, position.y, -position.z);
    }

}
