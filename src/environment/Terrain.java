package environment;

import glmodel.GL_Mesh;
import glmodel.GL_Vector;
import org.lwjgl.util.vector.Vector3f;

import java.awt.*;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 5/15/13
 * Time: 11:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class Terrain extends Model {
    private HashMap<Point, Float> heightMap;
    private float minY;

    public Terrain(String filename) {
        super(filename);
        heightMap = new HashMap<Point, Float>();
        calcHeightMap();
    }

    public Terrain(String filename, float scaleRatio, float rotX, float rotY, float rotZ,
                   float transX, float transY, float transZ) {
        super(filename, scaleRatio, rotX, rotY, rotZ, transX, transY, transZ);
        heightMap = new HashMap<Point, Float>();
        calcHeightMap();
    }

    public void calcHeightMap() {
        GL_Mesh mesh = model.mesh;
        if (mesh.numTriangles == 0)
            return;

        minY = mesh.vertices[0].pos.y * getScaleRatio() + transY;

        for (int i = 0; i < mesh.numTriangles; i++) {
            GL_Vector p1 = mesh.triangles[i].p1.pos;
            GL_Vector p2 = mesh.triangles[i].p2.pos;
            GL_Vector p3 = mesh.triangles[i].p3.pos;
            fillHeightMap(p1, p2);
            fillHeightMap(p2, p3);
            fillHeightMap(p1, p3);
        }
    }

    public void fillHeightMap(GL_Vector start, GL_Vector end) {
        float x0 = start.x * getScaleRatio() + transX;
        float y0 = start.y * getScaleRatio() + transY;
        float z0 = start.z * getScaleRatio() + transZ;
        float x1 = end.x * getScaleRatio() + transX;
        float y1 = end.y * getScaleRatio() + transY;
        float z1 = end.z * getScaleRatio() + transZ;

        float dx = x1 - x0;
        float dy = y1 - y0;
        float dz = z1 - z0;

        float step = 1 / Math.max(Math.abs(dx), Math.abs(dz));

        for (float t = 0; t <= 1; t += step) {
            int x = Math.round(dx * t + x0);
            int z = Math.round(dz * t + z0);
            float y = y0 + t * dy;

            addPointToMap(x, y, z);
        }

        float tempY = Math.min(y0, y1);
        if (tempY < minY) minY = tempY;
    }

    public void addPointToMap(int x, float y, int z) {
        Point p = new Point(x, z);
        if (!heightMap.containsKey(p) || y > heightMap.get(p)) {
            heightMap.put(p, y);
        }
    }

    public boolean checkHeightMap(Vector3f vec, float radius) {
        return checkHeightMap(new Point((int) vec.x, (int) vec.z), vec.y, radius);
    }

    public boolean checkHeightMap(Point p, float y, float radius) {
        if (!heightMap.containsKey(p)) {
            p = findClosestMatch(p, radius);
            if (y - radius < minY)
                return true;
            else if (p == null)
                return false;
        }
        return heightMap.get(p) > y - radius;
    }

    private Point findClosestMatch(Point point, float radius) {
        float diff = Float.MAX_VALUE;
        Point result = point;
        for (Point p : heightMap.keySet()) {
            float temp = (float) Math.sqrt(Math.pow(p.x - point.x, 2) + Math.pow(p.y - point.y, 2));
            if (temp < diff) {
                diff = temp;
                result = p;
            }
        }
        if (diff > radius)
            return null;
        return result;
    }
}
