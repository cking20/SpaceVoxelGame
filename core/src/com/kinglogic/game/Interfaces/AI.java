package com.kinglogic.game.Interfaces;

/**
 * Created by chris on 4/12/2018.
 */

public interface AI extends Controllable {
    void Think();
    void Speak();
    void SetPiloting(Controllable c);
    Controllable getPiloting();

}
