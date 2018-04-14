package com.kinglogic.game.Physics;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Actors.Voxel.VoxelUtils;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Managers.WorldManager;

import java.util.HashSet;
import java.util.List;

/**
 * Created by chris on 4/1/2018.
 */

public class DynamicGrid extends Grid{

    public DynamicGrid(VoxelCollection v) {
        super(v);
        bodyDef.linearDamping = .5f;
    }

    @Override
    public void recalculateShape(){
        recalculateVerts();
        if (verts != null && myBody != null) {
            //todo dispose of the old chain and get a new one from the resource manager
            for(PhysicsShape s : (HashSet<PhysicsShape>)physicsShapes.clone()){
                ResourceManager.ins().disposeOfShape(s.shape);
                myBody.destroyFixture(s.fixture);
                physicsShapes.remove(s);
            }
            PolygonShape temp;
            float[] vertPos;
            Vector2[] v = verts.get(0);
            for(int i = 0; i < v.length/4; i++){
                temp = ResourceManager.ins().getNewPolyShape();
                vertPos = new float[8];
                vertPos[0] = v[i*4+0].x;
                vertPos[1] = v[i*4+0].y;
                vertPos[2] = v[i*4+1].x;
                vertPos[3] = v[i*4+1].y;
                vertPos[4] = v[i*4+2].x;
                vertPos[5] = v[i*4+2].y;
                vertPos[6] = v[i*4+3].x;
                vertPos[7] = v[i*4+3].y;
                temp.set(vertPos);
                physicsShapes.add(new PhysicsShape(temp, myBody));
            }

            bodyDef.position.set(voxels.getX(),voxels.getY());

        } else {
            System.err.println("default shape");
//            verts = new Vector2[4];
//            verts[0] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize);
//            verts[1] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize);
//            verts[2] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize);
//            verts[3] = new Vector2(VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize, VoxelCollection.maxSize / 2 * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize);
//            if(myBody != null) {
//                for (PhysicsShape s : (HashSet<PhysicsShape>) physicsShapes.clone()) {
//                    ResourceManager.ins().disposeOfShape(s.shape);
//                    myBody.destroyFixture(s.fixture);
//                    physicsShapes.remove(s);
//                }
//                PolygonShape stat = ResourceManager.ins().getNewPolyShape();
//                stat.set(verts);
//                physicsShapes.add(new PhysicsShape(stat, myBody));
                WorldManager.ins().removeGridFromWorld(this);
//            }
        }

//        bodyDef.position.set(voxels.getX(),voxels.getY());
        //voxels.setOrigin((ResourceManager.voxelPixelSize * VoxelCollection.maxSize)/2-ResourceManager.voxelPixelSize/2,(ResourceManager.voxelPixelSize * VoxelCollection.maxSize)/2 - ResourceManager.voxelPixelSize/2);
    }


    @Override
    public List<Vector2[]> recalculateVerts(){
        //todo new voxel utils function
        List<Vector2[]> verticies = VoxelUtils.LeastRects(voxels.getGrid());
        if(verticies != null) {
//            Vector2[] ret = new Vector2[verticies.size()];
            verts = verticies;//.toArray(ret);
        }else {
            verts = null;
        }
        return verts;
    }
}
