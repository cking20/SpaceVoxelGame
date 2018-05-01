package com.kinglogic.game.Actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kinglogic.game.Actors.Voxel.Voxel;
import com.kinglogic.game.Managers.IDs;
import com.kinglogic.game.Managers.ResourceManager;

/**
 * Created by chris on 5/1/2018.
 */

public class SelectedBlockActor extends Group {
    String currentBlockName = IDs.METAL_TEX;
    Voxel currentVoxel;

    public SelectedBlockActor(){
        currentVoxel = new Voxel(currentBlockName);
        addActor(currentVoxel);
    }
    public Drawable getSelectedDrawable(){
        return currentVoxel.getDrawable();
    }


    public void UpdateSelectedBlock(String name){
        if(currentBlockName.compareTo(name) == 0) return;
        currentBlockName = name;
        //update the selected voxel image
        currentVoxel.setDrawable(new TextureRegionDrawable(ResourceManager.ins().getVoxTex(name)));
    }
}
