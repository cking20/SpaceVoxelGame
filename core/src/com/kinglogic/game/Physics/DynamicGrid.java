package com.kinglogic.game.Physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Managers.ResourceManager;

import java.util.HashSet;
import java.util.List;

/**
 * Created by chris on 4/1/2018.
 */

public class DynamicGrid extends Grid{

    public DynamicGrid(VoxelCollection v) {
        super(v);
    }

    @Override
    public void recalculateShape(){
        recalculateRects();
        if (verts != null && myBody != null) {
            //todo dispose of the old chain and get a new one from the resource manager
            for(PhysicsShape s : (HashSet<PhysicsShape>)physicsShapes.clone()){
                ResourceManager.ins().disposeOfShape(s.shape);
                myBody.destroyFixture(s.fixture);
                physicsShapes.remove(s);
            }

            PolygonShape stat = ResourceManager.ins().getNewPolyShape();
            stat.set(verts);
            physicsShapes.add(new PhysicsShape(stat, myBody));

        } else {
            System.err.println("default shape");
            verts = new Vector2[4];
            verts[0] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize);
            verts[1] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize);
            verts[2] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize);
            verts[3] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize);
            if(myBody != null) {
                for (PhysicsShape s : (HashSet<PhysicsShape>) physicsShapes.clone()) {
                    ResourceManager.ins().disposeOfShape(s.shape);
                    myBody.destroyFixture(s.fixture);
                    physicsShapes.remove(s);
                }
                PolygonShape stat = ResourceManager.ins().getNewPolyShape();
                stat.set(verts);
                physicsShapes.add(new PhysicsShape(stat, myBody));
            }
        }

        bodyDef.position.set(voxels.getX(),voxels.getY());
        //voxels.setOrigin((ResourceManager.voxelPixelSize * VoxelCollection.maxSize)/2-ResourceManager.voxelPixelSize/2,(ResourceManager.voxelPixelSize * VoxelCollection.maxSize)/2 - ResourceManager.voxelPixelSize/2);
    }


    private void recalculateRects(){

    }
}
