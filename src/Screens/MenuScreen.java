package Screens;

import helpers.Delegate;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
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
    protected int selectedIndex;
    protected int lastKeyPressed;
    protected TrueTypeFont font;

    public MenuScreen(Delegate d) {
        super(d);
        Font awtFont = new Font("Verdana", Font.PLAIN, 24);
        font = new TrueTypeFont(awtFont, false);
        selectedIndex = 0;
        lastKeyPressed = -1;
    }

    //TODO: make method here that deals with rendering background image.

    //TODO: make method here that deals with rendering the list of texts

    //TODO: make method here that deals with updating the selected option
}
