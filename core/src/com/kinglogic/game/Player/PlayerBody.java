package com.kinglogic.game.Player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kinglogic.game.Actors.Entities.Entity;
import com.kinglogic.game.Constants;
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
    public boolean buildMode = false;
    public Vector2 buildPosition;
    PhysicsShape groundSensor;

    Entity armView;
    Body armBody;
    PhysicsShape arm;
    Joint shoulder;

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
            //create the player body's shape
            physicsShape = new PhysicsShape(myBody, ResourceManager.VOXEL_PIXEL_SIZE /2-1,ResourceManager.VOXEL_PIXEL_SIZE -1,
                    new Vector2(view.getWidth()/2, view.getHeight()/2-1),view.getRotation());
            Filter filter = new Filter();
            filter.maskBits = FilterIDs.ENTITY | FilterIDs.GRID | FilterIDs.BULLET | FilterIDs.SENSOR;
            filter.categoryBits = FilterIDs.ENTITY | FilterIDs.PLAYER;
            physicsShape.fixture.setFilterData(filter);
            physicsShape.fixture.setUserData(this);

            //create the arm shape
            BodyDef def = new BodyDef();
            def.type = BodyDef.BodyType.DynamicBody;
            def.position.x = myBody.getPosition().x;
            def.position.y = myBody.getPosition().y;
            def.linearDamping = 0;
            def.angularDamping = 0;
            armView = new Entity("arm");
            armView.setOrigin(armView.getOriginX(), armView.getHeight()/2f);
            armBody = WorldManager.ins().addBodyToWorld(armView,def);
            WorldManager.ins().SetOnTop(view,armView);
            FixtureDef fixDef = new FixtureDef();
            fixDef.restitution = 0; fixDef.density = 0; fixDef.friction = 0;
            fixDef.filter.categoryBits = FilterIDs.ENTITY;
            arm = new PhysicsShape(armBody, fixDef,1f,1f,armBody.getLocalCenter(), 0);

            RevoluteJointDef rjd = new RevoluteJointDef();
            rjd.bodyA = myBody;
            rjd.bodyB = armBody;
            rjd.localAnchorA.set(myBody.getLocalCenter().x,myBody.getLocalCenter().y+4);
            rjd.localAnchorB.set(armBody.getLocalCenter());
            shoulder = WorldManager.ins().getWorldPhysics().createJoint(rjd);


        }
    }

    @Override
    public void updateRendering(){
        super.updateRendering();
        view.setPosition(myBody.getPosition().x, myBody.getPosition().y);
        armView.setPosition(armBody.getPosition().x, armBody.getPosition().y);
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
        armView.setRotation((float) Math.toDegrees(armBody.getTransform().getRotation()));
    }
    @Override
    public void SetTouchingGround(boolean t){
        super.SetTouchingGround(t);
        if(onGround)
                this.view.setDrawable(new TextureRegionDrawable(ResourceManager.ins().getSpriteTex("player")));
        else
                this.view.setDrawable(new TextureRegionDrawable(ResourceManager.ins().getSpriteTex("playerjump")));

    }

    @Override
    public void dispose(){
        super.dispose();

        ResourceManager.ins().disposeOfShape(arm.shape);
        myBody.destroyFixture(arm.fixture);

        ResourceManager.ins().disposeOfShape(groundSensor.shape);
        myBody.destroyFixture(groundSensor.fixture);

        ResourceManager.ins().disposeOfShape(arm.shape);
        armBody.destroyFixture(arm.fixture);

        WorldManager.ins().getWorldPhysics().destroyJoint(shoulder);
        WorldManager.ins().getWorldPhysics().destroyBody(armBody);

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

            WorldManager.ins().addLightToBody(myBody,.2f,32,myBody.getLocalCenter(), new Vector2(16f,16f));
        }

    }
    @Override
    public void LookToward(Vector2 direction) {
        armBody.setTransform(armBody.getPosition(), (float)Math.toRadians(direction.cpy().rotate90(-1).angle())+(myBody.getAngle()));
    }

    @Override
    public void FireMain(){
        //todo should be put in the Resource Manager or the world Manager
//        Projectile p;
//        if(mirrorView)
//            p = ResourceManager.ins().getProjectile("projectile",myBody.getPosition()
//                    .add(new Vector2(-ResourceManager.VOXEL_PIXEL_SIZE /2,ResourceManager.VOXEL_PIXEL_SIZE *1.5f).rotate(view.getRotation()))
//                    .add(myBody.getTransform().getOrientation().scl(ResourceManager.VOXEL_PIXEL_SIZE)));
////            p = new Projectile("projectile", myBody.getPosition()
////                    .add(new Vector2(0,ResourceManager.VOXEL_PIXEL_SIZE*1.5f).rotate(view.getRotation()))
////                    .add(myBody.getTransform().getOrientation().scl(ResourceManager.VOXEL_PIXEL_SIZE))
////            );
//        else
//            p = ResourceManager.ins().getProjectile("projectile",myBody.getPosition()
//                    .add(new Vector2(ResourceManager.VOXEL_PIXEL_SIZE,ResourceManager.VOXEL_PIXEL_SIZE).rotate(view.getRotation()))
//                    .add(myBody.getTransform().getOrientation().scl(ResourceManager.VOXEL_PIXEL_SIZE)));
////            p = new Projectile("projectile", myBody.getPosition()
////                    .add(new Vector2(0,ResourceManager.VOXEL_PIXEL_SIZE).rotate(view.getRotation()))
////                    .add(myBody.getTransform().getOrientation().scl(ResourceManager.VOXEL_PIXEL_SIZE)));
//        //WorldManager.ins().addEntityToWorld(p);
//        if(mirrorView)
//            p.Fire(myBody.getTransform().getOrientation().rotate(180f).scl(missleSpeed));
//        else
//            p.Fire(myBody.getTransform().getOrientation().scl(missleSpeed));
        Vector2 direction = new Vector2(0,1).rotate((float) Math.toDegrees(armBody.getAngle()));
        Projectile p;
        p = ResourceManager.ins().getProjectile("projectile",
                myBody.getPosition()
                        .add(new Vector2(ResourceManager.VOXEL_PIXEL_SIZE,ResourceManager.VOXEL_PIXEL_SIZE).rotate(view.getRotation()))
                        .add(direction.rotate90(-1).rotate(view.getRotation()).scl(16f))
        );

        p.hitPlayers = false;
        p.Fire((direction).scl(10));
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
