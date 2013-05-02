package screens;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 5/2/13
 * Time: 2:47 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ScreenManager {

    public void initScreens();

    public void switchScreens(int val);

    public void Initialize();

    public void Render();

    public void Update();

}
