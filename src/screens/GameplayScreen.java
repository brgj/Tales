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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 23/04/13
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */

public class GameplayScreen extends Screen {
    //TODO: get rid of these
    static boolean tempEnemyCollide = false;
    static boolean tempTerrainCollide = false;
    static boolean lastFired = false;
    Vector3f playerPos;
    Camera cam;
    Terrain terrain;
    Model mountain;            // surround boundary and doesnt do anything
    Player player;
    Background background;
    Light l;
    ArrayList<LaserBeam> lasers;
    ArrayList<Explosion> explosions;
    HashMap<Byte, Enemy> enemies = new HashMap<Byte, Enemy>();
    int numEnemies;

    public GameplayScreen(Delegate d, int numEnemies) {
        super(d);
        this.numEnemies = numEnemies;
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
        enemies = new HashMap<Byte, Enemy>();
        terrain = new Terrain("data/terrain/terrain.obj", 20, 0.0f, 0.0f, 0.0f, 0.0f, -10.0f, 0.0f);
        mountain = new Model("data/terrain/mountain.obj", 20, 0.0f, 0.0f, 0.0f, 0.0f, -10.2f, 0.0f);
        //mountain = new Model("data/terrain/mountain.obj", 20, 0.0f, 0.0f, 0.0f, 0.0f, -10.2f, 0.0f);

        lasers = new ArrayList<LaserBeam>();
        explosions = new ArrayList<Explosion>();

        player.Initialize();
        Vector3f temp = randomizePosition();
        cam = new Camera(new Vector3f(temp.x, -temp.y, temp.z));
        cam.initializePitchYaw();

        for(int i = 0; i < numEnemies; i++) {
            spawnEnemy();
        }
    }

    public void Render() {
        // Clear colour and depth buffers
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        glMatrixMode(GL_PROJECTION);

        //Player position accounting for offset and camera position
        playerPos = new Vector3f();
        Vector3f.sub(player.offset, cam.getPosition(), playerPos);
        player.fatalCrashPos = new Vector3f(playerPos);

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
            if (player.state != Entity.State.Dead) {
                glPushMatrix();
                {
                    glLoadIdentity();
                    player.Render();
//                    Vector3f vec;
//                    if (tempEnemyCollide) {
//                        vec = new Vector3f(0.0f, 1.0f, 0.0f);
//                    } else if (tempTerrainCollide) {
//                        vec = new Vector3f(0.0f, 0.0f, 1.0f);
//                    } else {
//                        vec = new Vector3f(1.0f, 1.0f, 1.0f);
//                    }
//
//                    float scale = player.model.getScaleRatio();
//                    glScalef(1.0f / scale, 1.0f / scale, 1.0f / scale);
//                    GLHelper.renderSphere(player.center, player.radius, vec);
                }
                glPopMatrix();
            }

            //Translate camera
            cam.setCameraPosition();

            glPushMatrix();
            {
                terrain.transform();
                terrain.render();
            }
            glPopMatrix();
            //Render active lasers and perform cleanup
            glPushMatrix();
            {
                for (Iterator<LaserBeam> i = lasers.iterator(); i.hasNext(); ) {
                    LaserBeam laser = i.next();
                    GLHelper.renderSphere(new Vector3f(laser.getPosition().x, laser.getPosition().y, laser.getPosition().z),
                            laser.radius, new Vector3f(0, 0, 1));
                    if (laser.isExpired) {
                        i.remove();
                    } else {
                        laser.Render();
                    }
                }
            }
            glPopMatrix();

            Vector2f chPos = player.hud.crosshairPos;

            tempEnemyCollide = false;

            //Draw other 3d models not focused by the camera and check for intersection with crosshairs
            HashMap<Byte, Enemy> tempEnemies = new HashMap<Byte, Enemy>();
            tempEnemies.putAll(enemies);
            for (Byte id : tempEnemies.keySet()) {
                Enemy enemy = tempEnemies.get(id);
                glPushMatrix();
                {
                    enemy.Render();
                    if (!enemyInTarget)
                        enemyInTarget = CheckPickingRay(chPos.x + Display.getWidth() / 2, chPos.y + Display.getHeight() / 2, enemy);
                    if (enemy.isAI())
                        enemy.setTarget(playerPos);
                    if (CheckCollisionWithPlayer(enemy)) {
                        //Create explosion on collision
                        Explosion ex = new Explosion(.5f, .01f, playerPos);
                        explosions.add(ex);
                        tempEnemyCollide = true;
                        crash(2);
                        player.lastHitBy = id;
                        if (player.health > 0)
                            player.setHealth(.21f);
                        if(enemy.isAI()) {
                            spawnEnemy(enemy);
                        }
                    }

                    //Check collisions with active lasers
                    for (LaserBeam laser : lasers) {
                        if (enemies.get(laser.ownerID) != enemy && CheckCollision2(laser.getPosition(), enemy.getPosition(),
                                laser.radius, enemy.radius)) {
                            //Create explosion on collision and delete laser
                            Explosion ex = new Explosion(.5f, .01f, enemy.getPosition());
                            explosions.add(ex);
                            laser.isExpired = true;
                            if(enemy.isAI()) {
                                player.score++;
                                spawnEnemy(enemy);
                            }
                        }
                    }

                    // Check collision with terrain
                    if (terrain.checkHeightMap(enemy.getPosition(), enemy.radius) != 0) {
                        Explosion ex = new Explosion(.5f, .01f, enemy.getPosition());
                        explosions.add(ex);
                        if(enemy.isAI()) {
                            spawnEnemy(enemy);
                        }
                    }

                    float scale = enemy.model.getScaleRatio();
                    glScalef(1.0f / scale, 1.0f / scale, 1.0f / scale);
                    GLHelper.renderSphere(enemy.center, enemy.radius, new Vector3f(1.0f, 0.0f, 0.0f));
                }
                glPopMatrix();
            }

            // Player laser collision
            for (LaserBeam laser : lasers) {
                if (laser.ownerID != 16 && CheckCollision2(laser.getPosition(), playerPos, laser.radius, player.radius)) {
                    //Create explosion on collision and delete laser
                    Explosion ex = new Explosion(.5f, .01f, playerPos);
                    explosions.add(ex);
                    laser.isExpired = true;

                    crash(0);
                    player.lastHitBy = laser.ownerID;
                    if (player.health > 0)
                        player.setHealth(.21f);
                }
            }

            //Terrain Collision
            int val = terrain.checkHeightMap(playerPos, player.radius);
            if (val != 0) {
                tempTerrainCollide = true;
                Explosion ex = new Explosion(.5f, .01f, playerPos);
                explosions.add(ex);
                crash(val);
                if(player.state != Entity.State.FatalCrash)
                    player.lastHitBy = -1;
                if (player.health > 0)
                    player.setHealth(.21f);
            } else {
                tempTerrainCollide = false;
            }

            //Render active Explosions
            glPushMatrix();
            {
                for (Iterator<Explosion> i = explosions.iterator(); i.hasNext(); ) {
                    Explosion explosion = i.next();
                    if (explosion.getAlpha() <= 0) {
                        i.remove();
                    } else {
                        explosion.render();
                    }
                }
            }

            int boundaryHit = terrain.checkBoundary(playerPos);
            if(boundaryHit != -1)
            {
                int destination;
                float yaw = cam.getYaw();

                if(yaw < 0)
                    yaw = 360 - (yaw * -1);

                player.turning = true;
                player.state = Entity.State.Invincible;
                destination = (boundaryHit + 180) % 360;
                if(destination == 0)
                    destination = 1;

                if(yaw < boundaryHit || (boundaryHit == 0 && yaw >= 270))
                    destination *= -1;
                player.boundaryDirection = destination;
            }

            glPopMatrix();

            glMatrixMode(GL_PROJECTION);
            //endregion
        }
        glPopMatrix();

        player.hud.render(enemyInTarget, player.health, player.score);
    }

    public void Update() {
        moveXYZ(0.7f, 0);

        //Logic to handle camera movement in different states of animation / gameplay
        if ((player.state == Entity.State.Invincible || player.state == Entity.State.Alive) && !player.turning) {
            rotate(0.005f, player.hud);
        }
        //Camera follows ship slightly down when destroyed
        else if (player.state == Entity.State.FatalCrash) {
            cam.setPitch(.2f);
            if (tempTerrainCollide || player.model.pitch < -90) {
                player.state = Entity.State.Dead;
                Explosion ex = new Explosion(.5f, .05f, player.fatalCrashPos);
                explosions.add(ex);
                player.score--;
                spawnPlayer();
            }
        }
        //Logic to handle boundaries
        else if(player.turning)
        {
            if(checkCorrectDirection())
            {
                player.hud.crosshairReset();
                if(terrain.checkBoundary(playerPos) == -1)
                {
                    player.state = Entity.State.Alive;
                    player.turning = false;
                }
            }
            else
            {
                cam.setYaw(.005f * player.hud.crosshairPos.x);
                cam.setPitch(-cam.getPitch());
            }
        }

        player.Update();
        player.setOffset(cam.getYaw(), cam.getPitch(), new Vector3f(player.model.getPosition().x, player.model.getPosition().y, -player.model.getPosition().z));

        //Update Lasers
        for (LaserBeam laser : lasers) {
            laser.Update();
        }
        //Update Enemies
        for (byte id : enemies.keySet()) {
            Enemy enemy = enemies.get(id);
            enemy.Update();
            if (enemy.isAI() && enemy.ai.isShooting) {
                enemy.ai.resetTimer();
                LaserBeam temp = new LaserBeam(
                        new Vector3f(-enemy.model.transX, -enemy.model.transY, -enemy.model.transZ),
                        enemy.model.yaw,
                        enemy.model.pitch,
                        id, -1);
                lasers.add(temp);
            }
        }

        //Player input
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            Exit();
            return;
        }
        if ((player.state == Entity.State.Alive || player.state == Entity.State.Invincible) && !player.turning) {
            if (Mouse.isButtonDown(0)) {
                if (!lastFired)
                    shootLaser();
                lastFired = true;
            } else {
                lastFired = false;
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
                moveXZ(0.3f, 0);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                moveXZ(0.3f, 90);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                moveXZ(0.3f, 270);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
                moveXYZ(0.3f, 180);
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                player.barrelRoll();
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

    protected void shootLaser() {
        LaserBeam temp = new LaserBeam(cam, player.offset, player.hud);
        temp.ownerID = 16;
        lasers.add(temp);
    }

    private void crash(int val) {
        if (val == 1) {
            cam.setPitch(-20);
        } else if (val == 2) {
            if (cam.getYaw() > 0)
                cam.setYaw(45);
            else
                cam.setYaw(-45);
            moveXYZ(5.0f, 180);
        }
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

        discriminant += HUD.TARGETY / 2;

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
    private boolean CheckCollisionWithPlayer(Entity entity) {
        // Get the position of the player and entity and create a vector from the player to the entity
        Vector3f playerPos = new Vector3f();
        Vector3f.sub(player.offset, cam.getPosition(), playerPos);

        Vector3f v = new Vector3f();
        Vector3f.sub(playerPos, entity.getPosition(), v);

        // Calculate the magnitude of vector v as the distance and create a minimum acceptable distance for collision
        float dist = v.x * v.x + v.y * v.y + v.z * v.z;
        float minDist = player.radius + entity.radius;

        return dist <= minDist * minDist;
    }

    //TODO: Delete this later, testing
    private boolean CheckCollision2(Vector3f entityPos, Vector3f entity2Pos, float entityRadius, float entity2Radius) {
        // Get the position of the player and entity and create a vector from the player to the entity

        Vector3f v = new Vector3f();
        Vector3f.sub(entityPos, entity2Pos, v);

        // Calculate the magnitude of vector v as the distance and create a minimum acceptable distance for collision
        float dist = v.x * v.x + v.y * v.y + v.z * v.z;
        float minDist = entityRadius + entity2Radius;

        return dist <= minDist * minDist;
    }

    private Vector3f randomizePosition(float maxX, float maxZ, float minX, float minZ) {
        Random rand = new Random();
        int wallNum = rand.nextInt(4);
        float x = rand.nextInt((int) maxX - 25) + 25,
                y = 30,
                z = rand.nextInt((int) maxZ - 25) + 25;
        switch (wallNum) {
            case 0:
                z = minZ;
                break;
            case 1:
                x = maxX;
                break;
            case 2:
                z = maxZ;
                break;
            case 3:
                x = minX;
                break;
        }
        return new Vector3f(x, y, z);
    }

    private Vector3f randomizePosition() {
        return randomizePosition(terrain.getMaxX(), terrain.getMaxZ(), terrain.getMinX(), terrain.getMinZ());
    }

    private void spawnEnemy(Enemy enemy) {
        enemy.Initialize();
        Vector3f temp = randomizePosition();
        enemy.model.updatePosition(temp.x, temp.y, temp.z);
    }

    private void spawnEnemy() {
        Enemy enemy = new Enemy();
        enemy.Initialize();
        Vector3f temp = randomizePosition();
        enemy.model.updatePosition(temp.x, temp.y, temp.z);
        enemies.put((byte)-(enemies.size() + 1), enemy);
    }

    protected void spawnPlayer() {
        player.Initialize();
        Vector3f temp = randomizePosition();
        cam = new Camera(new Vector3f(temp.x, -temp.y, temp.z));
        cam.initializePitchYaw();
    }

    //Check if the camera is facing the correct direction from OOB. Accounts for approximations and special case
    private boolean checkCorrectDirection()
    {
        float yaw = cam.getYaw();

        if(yaw < 0)
            yaw = 360 - (yaw * -1);

        if(Math.abs(player.boundaryDirection) == 1)
        {
            if(yaw > 350 || yaw < 10)
                return true;
            else
                return false;
        }

        else if(yaw > Math.abs(player.boundaryDirection) - 10 && yaw < Math.abs(player.boundaryDirection) + 10)
        {
            return true;
        }
        return false;
    }
}

