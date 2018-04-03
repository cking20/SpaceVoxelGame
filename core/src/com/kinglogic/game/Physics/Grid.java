package com.kinglogic.game.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.kinglogic.game.Actors.Voxel.Voxel;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Actors.Voxel.VoxelUtils;
import com.kinglogic.game.Managers.ResourceManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chris on 4/1/2018.
 */

public class Grid {
    public VoxelCollection voxels;
    public Body myBody;
    public ChainShape shape;
    public BodyDef bodyDef;
    public FixtureDef fixtureDef;
    public Fixture fixture;
    public Vector2[] verts;

    public Grid(VoxelCollection v){
        voxels = v;
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        shape = ResourceManager.ins().getNewChainShape();
        recalculateShape();
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit

// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.linearDamping = .5f;
        bodyDef.angularDamping = .5f;
// Set our body's starting position in the world
        bodyDef.position.set(v.getX(),v.getY());

        //recalculateVerts();
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
            if (verts != null) {
                //todo dispose of the old chain and get a new one from the resource manager
                shape.createChain(verts);
                fixtureDef.shape = shape;
            } else {
                verts = new Vector2[4];
                verts[0] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize);
                verts[1] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize);
                verts[2] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize);
                verts[3] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize);
                shape.createChain(verts);
                fixtureDef.shape = shape;
            }

        bodyDef.position.set(voxels.getX(),voxels.getY());
        //voxels.setOrigin((ResourceManager.voxelPixelSize * VoxelCollection.maxSize)/2-ResourceManager.voxelPixelSize/2,(ResourceManager.voxelPixelSize * VoxelCollection.maxSize)/2 - ResourceManager.voxelPixelSize/2);

    }

    public Vector2[] recalculateVerts(){
        //todo parse through the voxels, counter clockwise
        int vCount = 0;
        List<Vector2> verticies = VoxelUtils.MarchingSquares(voxels.getGrid());

        if(verticies != null) {
            Vector2[] ret = new Vector2[verticies.size()];
            verts = verticies.toArray(ret);
        }
        return verts;
    }
    public boolean addVoxelScreenPos(Voxel v, Vector2 screenPosition){
        boolean good = voxels.addVoxelScreenPos(v,screenPosition);
        if(good) {
            recalculateShape();
        }
        return good;
    }
    public boolean removeVoxelScreenPos(Vector2 screenPosition){
        boolean good = voxels.removeVoxelScreenPos(screenPosition);
        if(good) {
            recalculateShape();
        }
        return good;
    }
}
