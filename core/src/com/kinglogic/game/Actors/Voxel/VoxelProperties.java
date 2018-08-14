package com.kinglogic.game.Actors.Voxel;

import com.kinglogic.game.ChemestryFramework.Properties;

/**
 * Created by chris on 4/1/2018.
 */

public class VoxelProperties extends Properties{


    //These bits will hold the level of each fluid in the voxel
    //AIR
    public static final byte AIR_1 = 1;
    public static final byte AIR_2 = 2;

    //WATER
    public static final byte WATER_1 = 4;
    public static final byte WATER_2 = 8;

    //LAVA
    public static final byte LAVA_1 = 16;
    public static final byte LAVA_2 = 32;

    //This bit decides if the voxel will send/collect fluids
    public static final byte PERMEABLE = 64;


    private byte fluids;

    public VoxelProperties(){
        level = 1;
        health = 127;
        properties = 0;
        fluids = 0;
    }


    /**
     * Returns true iff the voxel should allow the transfer of fluids
     * @return
     */
    public boolean isPermeable(){
        return !((fluids ^ PERMEABLE) == PERMEABLE);
    }
    public int getWater(){
        return fluids ^ WATER_1 + fluids ^ WATER_2;
    }
    public int getAir(){
        return fluids ^ AIR_1 + fluids ^ AIR_2;
    }
    public int getLava(){
        return fluids ^ LAVA_1 + fluids ^ LAVA_2;
    }

    public void setPermeable(boolean c){
        if(c)
            fluids |= PERMEABLE;
        else
            fluids &= ~PERMEABLE;
    }

    public void copy(VoxelProperties toCopy){
        properties = toCopy.properties;
        health = toCopy.health;
        fluids = toCopy.fluids;
        level = toCopy.level;
    }

    public int getProps(){
        return (int)properties;
    }

    public void setProps(byte b){
        properties = b;
    }


}
