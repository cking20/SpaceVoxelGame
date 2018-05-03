package com.kinglogic.game.Actors.Voxel;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kinglogic.game.Managers.IDs;
import com.kinglogic.game.Managers.ResourceManager;

/**
 * Created by chris on 4/1/2018.
 */

public class Voxel extends Image {
    public VoxelProperties properties;

    public Voxel(String name){
        super(ResourceManager.ins().getVoxTex(name));
        this.setName(name);
        properties = new VoxelProperties();
        properties.setProperty(true,VoxelProperties.COLLIDABLE);
    }

    public static Voxel Build(String name){
        Voxel v = new Voxel(name);
        if(name.compareTo(IDs.BASE_TEX) == 0){
            v.properties.setProperty(false, VoxelProperties.COLLIDABLE);
        }
        return v;
    }

    public static Voxel Build(String name, VoxelProperties custom){
        Voxel v =  new Voxel(name);
        v.properties.copy(custom);
        return v;
    }
}
