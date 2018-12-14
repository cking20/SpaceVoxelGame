package com.kinglogic.game.Actors.Voxel.Blocks;

import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.ChemestryFramework.ChemicalEvent;
import com.kinglogic.game.ChemestryFramework.ChemistryManager;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Physics.Grid;
import com.kinglogic.game.Physics.Projectile;

/**
 * Created by chris on 9/11/2018.
 */

public class Cannon extends Voxel {
    private byte openTime = 0;
    private Grid parentGrid;

    public Cannon(String name) {
        super(name);
    }

    /**
     * This assumes that the position of the voxel in worldSpace
     * @param event
     */
    @Override
    public void Receive(ChemicalEvent event) {
        switch (event.event){
            case SHOT:
                //todo this isnt how this should work, the shooting should be based on controlling input, not a shot event!!!!
                System.out.println("Cannon shot, shooting @ " + event.position);
                if(event.sentBy instanceof Grid){
                    ChemicalEvent newEvent = event.clone();
                    newEvent.event = ChemistryManager.EventTypes.TRIGGERED;
                    parentGrid = (Grid)event.sentBy;
                    Shoot(event.position);
                }
            case TRIGGERED:
                //the other door pieces already have it
                if(event.sentBy instanceof Grid) {
                    parentGrid = (Grid)event.sentBy;
                    Shoot(event.position);
                }

        }


    }

    protected void Shoot(Vector2 position){
        Shoot(new Vector2(0,1), position);
    }

    private void Shoot(Vector2 direction, Vector2 voxelPosition){
        Projectile p;
        p = ResourceManager.ins().getProjectile("projectile", voxelPosition.cpy().add(direction.scl(16f).rotate((float) Math.toDegrees(parentGrid.myBody.getAngle()))));
        p.lifetime = 2000;
        p.hitPlayers = true;
        p.Fire((direction).scl(100));

    }
}
