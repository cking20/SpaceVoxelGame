package com.kinglogic.game.Actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kinglogic.game.Actors.Voxel.Blocks.Voxel;
import com.kinglogic.game.Managers.IDs;
import com.kinglogic.game.Managers.ResourceManager;

/**
 * Created by chris on 5/1/2018.
 */

public class SelectedBlockActor extends Group {
    public Color selectedColor = IDs.ins().getColor(0);
    String currentBlockName = IDs.getIDList().get(0);
    private Voxel myVoxel;


    public SelectedBlockActor(){
        super();
        myVoxel = Voxel.Build(currentBlockName);
        this.addActor(myVoxel);
//        setDrawable(new TextureRegionDrawable(ResourceManager.ins().getVoxTex(currentBlockName)));
    }

    public void UpdateSelectedBlock(String name, Color color){
        if(currentBlockName.compareTo(name) != 0) {
            currentBlockName = name;
            //update the selected voxel image
            myVoxel.setDrawable(new TextureRegionDrawable(ResourceManager.ins().getVoxTex(name)));

        }
        if(!selectedColor.equals(color)){
            selectedColor = color;
            myVoxel.setColor(color);
        }
    }
}
