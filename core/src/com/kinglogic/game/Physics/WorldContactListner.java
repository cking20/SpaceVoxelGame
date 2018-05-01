package com.kinglogic.game.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.kinglogic.game.AI.DestructoEnemy;
import com.kinglogic.game.AI.Enemy;
import com.kinglogic.game.Actors.Entities.Entity;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Managers.WorldManager;
import com.kinglogic.game.Player.PlayerBody;

/**
 * Created by chris on 4/12/2018.
 */

public class WorldContactListner implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
//        System.out.println("beginContact");
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if(a == null || a.getBody().getUserData() == null || b == null || b.getBody().getUserData() == null)
            return;

        if(a.isSensor()){
            if(isEntitySightOfEntity(a, b)){
                ((EntityBody)a.getUserData()).enterSight((EntityBody) b.getBody().getUserData());
                return;
            }
            if(isGroundSensorHittingGround(a,b)){
                ((PlayerBody)a.getBody().getUserData()).SetTouchingGround(true);
            }
        }else if(b.isSensor()){
            if(isEntitySightOfEntity(b, a)){
                ((EntityBody)b.getUserData()).enterSight((EntityBody) a.getBody().getUserData());
                return;
            }
            if(isGroundSensorHittingGround(b,a)){
                ((PlayerBody)b.getBody().getUserData()).SetTouchingGround(true);
            }
        } else {
            if(isEnemyHittingEnemy(a,b))
                return;

            if(isDestructoAIhittingStaticGrid(b,a))
                return;
            if(isDestructoAIhittingStaticGrid(a,b))
                return;

            if(isDestructoAIhittingDynGrid(b,a)) {
                handleRemoveGridVoxel(b, a, contact.getWorldManifold());
                return;
            }
            if(isDestructoAIhittingDynGrid(a,b)) {
                handleRemoveGridVoxel(a, b, contact.getWorldManifold());
                return;
            }

            if(isDynGridHittingDynGrid(a,b)){
                handleGridhitGridCollision(a,b,contact.getWorldManifold());
                return;
            }
            if(isDynGridHittingDynGrid(b,a)){
                handleGridhitGridCollision(b,a,contact.getWorldManifold());
                return;
            }
            if(isBulltetHittingEntity(a,b)){
                handleBulletEntityHit(a,b,contact.getWorldManifold());
                return;
            }
            if(isBulltetHittingEntity(b,a)){
                handleBulletEntityHit(b,a,contact.getWorldManifold());
                return;
            }


        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture a = contact.getFixtureA();
        Fixture b = contact.getFixtureB();
        if(a == null || a.getBody().getUserData() == null || b == null || b.getBody().getUserData() == null)
            return;

        if(a.isSensor()){
            if(isEntitySightOfEntity(a, b)){
                ((EntityBody)a.getUserData()).exitSight((EntityBody) b.getBody().getUserData());
                return;
            }
            if(isGroundSensorHittingGround(a,b)){
                ((PlayerBody)a.getBody().getUserData()).SetTouchingGround(false);
            }
        }else if(b.isSensor()){
            if(isEntitySightOfEntity(b, a)){
                ((EntityBody)b.getUserData()).exitSight((EntityBody) a.getBody().getUserData());
                return;
            }
            if(isGroundSensorHittingGround(b,a)){
                ((PlayerBody)b.getBody().getUserData()).SetTouchingGround(false);
            }
        }else {

        }


    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private void handleBulletEntityHit(Fixture a, Fixture b, WorldManifold m){
        WorldManager.ins().removeEntityFromWorld((Projectile)a.getBody().getUserData());
        WorldManager.ins().removeEntityFromWorld((EntityBody) b.getBody().getUserData());
        ResourceManager.ins().createExplosionEffect(m.getPoints()[0]);
        System.out.println("HITTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
    }
    private void handleRemoveGridVoxel(Fixture a, Fixture b, WorldManifold m){
//        System.out.println("handle that shit");
        Vector2[] points = m.getPoints();
        for(Vector2 p : points){
//            System.out.println(p);
            WorldManager.ins().removeVoxelWorldPosition(p.x,p.y);
        }

    }
    private void handleGridhitGridCollision(Fixture a, Fixture b, WorldManifold m){
        if(a.getBody().getLinearVelocity().len() < 10f &&  b.getBody().getLinearVelocity().len()< 10f) return;
        Vector2[] points = m.getPoints();
        for(Vector2 p : points){
//            System.out.println(p);
            WorldManager.ins().removeVoxelWorldPosition(p.x,p.y);
        }

    }
    private boolean isEnemyHittingEnemy(Fixture a, Fixture b){
        return a.getBody().getUserData() instanceof Enemy && b.getBody().getUserData() instanceof Enemy;
    }

    private boolean isBulltetHittingEntity(Fixture a, Fixture b){
        return a.getUserData() instanceof Projectile && b.getUserData() instanceof EntityBody;
    }

    private boolean isGroundSensorHittingGround(Fixture a, Fixture b){
        return a.getUserData().equals("ground") && b.getUserData() instanceof Grid;
    }

    private boolean isDestructoAIhittingDynGrid(Fixture a, Fixture b){
        if(a.getBody().getUserData() instanceof DestructoEnemy &&
                (b.getBody().getUserData() instanceof DynamicGrid ))
            return true;
        return false;
    }
    private boolean isDestructoAIhittingStaticGrid(Fixture a, Fixture b){
        if(a.getBody().getUserData() instanceof DestructoEnemy &&
                (b.getBody().getUserData() instanceof StaticGrid))
            return true;
        return false;
    }

    private boolean isEntitySightOfEntity(Fixture a, Fixture b){
        if(a.isSensor() && a.getUserData() instanceof EntityBody && b.getBody().getUserData() instanceof EntityBody){
            return true;
        }
        return false;
    }

    private boolean isDynGridHittingDynGrid(Fixture a, Fixture b){
        if(a.getBody().getUserData() instanceof DynamicGrid && b.getBody().getUserData() instanceof DynamicGrid){
            return true;
        }
        return false;
    }
}
