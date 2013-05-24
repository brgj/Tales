package ai;

import entity.LaserBeam;
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
    public boolean isShooting;
    public int shotTime;
    private long startTime;

    public AI() {
        speed = 0.25f;
        isShooting = false;
        shotTime = 0;
        target = new Vector3f(0.0f, 0.0f, 0.0f);
        startTime = System.currentTimeMillis();
    }

    public void Update(Vector3f pos, Model model) {
        Vector3f ET = new Vector3f(target.getX() - pos.getX(),
                target.getY() - pos.getY(),
                target.getZ() - pos.getZ());
//        calculate the new pitch and yaw
        model.yaw = (float) Math.toDegrees(calculateYaw(ET));
        model.pitch = (float) Math.toDegrees(calculatePitch(ET));

        double radPitch = Math.toRadians(model.pitch);
        double radYaw = Math.toRadians(model.yaw);
        model.updatePosition(pos.getX() + (speed * (float)Math.sin(radYaw)),
                pos.getY() - (speed * (float)Math.sin(radPitch)),
                pos.getZ() + (speed * (float)Math.cos(radYaw)));

        // checks to see if the ai is close enough to the player to shoot
        if(calcTargetDistance(ET) < 25.0f && timePassed(0.1f))
            isShooting = true;
        else
            isShooting = false;
    }

    private float calculatePitch(Vector3f v) {
        float temp = Math.abs(v.getZ());
        if(temp < 5) {
            temp = 5;
        }
        return 0 - (float)Math.atan(v.getY() / temp);
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

    private float calcTargetDistance(Vector3f v){
        return (float)Math.sqrt(Math.pow(v.x,2) + Math.pow(v.y,2) + Math.pow(v.z,2));
    }

    public void resetTimer(){
        startTime = System.currentTimeMillis();
    }

    public boolean timePassed(float sec){
        float timeDelta = (System.currentTimeMillis() - startTime )/ 1000;
        return sec < timeDelta;
    }


}
