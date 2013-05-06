package entity;

import display.HUD;
import environment.Model;

/**
 * Created with IntelliJ IDEA.
 * User: Owner
 * Date: 02/05/13
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class Player extends Entity {

    public HUD hud;

    public Player(Model model)
    {
        super(model);
        hud = new HUD();
    }

    public void Render()
    {
        super.Render();
    }

    public void Update()
    {
        model.updateRotation(-hud.crosshairPos.y * .1f, -hud.crosshairPos.x * .1f, -hud.crosshairPos.x * .1f);
    }

    public void Initialize()
    {

    }
}
