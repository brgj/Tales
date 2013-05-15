package helpers;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 24/04/13
 * Time: 1:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class GLHelper {
    private GLHelper() {

    }

    public static Texture LoadTexture(String type, String path) {
        try {
            return TextureLoader.getTexture(type, new FileInputStream(new File(path)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static Matrix4f getInverseModelViewMatrix() {
        ByteBuffer buf = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder());

        glGetFloat(GL_MODELVIEW_MATRIX, buf.asFloatBuffer());

        Matrix4f mat = new Matrix4f();

        mat.load(buf.asFloatBuffer());

        mat.m32 = -mat.m32;

        mat.invert();

        return mat;
    }

    public static void glGetFloatv(int pname, float[] params) {
        ByteBuffer bytes = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder());

        glGetFloat(pname, (FloatBuffer) bytes.asFloatBuffer().put(params).flip());

        bytes.asFloatBuffer().get(params);
    }

    public static void renderSphere(Vector3f center, float radius) {
        glColor3f(1.0f, 1.0f, 1.0f);
        glBegin(GL_LINE_LOOP);
        {
            for (int i = 0; i < 360; i++) {
                double degInRad = i * Math.PI / 180;
                glVertex3f((float) (center.x + Math.cos(degInRad) * radius), (float) (center.y + Math.sin(degInRad) * radius), center.z);
            }
        }
        glEnd();

        glBegin(GL_LINE_LOOP);
        {
            for (int i = 0; i < 360; i++) {
                double degInRad = i * Math.PI / 180;
                glVertex3f(center.x, (float) (center.y + Math.cos(degInRad) * radius), (float) (center.z + Math.sin(degInRad) * radius));
            }
        }
        glEnd();
    }
}
