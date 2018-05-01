package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
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
    private TextureAtlas particleAtlas;
    private ParticleEffectPool explosionPool;
    private Array<ParticleEffectPool.PooledEffect> effects;
    private ParticleEffect explosion;
    public Texture nebula;


    public static ResourceManager ins() {
        if(instance == null)
            instance = new ResourceManager();
        return instance;
    }

    private ResourceManager(){
        shapes = new ArrayList<Shape>();
        projecttilePool = new ArrayDeque<Projectile>();
        explosion = new ParticleEffect();
        effects = new Array<ParticleEffectPool.PooledEffect>();
        //explosion.lo
        particleAtlas = new TextureAtlas(Gdx.files.internal("particles/particleAtlas.atlas"));
        explosion.load(Gdx.files.internal("particles/Explosion"), particleAtlas, "whitelight");
        explosionPool = new ParticleEffectPool(explosion, 1, 10);
//        projecttilePool = new Projectile[50];
        ui = new Skin(Gdx.files.internal("skin/skin.json"));
//            ui.getFont("font").getData().
        ui.getFont("font").setUseIntegerPositions(false);
        ui.getFont("font").getData().setScale(.3f,.3f);
        voxelAtlas = new TextureAtlas(Gdx.files.internal("images/voxelAtlas.atlas"));
        spriteAtlas = new TextureAtlas(Gdx.files.internal("images/spriteAtlas.atlas"));
        nebula = new Texture(Gdx.files.internal("images/nebula.png"));

    }
    public void Update(float delta){
        WorldManager.ins().getBatch().begin();
        for (int i = effects.size - 1; i >= 0; i--) {
            ParticleEffectPool.PooledEffect effect = effects.get(i);
            effect.draw(WorldManager.ins().getBatch(), delta);
            if (effect.isComplete()) {
                effect.free();
                effects.removeIndex(i);
            }
        }
        WorldManager.ins().getBatch().end();
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
    public void disposeOfProjectile(Projectile p){
        projecttilePool.remove(p);
        WorldManager.ins().removeEntityFromWorld(p);
    }

    public void createExplosionEffect(Vector2 position){
        ParticleEffectPool.PooledEffect p = explosionPool.obtain();
        p.setPosition(position.x,position.y);
        effects.add(p);
    }
    public void dispose(){
        for(Shape s: shapes){
            s.dispose();
        }
        for (int i = effects.size - 1; i >= 0; i--)
            effects.get(i).free();
        effects.clear();
        explosionPool.clear();
        explosion.dispose();
        ui.dispose();
        voxelAtlas.dispose();
        spriteAtlas.dispose();
        particleAtlas.dispose();
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
