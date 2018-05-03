package com.kinglogic.game.Models;

import com.kinglogic.game.Physics.EntityBody;
import com.kinglogic.game.Physics.Grid;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by chris on 5/2/2018.
 */

public class SectorState {
    public String name;
    public float asteroidDensity;
    public HashSet<Grid> grids;
    public HashSet<EntityBody> entities;


    public SectorState(String name){
        this.name = name;
        asteroidDensity = .4f;
        grids = new HashSet<Grid>();
        entities = new HashSet<EntityBody>();

    }
    public void PopulateGrids(ArrayList<Grid> grids){
        this.grids.addAll(grids);
    }
    public void PopulateEntities(ArrayList<EntityBody> entityBodies){
        this.entities.addAll(entityBodies);
    }
}
