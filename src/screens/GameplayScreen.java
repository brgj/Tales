package screens;

import display.Camera;
import display.HUD;
import display.Ray;
import entity.Player;
import environment.Background;
import environment.Light;
import environment.Model;
import glmodel.GLModel;
import glmodel.GL_Vector;
import helpers.Delegate;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 23/04/13
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */

public class GameplayScreen extends Screen {
    Camera cam;
    Model model, model2, terrain;
    Player player;
    Background background;
    Light l;

    public GameplayScreen(Delegate d) {
        super(d);
        cam = new Camera(new Vector3f(0, 0, -20));
    }

    public void Initialize() {
        // Enable Texture 2D for texturing models
        glEnable(GL_TEXTURE_2D);
        // Enable depth testing for correct drawing order
        glEnable(GL_DEPTH_TEST);

        // Create a light
        float lightDirection[] = {-2f, 12f, 2f, 0f}; //direction/position
        float diffuse[] = {16f, 16f, 16f, 16f};
        float ambient[] = {56.6f, 56.6f, 56.9f, 57f};
        float specular[] = {6f, 6f, 6f, 6f};
        l = new Light(lightDirection, diffuse, ambient, specular);
        l.setLight();

        //Start Gamescreen music
        killAudio();
        try {
            audio = AudioLoader.getAudio("WAV", ResourceLoader.getResourceAsStream("music/36-VersusMode.wav"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (audioOn)
            audio.playAsMusic(1.0f, 1.0f, true);

        //Create Background
        background = new Background();
        //load the model
        player = new Player(new Model("data/Arwing/arwing.obj", 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -5.0f));
        model2 = new Model("data/DarkFighter/dark_fighter.obj", 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
        terrain = new Model("data/terrain/terrain.obj", 20.0f, 0.0f, 0.0f, 0.0f, 0.0f, -3.0f, -10.0f);
    }

    public void Render() {
        // Clear colour and depth buffers
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glMatrixMode(GL_PROJECTION);

        boolean a;
        // Push the 2D projection to stack
        glPushMatrix();
        {
            //region 3D stuff
            glLoadIdentity();
            // Specify the camera perspective
            GLU.gluPerspective(45.0f, (float) Display.getWidth() / (float) Display.getHeight(), .1f, 5000f);
            glMatrixMode(GL_MODELVIEW);

            // Rotate camera
            cam.setCameraView();

            background.drawSkybox(50.0f);

            //Render the model the camera is focusing
            glPushMatrix();
            {
                glLoadIdentity();
                player.Update();
                player.Render();
            }
            glPopMatrix();

            cam.getWorldTransform();

            float[] view = new float[16];

            ByteBuffer bytes = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder());

            glGetFloat(GL_MODELVIEW_MATRIX, (FloatBuffer) bytes.asFloatBuffer().put(view).flip());
            bytes.asFloatBuffer().get(view);

            //Draw other 3d models not focused by the camera
            glPushMatrix();
            {
                model2.render();

                Vector2f chPos = player.hud.crosshairPos;

                a = CheckPickingRay(chPos.x + Display.getWidth() / 2, -chPos.y + Display.getHeight() / 2, view);
//                a = CheckPickingRay(Mouse.getX(), Mouse.getY());
//                System.out.println("mouse= " + Mouse.getX() + ", " + Mouse.getY());
//                System.out.println("hud= " + (chPos.x + Display.getWidth() / 2) + ", " + (-chPos.y + Display.getHeight() / 2));
            }
            glPopMatrix();
            terrain.render();
            glMatrixMode(GL_PROJECTION);
            //endregion
        }
        glPopMatrix();


        //region 2D stuff
        player.hud.render(a);
        //endregion

    }

    public void Update() {
        rotate(0.2f, player.hud);

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Exit();
            return;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            moveXZ(0.2f, 90);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            moveXZ(0.2f, 270);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            moveXYZ(0.2f, 0);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            moveXYZ(0.2f, 180);
        }
    }

    protected void rotate(float mouseSpeed, HUD hud) {
        cam.rotate(mouseSpeed, hud);
    }

    protected void moveXZ(float units, int dir) {
        cam.move(units, dir);
    }

    protected void moveXYZ(float units, int dir) {
        cam.move(units, dir);
        cam.moveUp(units, dir);
    }

    protected void moveY(float units, int dir) {
        cam.moveUp(units, dir);
    }

    protected void Exit() {
        delegate.change(0);
    }

    private boolean CheckPickingRay(float x, float y, float[] view) {
        Ray ray = CalcPickingRay(x, y);

        Matrix4f mat = getMatrix4f(view);

        mat.invert();

        TransformRay(ray, mat);

        return CheckCollision(ray, model2);
    }

    private Matrix4f getMatrix4f(float[] array) {
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

    private Ray CalcPickingRay(float x, float y) {
        float px = 0.0f;
        float py = 0.0f;

        float[] proj = new float[16];

        ByteBuffer bytes = ByteBuffer.allocateDirect(64).order(ByteOrder.nativeOrder());

        glGetFloat(GL_PROJECTION_MATRIX, (FloatBuffer) bytes.asFloatBuffer().put(proj).flip());
        bytes.asFloatBuffer().get(proj);

        px = (((2.0f * x) / Display.getWidth()) - 1.0f) / proj[0];
        py = (((-2.0f * y) / Display.getHeight()) + 1.0f) / proj[5];

        Ray ray = new Ray();
        ray.origin = new Vector4f(0, 0, 0, 1);
        ray.direction = new Vector4f(px, py, 1, 0);

        return ray;
    }

    //TODO: Fix the -translation issue
    private boolean CheckCollision(Ray ray, Model model) {
        Matrix4f world = new Matrix4f();
        world.setIdentity();

        world.translate(model.getTranslation());
        world.rotate(model.getPitch(), new Vector3f(1, 0, 0));
        world.rotate(model.getYaw(), new Vector3f(0, 1, 0));

        world.invert();

        TransformRay(ray, world);

        return RaySphereIntTest(ray, model);
    }

    private boolean RaySphereIntTest(Ray ray, Model model) {
        // TODO: Associate bounding sphere with player class
        Vector3f center = model.getCenter();
        float radius = model.getRadius();

        radius *= 3.0f/4.0f;

        radius *= model.getScaleRatio();

        renderSphere(center, radius);

        Vector3f v = new Vector3f(ray.origin.x - center.x, ray.origin.y - center.y, ray.origin.z - center.z);

        float b = 2.0f * Vector3f.dot(new Vector3f(ray.direction.x, ray.direction.y, ray.direction.z), v);
        float c = Vector3f.dot(v, v) - (radius * radius);

        double discriminant = (b * b) - (4 * c);

        if (discriminant < 0.0)
            return false;

        discriminant = Math.sqrt(discriminant);

        double s0 = (-b + discriminant) / 2.0;
        double s1 = (-b - discriminant) / 2.0;

        return (s0 >= 0.0 || s1 >= 0.0);
    }

    public void renderSphere(Vector3f center, float radius) {
//        glPushMatrix();
//        glLoadIdentity();
//        glPushAttrib(GL_ALL_ATTRIB_BITS);
//        glDisable(GL_LIGHT1);
//        glDisable(GL_LIGHTING);
//        glDisable(GL_BLEND);
//        glDisable(GL_TEXTURE_2D);
//        glDisable(GL_DEPTH_TEST);

        Vector3f vec = model2.getTranslation();
        vec.scale(model2.getScaleRatio());
        glColor3f(1.0f, 1.0f, 1.0f);
        glBegin(GL_LINE_LOOP);
        {
            for (int i = 0; i < 360; i++) {
                double degInRad = i * Math.PI / 180;
                glVertex3f((float) (vec.x + center.x + Math.cos(degInRad) * radius), (float) (vec.y + center.y + Math.sin(degInRad) * radius), vec.z + center.z);
            }
        }
        glEnd();

        glBegin(GL_LINE_LOOP);
        {
            for (int i = 0; i < 360; i++) {
                double degInRad = i * Math.PI / 180;
                glVertex3f(vec.x + center.x, (float) (vec.y + center.y + Math.cos(degInRad) * radius), (float) (vec.z + center.z + Math.sin(degInRad) * radius));
            }
        }
        glEnd();
//        glPopAttrib();
//        glPopMatrix();
    }

    private void TransformRay(Ray ray, Matrix4f mat) {
        Matrix4f.transform(mat, ray.origin, ray.origin);
        Matrix4f.transform(mat, ray.direction, ray.direction);

        ray.direction.normalise();
    }
}