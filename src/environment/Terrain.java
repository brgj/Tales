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
    private Point min, max;

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
        if (mesh.numVertices == 0)
            return;

        float minX = mesh.vertices[0].pos.x * getScaleRatio() + transX;
        float maxX = minX;
        float minZ = mesh.vertices[0].pos.z * getScaleRatio() + transZ;
        float maxZ = minZ;

        for (int i = 0; i < mesh.numVertices; i++) {
            GL_Vector v = mesh.vertices[i].pos;

            float x = v.x * getScaleRatio() + transX;
            float y = v.y * getScaleRatio() + transY;
            float z = v.z * getScaleRatio() + transZ;

            Point p = new Point(Math.round(x), Math.round(z));
            if (!heightMap.containsKey(p) || y > heightMap.get(p)) {
                heightMap.put(p, y);
            }

            if (x < minX) minX = x;
            if (z < minZ) minZ = z;
            if (x > maxX) maxX = x;
            if (z > maxZ) maxZ = z;
        }
        min = new Point(Math.round(minX), Math.round(minZ));
        max = new Point(Math.round(maxX), Math.round(maxZ));
    }

    public boolean checkHeightMap(Vector3f vec, float radius) {
//        System.out.println(vec);
        return checkHeightMap(new Point((int) vec.x, (int) vec.z), vec.y, radius);
    }

    public boolean checkHeightMap(Point p, float y, float radius) {
        if (!heightMap.containsKey(p)) {
            if (outOfRange(p))
                return false;
            p = findClosestMatch(p);
        }
        return heightMap.get(p) > y - radius;
    }

    private boolean outOfRange(Point p) {
        return p.x > max.x || p.x < min.x || p.y > max.y || p.y < min.y;
    }

    private Point findClosestMatch(Point point) {
        float diff = Float.MAX_VALUE;
        Point result = point;
        for (Point p : heightMap.keySet()) {
            float temp = Math.abs(p.x - point.x) + Math.abs(p.y - point.y);
            if (temp < diff) {
                diff = temp;
                result = p;
            }
        }
        return result;
    }
}
