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
import com.kinglogic.game.ChemestryFramework.ChemicalEvent;
import com.kinglogic.game.ChemestryFramework.ChemistryManager;
import com.kinglogic.game.ChemestryFramework.MaterialModel;
import com.kinglogic.game.Managers.GameManager;
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

        if(!handleBeginContacts(a,b, contact))
            handleBeginContacts(b,a, contact);
//
//
//        if(a.isSensor()){
//            if(isEntitySightOfEntity(a, b)){
//                ((EntityBody)a.getUserData()).enterSight((EntityBody) b.getBody().getUserData());
//                return;
//            }
//            if(isGroundSensorHittingGround(a,b)){
//                ((PlayerBody)a.getBody().getUserData()).SetTouchingGround(true);
//
//            }
//        }else if(b.isSensor()){
//            if(isEntitySightOfEntity(b, a)){
//                ((EntityBody)b.getUserData()).enterSight((EntityBody) a.getBody().getUserData());
//                return;
//            }
//            if(isGroundSensorHittingGround(b,a)){
//                ((PlayerBody)b.getBody().getUserData()).SetTouchingGround(true);
//
//            }
//        } else {
//            if(isEnemyHittingEnemy(a,b))
//                return;
//
//            if(isDynGridHittingDynGrid(a,b)){
//                handleGridhitGridCollision(a,b,contact.getWorldManifold());
//                return;
//            }
//            if(isDynGridHittingDynGrid(b,a)){
//                handleGridhitGridCollision(b,a,contact.getWorldManifold());
//                return;
//            }
//            if(isEnemyHittingPlayer(a,b)){
//                GameManager.ins().GameOver();
//                return;
//            }
//            if(isEnemyHittingPlayer(b,a)){
//                GameManager.ins().GameOver();
//                return;
//            }
//
//            if(isDestructoAIhittingStaticGrid(b,a))
//                return;
//            if(isDestructoAIhittingStaticGrid(a,b))
//                return;
//
//            if(isDestructoAIhittingDynGrid(b,a)) {
//                handleRemoveGridVoxel(b, a, contact.getWorldManifold());
//                return;
//            }
//            if(isDestructoAIhittingDynGrid(a,b)) {
//                handleRemoveGridVoxel(a, b, contact.getWorldManifold());
//                return;
//            }
//
//            if(isBulltetHittingEntity(a,b)){
//                handleBulletEntityHit(a,b,contact.getWorldManifold());
//                return;
//            }
//            if(isBulltetHittingEntity(b,a)){
//                handleBulletEntityHit(b,a,contact.getWorldManifold());
//                return;
//            }
//
//            if(isEntityHittingGrid(a,b)){
//                System.out.println("entity hit grid ab");
//                handlePlayerHitGrid(a,b,contact.getWorldManifold());
//                return;
//            }
//            if(isEntityHittingGrid(b,a)){
//                System.out.println("entity hit grid ba");
//                handlePlayerHitGrid(b,a,contact.getWorldManifold());
//                return;
//            }
//
//        }
    }

    private boolean handleBeginContacts(Fixture a, Fixture b, Contact contact){
        if(a.isSensor()){
            if(isEntitySightOfEntity(a, b)){
                ((EntityBody)a.getUserData()).enterSight((EntityBody) b.getBody().getUserData());
                return true;
            }
            if(isGroundSensorHittingGround(a,b)){
                System.out.print("TOUCHING GROUND");
                ((EntityBody)a.getBody().getUserData()).SetTouchingGround(true);
                return true;
            }
            //this is the proper way and will allow everything to define its own actions instead of the
            //contact listner performing the task
            if(b.getUserData() instanceof MaterialModel && a.getUserData() instanceof MaterialModel) {
                ChemicalEvent event = new ChemicalEvent();
                event.position = contact.getWorldManifold().getPoints()[0];
                event.sentBy = (MaterialModel) b.getUserData();
                event.event = ChemistryManager.EventTypes.TOUCHED;
                ((MaterialModel) a.getUserData()).Receive(event);
                return true;
            }
        } else {
            if(isEnemyHittingEnemy(a,b))
                return true;

            if(isDynGridHittingDynGrid(a,b)){
                handleGridhitGridCollision(a,b,contact.getWorldManifold());
                return true;
            }
            if(isEnemyHittingPlayer(a,b)){
                GameManager.ins().GameOver();
                return true;
            }
            if(isDestructoAIhittingStaticGrid(a,b))
                return true;
            if(isDestructoAIhittingDynGrid(a,b)) {
                handleRemoveGridVoxel(a, b, contact.getWorldManifold());
                return true;
            }
            if(isBulltetHittingEntity(a,b)){
                handleBulletEntityHit(a,b,contact.getWorldManifold());
                return true;
            }
            if(isBulltetHittingGrid(a,b)){
                handleBulletGridHit(a,b,contact.getWorldManifold());
                return true;
            }
            if(isEntityHittingGrid(a,b)){
                handlePlayerHitGrid(a,b,contact.getWorldManifold());
                return true;
            }
        }
        return false;
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
                return;
            }
        }else if(b.isSensor()){
            if(isEntitySightOfEntity(b, a)){
                ((EntityBody)b.getUserData()).exitSight((EntityBody) a.getBody().getUserData());
                return;
            }
            if(isGroundSensorHittingGround(b,a)){
                //((PlayerBody)b.getBody().getUserData()).SetTouchingGround(false);
                return;
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

    private void handleBulletGridHit(Fixture a, Fixture b, WorldManifold m){
        WorldManager.ins().removeEntityFromWorld((Projectile)a.getBody().getUserData());
        //todo send grid shot event

//        for(int i = 0; i <  m.getPoints().length; i++) {
            ChemicalEvent event = new ChemicalEvent();
            event.event = ChemistryManager.EventTypes.SHOT;
            event.sentBy = (Projectile)a.getUserData();
            event.element = ((Projectile)a.getUserData()).getPrimaryElement();
            event.position = m.getPoints()[0].cpy().add(m.getNormal().cpy().scl(-1));
            System.out.println("hit @"+ event.position +"aka"+m.getPoints()[0]);
            ResourceManager.ins().createExplosionEffect(event.position);
            ((Grid) b.getBody().getUserData()).Receive(event);


            ChemicalEvent event2 = new ChemicalEvent();
            event2.event = ChemistryManager.EventTypes.SHOT;
            event2.sentBy = (Projectile)a.getUserData();
            event2.element = ((Projectile)a.getUserData()).getPrimaryElement();
            event2.position = m.getPoints()[0].cpy().add(m.getNormal().cpy().scl(1));
            System.out.println("hit @"+ event2.position +"aka"+m.getPoints()[0]);
            ResourceManager.ins().createExplosionEffect(event2.position);
            ((Grid) b.getBody().getUserData()).Receive(event2);

//        }
    }

    private void handleBulletEntityHit(Fixture a, Fixture b, WorldManifold m){
        if(((Projectile)a.getBody().getUserData()).hitPlayers == false && b.getBody().getUserData() instanceof PlayerBody)
            return;
        WorldManager.ins().removeEntityFromWorld((Projectile)a.getBody().getUserData());
        WorldManager.ins().removeEntityFromWorld((EntityBody) b.getBody().getUserData());
        ResourceManager.ins().createExplosionEffect(m.getPoints()[0]);
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
        if(a.getBody().getLinearVelocity().len() < 50f &&  b.getBody().getLinearVelocity().len()< 50f) return;
        Vector2[] points = m.getPoints();
        for(Vector2 p : points){
//            System.out.println(p);
            WorldManager.ins().removeVoxelWorldPosition(p.x,p.y);
        }

    }
    private boolean isEnemyHittingEnemy(Fixture a, Fixture b){
        return a.getBody().getUserData() instanceof Enemy && b.getBody().getUserData() instanceof Enemy;
    }
    private boolean isEnemyHittingPlayer(Fixture a, Fixture b){
        return a.getBody().getUserData() instanceof Enemy && b.getBody().getUserData() instanceof PlayerBody;
    }

    private boolean isBulltetHittingGrid(Fixture a, Fixture b){
        return a.getUserData() instanceof Projectile && b.getBody().getUserData() instanceof Grid;
    }

    private boolean isBulltetHittingEntity(Fixture a, Fixture b){
        return a.getUserData() instanceof Projectile && b.getUserData() instanceof EntityBody;
    }

    private boolean isGroundSensorHittingGround(Fixture a, Fixture b){
        return a.getUserData().equals("ground") && b.getBody().getUserData() instanceof Grid;
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

    //todo restructure this to become more generic
    private boolean isEntityHittingGrid(Fixture a, Fixture b){
        if(a.getBody().getUserData() instanceof EntityBody && b.getBody().getUserData() instanceof DynamicGrid) {
            return true;
        }
        return false;
    }
    private void handlePlayerHitGrid(Fixture a, Fixture b, WorldManifold m){
//        System.out.println("player to control should be switched");
        ((EntityBody)a.getBody().getUserData()).setToControl(((Grid)b.getBody().getUserData()));//;
    }
}
