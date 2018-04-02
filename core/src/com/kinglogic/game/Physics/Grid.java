package com.kinglogic.game.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.kinglogic.game.Actors.Voxel.Voxel;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Managers.ResourceManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chris on 4/1/2018.
 */

public class Grid {
    public VoxelCollection voxels;
    public Body myBody;
    public PolygonShape polygonShape;
    public BodyDef bodyDef;
    public FixtureDef fixtureDef;
    public Fixture fixture;
    public Vector2[] verts;

    public Grid(VoxelCollection v){
        voxels = v;
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        polygonShape = ResourceManager.ins().getNewPolyShape();
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

        recalculateVerts();
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
        polygonShape.set(verts);
        fixtureDef.shape = polygonShape;
        bodyDef.position.set(voxels.getX(),voxels.getY());
        voxels.setOrigin((ResourceManager.voxelPixelSize * VoxelCollection.maxSize)/2-ResourceManager.voxelPixelSize/2,(ResourceManager.voxelPixelSize * VoxelCollection.maxSize)/2 - ResourceManager.voxelPixelSize/2);

    }

    public Vector2[] recalculateVerts(){
        //todo parse through the voxels, counter clockwise
        int vCount = 0;
        List<Float> temp = new LinkedList<Float>();
        Voxel[][] grid = voxels.getGrid();
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){

            }
        }
        Vector2[] tempVerts = new Vector2[4];
        tempVerts[0] = new Vector2(0,0);
        tempVerts[1] = new Vector2(ResourceManager.voxelPixelSize,0);
        tempVerts[2] = new Vector2(ResourceManager.voxelPixelSize,ResourceManager.voxelPixelSize);
        tempVerts[3] = new Vector2(0,ResourceManager.voxelPixelSize);

        //todo stuff

        verts = tempVerts;
        return tempVerts;
    }
}
