package com.kinglogic.game.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Physics.EntityBody;
import com.kinglogic.game.Physics.FilterIDs;
import com.kinglogic.game.Physics.PhysicsShape;
import com.kinglogic.game.Physics.Projectile;

/**
 * Created by chris on 4/24/2018.
 */

public class PlayerBody extends EntityBody {
    public Vector2 buildPosition;
    PhysicsShape groundSensor;
    boolean mirrorView = false;
    boolean shotCooldown = false;
    float shotCooldownTime = 1.0f;
    float missleSpeed = 2000f;

    public PlayerBody(String name, Vector2 position) {
        super(name, position);
        speed = 4000f;
        buildPosition = new Vector2(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2);

    }



    @Override
    public void CreateFixture(){
        if(myBody != null){
            physicsShape = new PhysicsShape(myBody, ResourceManager.VOXEL_PIXEL_SIZE /2-1,ResourceManager.VOXEL_PIXEL_SIZE -1,
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
        super.updateRendering();
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
            groundSensor = new PhysicsShape(myBody, ResourceManager.VOXEL_PIXEL_SIZE /4, 4f, new Vector2(view.getHeight()/2, 0),view.getRotation());
            Filter filter = new Filter();
            filter.maskBits = FilterIDs.GRID;
            filter.categoryBits = FilterIDs.SENSOR;
            groundSensor.fixture.setFilterData(filter);
            groundSensor.fixture.setSensor(true);//was true
            groundSensor.fixture.setUserData("ground");
        }

    }

    @Override
    public void FireMain(){
        //todo should be put in the Resource Manager or the world Manager
        Projectile p;
        if(mirrorView)
            p = ResourceManager.ins().getProjectile("projectile",myBody.getPosition()
                    .add(new Vector2(-ResourceManager.VOXEL_PIXEL_SIZE /2,ResourceManager.VOXEL_PIXEL_SIZE *1.5f).rotate(view.getRotation()))
                    .add(myBody.getTransform().getOrientation().scl(ResourceManager.VOXEL_PIXEL_SIZE)));
//            p = new Projectile("projectile", myBody.getPosition()
//                    .add(new Vector2(0,ResourceManager.VOXEL_PIXEL_SIZE*1.5f).rotate(view.getRotation()))
//                    .add(myBody.getTransform().getOrientation().scl(ResourceManager.VOXEL_PIXEL_SIZE))
//            );
        else
            p = ResourceManager.ins().getProjectile("projectile",myBody.getPosition()
                    .add(new Vector2(ResourceManager.VOXEL_PIXEL_SIZE,ResourceManager.VOXEL_PIXEL_SIZE).rotate(view.getRotation()))
                    .add(myBody.getTransform().getOrientation().scl(ResourceManager.VOXEL_PIXEL_SIZE)));
//            p = new Projectile("projectile", myBody.getPosition()
//                    .add(new Vector2(0,ResourceManager.VOXEL_PIXEL_SIZE).rotate(view.getRotation()))
//                    .add(myBody.getTransform().getOrientation().scl(ResourceManager.VOXEL_PIXEL_SIZE)));
        //WorldManager.ins().addEntityToWorld(p);
        if(mirrorView)
            p.Fire(myBody.getTransform().getOrientation().rotate(180f).scl(missleSpeed));
        else
            p.Fire(myBody.getTransform().getOrientation().scl(missleSpeed));
    }
    @Override
    public void FireMain(Vector2 direction){
        Projectile p;
        p = ResourceManager.ins().getProjectile("projectile",
                myBody.getPosition()
                .add(new Vector2(ResourceManager.VOXEL_PIXEL_SIZE,ResourceManager.VOXEL_PIXEL_SIZE).rotate(view.getRotation()))
                .add(direction.rotate90(-1).rotate(view.getRotation()).scl(16f))
                );

        p.hitPlayers = false;
        p.Fire((direction).scl(10));
    }

    public Vector2 getBuildPosition(){
        return buildPosition;
    }
    public void TurnLeft(){
        mirrorView = true;

    }
    public void TurnRight(){
        mirrorView = false;
    }

    @Override
    public void GoForeward() {
        if(controlling != null)
            controlling.GoForeward();
        else
            if(!isGravLocked() || (isGravLocked() && onGround) || jumpCounter < maxJumpCounter) {
//                myBody.setLinearVelocity(myBody.getLinearVelocity().add((myBody.getTransform().getOrientation().rotate90(1).scl(400f))));
                myBody.applyForceToCenter(myBody.getTransform().getOrientation().rotate90(1).scl(speed*myBody.getMass()),true);
                jumpCounter++;
            }
        this.SetTouchingGround(false);
    }


}
