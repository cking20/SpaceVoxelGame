package com.kinglogic.game.Physics;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.kinglogic.game.Actors.Entities.Entity;
import com.kinglogic.game.ChemestryFramework.Properties;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Managers.WorldManager;

/**
 * Created by chris on 4/24/2018.
 */

public class Projectile extends EntityBody{
    public boolean hitPlayers = false;
    public int lifetime = 20;

    public Projectile(String name, Vector2 position) {
        super(name, position);
        bodyDef.linearDamping =0f;
        bodyDef.bullet = true;
        this.properties.setProperty(false,Properties.AFFECTED_BY_GRAVITY);
        this.speed = 5000f;
    }
    @Override
    public void CreateFixture(){
        if(myBody != null){
            physicsShape = new PhysicsShape(myBody, view);
            Filter filter = new Filter();
            filter.maskBits = FilterIDs.ENTITY | FilterIDs.GRID | FilterIDs.BULLET;
            filter.categoryBits = FilterIDs.ENTITY | FilterIDs.BULLET;
            physicsShape.fixture.setFilterData(filter);
            physicsShape.fixture.setUserData(this);
        }
    }
    public void Fire(Vector2 velocity){
        float angleToTarget = new Vector2(1,0).angle(velocity);
        myBody.setTransform(myBody.getPosition().x, myBody.getPosition().y, MathUtils.degRad* angleToTarget);
        myBody.setLinearVelocity(velocity);
    }

    @Override
    public void updateRendering(){
        super.updateRendering();
        lifetime--;
        if(lifetime < 0)
            WorldManager.ins().removeEntityFromWorld(this);

    }

    @Override
    public void GoForeward(){

    }

}
