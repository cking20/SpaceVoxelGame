package com.kinglogic.game.Physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

import java.util.HashSet;

/**
 * Created by chris on 4/11/2018.
 */

public class PhysicsShape {
    public Shape shape;
    public FixtureDef fixtureDef;
    public Fixture fixture;


    public PhysicsShape(Shape s, Body b){
        shape = s;
        fixtureDef = new FixtureDef();
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit
        fixtureDef.shape = shape;
        fixture = b.createFixture(fixtureDef);

    }
}
