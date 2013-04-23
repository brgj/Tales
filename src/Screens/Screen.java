package Screens;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 23/04/13
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class  Screen{

    public String ScreenName;

    public Screen(){}

    public Screen (String name){
        this.ScreenName = name;
    }

    //region Update / Render

    public void Render(){}

    public void Update(){}

    //endregion

    //region Other Methods

    public void SetScreenName(String name)
    {
        this.ScreenName = name;
    }

    //endregion
}

