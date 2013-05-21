package screens;

import display.Camera;
import display.HUD;
import display.Ray;
import entity.Enemy;
import entity.Entity;
import entity.LaserBeam;
import entity.Player;
import environment.*;
import helpers.Delegate;
import helpers.GLHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
    Terrain terrain;
    Player player;
    Background background;
    Light l;
    Laser laser1;
    ArrayList<LaserBeam> lasers;
    Explosion ex;
    HashMap<Byte, Enemy> enemies = new HashMap<Byte, Enemy>();

    public GameplayScreen(Delegate d) {
        super(d);
        cam = new Camera(new Vector3f(0, 0, 0));
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
        player = new Player(new Model("data/Arwing/arwing.obj", 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, -.7f, -5.0f));
        enemies = new HashMap<Byte, Enemy>();
        enemies.put((byte) -1, new Enemy(new Model("data/DarkFighter/dark_fighter.obj", 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f), new Vector3f(0, 0, 0)));
        terrain = new Terrain("data/terrain/terrain.obj", 20, 0.0f, 0.0f, 0.0f, 0.0f, -10.0f, 0.0f);
        laser1 = new Laser(cam);

        lasers = new ArrayList<LaserBeam>();

        enemies.get((byte) -1).Initialize();

        ex = new Explosion();
    }

    //TODO: get rid of these
    static boolean tempEnemyCollide = false;
    static boolean tempTerrainCollide = false;

    public void Render() {
        // Clear colour and depth buffers
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glMatrixMode(GL_PROJECTION);

        // true if enemy is in crosshairs
        boolean enemyInTarget = false;
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

            //Render Background
            background.drawSkybox(50.0f);

            //Render the model the camera is focusing
            glPushMatrix();
            {
                glLoadIdentity();
                player.Render();
                Vector3f vec;
                if (tempEnemyCollide) {
                    vec = new Vector3f(0.0f, 1.0f, 0.0f);
                } else if (tempTerrainCollide) {
                    vec = new Vector3f(0.0f, 0.0f, 1.0f);
                } else {
                    vec = new Vector3f(1.0f, 1.0f, 1.0f);
                }
                GLHelper.renderSphere(player.center, player.radius, vec);
                //laser1.render();
            }
            glPopMatrix();

            //Translate camera
            cam.setCameraPosition();

            glPushMatrix();
            {
                terrain.transform();
                terrain.render();
            }
            glPopMatrix();

            Vector2f chPos = player.hud.crosshairPos;

            for (LaserBeam e : lasers) {
                 GLHelper.renderSphere(new Vector3f(-e.getPosition().x, -e.getPosition().y, -e.getPosition().z), e.radius, new Vector3f(1, 0, 0));
            }

            //Draw other 3d models not focused by the camera and check for intersection with crosshairs
            for (Enemy enemy : enemies.values()) {
                glPushMatrix();
                {
                    enemy.Render();
//                    GLHelper.renderSphere(enemy.center, enemy.radius, new Vector3f(1.0f, 1.0f, 1.0f));
                    if (!enemyInTarget)
                        enemyInTarget = CheckPickingRay(chPos.x + Display.getWidth() / 2, chPos.y + Display.getHeight() / 2, enemy);
//                    enemy.setTarget(cam.getPosition());
                }
                glPopMatrix();
            }
            glPushMatrix();
            {
                for (Iterator<LaserBeam> i = lasers.iterator(); i.hasNext(); ) {
                    LaserBeam laser = i.next();
                    if (laser.isExpired) {
                        i.remove();
                    } else {
                        laser.Render();
                    }
                }
            }
            glPopMatrix();
            tempEnemyCollide = false;
            glPushMatrix();
            {
                for (Enemy e : enemies.values()) {
                    //glLoadIdentity();
                    if (CheckCollision(e)) {
                        //DO STUFF
                        ex.drawExplosion();
                        tempEnemyCollide = true;
                        crash();
                        if (player.health > 0) {
                            player.health -= .01f;
                        }
                        else if(player.health < 0)
                        {
                            player.health = 0;
                        }
                    }
                }
            }
            glPopMatrix();
            glPushMatrix();
            {
                for (LaserBeam e : lasers) {
                    if (CheckCollision2(e, enemies.get((byte) -1))) {
                        System.out.print("HIT!");
                        ex.drawExplosion();
                        ex.reset();
                    }
                }
            }
            glPopMatrix();
            glPopMatrix();
            glMatrixMode(GL_PROJECTION);
            //endregion
        }
        glPopMatrix();

        player.hud.render(enemyInTarget, player.health);

        glMatrixMode(GL_MODELVIEW);

        //tempEnemyCollide = false;

        //Check collisions
        /*for (Enemy e : enemies.values()) {
            //glLoadIdentity();
            if (CheckCollision(e)) {
                //DO STUFF
                System.out.println("collision");
                ex.drawExplosion();
                tempEnemyCollide = true;
                crash();
                if (player.health > 0) {
                    player.health -= .01f;
                }
                else if(player.health < 0)
                {
                    player.health = 0;
                }
            }
        }               */

        //Laser collision TESTING WITH ENEMY
        /*for (LaserBeam e : lasers) {
            if (CheckCollision2(e, enemies.get((byte) -1))) {
                System.out.print("HIT!");
            }
        }  */

        Vector3f playerPos = new Vector3f();
        Vector3f.sub(player.offset, cam.getPosition(), playerPos);
        if (terrain.checkHeightMap(playerPos, player.radius)) {
            tempTerrainCollide = true;
            crash();
        } else {
            tempTerrainCollide = false;
        }
    }


    public void Update() {
        rotate(0.2f, player.hud);
        player.Update();
        player.setOffset(cam.getYaw(), cam.getPitch(), -player.model.getPosition().z);

        //Update Lasers
        for (LaserBeam laser : lasers) {
            laser.Update();
        }
        //Update Enemies
        for (Enemy enemy : enemies.values()) {
            enemy.Update();
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Exit();
            return;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            LaserBeam temp = new LaserBeam(cam, player.offset, player.hud);
            lasers.add(temp);
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
        if (Keyboard.next()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
                cam.moveUp(10, 90);
            }
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

    private void crash() {
        moveXYZ(5.0f, 180);
        player.crash();
    }

    protected void Exit() {
        delegate.change(0);
    }

    /**
     * Calculate a picking ray and check if it intersects with an enemy
     *
     * @param x
     * @param y
     * @param enemy
     * @return
     */
    private boolean CheckPickingRay(float x, float y, Enemy enemy) {
        Ray ray = CalcPickingRay(x, y);

        return RaySphereIntTest(ray, enemy);
    }

    /**
     * Calculate the picking ray based on x and y screen coordinates
     *
     * @param x
     * @param y
     * @return
     */
    private Ray CalcPickingRay(float x, float y) {
        float px;
        float py;

// Retrieve the projection matrix from OpenGL
        float[] proj = new float[16];
        GLHelper.glGetFloatv(GL_PROJECTION_MATRIX, proj);

// Use the scaling in the projection matrix to adjust the x and y positions
        px = (((2.0f * x) / Display.getWidth()) - 1.0f) / proj[0];
        py = (((-2.0f * y) / Display.getHeight()) + 1.0f) / proj[5];

// Create the Ray with an initial position of 0 and the direction of the mouse click
        Ray ray = new Ray();
        ray.origin = new Vector4f(0, 0, 0, 1);
        ray.direction = new Vector4f(px, py, 1, 0);

// Get the current camera coords + enemy coords and invert them
        Matrix4f world = GLHelper.getInverseModelViewMatrix();

// Transform the ray by the inverse of the absolute world coords of the enemy
        TransformRay(ray, world);

        return ray;
    }

    /**
     * Check if ray is intersecting with an enemy's hit sphere
     *
     * @param ray
     * @param enemy
     * @return
     */
    private boolean RaySphereIntTest(Ray ray, Enemy enemy) {
        // Create a vector from the center of the enemy to the origin of the ray
        Vector3f v = new Vector3f(ray.origin.x - enemy.center.x, ray.origin.y - enemy.center.y, ray.origin.z - enemy.center.z);

// Form the quadratic equation, leaving out A because u is normalized, so A = u * u = 1
        float b = 2.0f * Vector3f.dot(new Vector3f(ray.direction.x, ray.direction.y, ray.direction.z), v); // B=2*(u.v)
        float c = Vector3f.dot(v, v) - (enemy.radius * enemy.radius); // C=v.v-r^2

// calc the discriminant of the quadratic
        double discriminant = (b * b) - (4 * c);

// If we are trying to sqrt a negative, i.e. number is imaginary, return false
        if (discriminant < 0.0)
            return false;

        discriminant = Math.sqrt(discriminant);

// Find both solutions to the quadratic
        double s0 = (-b + discriminant) / 2.0;
        double s1 = (-b - discriminant) / 2.0;

// If at least one is positive, intersection has occurred
        return (s0 >= 0.0 || s1 >= 0.0);
    }

    /**
     * Transform the ray origin and direction by a matrix and normalize the result
     *
     * @param ray
     * @param mat
     */
    private void TransformRay(Ray ray, Matrix4f mat) {
        Matrix4f.transform(mat, ray.origin, ray.origin);
        Matrix4f.transform(mat, ray.direction, ray.direction);
        ray.direction.normalise();
    }

    /**
     * Check if current player is colliding with another entity
     */
    private boolean CheckCollision(Entity entity) {
        // Get the position of the player and entity and create a vector from the player to the entity
        Vector3f entityPos = new Vector3f();
        Vector3f.add(entity.offset, entity.getPosition(), entityPos);

        Vector3f playerPos = new Vector3f();
        Vector3f.sub(player.offset, cam.getPosition(), playerPos);

        Vector3f v = new Vector3f();
        Vector3f.sub(playerPos, entityPos, v);

// Calculate the magnitude of vector v as the distance and create a minimum acceptable distance for collision
        float dist = v.x * v.x + v.y * v.y + v.z * v.z;
        float minDist = player.radius + entity.radius;

        return dist <= minDist * minDist;
    }

    //TODO: Delete this later, testing
    private boolean CheckCollision2(Entity entity, Entity entity2) {
        // Get the position of the player and entity and create a vector from the player to the entity
        Vector3f entityPos = entity.getPosition();

        Vector3f entity2Pos = entity2.getPosition();


        Vector3f v = new Vector3f();
        Vector3f.sub(entity2Pos, entityPos, v);

// Calculate the magnitude of vector v as the distance and create a minimum acceptable distance for collision
        float dist = v.x * v.x + v.y * v.y + v.z * v.z;
        float minDist = entity2.radius + entity.radius;

        return dist <= minDist * minDist;
    }

}
