package Screens;

import helpers.Delegate;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: hp
 * Date: 23/04/13
 * Time: 3:31 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MenuScreen extends Screen {
    ArrayList<String> MenuOptions;

    public MenuScreen(Delegate d) {
        super(d);
    }
}
