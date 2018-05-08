package com.kinglogic.game.AI;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.kinglogic.game.Interfaces.AI;
import com.kinglogic.game.Interfaces.Controllable;
import com.kinglogic.game.Physics.EntityBody;
import com.kinglogic.game.Physics.PhysicsShape;

import java.util.ArrayList;

/**
 * Created by chris on 4/17/2018.
 */

public class BaseAIBody extends EntityBody implements AI {
    public EntityBody targeting;

    public BaseAIBody(String name, Vector2 position) {
        super(name, position);
        this.viewDistance = 400f;

    }

    @Override
    public void Think(World perception) {
        Array<Body> pr = new Array<Body>();
        perception.getBodies(pr);

    }
    @Override
    public void CreateFixture(){
        super.CreateFixture();
        physicsShape.fixture.setFriction(0f);

    }
    @Override
    public void enterSight(EntityBody seeing){
        perceptions.add(seeing);
        targeting = seeing;
    }
    @Override
    public void exitSight(EntityBody seeing){
        perceptions.remove(seeing);
        if(!perceptions.isEmpty())
            targeting = perceptions.get(0);
        else
            targeting = null;
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
