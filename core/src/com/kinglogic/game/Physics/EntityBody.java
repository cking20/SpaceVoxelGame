package com.kinglogic.game.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kinglogic.game.Actors.Entities.Entity;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.ChemestryFramework.ChemicalEvent;
import com.kinglogic.game.ChemestryFramework.ChemistryManager;
import com.kinglogic.game.ChemestryFramework.MaterialModel;
import com.kinglogic.game.ChemestryFramework.Properties;
import com.kinglogic.game.Interfaces.Controllable;
import com.kinglogic.game.Managers.CameraManager;
import com.kinglogic.game.Managers.ResourceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by chris on 4/12/2018.
 */

public class EntityBody  implements Controllable, MaterialModel{
    public Entity view;
    public Body myBody;
    //public ChainShape shape;
    public PhysicsShape physicsShape;
    public PhysicsShape sight;
    public BodyDef bodyDef;
    protected Controllable controlling;
    public Controllable lastControlled;
    private boolean gravLock = false;
    protected EntityProperties properties;


    public float viewDistance = 20f;
    public float speed = 1000f;
    protected ArrayList<EntityBody> perceptions;

    public EntityBody(String name, Vector2 position){
        properties = new EntityProperties();
        properties.setProperty(true, Properties.AFFECTED_BY_GRAVITY);
        properties.setProperty(true, Properties.COLLIDABLE);
        properties.setProperty(true, Properties.ELECTRICUTABLE);
        properties.setProperty(true, Properties.FLAMMABLE);

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
            filter.maskBits = FilterIDs.ENTITY | FilterIDs.GRID;
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

        //apply gravity
        if(properties.gravity != null) {
            Vector2 artificialGravity = new Vector2(properties.gravity);
            artificialGravity.rotate((myBody.getTransform().getRotation()));
            myBody.applyForceToCenter(artificialGravity.x, artificialGravity.y, true);
        }
    }
    public void enterSight(EntityBody seeing){
        perceptions.add(seeing);
    }
    public void exitSight(EntityBody seeing){
        perceptions.remove(seeing);
    }

    public void ToggleGravLock(){
        gravLock = !gravLock;
        myBody.setFixedRotation(gravLock);
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
        myBody.applyForceToCenter(myBody.getTransform().getOrientation().rotate90(1).scl(speed*myBody.getMass()),true);
    }

    @Override
    public void GoBackward() {
        if(controlling != null)
            controlling.GoBackward();
        else
        myBody.applyForceToCenter(myBody.getTransform().getOrientation().rotate90(1).scl(-speed/4f*myBody.getMass()),true);
    }

    @Override
    public void GoLeft() {
        if(controlling != null)
            controlling.GoLeft();
        else
        myBody.applyForceToCenter(myBody.getTransform().getOrientation().scl(-speed/2f*myBody.getMass()),true);
    }

    @Override
    public void GoRight() {
        if(controlling != null)
            controlling.GoRight();
        else
            myBody.applyForceToCenter(myBody.getTransform().getOrientation().scl(speed/2f*myBody.getMass()),true);
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
        if(toControl == null)return;
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
    public void FireMain(Vector2 direction) {

    }

    @Override
    public void FireAlt() {

    }

    @Override
    public void FireAlt(Vector2 direction) {

    }

    @Override
    public void Deactivate() {

    }

    @Override
    public void setToControl(Controllable that){
        lastControlled = that;
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

    @Override
    public LinkedList<ChemicalEvent> Output() {
        return null;
    }

    @Override
    public boolean Update(float delta) {
        properties.gravity = null;
        return false;
    }

    @Override
    public void Recieve(ChemicalEvent event) {
        switch (event.event){
            case SEND_GRAVITY:
                if(properties.hasProperty(Properties.AFFECTED_BY_GRAVITY)) {
                    System.out.println("setting gravity of " + this + " to " + event.position);
                    properties.gravity = event.position;
                    ChemistryManager.ins().CheckThis(this);
                }
        }
    }
}
