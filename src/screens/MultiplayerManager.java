package screens;

import helpers.Delegate;

/**
 * Created with IntelliJ IDEA.
 * User: Brad
 * Date: 5/2/13
 * Time: 12:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class MultiplayerManager extends Screen implements ScreenManager {
    private Screen CurrentScreen;
    private Screen[] screens;
    private IPPlaceHolder ph;

    public MultiplayerManager(Delegate d) {
        super(d);
        initScreens();
    }

    @Override
    public void initScreens() {

        Delegate d = new Delegate() {
            @Override
            public void change(int val) {
                switchScreens(val);
            }
        };

        ph = new IPPlaceHolder();

        screens = new Screen[]{
                new MultiMenuScreen(d),
                new MultiGameScreen(d),
                new MultiGameScreen(d, ph)
        };

        CurrentScreen = screens[0];
    }

    @Override
    public void switchScreens(int val) {
        CurrentScreen = screens[val];

        if (val == 0)
            delegate.change(0);
        else if(val == 2)
            ph.val = ((MultiMenuScreen)screens[0]).getIP();

        CurrentScreen.Initialize();
    }

    @Override
    public void Initialize() {
        CurrentScreen.Initialize();
    }

    @Override
    public void Render() {
        CurrentScreen.Render();
    }

    @Override
    public void Update() {
        CurrentScreen.Update();
    }
}
class IPPlaceHolder {
    public String val;
}