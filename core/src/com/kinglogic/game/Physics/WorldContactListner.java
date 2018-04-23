package com.kinglogic.game.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.kinglogic.game.AI.DestructoEnemy;
import com.kinglogic.game.Managers.WorldManager;
import com.sun.org.apache.bcel.internal.classfile.Constant;

/**
 * Created by chris on 4/12/2018.
 */

public class WorldContactListner implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        System.out.println("beginContact");
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if(a == null || a.getBody().getUserData() == null || b == null || b.getBody().getUserData() == null)
            return;
        if(isDestructoAIhittingGrid(a,b))
            handleDestructoHitGrid(a,b,contact.getWorldManifold());
        if(isDestructoAIhittingGrid(b,a))
            handleDestructoHitGrid(b,a,contact.getWorldManifold());


    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private void handleDestructoHitGrid(Fixture a, Fixture b, WorldManifold m){
        System.out.println("handle that shit");
        Vector2[] points = m.getPoints();
        for(Vector2 p : points){
            WorldManager.ins().removeVoxelWorldPosition(p.x,p.y);
        }

    }

    private boolean isDestructoAIhittingGrid(Fixture a, Fixture b){
        if(a.getBody().getUserData() instanceof DestructoEnemy &&
                (b.getBody().getUserData() instanceof DynamicGrid || b.getBody().getUserData() instanceof StaticGrid))
            return true;
        return false;
    }
}
