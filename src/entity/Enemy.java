package entity;

import ai.AI;
import environment.Model;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Owner
 * Date: 02/05/13
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class Enemy extends Entity {

    public AI ai;

    public Enemy(Model model) {
        super(model);
        this.ai = null;
    }

    public Enemy(Model model, AI ai) {
        this(model);
        this.ai = ai;
    }

    public void Render() {
        model.transform();
        model.render();
    }

    public void Update() {
        // Note: target must be set before calling update.
        // calculate ET
        if(isAI()) {
            ai.Update(getPosition(), model);
        }
    }

    public void Initialize() {
        super.Initialize();
    }

    public boolean isAI() {
        return ai != null;
    }

    public void setTarget(Vector3f t) {
        ai.setTarget(t);
    }
}
