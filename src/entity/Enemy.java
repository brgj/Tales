package entity;

import environment.Model;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created with IntelliJ IDEA.
 * User: Owner
 * Date: 02/05/13
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class Enemy extends Entity {

    private Vector3f target;
    private float speed;

    public Enemy(Model model, Vector3f offset) {
        super(model);
        this.offset = offset;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float s) {
        speed = s;
    }

    public void Render() {
        GL11.glTranslatef(offset.x, offset.y, offset.z);
        model.transform();
        model.render();
    }

    public void Update() {
        //TODO: Set if AI should be used or not... Can't be always on.
        // Note: target must be set before calling update.
        // calculate ET
//        Vector3f position = getPosition();
//        Vector3f ET = new Vector3f(target.getX() - position.getX(),
//                target.getY() - position.getY(),
//                target.getZ() - position.getZ());
        // calculate the new pitch and yaw
//        model.pitch = (float) Math.toDegrees(calculatePitch(ET));
//        model.yaw = (float) Math.toDegrees(calculateYaw(ET));

//        double radPitch = Math.toRadians(model.pitch);
//        double radYaw = Math.toRadians(model.yaw);
//        position.setX(position.getX() + (speed * (float)Math.sin(radYaw)));
//        position.setY(position.getY() - (speed * (float)Math.sin(radPitch)));
//        position.setZ(position.getZ() - (speed * (float)Math.cos(radYaw)));

    }

    public void Initialize() {
        super.Initialize();
        target = new Vector3f(0.0f, 0.0f, 0.0f);
        speed = 0.1f;
        model.roll = 180;
    }

    public void setTarget(Vector3f t) {
        this.target = t;
    }

    private float calculatePitch(Vector3f v) {
        return 0 - (float)Math.atan2(v.getY(), v.getZ());
    }

    private float calculateYaw(Vector3f v) {
        return  0 - (float)Math.atan2(v.getX(), v.getZ());
    }
}
