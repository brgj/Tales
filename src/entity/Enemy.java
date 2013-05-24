package entity;

import ai.AI;
import environment.Model;
import glmodel.GLModel;
import glmodel.GL_Mesh;
import helpers.GLHelper;
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
    public static GL_Mesh mesh = GLHelper.loadMesh("data/DarkFighter/dark_fighter.obj");

    public AI ai;

    public Enemy() {
        super(mesh);
        this.ai = new AI();
    }

    public Enemy(Model model) {
        super(model);
        this.ai = null;
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
