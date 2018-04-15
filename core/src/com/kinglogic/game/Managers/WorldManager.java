package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kinglogic.game.Actors.Entities.Entity;
import com.kinglogic.game.Actors.Voxel.Voxel;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Constants;
import com.kinglogic.game.Physics.DynamicGrid;
import com.kinglogic.game.Physics.EntityBody;
import com.kinglogic.game.Physics.Grid;
import com.kinglogic.game.Physics.StaticGrid;

import java.util.HashSet;

/**
 * Created by chris on 4/1/2018.
 */

public class WorldManager {
    private static WorldManager instance;
    private Stage worldStage;
    private World worldPhysics;
    private Viewport view;
    public final OrthographicCamera viewCam;
    private Box2DDebugRenderer debugRenderer;
    private HashSet<Grid> grids;
    private HashSet<EntityBody> entities;


    public static WorldManager ins() {
        if(instance == null)
            instance = new WorldManager();
        return instance;
    }

    private WorldManager(){
        grids = new HashSet<Grid>();
        entities = new HashSet<EntityBody>();
        viewCam = CameraManager.ins().mainCamera;
        view = new FitViewport(Gdx.graphics.getHeight(), Gdx.graphics.getHeight()*ResourceManager.ins().calculateAspectRatio(), viewCam);
        view.apply();
        BuildWorldt();
        debugRenderer = new Box2DDebugRenderer();
    }

    public boolean addVoxelScreenPosition(float x, float y, String block){
        boolean hitFlag = false;
        for(Grid g : grids){
            if(g.addVoxelScreenPos(new Voxel(block),new Vector2(x,y))){
                hitFlag = true;
                rethinkShape(g);
            }
        }
        return hitFlag;
    }

    public boolean removeVoxelScreenPosition(float x, float y){
        boolean hitFlag = false;
        HashSet<Grid> gridsClone = (HashSet<Grid>) grids.clone();
        for(Grid g : gridsClone){
            if(g.removeVoxelScreenPos(new Vector2(x,y))){
                hitFlag = true;
                rethinkShape(g);
            }
        }
        return hitFlag;
    }

    public void BuildWorldt(){
        if(worldStage == null)
            worldStage = new Stage(view);
        if(worldPhysics == null)
            worldPhysics = new World(new Vector2(0,0), true);
        //todo generate stuff with a procedural algorithm
    }

    public void update(float delta){
        for(Grid g : grids) {
            g.updateRendering();
            //g.myBody.setTransform(g.myBody.getPosition().x%(Gdx.graphics.getWidth()),(g.myBody.getPosition().y%(Gdx.graphics.getHeight())), g.myBody.getAngle());
        }
        for(EntityBody e : entities) {
            e.updateRendering();
            //g.myBody.setTransform(g.myBody.getPosition().x%(Gdx.graphics.getWidth()),(g.myBody.getPosition().y%(Gdx.graphics.getHeight())), g.myBody.getAngle());
        }
        worldStage.act(delta);
        doPhysicsStep(delta);
        //todo update actors
    }
    public void render(){
        worldStage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
        worldStage.getViewport().apply(true);

//        worldStage.getCamera().translate(200f,0,0);
        System.out.println(worldStage.getCamera().position);
        CameraManager.ins().Update(0f);
        System.out.println(worldStage.getCamera().position);

        worldStage.draw();
        debugRenderer.render(worldPhysics, viewCam.combined);
    }
    public void resize(int width, int height){
        worldStage.getViewport().update(width,height, true);
    }

    private float accumulator = 0;
    private void doPhysicsStep(float deltaTime) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= Constants.TIME_STEP) {
            worldPhysics.step(Constants.TIME_STEP, Constants.VELOCITY_ITERATIONS, Constants.POSITION_ITERATIONS);
            accumulator -= Constants.TIME_STEP;
        }
    }

    public void dispose(){
        if(worldStage != null)
            worldStage.dispose();
        if(worldPhysics != null)
            worldPhysics.dispose();
    }

    public Vector2 screenToWorldCoords(Vector2 pos){
        return worldStage.screenToStageCoordinates(pos);
    }

    public void addGridToWorld(Grid d){
        if(!grids.contains(d)) {
            worldStage.addActor(d.voxels);
            //todo make funciton in Grid to parse through voxels and create fixture
            //d.recalculateShape();
            d.myBody = worldPhysics.createBody(d.bodyDef);
            System.out.println(d.myBody);
            System.out.println(d.physicsShapes);
            d.recalculateShape();
            //d.fixture.add(d.myBody.createFixture(d.fixtureDef));
            grids.add(d);
        }
    }
    public void removeGridFromWorld(Grid g){
        System.out.println("remove grid called");
        grids.remove(g);
        worldStage.getActors().removeValue(g.voxels,true);
        if(g.myBody != null){
            g.dispose();
            worldPhysics.destroyBody(g.myBody);
        }
    }

    public void addEntityToWorld(EntityBody e){
        if(!entities.contains(e)) {
            worldStage.addActor(e.view);
            //todo make funciton in Grid to parse through voxels and create fixture
            //d.recalculateShape();
            e.myBody = worldPhysics.createBody(e.bodyDef);
            e.CreateFixture();
            entities.add(e);
        }
    }
    public void removeEntityFromWorld(EntityBody e){
        entities.remove(e);
        worldStage.getActors().removeValue(e.view,true);
        if(e.myBody != null){
            e.dispose();
            worldPhysics.destroyBody(e.myBody);
        }
    }

    public void rethinkShape(Grid d){
//        d.recalculateShape();
        if(!grids.contains(d))
            grids.add(d);
    }


    public void GenerateAsteroid(int posX, int posY, int size){
        int start = VoxelCollection.maxSize/2-size/2;
        int end = VoxelCollection.maxSize/2+size/2;
        VoxelCollection astVox = new VoxelCollection(new Voxel(IDs.ROCK_TEX), new Vector2(posX,posY));
        for (int i = start; i < end; i++){
            for (int j = start; j < end; j++){
                astVox.addVoxelIndex(new Voxel(IDs.ROCK_TEX),i,j);
            }
        }
        StaticGrid astGrid = new StaticGrid(astVox);
        addGridToWorld(astGrid);
    }

}
