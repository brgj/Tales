package Screens;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 23/04/13
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScreenManager {
    private Screen CurrentScreen;
    private MainMenuScreen MainMenu;
    private GameplayScreen GameScreenSingle;
    private GameplayScreen GameScreenMulti;
    private SettingsScreen SettingsMenu;

    public void Initialize(){
        CurrentScreen.Initialize();
    }

    public void Render(){
        CurrentScreen.Render();
    }

    public void Update(){
        CurrentScreen.Update();
    }


}
