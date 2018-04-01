package com.kinglogic.game.Physics;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.kinglogic.game.Actors.Voxel.Voxel;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chris on 4/1/2018.
 */

public class Grid {
    public VoxelCollection voxels;
    public Body myBody;
    public BodyDef bodyDef;
    public float[] verts;

    public Grid(VoxelCollection v){
        voxels = v;
        bodyDef = new BodyDef();
// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
        bodyDef.type = BodyDef.BodyType.DynamicBody;
// Set our body's starting position in the world
        bodyDef.position.set(v.getX(),v.getY());
    }

    public float[] recalculateVerts(){
        //todo parse through the voxels, counter clockwise
        int vCount = 0;
        List<Float> temp = new LinkedList<Float>();
        Voxel[][] grid = voxels.getGrid();
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){

            }
        }
        float[] verts = new float[0];
        return null;
    }
}
