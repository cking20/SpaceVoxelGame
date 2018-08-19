package com.kinglogic.game.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.kinglogic.game.Actors.Entities.Entity;
import com.kinglogic.game.Managers.ResourceManager;

import java.util.HashSet;

/**
 * Created by chris on 4/11/2018.
 */

//todo just use a builder pattern jezzzz
public class PhysicsShape {
    public Shape shape;
    public FixtureDef fixtureDef;
    public Fixture fixture;

    public static float density = .5f;
    public static float friction = .5f;
    public static float restitution = .1f;


    public PhysicsShape(Shape s, Body b){
        shape = s;
        fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.shape = shape;
        fixture = b.createFixture(fixtureDef);

    }

    public PhysicsShape(Body b, Entity view, float radius){
        CircleShape c = ResourceManager.ins().getNewCircleShape();
        c.setRadius(radius);
        c.setPosition(new Vector2(view.getWidth()/2, view.getHeight()/2));
        shape = c;
        shape.setRadius(radius);
        fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.shape = shape;
        fixture = b.createFixture(fixtureDef);

    }

    public PhysicsShape(Body b, Vector2 pos, float radius){
        CircleShape c = ResourceManager.ins().getNewCircleShape();
        c.setRadius(radius);
        c.setPosition(pos);
        shape = c;
        shape.setRadius(radius);
        fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.shape = shape;
        fixture = b.createFixture(fixtureDef);

    }

    public PhysicsShape(Body b, Entity view){
        PolygonShape ps = ResourceManager.ins().getNewPolyShape();
        ps.setAsBox(view.getWidth()/2-1f,view.getHeight()/2-1f, new Vector2(view.getWidth()/2, view.getHeight()/2),view.getRotation());

        shape = ps;

        fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.shape = shape;
        fixture = b.createFixture(fixtureDef);
    }
    public PhysicsShape(Body b, float height, float width, Vector2 center, float rotation){
        PolygonShape ps = ResourceManager.ins().getNewPolyShape();
        ps.setAsBox(height,width, center, rotation);
        shape = ps;
        fixtureDef = new FixtureDef();
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.shape = shape;
        fixture = b.createFixture(fixtureDef);

    }
}
