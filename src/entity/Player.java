package entity;

import display.Camera;
import display.HUD;
import environment.Model;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

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
        model.render();
    }

    public void Update()
    {
        model.updateRotation(-hud.crosshairPos.y * .1f, -hud.crosshairPos.x * .1f, -hud.crosshairPos.x * .1f);
    }

    public void Initialize()
    {
        super.Initialize();
    }

    //TODO: Find better way of passing camera information
    public void setWorldPosition(Camera cam)
    {
        Matrix4f origin = new Matrix4f();
        origin.setZero();

        //Account for initial model movement, may have to adjust others values in the future
        origin.m02 = -model.getTranslation().z;
        origin.m03 = 1;

        origin.rotate((float)Math.toRadians(cam.getPitch()), new Vector3f(1.0f, 0.0f, 0.0f), origin, origin);
        origin.rotate((float)Math.toRadians(cam.getYaw()), new Vector3f(0.0f, 1.0f, 0.0f), origin, origin);

        position.x = origin.m00 - cam.getPosition().x;
        position.y = origin.m01 - cam.getPosition().y;
        position.z = -origin.m02 - cam.getPosition().z;
    }

}
