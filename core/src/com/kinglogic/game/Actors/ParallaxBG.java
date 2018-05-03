package com.kinglogic.game.Actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.kinglogic.game.Managers.ResourceManager;

/**
 * Created by chris on 5/2/2018.
 */

public class ParallaxBG extends Image {
    Array<TextureRegion> layers;

    public ParallaxBG(){
        super();
        layers = new Array<TextureRegion>();
        //ResourceManager.ins()
        //todo https://libgdx.info/Parallax/
    }
}
