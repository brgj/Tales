package core;
import core.Background;
import core.Camera;
import helpers.Delegate;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import glapp.*;
import glmodel.*;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tammy
 * Date: 4/26/13
 * Time: 10:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class Model {
    GLModel model;
    float scaleratio = 1.0f, rotx = 0.0f, roty = 0.0f, rotz = 0.0f,
            transx = 0.0f, transy = 0.0f, transz = 0.0f;
    public Model(String filename)
    {
        model = new GLModel(filename);
        model.regenerateNormals();


    }
    public Model(String filename,float scaleratio,float rotx,float roty,float rotz,
                 float transx,float transy,float transz)
    {
        model = new GLModel(filename);
        this.scaleratio = scaleratio;
        this.rotx = rotx;
        this.roty = roty;
        this.rotz = rotz;
        this.transx = transx;
        this.transy = transy;
        this.transz = transz;
        model.regenerateNormals();


    }
    public void render()
    {
        GL11.glScalef(scaleratio,scaleratio,scaleratio);
        GL11.glRotatef(rotx,1,0,0);
        GL11.glRotatef(roty,0,1,0);
        GL11.glRotatef(rotz,0,0,1);
        GL11.glTranslatef(transx,transy,transz);
        model.render();
    }
    public void updatePosition(float transx,float transy,float transz)
    {
        this.transx = transx;
        this.transy = transy;
        this.transz = transz;
    }
    public void updateRotation(float rotx,float roty,float rotz)
    {
        this.rotx = rotx;
        this.roty = roty;
        this.rotz = rotz;
    }
}
