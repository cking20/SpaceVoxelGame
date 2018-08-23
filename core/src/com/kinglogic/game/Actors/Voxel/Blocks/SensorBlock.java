package com.kinglogic.game.Actors.Voxel.Blocks;

import com.kinglogic.game.ChemestryFramework.ChemicalEvent;

/**
 * Created by chris on 8/23/2018.
 */

public class SensorBlock extends Voxel {
    public SensorBlock(String name) {
        super(name);
    }

    @Override
    public boolean Update(float delta) {
        return false;
    }

    @Override
    public void Receive(ChemicalEvent event) {
        System.out.println("sensor block got" +event.event);
    }


    @Override
    public int buildCollisionSensor(){
        return 1;
    }
}
