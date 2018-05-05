package com.kinglogic.game.Models;

import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.Managers.PersistenceManager;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Managers.WorldManager;
import com.kinglogic.game.Physics.EntityBody;
import com.kinglogic.game.Physics.Grid;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by chris on 5/4/2018.
 */

public class WorldState {
    private static int chunkSize = 100;
    private int numChunksToLoad = 3;

//    private SectorState TL;
//    private SectorState TM;
//    private SectorState TR;
//
//    private SectorState CL;
//    private SectorState CM;
//    private SectorState CR;
//
//    private SectorState BL;
//    private SectorState BM;
//    private SectorState BR;

    public String name;
    public HashSet<Grid> grids;
    public HashSet<EntityBody> entities;

    public SectorState[][] sectors;

    //
    //-1,0  0,0
    //-1,-1 0,-1

    private int x, y;


    public WorldState(String name, int centerX, int centerY){
        this.name = name;
        x = centerX;
        y = centerY;
        sectors = new SectorState[numChunksToLoad][numChunksToLoad];
        grids = new HashSet<Grid>();
        entities = new HashSet<EntityBody>();
        //todo make this based on numChunksToLoad


    }

    public static Vector2 mapToChunkIndex(Vector2 worldPos){
        int x = (int)worldPos.x/(chunkSize*ResourceManager.voxelPixelSize);
        int y = (int)worldPos.y/(chunkSize*ResourceManager.voxelPixelSize);
        System.out.println("mapped to"+x+","+y);
        return new Vector2(x,y);
    }

    public static Vector2 mapFromChunkIndex(int x, int y){
        return new Vector2(x*chunkSize* ResourceManager.voxelPixelSize,y*chunkSize*ResourceManager.voxelPixelSize);
    }

    public void LoadUpSectors(){
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                sectors[i+1][j+1] = PersistenceManager.ins().LoadLevel((x+i),(y+j));
            }
        }
    }


    public void moveTo(int newX, int newY){

    }

    public void PopulateGrids(ArrayList<Grid> grids){
        this.grids.addAll(grids);
    }
    public void PopulateEntities(ArrayList<EntityBody> entityBodies){
        this.entities.addAll(entityBodies);
    }

    public ArrayList<Grid> GetGridsInSector(SectorState s){
        ArrayList<Grid> grids = new ArrayList<Grid>();
        HashSet<Grid> worldGrids = (HashSet<Grid>) WorldManager.ins().currentLevel.grids.clone();
        for (Grid g: worldGrids) {
            System.out.println("grid @"+g.myBody.getPosition());
            Vector2 pos = mapToChunkIndex(g.myBody.getPosition());
            System.out.println("mapped to "+pos);
            int i = (int)pos.x;
            int j = (int)pos.y;
            if(i == s.x && j == s.y)
                grids.add(g);
        }
        return grids;
    }

    public int getCenterX() {
        return x;
    }
    public int getCenterY(){
        return y;
    }
}
