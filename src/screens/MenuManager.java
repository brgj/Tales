package screens;

import helpers.Delegate;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 23/04/13
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class MenuManager implements ScreenManager {
    private Screen CurrentScreen;
    private Screen[] screens;

    public MenuManager() {
        initScreens();
    }

    @Override
    public void initScreens() {

        // Create delegate that will be invoked by screens that allows screen switching
        Delegate d = new Delegate() {
            @Override
            public void change(int val) {
                switchScreens(val);
            }
        };

        screens = new Screen[]{
                new MainMenuScreen(d),
                new SettingsScreen(d),
                new GameplayScreen(d, 5),
                new MultiplayerManager(d)};

        CurrentScreen = screens[0];
    }

    @Override
    public void switchScreens(int val) {
        CurrentScreen = screens[val];
        Initialize();
    }

    @Override
    public void Initialize() {
        CurrentScreen.Initialize();
        CurrentScreen.Update();
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
