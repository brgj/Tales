package ai;

import environment.Model;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 02/05/13
 * Time: 12:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class AI {
    private float speed;
    private Vector3f target;

    public AI() {
        speed = 0.1f;
        target = new Vector3f(0.0f, 0.0f, 0.0f);
    }

    public void Update(Vector3f pos, Model model) {
        Vector3f ET = new Vector3f(target.getX() - pos.getX(),
                target.getY() - pos.getY(),
                target.getZ() - pos.getZ());
//        calculate the new pitch and yaw
        model.yaw = (float) Math.toDegrees(calculateYaw(ET));
        model.pitch = (float) Math.toDegrees(calculatePitch(new Vector3f(ET.x, ET.y, Math.abs(ET.z))));

//        System.out.println(model.yaw);
//        System.out.println(model.pitch);

//        System.out.println(target);
//        System.out.println(ET);


        double radPitch = Math.toRadians(model.pitch);
        double radYaw = Math.toRadians(model.yaw);
        model.updatePosition(pos.getX() + (speed * (float)Math.sin(radYaw)),
                pos.getY() - (speed * (float)Math.sin(radPitch)),
                pos.getZ() + (speed * (float)Math.cos(radYaw)));
//        position.setX(position.getX() - (speed * (float)Math.sin(radYaw)));
//        position.setY(position.getY() - (speed * (float)Math.sin(radPitch)));
//        position.setZ(position.getZ() + (speed * (float)Math.cos(radYaw)));
//        System.out.println((speed * (float)Math.sin(radYaw)));
//        System.out.println(position.getX());
    }

    private float calculatePitch(Vector3f v) {
        return 0 - (float)Math.atan(v.getY() / v.getZ());
    }

    private float calculateYaw(Vector3f v) {
        return  (float)Math.atan2(v.getX(), v.getZ());
    }

    public void setSpeed(float s) {
        speed = s;
    }

    public float getSpeed() {
        return speed;
    }

    public void setTarget(Vector3f t) {
        this.target = t;
    }
}
