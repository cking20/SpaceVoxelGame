package com.kinglogic.game.Actors.Voxel.Blocks;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kinglogic.game.Actors.Voxel.VoxelProperties;
import com.kinglogic.game.ChemestryFramework.ChemicalEvent;
import com.kinglogic.game.ChemestryFramework.ChemistryManager;
import com.kinglogic.game.ChemestryFramework.MaterialModel;
import com.kinglogic.game.ChemestryFramework.Properties;
import com.kinglogic.game.Managers.IDs;
import com.kinglogic.game.Managers.ResourceManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by chris on 4/1/2018.
 */

public class Voxel extends Image implements MaterialModel{
    public VoxelProperties properties;

    public Voxel(String name){
        super(ResourceManager.ins().getVoxTex(name));
        this.setName(name);
        properties = new VoxelProperties();
        properties.setProperty(true,VoxelProperties.COLLIDABLE);
    }

    //Set the appropriate properties of the Voxel being built
    public static Voxel Build(String name){
        Voxel v;
        if(name.contains(IDs.BASE_TEX) || name.contains(IDs.GRASS_TEX)|| name.contains(IDs.GRID_TEX)){
            v = new Voxel(name);
            v.properties.setProperty(false, VoxelProperties.COLLIDABLE);
            v.properties.setPermeable(true);
        }else if(name.contains(IDs.DOOR_TEX)){
            v = new Door(name);
        }else
            v = new Voxel(name);

        return v;
    }

    public static Voxel Build(String name, VoxelProperties custom){
        Voxel v =  new Voxel(name);
        v.properties.copy(custom);
        return v;
    }

    @Override
    public LinkedList<ChemicalEvent> Output() {
        System.out.println("output: ");
        return null;
    }

    @Override
    public boolean Update(float delta) {
        System.out.println("updating voxel");
        return true;
    }

    @Override
    public void Recieve(ChemicalEvent event) {
//        System.out.println(event.event);
    }

    @Override
    public ChemistryManager.Elements getPrimaryElement() {
        if(properties.hasStatus(Properties.ON_FIRE))
            return ChemistryManager.Elements.FIRE;
        if(properties.hasStatus(Properties.ELECTROCUTED))
            return ChemistryManager.Elements.ELECTRICITY;

        return ChemistryManager.Elements.AIR;
    }
}
