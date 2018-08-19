package com.kinglogic.game.Models;

import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.Constants;
import com.kinglogic.game.Managers.GameManager;
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
    public static final int chunkSize = Constants.MAX_GRID_SIZE;

    public String name;
    public HashSet<Grid> grids;
    public HashSet<EntityBody> entities;

    public SectorState sector;

    private int x, y;


    public WorldState(String name, int currentX, int currentY){
        this.name = name;
        x = currentX;
        y = currentY;
        sector = new SectorState(x+","+y, x,y, false);
        grids = new HashSet<Grid>();
        entities = new HashSet<EntityBody>();
    }

    public static Vector2 mapToChunkIndex(Vector2 worldPos){
        int x = (int)(worldPos.x/(chunkSize*ResourceManager.VOXEL_PIXEL_SIZE));
        int y = (int)(worldPos.y/(chunkSize*ResourceManager.VOXEL_PIXEL_SIZE));
        //System.out.println("mapped to"+x+","+y);
        return new Vector2(x,y);
    }

    public static Vector2 mapFromChunkIndex(int x, int y){
        return new Vector2(((float) x)*chunkSize* ResourceManager.VOXEL_PIXEL_SIZE,((float) y)*chunkSize*ResourceManager.VOXEL_PIXEL_SIZE);
    }

    public void LoadUpSector(){
        sector = PersistenceManager.ins().LoadLevel(x,y);
    }

    public void PopulateGrids(ArrayList<Grid> grids){
        for (Grid g: grids) {
            WorldManager.ins().addGridToWorld(g);
        }
    }
    public void PopulateEntities(ArrayList<EntityBody> entityBodies){
        for (EntityBody e: entityBodies) {
            WorldManager.ins().addEntityToWorld(e);
        }
    }

    public void ShiftTo(int newX, int newY){
        System.out.println("shifting");

        PersistenceManager.ins().SaveLevel(sector);

        WorldManager.ins().clearLevel();
        //todo dont put player in the center, and the ship has to come with
        GameManager.ins().getThePlayer().myBody.setTransform(0,0,0);

        sector = PersistenceManager.ins().LoadLevel(newX, newY);
        x = newX;
        y = newY;

    }
    public ArrayList<Grid> GetGridsInSector(SectorState s){
        ArrayList<Grid> grids = new ArrayList<Grid>();
        HashSet<Grid> worldGrids = (HashSet<Grid>) WorldManager.ins().currentLevel.grids.clone();
        for (Grid g: worldGrids) {
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
