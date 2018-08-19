package com.kinglogic.game.Actors.Voxel.Blocks;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kinglogic.game.Actors.Voxel.VoxelProperties;
import com.kinglogic.game.ChemestryFramework.ChemicalEvent;
import com.kinglogic.game.ChemestryFramework.ChemistryManager;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Managers.WorldManager;
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
                System.out.print("Door shot");
                //have to send it to the other door pieces
                toggle();
                if(event.sentBy instanceof Grid){
                    ChemicalEvent newEvent = event.clone();
                    newEvent.event = ChemistryManager.EventTypes.TRIGGERED;
                    ((Grid)event.sentBy).PropagateEvent(newEvent, this, false);//todo connected only should be true
                }
            case TRIGGERED:
                //the other door pieces already have it
                toggle();
                if(event.sentBy instanceof Grid)
                    WorldManager.ins().recalculateGrid(((Grid)event.sentBy));

        }

    }

    private void toggle(){
        open = !open;
        if(open)
            this.setDrawable(new TextureRegionDrawable(ResourceManager.ins().getVoxTex("base")));
        else
            this.setDrawable(new TextureRegionDrawable(ResourceManager.ins().getVoxTex("door")));
        this.properties.setProperty(!open, VoxelProperties.COLLIDABLE);
        this.properties.setPermeable(open);
    }
}
