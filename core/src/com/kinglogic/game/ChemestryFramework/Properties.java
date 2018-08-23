package com.kinglogic.game.ChemestryFramework;

/**
 * Created by chris on 8/6/2018.
 */

public class Properties {
    public static final byte COLLIDABLE = 1;
    public static final byte FLAMMABLE = 2;
    public static final byte ELECTRICUTABLE = 4;
    public static final byte REQUIRES_POWER = 8;
    public static final byte AFFECTED_BY_GRAVITY = 16;

    //Statuses that wear off
    public static final byte GRAVITIZED = 16;
    public static final byte ON_FIRE = 1;
    public static final byte ELECTROCUTED = 2;

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

    protected byte level;
    protected byte health;
    protected byte properties;
    protected byte status;
    protected int  durration = -1;

    public Properties(){
        level = 1;
        health = 127;
        properties = 0;
        status = 0;
        fluids = 0;
    }

    public Properties(byte level, byte health, byte properties, byte status, int durration, byte fluids){
        this.level = level;
        this.health = health;
        this.properties = properties;
        this.status = status;
        this.durration = durration;
        this.fluids = fluids;

    }

    /**
     * Returns true iff this voxel has the property
     * @param property to check
     * @return
     */
    public boolean hasProperty(byte property){
        return ((properties & property) == property);
    }


    public boolean hasStatus(byte state){
        return ((status & state) == state);
    }

    public void setProperty(boolean c, byte property){
        if(c)
            properties |= property;
        else
            properties &= ~property;
    }

    public void applyStatusFor(int ticks, byte state){
        status |= state;
        durration = ticks;
    }

    /**
     * Should be used to manually set a status, ignoring durations
     * @param c
     * @param state
     */
    public void setStatus(boolean c, byte state){
        if(c)
            status |= state;
        else
            status &= ~state;
    }

    public void updateStatusDurrations(){
        if (durration > 0)
            durration--;
        if (durration == 0){
            status = 0;
            durration = -1;
        }
    }

    public byte getLevel() {
        return level;
    }

    public byte getHealth() {
        return health;
    }

    public byte getProperties() {
        return properties;
    }

    public byte getStatus() {
        return status;
    }

    public int getDurration() {
        return durration;
    }












    /**
     * Returns true iff the voxel should allow the transfer of fluids
     * @return
     */
    public boolean isPermeable(){
        return !((fluids ^ PERMEABLE) == PERMEABLE);
    }
    public int getWater(){
        return (fluids ^ WATER_1 + fluids ^ WATER_2) >> 2;
    }
    public int getAir(){
        return (fluids ^ AIR_1 + fluids ^ AIR_2);
    }
    public int getLava(){
        return (fluids ^ LAVA_1 + fluids ^ LAVA_2) >> 4;
    }

    public byte getFluids(){
        return fluids;
    }

    public void setPermeable(boolean c){
        if(c)
            fluids |= PERMEABLE;
        else
            fluids &= ~PERMEABLE;
    }

    public void copy(Properties toCopy){
        properties = toCopy.properties;
        health = toCopy.health;
        fluids = toCopy.fluids;
        level = toCopy.level;
    }
}
