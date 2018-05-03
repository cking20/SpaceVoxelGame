package com.kinglogic.game.Actors.Voxel;

/**
 * Created by chris on 4/1/2018.
 */

public class VoxelProperties {
    public static final byte COLLIDABLE = 1;

    private byte properties;

    public VoxelProperties(){
        properties = 0;
    }

    public boolean is(byte property){
        return !((properties ^ property) == property);
    }
    public void setProperty(boolean c, byte property){
        if(c)
            properties |= property;
        else
            properties &= ~property;
    }
    public void copy(VoxelProperties toCopy){
        properties = toCopy.properties;
    }

    public int getData(){
        return (int)properties;
    }
    public void setData(byte b){
        properties = b;
    }

}
