package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.kinglogic.game.Physics.Projectile;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by chris on 4/1/2018.
 */

public class ResourceManager {
    public static final int voxelPixelSize = 16;
    public Skin ui;

    private static ResourceManager instance;
    private final TextureAtlas voxelAtlas;
    private final TextureAtlas spriteAtlas;
    private ArrayList<Shape> shapes;
    private Queue<Projectile> projecttilePool;
    public Texture nebula;


    public static ResourceManager ins() {
        if(instance == null)
            instance = new ResourceManager();
        return instance;
    }

    private ResourceManager(){
        shapes = new ArrayList<Shape>();
        projecttilePool = new ArrayDeque<Projectile>();
//        projecttilePool = new Projectile[50];
        ui = new Skin(Gdx.files.internal("skin/skin.json"));
//            ui.getFont("font").getData().
        ui.getFont("font").setUseIntegerPositions(false);
        ui.getFont("font").getData().setScale(.3f,.3f);
        voxelAtlas = new TextureAtlas(Gdx.files.internal("images/voxelAtlas.atlas"));
        spriteAtlas = new TextureAtlas(Gdx.files.internal("images/spriteAtlas.atlas"));
        nebula = new Texture(Gdx.files.internal("images/nebula.png"));

    }
    public PolygonShape getNewPolyShape(){
        PolygonShape p = new PolygonShape();
        shapes.add(p);
        return p;
    }
//    public Projectile Fire(Vector2 position, Vector2 velocity){
//
//    }
    public CircleShape getNewCircleShape(){
        CircleShape c = new CircleShape();
        shapes.add(c);
        return c;
    }
    public ChainShape getNewChainShape(){
        ChainShape s = new ChainShape();
        shapes.add(s);
        return s;
    }
    public void disposeOfShape(Shape s){
        shapes.remove(s);
        s.dispose();
    }
    public Projectile getProjectile(String texName, Vector2 position){
        Projectile p;
        if(projecttilePool.size() < 50){
            p = new Projectile(texName, position);
            projecttilePool.add(p);
            WorldManager.ins().addEntityToWorld(p);
        }else {
            p = projecttilePool.poll();
            WorldManager.ins().removeEntityFromWorld(p);
            p = new Projectile(texName, position);
            projecttilePool.add(p);
            WorldManager.ins().addEntityToWorld(p);
        }
//        p.myBody.setTransform(position.x, position.y, 0);
        return p;
    }

    public void dispose(){
        for(Shape s: shapes){
            s.dispose();
        }
        ui.dispose();
        voxelAtlas.dispose();
        spriteAtlas.dispose();
        nebula.dispose();

    }

    public float calculateAspectRatio(){
        return (float) Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
    }
    public TextureAtlas.AtlasRegion getVoxTex(String name){
        return voxelAtlas.findRegion(name);
    }
    public TextureAtlas.AtlasRegion getSpriteTex(String name){
        return spriteAtlas.findRegion(name);
    }

}
