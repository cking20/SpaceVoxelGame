package com.kinglogic.game.Player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Managers.WorldManager;
import com.kinglogic.game.Physics.EntityBody;
import com.kinglogic.game.Physics.FilterIDs;
import com.kinglogic.game.Physics.PhysicsShape;
import com.kinglogic.game.Physics.Projectile;

/**
 * Created by chris on 4/24/2018.
 */

public class PlayerBody extends EntityBody {
    PhysicsShape groundSensor;
    boolean mirrorView = false;
    boolean onGround = false;
    boolean shotCooldown = false;
    float shotCooldownTime = 1.0f;

    public PlayerBody(String name, Vector2 position) {
        super(name, position);
    }

    @Override
    public void CreateFixture(){
        if(myBody != null){
            physicsShape = new PhysicsShape(myBody, ResourceManager.voxelPixelSize/2,ResourceManager.voxelPixelSize+1,
                    new Vector2(view.getWidth()/2, view.getHeight()/2-1),view.getRotation());
            Filter filter = new Filter();
            filter.maskBits = FilterIDs.ENTITY | FilterIDs.GRID | FilterIDs.BULLET | FilterIDs.SENSOR;
            filter.categoryBits = FilterIDs.ENTITY | FilterIDs.PLAYER;
            physicsShape.fixture.setFilterData(filter);
            physicsShape.fixture.setUserData(this);
        }
    }
    @Override
    public void updateRendering(){
        view.setPosition(myBody.getPosition().x, myBody.getPosition().y);
        if(mirrorView){
            view.setScaleX(-1);
            Vector2 v = new Vector2(view.getWidth(),0).rotate((float) Math.toDegrees(myBody.getTransform().getRotation()));
            view.moveBy(v.x, v.y);
        }else {
            view.setScaleX(1);
            Vector2 v = new Vector2(-view.getWidth(),0).rotate((float) Math.toDegrees(myBody.getTransform().getRotation()));;
            //view.moveBy(v.x, v.y);
        }
        view.setRotation((float) Math.toDegrees(myBody.getTransform().getRotation()));


    }

    @Override
    public void CreateSight(float radius){
        super.CreateSight(radius);
        //create the playr spectific sensors, such as on ground
        if(myBody != null) {
            groundSensor = new PhysicsShape(myBody, ResourceManager.voxelPixelSize/2, 4f, new Vector2(view.getHeight()/2, 0),view.getRotation());
            Filter filter = new Filter();
            filter.maskBits = FilterIDs.GRID;
            filter.categoryBits = FilterIDs.SENSOR;
            groundSensor.fixture.setFilterData(filter);
            groundSensor.fixture.setSensor(true);
            groundSensor.fixture.setUserData("ground");

        }

    }

    @Override
    public void FireMain(){
        //todo should be put in the Resource Manager or the world Manager
        Projectile p;
        if(mirrorView)
            p = ResourceManager.ins().getProjectile("projectile",myBody.getPosition()
                    .add(new Vector2(0,ResourceManager.voxelPixelSize*1.5f).rotate(view.getRotation()))
                    .add(myBody.getTransform().getOrientation().scl(ResourceManager.voxelPixelSize)));
//            p = new Projectile("projectile", myBody.getPosition()
//                    .add(new Vector2(0,ResourceManager.voxelPixelSize*1.5f).rotate(view.getRotation()))
//                    .add(myBody.getTransform().getOrientation().scl(ResourceManager.voxelPixelSize))
//            );
        else
            p = ResourceManager.ins().getProjectile("projectile",myBody.getPosition()
                    .add(new Vector2(0,ResourceManager.voxelPixelSize).rotate(view.getRotation()))
                    .add(myBody.getTransform().getOrientation().scl(ResourceManager.voxelPixelSize)));
//            p = new Projectile("projectile", myBody.getPosition()
//                    .add(new Vector2(0,ResourceManager.voxelPixelSize).rotate(view.getRotation()))
//                    .add(myBody.getTransform().getOrientation().scl(ResourceManager.voxelPixelSize)));
        //WorldManager.ins().addEntityToWorld(p);
        if(mirrorView)
            p.Fire(myBody.getTransform().getOrientation().rotate(180f).scl(2000f));
        else
            p.Fire(myBody.getTransform().getOrientation().scl(2000f));
    }

    public void SetTouchingGround(boolean t){
        onGround = t;
    }
    public void TurnLeft(){
        mirrorView = true;

    }
    public void TurnRight(){
        mirrorView = false;
    }


}
