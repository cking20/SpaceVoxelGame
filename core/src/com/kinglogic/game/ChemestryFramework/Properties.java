package com.kinglogic.game.ChemestryFramework;

/**
 * Created by chris on 8/6/2018.
 */

public abstract class Properties {
    public static final byte COLLIDABLE = 1;
    public static final byte FLAMMABLE = 2;
    public static final byte ELECTRICUTABLE = 4;
    public static final byte REQUIRES_POWER = 8;
    public static final byte AFFECTED_BY_GRAVITY = 16;

    //Statuses that wear off
    public static final byte GRAVITIZED = 16;
    public static final byte ON_FIRE = 1;
    public static final byte ELECTROCUTED = 2;

    protected byte level;
    protected byte health;
    protected byte properties;
    protected byte status;
    protected int  durration = -1;

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
}
