package com.kinglogic.game;

import com.badlogic.gdx.Gdx;

/**
 * Created by chris on 4/1/2018.
 */

public class Constants {
    public static final boolean DEBUG = false;
    public static final boolean FULLSCREEN = false;
    public static final float TIME_STEP = 1/80f;
    public static final int VELOCITY_ITERATIONS = 4;//was 4 then was 7
    public static final int  POSITION_ITERATIONS = 2;//was 2
    public static final float SECTOR_SIZE = 9000f;//was 4036
    public static final int NUM_SECTORS = 4;
    public static final int START_WIDTH = 1920;
    public static final int START_HEIGHT = 1080;
    public static final float CHEMISTRY_SIM_STEP = .1f;
    public static final int NUM_ROOM_TYPES = 30;
    public static final int NUM_ALTS = 1;
    public static final int ROOM_SIZE = 16;
    public static final int NUM_ROOMS_PER_STATION = 8;

    public static final int MAX_GRID_SIZE =  ROOM_SIZE * NUM_ROOMS_PER_STATION + 4;//260;//roomSize * numRooms + 4(the 4 is because of the borders breaking )



//    public static final float BLOCK_MASS = 100000f;

}
