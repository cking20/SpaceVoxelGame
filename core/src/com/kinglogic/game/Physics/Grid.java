package com.kinglogic.game.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kinglogic.game.Actors.Voxel.Blocks.Voxel;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Actors.Voxel.VoxelUtils;
import com.kinglogic.game.ChemestryFramework.ChemicalEvent;
import com.kinglogic.game.ChemestryFramework.ChemistryManager;
import com.kinglogic.game.ChemestryFramework.MaterialModel;
import com.kinglogic.game.Interfaces.Controllable;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Managers.WorldManager;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by chris on 4/1/2018.
 * Container for physics and rendering of grids
 */

public class Grid implements Controllable, MaterialModel{
    private static int gridID = 0;
    public String name;
    public VoxelCollection voxels;
    public Body myBody;
    //public ChainShape shape;
    public HashSet<PhysicsShape> physicsShapes;
    public BodyDef bodyDef;
    public List<Vector2[]> verts;
    public Vector2 gravity = new Vector2(0f,-180000f);

    public Grid(VoxelCollection v){
        name = ""+(gridID++);
        voxels = v;
        bodyDef = new BodyDef();
        physicsShapes = new HashSet<PhysicsShape>();
        if(myBody != null){
            PhysicsShape s = new PhysicsShape(ResourceManager.ins().getNewChainShape(), myBody);
            Filter filter = new Filter();
            filter.maskBits = FilterIDs.ENTITY | FilterIDs.GRID | FilterIDs.BULLET | FilterIDs.SENSOR;
            filter.categoryBits = FilterIDs.GRID;
            s.fixture.setFilterData(filter);
            physicsShapes.add(s);
//            physicsShapes.add(new PhysicsShape(ResourceManager.ins().getNewChainShape(), myBody));
        }
        //recalculateShape();

// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 1.5f;
        bodyDef.angularDamping = .5f;
// Set our body's starting position in the world
        bodyDef.position.set(v.getX(),v.getY());
        bodyDef.angle = (float) Math.toRadians(v.getRotation());

    }

    /**
     * Update the rendering
     */
    public void updateRendering(){
        //System.out.println("update Rendering");
        if(myBody == null)return;
        voxels.setPosition(myBody.getPosition().x, myBody.getPosition().y);
        //voxels.rotateBy(10f);
        //System.out.println("rot="+myBody.getTransform().getRotation());
        voxels.setRotation((float) Math.toDegrees(myBody.getTransform().getRotation()));

        Vector2 tempG = new Vector2(0f,-180000f).rotate((float) Math.toDegrees(myBody.getTransform().getRotation()));
        gravity.x = tempG.x;
        gravity.y = tempG.y;
    }

    /**
     * Rebuild the physics shapes of this grid
     * if there are no voxels, then destroy this grid
     */
    public void recalculateShape(){
        recalculateVerts();
            if (verts != null && myBody != null) {
                //todo dispose of the old chain and get a new one from the resource manager
                for(PhysicsShape s : (HashSet<PhysicsShape>)physicsShapes.clone()){
                    ResourceManager.ins().disposeOfShape(s.shape);
                    myBody.destroyFixture(s.fixture);
                    physicsShapes.remove(s);
                }
                for (Vector2[] chain : verts){
                    ChainShape stat = ResourceManager.ins().getNewChainShape();
                    stat.createChain(chain);
                    PhysicsShape s = new PhysicsShape(stat, myBody);
                    Filter filter = new Filter();
                    filter.maskBits = FilterIDs.ENTITY | FilterIDs.GRID | FilterIDs.BULLET | FilterIDs.SENSOR;
                    filter.categoryBits = FilterIDs.GRID;
                    s.fixture.setFilterData(filter);
                    physicsShapes.add(s);
                }



                bodyDef.position.set(voxels.getX(),voxels.getY());
            } else {
                System.err.println("default shape");
//                verts = new Vector2[4];
//                verts[0] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize);
//                verts[1] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize);
//                verts[2] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize);
//                verts[3] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize);
//                if(myBody != null) {
                    /*
                    for (PhysicsShape s : (HashSet<PhysicsShape>) physicsShapes.clone()) {
                        ResourceManager.ins().disposeOfShape(s.shape);
                        myBody.destroyFixture(s.fixture);
                        physicsShapes.remove(s);
                    }
                    if(myBody != null) {
                        ChainShape stat = ResourceManager.ins().getNewChainShape();
                        stat.createChain(verts);
                        physicsShapes.add(new PhysicsShape(stat, myBody));
                    }
                    */
                    WorldManager.ins().removeGridFromWorld(this);
//                }
            }

//        bodyDef.position.set(voxels.getX(),voxels.getY());
        //voxels.setOrigin((ResourceManager.voxelPixelSize * VoxelCollection.maxSize)/2-ResourceManager.voxelPixelSize/2,(ResourceManager.voxelPixelSize * VoxelCollection.maxSize)/2 - ResourceManager.voxelPixelSize/2);
    }

    public List<Vector2[]> recalculateVerts(){
        //todo parse through the voxels, counter clockwise
        List<Vector2[]> verticies = VoxelUtils.MarchingSquares(voxels.getGrid());

        if(verticies != null) {
            Vector2[] ret = new Vector2[verticies.size()];
            verts = verticies;//.toArray(ret);
        }else {
            verts = null;
        }
        return verts;
    }

    public boolean addVoxelScreenPos(Voxel v, Vector2 screenPosition){
        boolean good = voxels.addVoxelScreenPos(v,screenPosition);
        if(good) {
            recalculateShape();
//            MassData d = myBody.getMassData();
//            d.mass += Constants.BLOCK_MASS;
//            myBody.setMassData(d);
        }
        return good;
    }

    public boolean removeVoxelScreenPos(Vector2 screenPosition){
        boolean good = voxels.removeVoxelScreenPos(screenPosition);
        if(good) {
            recalculateShape();
//            MassData d = myBody.getMassData();
//            d.mass -= Constants.BLOCK_MASS;
//            myBody.setMassData(d);
        }
        return good;
    }

    public boolean addVoxelWorldPos(Voxel v, Vector2 screenPosition){
        boolean good = voxels.addVoxelWorldPos(v,screenPosition);
        if(good) {
            recalculateShape();
//            MassData d = myBody.getMassData();
//            d.mass += Constants.BLOCK_MASS;
//            myBody.setMassData(d);
        }
        return good;
    }

    public boolean removeVoxelWorldPos(Vector2 screenPosition){
        boolean good = voxels.removeVoxelWorldPos(screenPosition);
        if(good) {
            recalculateShape();
//            MassData d = myBody.getMassData();
//            d.mass -= Constants.BLOCK_MASS;
//            myBody.setMassData(d);
        }
        return good;
    }

    public boolean isWorldPositionInGrid(Vector2 worldPos){
        return voxels.isWorldPosInGrid(worldPos);
    }

    public void dispose(){
        for(PhysicsShape s : (HashSet<PhysicsShape>)physicsShapes.clone()){
            ResourceManager.ins().disposeOfShape(s.shape);
            if(myBody != null)
                myBody.destroyFixture(s.fixture);
            physicsShapes.remove(s);
        }
    }

    @Override
    public void GoForeward() {
        if(myBody != null)
        myBody.applyForceToCenter(myBody.getTransform().getOrientation().rotate90(1).scl(1000f*myBody.getMass()),true);
    }

    @Override
    public void GoBackward() {
        if(myBody != null)
        myBody.applyForceToCenter(myBody.getTransform().getOrientation().rotate90(1).scl(-1000f/4f*myBody.getMass()),true);
    }

    @Override
    public void GoLeft() {
        if(myBody != null)
        myBody.applyForceToCenter(myBody.getTransform().getOrientation().scl(-1000f/2f*myBody.getMass()),true);
    }

    @Override
    public void GoRight() {
        if(myBody != null)
        myBody.applyForceToCenter(myBody.getTransform().getOrientation().scl(1000f/2f*myBody.getMass()),true);
    }

    @Override
    public void RotateLeft() {
        if(myBody != null)
        myBody.applyTorque(300f*myBody.getMass()*myBody.getFixtureList().size,true);
    }

    @Override
    public void RotateRight() {
        if(myBody != null)
        myBody.applyTorque(-300f*myBody.getMass()*myBody.getFixtureList().size,true);
    }


    @Override
    public void Enter(Controllable toCtrl) {

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
    public boolean isControlling() {
        return false;
    }

    @Override
    public boolean isControlling(Controllable that) {
        return false;
    }

    @Override
    public void setToControl(Controllable that){}

    @Override
    public Actor GetView() {
        return voxels;
    }

    //Needed to simulate fluids moving constantly
    @Override
    public LinkedList<ChemicalEvent> Output() {
        return null;
    }

    @Override
    public boolean Update(float delta) {
        return false;
    }

    @Override
    public void Recieve(ChemicalEvent event) {
        //get the voxel at event position
        Voxel effected = voxels.getVoxelAtWorldPosition(event.position);
        if(effected == null){
            switch (event.event){
                default:
                    break;
            }
            return;
        }

        //apply grid specific actions
        switch (event.event){
            case TOUCHED:
                ChemicalEvent e = new ChemicalEvent();
                e.sentBy = this;
                e.position = gravity;
                e.event = ChemistryManager.EventTypes.SEND_GRAVITY;
                event.sentBy.Recieve(e);
                break;
            default:
                event.sentBy = this;
                break;
        }
        //send the event to that voxel
        effected.Recieve(event);
    }

    @Override
    public ChemistryManager.Elements getPrimaryElement() {
        return null;
    }

    public void PropagateEvent(ChemicalEvent event, Voxel type, boolean connectedOnly){
        if(connectedOnly){
            //todo BFS
        }
        else{
            //todo dont do this at all. its n^2 complexity. remove the boolean arg
            //all of that type
            Voxel[][] grid = voxels.getGrid();
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    if(grid[i][j] != null)
                        if(grid[i][j].getClass().equals(type.getClass())){
                            ChemicalEvent newEvent = new ChemicalEvent();
                            newEvent.sentBy = this;
                            newEvent.event = event.event;
                            newEvent.element = event.element;
                            newEvent.position = voxels.mapIndexesToWorldPos(i,j);
                            ChemistryManager.ins().EnqueueEvent(newEvent);
                        }

                }
            }
        }
    }
}
