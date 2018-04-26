package com.kinglogic.game.Actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kinglogic.game.Actors.Voxel.Voxel;

import java.util.ArrayList;

/**
 * Created by chris on 4/24/2018.
 */

public class InventoryActor extends Group {
    private Table myTable;
    public int rowLen = 2;

    public InventoryActor(ArrayList<Voxel> voxels){
        myTable = new Table();

        for (int i = 0; i < voxels.size(); i++) {
            for (int j = 0; j < rowLen; j++) {
                myTable.addActor(voxels.get(i));
            }
            myTable.row();

        }

    }
}
