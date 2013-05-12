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
    }

    public void Render()
    {
            setWorldPosition();
            super.Render();
    }

    public void Update()
    {
        // Note: target must be set before calling update.
        // calculate ET
        Vector3f ET = new Vector3f(target.getX() - this.position.getX(),
                target.getY() - this.position.getY(),
                target.getZ() - this.position.getZ());
        // calculate the pitch and yaw
        float yawToTarget = calculateVectorAngle(ET, new Vector3f(ET.getX(), ET.getY(), 0));
        float pitchToTarget = calculateVectorAngle(ET, new Vector3f(0, ET.getY(), 0));

        // Todo: decide weather its negative or positive
        // turn towards target by 30% each fame
        pitch -= pitchToTarget * 0.3f;
        yaw -= yawToTarget * 0.3f;
        // Updating position
        this.position.setX(this.position.getX() - (speed * (float)Math.sin(yaw)));
        this.position.setZ(this.position.getZ() + (speed * (float) Math.cos(yaw)));
        this.position.setY(this.position.getY() + (speed * (float)Math.sin(pitch)));
    }

    public void Initialize()
    {
        target = new Vector3f(0.0f,0.0f,0.0f);
        this.position = new Vector3f(0.0f,0.0f,0.0f);
        pitch = 0;
        yaw = 0;
        roll = 0;
        speed = 0.1f;
    }

    public void setTarget(Vector3f t){
        this.target = t;
    }
    public void setWorldPosition(){
            glMatrixMode(GL_MODELVIEW);
            //Sets the cameras X rotation
            glRotatef(pitch, 1.0f, 0.0f, 0.0f);
            //Sets the cameras Y rotation
            glRotatef(yaw, 0.0f, 1.0f, 0.0f);
            //Sets the cameras Z rotation
            glRotatef(roll, 0.0f, 0.0f, 1.0f);
            glTranslatef(this.position.getX(), this.position.getY(),this.position.getZ());
    }

    private float calculateVectorAngle(Vector3f a, Vector3f b){
        float magA = pythag3(a);
        float magB = pythag3(b);
        float dp = dotProduct(a, b);
        float res1 = (float)Math.acos(dp/(magA* magB));
        return (magA* magB) == 0 ? 0.0f : res1;
        //return (magA* magB) == 0 ? (float)Math.acos(0) : (float)Math.acos(dp/(magA* magB));
    }

    private float dotProduct(Vector3f a, Vector3f b){
        return (a.getX()*b.getX()) + (a.getY() + b.getY()) + (a.getZ() + b.getZ());
    }
    private float pythag3(float x, float y, float z){
        return (float)Math.sqrt((Math.pow(x,2)+ Math.pow(y,2)+ Math.pow(z,2)));
    }
    private float pythag3(Vector3f v){
        return pythag3(v.getX(),v.getY(),v.getZ());
    }
}
