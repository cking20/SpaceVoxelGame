package com.kinglogic.game.Actors.Voxel;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kinglogic.game.Managers.ResourceManager;

/**
 * Created by chris on 4/1/2018.
 */

public class Voxel extends Image {
    public VoxelProperties properties;

    public Voxel(String name){
        super(ResourceManager.ins().getVoxTex(name));
        properties = new VoxelProperties();
    }

}
