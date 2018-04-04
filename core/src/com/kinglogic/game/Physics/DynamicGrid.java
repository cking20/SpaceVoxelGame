package com.kinglogic.game.Physics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Managers.ResourceManager;

/**
 * Created by chris on 4/1/2018.
 */

public class DynamicGrid extends Grid{

    public DynamicGrid(VoxelCollection v) {
        super(v);
    }

    @Override
    public void recalculateShape(){
        recalculateVerts();
        if (verts != null) {
            //todo THIS HAS TO BE POLYGONS
            ResourceManager.ins().disposeOfShape(shape);
            shape = ResourceManager.ins().getNewPolyShape();
            ((PolygonShape)shape).set(verts);
            fixtureDef.shape = shape;
        } else {
            System.err.println("default shape");
            verts = new Vector2[4];
            verts[0] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize);
            verts[1] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize);
            verts[2] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize);
            verts[3] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize);
            ResourceManager.ins().disposeOfShape(shape);
            shape = ResourceManager.ins().getNewPolyShape();
            ((PolygonShape)shape).set(verts);
            fixtureDef.shape = shape;
        }

        bodyDef.position.set(voxels.getX(),voxels.getY());
        //voxels.setOrigin((ResourceManager.voxelPixelSize * VoxelCollection.maxSize)/2-ResourceManager.voxelPixelSize/2,(ResourceManager.voxelPixelSize * VoxelCollection.maxSize)/2 - ResourceManager.voxelPixelSize/2);

    }
}
