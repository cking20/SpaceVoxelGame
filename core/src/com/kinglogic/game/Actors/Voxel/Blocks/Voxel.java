package com.kinglogic.game.Actors.Voxel.Blocks;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
    public Properties properties;

    public Voxel(String name){
        super(ResourceManager.ins().getVoxTex(name));
        this.setName(name);
        properties = new Properties();
        properties.setProperty(true,Properties.COLLIDABLE);
    }

    //Set the appropriate properties of the Voxel being built
    public static Voxel Build(String name){
        Voxel v;
        if(name.contains(IDs.BASE_TEX) || name.contains(IDs.GRASS_TEX)|| name.contains(IDs.GRID_TEX)){
            v = new Voxel(name);
            v.properties.setProperty(false, Properties.COLLIDABLE);
            v.properties.setPermeable(true);
        }else if(name.contains(IDs.DOOR_TEX)){
            v = new Door(name);
        }else
            v = new Voxel(name);

        return v;
    }

    public static Voxel Build(String name, Properties custom){
        Voxel v =  new Voxel(name);
        v.properties.copy(custom);
        return v;
    }

    @Override
    public LinkedList<ChemicalEvent> Output() {
        return null;
    }

    @Override
    public boolean Update(float delta) {
        System.out.println("updating voxel. done.");
        return false;
    }

    @Override
    public void Receive(ChemicalEvent event) {
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

    @Override
    public void ReactToTouch(MaterialModel model, Vector2 worldPosition) {

    }

    /**
     * Tells if a sensor collision box should be made at this voxel, and the range of it.
     * 0 is implied that there should not be a sensor
     * @return the radius in blocks of the sensor
     */
    public int buildCollisionSensor(){
        return 0;
    }


}
