package com.kinglogic.game.Interfaces;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by chris on 4/12/2018.
 */

public interface Controllable {
    void GoForeward();
    void GoBackward();
    void GoLeft();
    void GoRight();
    void RotateLeft();
    void RotateRight();
    void Enter(Controllable toCtrl);
    void Exit();
    void Activate();
    void FireMain();
    void FireAlt();
    void Deactivate();
    boolean isControlling();
    boolean isControlling(Controllable that);
    void setToControl(Controllable that);
    Actor GetView();

}
