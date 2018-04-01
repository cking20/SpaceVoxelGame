package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by chris on 4/1/2018.
 */

public class ResourceManager {
    public static final int voxelPixelSize = 16;
    public Skin ui;

    private static ResourceManager instance;
    private final TextureAtlas voxelAtlas;


    public static ResourceManager ins() {
        if(instance == null)
            instance = new ResourceManager();
        return instance;
    }

    private ResourceManager(){
        ui = new Skin(Gdx.files.internal("skin/skin.json"));
//            ui.getFont("font").getData().
        ui.getFont("font").setUseIntegerPositions(false);
        ui.getFont("font").getData().setScale(.3f,.3f);
        voxelAtlas = new TextureAtlas(Gdx.files.internal("images/voxelAtlas.atlas"));

    }

    public void dispose(){
        ui.dispose();
        voxelAtlas.dispose();

    }

    public float calculateAspectRatio(){
        return (float) Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
    }
    public TextureAtlas.AtlasRegion getVoxTex(String name){
        return voxelAtlas.findRegion(name);
    }

}
