package com.kinglogic.game;

import com.badlogic.gdx.Gdx;

/**
 * Created by chris on 4/1/2018.
 */

public class Constants {
    public static final boolean DEBUG = true;
    public static final boolean FULLSCREEN = false;
    public static final float TIME_STEP = 1/80f;
    public static final int VELOCITY_ITERATIONS = 10;//was 4 then was 7
    public static final int  POSITION_ITERATIONS = 3;//was 2
    public static final int MAX_GRID_SIZE = 250;
    public static final float SECTOR_SIZE = 2036f;//was 4036
    public static final int NUM_SECTORS = 4;
    public static final int START_WIDTH = 1920;
    public static final int START_HEIGHT = 1080;
    public static final float CHEMISTRY_SIM_STEP = .1f;
//    public static final float BLOCK_MASS = 100000f;

}
