package com.kinglogic.game.Models;

import com.kinglogic.game.Actors.Voxel.Voxel;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Managers.IDs;
import com.kinglogic.game.Managers.WorldManager;
import com.kinglogic.game.Physics.DynamicGrid;
import com.kinglogic.game.Physics.EntityBody;
import com.kinglogic.game.Physics.Grid;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by chris on 5/2/2018.
 */

public class SectorState {
    public String name;
    public final int x;
    public final int y;
    public float asteroidDensity;



    public SectorState(String name, int i, int j, boolean generate){
        x = i;
        y = j;
        this.name = name;
        asteroidDensity = .4f;

        if(generate)
            WorldManager.ins().addGridToWorld(new DynamicGrid(new VoxelCollection(new Voxel(IDs.ROCK_TEX),WorldState.mapFromChunkIndex(x,y))));


    }

}
