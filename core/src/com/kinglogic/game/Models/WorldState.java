package com.kinglogic.game.Models;

import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Constants;
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
    public static final int chunkSize = Constants.MAX_SIZE;
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
        int x = (int)(worldPos.x/(chunkSize*ResourceManager.voxelPixelSize));
        int y = (int)(worldPos.y/(chunkSize*ResourceManager.voxelPixelSize));
        //System.out.println("mapped to"+x+","+y);
        return new Vector2(x,y);
    }

    public static Vector2 mapFromChunkIndex(int x, int y){
        return new Vector2(((float) x)*chunkSize* ResourceManager.voxelPixelSize,((float) y)*chunkSize*ResourceManager.voxelPixelSize);
    }

    public void LoadUpSector(){
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

    public void ShiftCenterUp(int curX, int newY){
        System.out.println("shifting up");
        //save x= 0
        //todo fix the bug in the saving/loading individual grids
        for (int i = 0; i < numChunksToLoad; i++) {
            PersistenceManager.ins().SaveLevel(sectors[i][0]);
        }
        //shift down
        for (int i = 0; i < numChunksToLoad; i++) {
            for (int j = 1; j < numChunksToLoad-1; j++) {
                sectors[i][j-1] = sectors[i][j];
            }
        }
        //load num-1

//        for (int i = 0; i < numChunksToLoad; i++) {
        //todo loop this
        sectors[0][numChunksToLoad-1] = PersistenceManager.ins().LoadLevel(curX-1, newY+1);
        sectors[1][numChunksToLoad-1] = PersistenceManager.ins().LoadLevel(curX, newY+1);
        sectors[2][numChunksToLoad-1] = PersistenceManager.ins().LoadLevel(curX+1, newY+1);
//        }

    }
    public void ShiftDown(int curX, int newY){
        //save x= max-1
        for (int i = 0; i < numChunksToLoad; i++) {
            PersistenceManager.ins().SaveLevel(sectors[i][numChunksToLoad-1]);
        }
        //shift up
        for (int i = 0; i < numChunksToLoad; i++) {
            for (int j = numChunksToLoad-1; j > 0; j--) {
                sectors[i][j] = sectors[i][j-1];
            }
        }
        //load num-1

//        for (int i = 0; i < numChunksToLoad; i++) {
        //todo loop this
        sectors[0][0] = PersistenceManager.ins().LoadLevel(curX-1, newY-1);
        sectors[1][0] = PersistenceManager.ins().LoadLevel(curX, newY-1);
        sectors[2][0] = PersistenceManager.ins().LoadLevel(curX+1, newY-1);
//        }

    }
    public void ShiftLeft(int newX, int curY){
        //save y = max-1
        for (int i = 0; i < numChunksToLoad; i++) {
            PersistenceManager.ins().SaveLevel(sectors[numChunksToLoad-1][i]);
        }
        //shift right
        for (int i = 0; i < numChunksToLoad; i++) {
            for (int j = numChunksToLoad-1; j > 0; j--) {
                sectors[j][i] = sectors[j-1][i];
            }
        }
        //load num-1

//        for (int i = 0; i < numChunksToLoad; i++) {
        //todo loop this
        sectors[0][0] = PersistenceManager.ins().LoadLevel(newX-1, curY+1);
        sectors[0][1] = PersistenceManager.ins().LoadLevel(newX-1, curY);
        sectors[0][2] = PersistenceManager.ins().LoadLevel(newX-1, curY-1);
//        }

    }
    public void ShiftRight(int newX, int curY){
        //save x= 0
        for (int i = 0; i < numChunksToLoad; i++) {
            PersistenceManager.ins().SaveLevel(sectors[0][i]);
        }
        //shift down
        for (int i = 0; i < numChunksToLoad; i++) {
            for (int j = 1; j < numChunksToLoad-1; j++) {
                sectors[j-1][i] = sectors[j][i];
            }
        }
        //load num-1

//        for (int i = 0; i < numChunksToLoad; i++) {
        //todo loop this
        sectors[0][0] = PersistenceManager.ins().LoadLevel(newX+1, curY+1);
        sectors[0][1] = PersistenceManager.ins().LoadLevel(newX+1, curY);
        sectors[0][2] = PersistenceManager.ins().LoadLevel(newX+1, curY-1);
//        }
    }

    public ArrayList<Grid> GetGridsInSector(SectorState s){
        ArrayList<Grid> grids = new ArrayList<Grid>();
        HashSet<Grid> worldGrids = (HashSet<Grid>) WorldManager.ins().currentLevel.grids.clone();
        for (Grid g: worldGrids) {
            if(g.myBody == null){
                System.out.println("null body wtf(get grids in sector)");
                WorldManager.ins().removeGridFromWorld(g);
                continue;
            }
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
