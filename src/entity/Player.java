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
    public float health;
    private boolean crashed, posTilt;
    private int crashTilt;

    public Player(Model model)
    {
        super(model);
        hud = new HUD();
        posTilt = false;
        crashTilt = 0;
    }

    public void Render()
    {
        super.Render();
    }

    public void Update()
    {
        model.updateRotation(-hud.crosshairPos.y * .1f, -hud.crosshairPos.x * .1f, -hud.crosshairPos.x * .1f);
        if(crashed)
            doCrash();
    }

    public void crash() {
        crashed = true;
    }

    private void doCrash() {
        int maxTilt = 30;
        int step = 10;

        if(crashTilt == maxTilt)
            posTilt = false;
        else if(crashTilt == -maxTilt)
            posTilt = true;

        crashTilt += posTilt ? step : -step;

        if(posTilt && crashTilt == 0)
            crashed = false;

        float crashPitch = crashTilt > 0 ? crashTilt : 0;

        model.updateRotation(crashPitch, 0.0f, crashTilt);
    }

    public void Initialize()
    {
        super.Initialize();
        health = 1f;
    }

}
