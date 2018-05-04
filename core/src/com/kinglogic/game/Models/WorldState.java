package com.kinglogic.game.Models;

/**
 * Created by chris on 5/4/2018.
 */

public class WorldState {
    private SectorState TL;
    private SectorState TM;
    private SectorState TR;

    private SectorState CL;
    private SectorState CM;
    private SectorState CR;

    private SectorState BL;
    private SectorState BM;
    private SectorState BR;

    private int x, y;


    public WorldState(int centerX, int centerY){
        x = centerX;
        y = centerY;


    }
}
