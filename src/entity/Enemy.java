package entity;

import environment.Model;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glRotatef;

/**
 * Created with IntelliJ IDEA.
 * User: Owner
 * Date: 02/05/13
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class Enemy extends Entity {

    private float pitch;
    private float yaw;
    private float roll;
    private Vector3f target;
    private float speed;
    public void setSpeed(float s){
        speed = s;
    }
    public float getSpeed(){
        return speed;
    }

    public Enemy(Model model)
    {
        super(model);
        this.Initialize();
    }

    public void Render()
    {
        model.render();
        setWorldPosition();
    }

    public void Update()
    {
        // Note: target must be set before calling update.
        // calculate ET
        Vector3f ET = new Vector3f(target.getX() - this.position.getX(),
                target.getY() - this.position.getY(),
                target.getZ() - this.position.getZ());
        // calculate the new pitch and yaw
        pitch = (float)Math.toDegrees(calculatePitch(ET));
        yaw = (float)Math.toDegrees(calculateYaw(ET));

        //double radPitch = Math.toRadians(pitch);
        //double radYaw = Math.toRadians(yaw);
        //position.setX(position.getX() + (speed * (float)Math.sin(radYaw)));
        //position.setY(position.getY() - (speed * (float)Math.sin(radPitch)));
        //position.setZ(position.getZ() - (speed * (float)Math.cos(radYaw)));

    }

    public void Initialize()
    {
        target = new Vector3f(0.0f,0.0f,0.0f);
        this.position = new Vector3f(0.0f,0.0f,0.0f);
        pitch = 0;
        yaw = 0;
        roll = 180;
        speed = 0.1f;
    }

    public void setTarget(Vector3f t){
        this.target = t;
    }
    public void setWorldPosition(){
            glMatrixMode(GL_MODELVIEW);
            //Sets the cameras X rotation
            glRotatef(-pitch, 1.0f, 0.0f, 0.0f);
            //Sets the cameras Y rotation
            glRotatef(-yaw, 0.0f, 1.0f, 0.0f);
            //Sets the cameras Z rotation
            glRotatef(roll, 0.0f, 0.0f, 1.0f);
            glTranslatef(this.position.getX(), this.position.getY(),this.position.getZ());
    }

    private float calculatePitch(Vector3f v){
        return (float)Math.atan2(v.getY(), v.getZ());
    }

    private float calculateYaw(Vector3f v){
        return (float)Math.atan2(v.getX(), v.getZ());
    }
}
