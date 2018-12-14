package com.kinglogic.game.Actors.Entities;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kinglogic.game.Managers.ResourceManager;

/**
 * Created by chris on 4/1/2018.
 */

public class Entity extends Image{
    //will use Animation<TextureRegion>
    public Entity(String name) {
        super(ResourceManager.ins().getSpriteTex(name));
        this.setName(name);
    }
}
