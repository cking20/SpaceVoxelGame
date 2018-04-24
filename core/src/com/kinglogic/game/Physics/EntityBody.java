package com.kinglogic.game.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kinglogic.game.Actors.Entities.Entity;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Interfaces.Controllable;
import com.kinglogic.game.Managers.CameraManager;
import com.kinglogic.game.Managers.ResourceManager;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by chris on 4/12/2018.
 */

public class EntityBody  implements Controllable{
    public Entity view;
    public Body myBody;
    //public ChainShape shape;
    public PhysicsShape physicsShape;
    public PhysicsShape sight;
    public BodyDef bodyDef;
    private Controllable controlling;

    public float viewDistance = 20f;
    protected ArrayList<EntityBody> perceptions;

    public EntityBody(String name, Vector2 position){
        perceptions = new ArrayList<EntityBody>();
        view = new Entity(name);
        bodyDef = new BodyDef();
        if(myBody != null){
           physicsShape = new PhysicsShape(myBody, view);
        }

// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.bullet = true;
        bodyDef.linearDamping = 1.5f;
        bodyDef.angularDamping = 5f;
// Set our body's starting position in the world
        bodyDef.position.set(position.x,position.y);
    }
    public void CreateFixture(){
        if(myBody != null){
            physicsShape = new PhysicsShape(myBody, view);
            Filter filter = new Filter();
            filter.maskBits = FilterIDs.ENTITY | FilterIDs.GRID | FilterIDs.BULLET | FilterIDs.SENSOR;
            filter.categoryBits = FilterIDs.ENTITY;
            physicsShape.fixture.setFilterData(filter);
            physicsShape.fixture.setUserData(this);
        }
    }

    public void CreateSight(float radius){
        if(myBody != null) {
            sight = new PhysicsShape(myBody, view, radius);
            sight.fixture.setDensity(0);
            myBody.resetMassData();
            Filter filter = new Filter();
            filter.maskBits = FilterIDs.ENTITY;
            filter.categoryBits = FilterIDs.SENSOR;
            sight.fixture.setFilterData(filter);
            sight.fixture.setSensor(true);
            sight.fixture.setUserData(this);
        }
    }

    public void updateRendering(){
        view.setPosition(myBody.getPosition().x, myBody.getPosition().y);
        //voxels.rotateBy(10f);
        //System.out.println("rot="+myBody.getTransform().getRotation());
        view.setRotation((float) Math.toDegrees(myBody.getTransform().getRotation()));
    }
    public void enterSight(EntityBody seeing){
        perceptions.add(seeing);
    }
    public void exitSight(EntityBody seeing){
        perceptions.remove(seeing);
    }

    public void dispose(){
        ResourceManager.ins().disposeOfShape(physicsShape.shape);
        myBody.destroyFixture(physicsShape.fixture);

    }

    @Override
    public void GoForeward() {
        if(controlling != null)
            controlling.GoForeward();
        else
        myBody.applyForceToCenter(myBody.getTransform().getOrientation().rotate90(1).scl(1000f*myBody.getMass()),true);
    }

    @Override
    public void GoBackward() {
        if(controlling != null)
            controlling.GoBackward();
        else
        myBody.applyForceToCenter(myBody.getTransform().getOrientation().rotate90(1).scl(-1000f/4f*myBody.getMass()),true);
    }

    @Override
    public void GoLeft() {
        if(controlling != null)
            controlling.GoLeft();
        else
        myBody.applyForceToCenter(myBody.getTransform().getOrientation().scl(-1000f/2f*myBody.getMass()),true);
    }

    @Override
    public void GoRight() {
        if(controlling != null)
            controlling.GoRight();
        else
            myBody.applyForceToCenter(myBody.getTransform().getOrientation().scl(1000f/2f*myBody.getMass()),true);
    }

    @Override
    public void RotateLeft() {
        if(controlling != null)
            controlling.RotateLeft();
        else
            myBody.applyTorque(700f*myBody.getMass(),true);
    }

    @Override
    public void RotateRight() {
        if(controlling != null)
            controlling.RotateRight();
        else
            myBody.applyTorque(-700f*myBody.getMass(),true);
    }

    @Override
    public void Enter(Controllable toControl) {
        CameraManager.ins().Track(toControl.GetView());
        controlling = toControl;
    }

    @Override
    public void Exit() {
        CameraManager.ins().Track(view);
        controlling = null;
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

    @Override
    public boolean isControlling() {
        return controlling != null;
    }

    @Override
    public boolean isControlling(Controllable that) {
        return that.equals(controlling);
    }

    @Override
    public Actor GetView() {
        return view;
    }
}
