package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;

/**
 * Created by chris on 4/1/2018.
 */

public class ResourceManager {
    public static final int voxelPixelSize = 16;
    public Skin ui;

    private static ResourceManager instance;
    private final TextureAtlas voxelAtlas;
    private ArrayList<Shape> shapes;


    public static ResourceManager ins() {
        if(instance == null)
            instance = new ResourceManager();
        return instance;
    }

    private ResourceManager(){
        shapes = new ArrayList<Shape>();
        ui = new Skin(Gdx.files.internal("skin/skin.json"));
//            ui.getFont("font").getData().
        ui.getFont("font").setUseIntegerPositions(false);
        ui.getFont("font").getData().setScale(.3f,.3f);
        voxelAtlas = new TextureAtlas(Gdx.files.internal("images/voxelAtlas.atlas"));

    }
    public PolygonShape getNewPolyShape(){
        PolygonShape p = new PolygonShape();
        shapes.add(p);
        return p;
    }
    public ChainShape getNewChainShape(){
        ChainShape s = new ChainShape();
        shapes.add(s);
        return s;
    }

    public void dispose(){
        for(Shape s: shapes){
            s.dispose();
        }
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
