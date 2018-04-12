package com.kinglogic.game.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.kinglogic.game.Actors.Voxel.Voxel;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Actors.Voxel.VoxelUtils;
import com.kinglogic.game.Constants;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Managers.WorldManager;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by chris on 4/1/2018.
 */

public class Grid {
    public VoxelCollection voxels;
    public Body myBody;
    //public ChainShape shape;
    public HashSet<PhysicsShape> physicsShapes;
    public BodyDef bodyDef;
    public Vector2[] verts;

    public Grid(VoxelCollection v){
        voxels = v;
        bodyDef = new BodyDef();
        physicsShapes = new HashSet<PhysicsShape>();
        if(myBody != null){
            physicsShapes.add(new PhysicsShape(ResourceManager.ins().getNewChainShape(), myBody));
        }
        //recalculateShape();

// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = 1.5f;
        bodyDef.angularDamping = .5f;
// Set our body's starting position in the world
        bodyDef.position.set(v.getX(),v.getY());

    }

    public void updateRendering(){
        //System.out.println("update Rendering");
        voxels.setPosition(myBody.getPosition().x, myBody.getPosition().y);
        //voxels.rotateBy(10f);
        //System.out.println("rot="+myBody.getTransform().getRotation());
        voxels.setRotation((float) Math.toDegrees(myBody.getTransform().getRotation()));
    }
    public void recalculateShape(){
        recalculateVerts();
            if (verts != null && myBody != null) {
                //todo dispose of the old chain and get a new one from the resource manager
                for(PhysicsShape s : (HashSet<PhysicsShape>)physicsShapes.clone()){
                    ResourceManager.ins().disposeOfShape(s.shape);
                    myBody.destroyFixture(s.fixture);
                    physicsShapes.remove(s);
                }

                ChainShape stat = ResourceManager.ins().getNewChainShape();
                stat.createChain(verts);
                physicsShapes.add(new PhysicsShape(stat, myBody));


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

    public Vector2[] recalculateVerts(){
        //todo parse through the voxels, counter clockwise
        List<Vector2> verticies = VoxelUtils.MarchingSquares(voxels.getGrid());

        if(verticies != null) {
            Vector2[] ret = new Vector2[verticies.size()];
            verts = verticies.toArray(ret);
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

    public void dispose(){
        for(PhysicsShape s : (HashSet<PhysicsShape>)physicsShapes.clone()){
            ResourceManager.ins().disposeOfShape(s.shape);
            myBody.destroyFixture(s.fixture);
            physicsShapes.remove(s);
        }
    }
}
