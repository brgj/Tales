package helpers;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 24/04/13
 * Time: 1:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class TextureHelper {
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
}
