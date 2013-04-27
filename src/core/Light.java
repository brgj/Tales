package core;

import glapp.GLApp;
import org.lwjgl.opengl.GL11;

/**
 * Created with IntelliJ IDEA.
 * User: Tammy
 * Date: 4/26/13
 * Time: 8:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Light {
    float lightDirection[];//direction , position
    float diffuse[] ;  // diffuse color
    float ambient[] ;    // ambient
    float specular[];
    public Light(float lightDirection[], float diffuse[], float ambient[], float specular[])
    {
        this.lightDirection = new float[4];
        this.diffuse = new float[4];
        this.ambient = new float[4];
        this.specular = new float[4];
        this.lightDirection = lightDirection;
        this.diffuse = diffuse;
        this.ambient = ambient;
        this.specular = specular;

    }
    public void setLight()
    {

        GL11.glEnable(GL11.GL_LIGHTING);
        GLApp.setLight(GL11.GL_LIGHT1,
                diffuse,    // diffuse color
                ambient,    // ambient
                specular,    // specular
                lightDirection);                     // direction/position
        GLApp.setAmbientLight(new float[] { .6f, .6f, .9f, 1f });
    }
}
