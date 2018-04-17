package com.kinglogic.game.Interfaces;

import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by chris on 4/12/2018.
 */

public interface AI extends Controllable {
    void Think(World w);
    String Speak();
    void SetPiloting(Controllable c);
    Controllable getPiloting();

}
