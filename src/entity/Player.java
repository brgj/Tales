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
    public float health;
    public Vector3f fatalCrashPos;
    public Vector3f offset;
    private boolean crashed, posTilt;
    private int crashTilt;

    public Player(Model model) {
        super(model);
        hud = new HUD();
        posTilt = false;
        crashTilt = 0;
        state = State.Alive;
        fatalCrashPos = new Vector3f();
        offset = new Vector3f();
    }

    public void Render() {
        super.Render();
    }

    public void Update() {
        if (state == State.Dead) {
            respawn();
        }

        if (health <= 0 && state != State.Dead) {
            state = State.FatalCrash;
            doFatalCrash();
        } else {
            if (crashed)
                doCrash();
            model.updateRotation(-hud.crosshairPos.y * .1f, -hud.crosshairPos.x * .1f, -hud.crosshairPos.x * .1f);
        }
    }

    public void respawn() {
        //TODO: respawn code
    }

    public void crash() {
        crashed = true;
    }

    private void doFatalCrash() {
        model.updateRotation(model.pitch - .5f, model.yaw, model.roll + 5f);
        model.updatePosition(model.transX - .05f, model.transY, model.transZ - .2f);
    }

    private void doCrash() {
        int maxTilt = 30;
        int step = 10;

        if (crashTilt == maxTilt)
            posTilt = false;
        else if (crashTilt == -maxTilt)
            posTilt = true;

        crashTilt += posTilt ? step : -step;

        if (posTilt && crashTilt == 0)
            crashed = false;

        float crashPitch = crashTilt > 0 ? crashTilt : 0;

        model.updateRotation(crashPitch, 0.0f, crashTilt);
    }

    public void Initialize() {
        super.Initialize();
        health = 1f;
    }

    public void setOffset(float yaw, float pitch, Vector3f pos) {
        //Account for initial model movement, may have to adjust others values in the future
        Vector4f position = new Vector4f(pos.x, pos.y, pos.z, 1);

        Matrix4f rotate = new Matrix4f();
        rotate.setIdentity();

        Matrix4f.rotate((float) Math.toRadians(pitch), new Vector3f(1.0f, 0.0f, 0.0f), rotate, rotate);
        Matrix4f.transform(rotate, position, position);

        rotate.setIdentity();

        Matrix4f.rotate((float) Math.toRadians(yaw), new Vector3f(0.0f, 1.0f, 0.0f), rotate, rotate);
        Matrix4f.transform(rotate, position, position);

        offset = new Vector3f(position.x, position.y, -position.z);
    }
}
