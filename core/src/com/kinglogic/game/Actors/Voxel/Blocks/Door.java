package com.kinglogic.game.Actors.Voxel.Blocks;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kinglogic.game.ChemestryFramework.ChemicalEvent;
import com.kinglogic.game.ChemestryFramework.ChemistryManager;
import com.kinglogic.game.ChemestryFramework.Properties;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Managers.WorldManager;
import com.kinglogic.game.Physics.Grid;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chris on 8/7/2018.
 */

public class Door extends Voxel {
    public static final boolean buildSensor = true;
    private boolean open = false;
    private byte openTime = 0;
    private Grid parentGrid;

    public Door(String name) {
        super(name);
    }

    @Override
    public boolean Update(float delta) {
        if(open){
            openTime--;
            if(openTime == 0) {
                toggle();
                return false;
            }else
                return true;
        }else{
            return false;
        }
    }

    @Override
    public void Receive(ChemicalEvent event) {
        switch (event.event){
            case SHOT:
                System.out.print("Door shot");
                //have to send it to the other door pieces
                if(event.sentBy instanceof Grid){
                    ChemicalEvent newEvent = event.clone();
                    newEvent.event = ChemistryManager.EventTypes.TRIGGERED;
                    parentGrid = (Grid)event.sentBy;
                    openTime = 64;
                    ((Grid)event.sentBy).PropagateEvent(newEvent, this, true);
                }
                toggle();
            case TRIGGERED:
                //the other door pieces already have it
                if(event.sentBy instanceof Grid) {
                    openTime = 64;
                    parentGrid = (Grid)event.sentBy;
                }
                toggle();

        }
        if(openTime > 0)
            ChemistryManager.ins().CheckThis(this);

    }

    private void toggle(){
        System.out.println("toggling "+this);
        open = !open;
        this.properties.setProperty(!open, Properties.COLLIDABLE);
        this.properties.setPermeable(open);
        if(open) {
            this.setDrawable(new TextureRegionDrawable(ResourceManager.ins().getVoxTex("base")));
            if(parentGrid != null) WorldManager.ins().recalculateGrid(parentGrid);
        }else {
            this.setDrawable(new TextureRegionDrawable(ResourceManager.ins().getVoxTex("door")));
            if(parentGrid != null) WorldManager.ins().recalculateGrid(parentGrid);
        }
    }
}
