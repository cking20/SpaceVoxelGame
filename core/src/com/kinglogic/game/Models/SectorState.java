package com.kinglogic.game.Models;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.AI.DestructoEnemy;
import com.kinglogic.game.Constants;
import com.kinglogic.game.Managers.PCGManager;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Managers.WorldManager;

import java.util.Random;

/**
 * Created by chris on 5/2/2018.
 */

public class SectorState {
    public String name;
    public final int x;
    public final int y;
    public boolean cleared = false;
    PCGManager.SECTOR_ARCHETYPE type;

    public SectorState(int i, int j, PCGManager.SECTOR_ARCHETYPE type){
        name = i+","+j;
        x = i;
        y = j;
        this.type = type;

//        if(generate){
//            //pcg stuff
//        }

//        if(generate) {
//            Random r = new Random();
//            int v = 10;
//            for (int k = 0; k < 2; k++) {
//                int vt = r.nextInt()% 5;
//                v = Math.min(v,Math.abs(vt));
//            }
//            Vector2 pos = new Vector2(0,0);//WorldState.mapFromChunkIndex(x, y);
//            WorldManager.ins().GenerateAsteroid((int)pos.x, (int)pos.y, WorldState.chunkSize-5, ((float) v)/10);
//
//            v = MathUtils.random(10);
//            for (int k = 0; k < 3; k++) {
//                int vt = MathUtils.random(10);
//                v = Math.min(v,Math.abs(vt));
//            }
//            for (int k = 0; k < v; k++) {
//                Vector2 spawn = new Vector2(x,y);//WorldState.mapFromChunkIndex(x,y);
//                spawn.x += MathUtils.random(ResourceManager.VOXEL_PIXEL_SIZE * Constants.MAX_GRID_SIZE);
//                spawn.y += MathUtils.random(ResourceManager.VOXEL_PIXEL_SIZE * Constants.MAX_GRID_SIZE);
//                WorldManager.ins().addEntityToWorld(new DestructoEnemy("pinkparasite", spawn));
//            }
//        }

    }

}
