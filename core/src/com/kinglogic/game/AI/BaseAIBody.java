package com.kinglogic.game.AI;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.kinglogic.game.Interfaces.AI;
import com.kinglogic.game.Interfaces.Controllable;
import com.kinglogic.game.Physics.EntityBody;

import java.util.ArrayList;

/**
 * Created by chris on 4/17/2018.
 */

public class BaseAIBody extends EntityBody implements AI {
    public BaseAIBody(String name, Vector2 position) {
        super(name, position);

    }

    @Override
    public void Think(World perception) {
        Array<Body> pr = new Array<Body>();
        perception.getBodies(pr);

        GoForeward();
        RotateLeft();


    }

    @Override
    public String Speak() {
        return "no words";
    }

    @Override
    public void SetPiloting(Controllable c) {

    }

    @Override
    public Controllable getPiloting() {
        return null;
    }
}
