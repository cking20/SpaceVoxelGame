package com.kinglogic.game.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.kinglogic.game.Actors.Entities.Entity;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Interfaces.Controllable;
import com.kinglogic.game.Managers.ResourceManager;

import java.util.HashSet;

/**
 * Created by chris on 4/12/2018.
 */

public class EntityBody  implements Controllable{
    public Entity view;
    public Body myBody;
    //public ChainShape shape;
    public PhysicsShape physicsShape;
    public BodyDef bodyDef;

    public EntityBody(String name, Vector2 position){
        view = new Entity(name);
        bodyDef = new BodyDef();
        if(myBody != null){
           physicsShape = new PhysicsShape(myBody, view);
        }

// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 1.5f;
        bodyDef.angularDamping = 1.5f;
// Set our body's starting position in the world
        bodyDef.position.set(position.x,position.y);
    }
    public void CreateFixture(){
        if(myBody != null){
            physicsShape = new PhysicsShape(myBody, view);
        }
    }

    public void updateRendering(){
        view.setPosition(myBody.getPosition().x, myBody.getPosition().y);
        //voxels.rotateBy(10f);
        //System.out.println("rot="+myBody.getTransform().getRotation());
        view.setRotation((float) Math.toDegrees(myBody.getTransform().getRotation()));
    }

    public void dispose(){
        ResourceManager.ins().disposeOfShape(physicsShape.shape);
        myBody.destroyFixture(physicsShape.fixture);

    }

    @Override
    public void GoForeward() {
        myBody.applyForceToCenter(myBody.getTransform().getOrientation().rotate90(1).scl(1000f*myBody.getMass()),true);
    }

    @Override
    public void GoBackward() {
        myBody.applyForceToCenter(myBody.getTransform().getOrientation().rotate90(1).scl(-1000f/4f*myBody.getMass()),true);
    }

    @Override
    public void GoLeft() {
        myBody.applyForceToCenter(myBody.getTransform().getOrientation().scl(-1000f/2f*myBody.getMass()),true);
    }

    @Override
    public void GoRight() {
        myBody.applyForceToCenter(myBody.getTransform().getOrientation().scl(1000f/2f*myBody.getMass()),true);
    }

    @Override
    public void RotateLeft() {
        myBody.applyTorque(300f*myBody.getMass(),true);
    }

    @Override
    public void RotateRight() {
        myBody.applyTorque(-300f*myBody.getMass(),true);
    }

    @Override
    public void Enter() {

    }

    @Override
    public void Exit() {

    }

    @Override
    public void Activate() {

    }

    @Override
    public void FireMain() {

    }

    @Override
    public void FireAlt() {

    }

    @Override
    public void Deactivate() {

    }
}
