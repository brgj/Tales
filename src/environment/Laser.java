package environment;
import display.Camera;
import entity.Player;
import helpers.GLHelper;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: Tammy
 * Date: 5/8/13
 * Time: 9:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class Laser  {
    public Vector3f position;
    public Camera cam;
    public Texture material  = GLHelper.LoadTexture("png", "images/glow/laser3.png");
    public Laser(Camera cam)
    {
        this.cam = cam;
        this.position = cam.getPosition();

    }
    public void update()
    {
        this.position = cam.getPosition();
    }
    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
    public void render()
    {

        float x = this.position.getX();
        float y = this.position.getY();
        float z = this.position.getZ();
        glEnable( GL_BLEND );
        glBlendFunc( GL_SRC_ALPHA, GL_ONE );
        material.bind();
        glBegin(GL_QUADS);
        {
            glTexCoord2f(1,0);
            glVertex3f(x-1.8f,y-0.6f,z-0.3f);

            glTexCoord2f(0,0);
            glVertex3f(x+1.8f,y-0.6f,z-0.3f);

            glTexCoord2f(0,1);
            glVertex3f(x+1.8f,y+0.9f,z-0.3f);

            glTexCoord2f(1,1);
            glVertex3f(x-1.8f,y+0.9f,z-0.3f);
        }
        glColor4f(0.0f,1.0f,0.0f,0.8f);
        glEnd();
        glDisable(GL_BLEND);
    }
}
