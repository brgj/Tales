package screens;

import helpers.Delegate;
import org.newdawn.slick.openal.Audio;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 23/04/13
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Screen {

    protected Delegate delegate;
    protected static Audio audio;

    public Screen(Delegate d) {
        delegate = d;
    }

    abstract public void Initialize();

    abstract public void Render();

    abstract public void Update();

    public static void killAudio() {
        if(audio != null) {
            audio.release();
            audio = null;
        }
    }

}
