package com.kinglogic.game.Actors.Voxel.Blocks;

import com.kinglogic.game.Actors.Voxel.VoxelProperties;
import com.kinglogic.game.ChemestryFramework.ChemicalEvent;
import com.kinglogic.game.ChemestryFramework.ChemistryManager;
import com.kinglogic.game.Physics.Grid;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chris on 8/7/2018.
 */

public class Door extends Voxel {
    private boolean open = false;

    public Door(String name) {
        super(name);
    }

    @Override
    public void Recieve(ChemicalEvent event) {
        switch (event.event){
            case SHOT:
                //have to send it to the other door pieces
                if(event.sentBy instanceof Grid){
                    ChemicalEvent newEvent = event.clone();
                    newEvent.event = ChemistryManager.EventTypes.TRIGGERED;
                    ((Grid)event.sentBy).PropagateEvent(newEvent, this, false);//todo connected only should be true
                }
            case TRIGGERED:
                //the other door pieces already have it
                toggle();
                if(event.sentBy instanceof Grid)
                    ((Grid)event.sentBy).recalculateShape();

        }

    }

    private void toggle(){
        open = !open;
        this.properties.setProperty(!open, VoxelProperties.COLLIDABLE);
        this.properties.setPermeable(open);
    }
}
