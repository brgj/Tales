package helpers;

import org.lwjgl.util.vector.Matrix4f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW_MATRIX;
import static org.lwjgl.opengl.GL11.glGetFloat;

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

    public static Matrix4f getInverseViewMatrix() {
        float[] view = new float[16];

        glGetFloatv(GL_MODELVIEW_MATRIX, view);

        Matrix4f mat = getViewMatrix(view);

        mat.invert();

        return mat;
    }

    public static Matrix4f getInverseModelMatrix() {
        float[] model = new float[16];

        glGetFloatv(GL_MODELVIEW_MATRIX, model);

        Matrix4f mat = getMatrix(model);

        mat.invert();

        return mat;
    }

    public static Matrix4f getViewMatrix(float[] array) {
        Matrix4f mat = new Matrix4f();
        mat.m00 = array[0];
        mat.m01 = array[1];
        mat.m02 = array[2];
        mat.m03 = array[3];
        mat.m10 = array[4];
        mat.m11 = array[5];
        mat.m12 = array[6];
        mat.m13 = array[7];
        mat.m20 = array[8];
        mat.m21 = array[9];
        mat.m22 = array[10];
        mat.m23 = array[11];
        mat.m30 = array[12];
        mat.m31 = array[13];
        mat.m32 = -array[14];
        mat.m33 = array[15];
        return mat;
    }

    public static Matrix4f getMatrix(float[] array) {
        Matrix4f mat = new Matrix4f();
        mat.m00 = array[0];
        mat.m01 = array[1];
        mat.m02 = array[2];
        mat.m03 = array[3];
        mat.m10 = array[4];
        mat.m11 = array[5];
        mat.m12 = array[6];
        mat.m13 = array[7];
        mat.m20 = array[8];
        mat.m21 = array[9];
        mat.m22 = array[10];
        mat.m23 = array[11];
        mat.m30 = array[12];
        mat.m31 = array[13];
        mat.m32 = array[14];
        mat.m33 = array[15];
        return mat;
    }

    public static void glGetFloatv(int pname, float[] params) {
        ByteBuffer bytes = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder());

        glGetFloat(pname, (FloatBuffer) bytes.asFloatBuffer().put(params).flip());

        bytes.asFloatBuffer().get(params);
    }
}