package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kinglogic.game.Actors.Entities.Entity;
import com.kinglogic.game.Actors.Voxel.Voxel;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Constants;
import com.kinglogic.game.Interfaces.AI;
import com.kinglogic.game.Physics.DynamicGrid;
import com.kinglogic.game.Physics.EntityBody;
import com.kinglogic.game.Physics.FilterIDs;
import com.kinglogic.game.Physics.Grid;
import com.kinglogic.game.Physics.StaticGrid;
import com.kinglogic.game.Physics.WorldContactListner;

import java.util.ArrayList;
import java.util.HashSet;

import box2dLight.DirectionalLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

/**
 * Created by chris on 4/1/2018.
 */

public class WorldManager {
    private static final boolean debug = Constants.DEBUG;
    private static WorldManager instance;
    private Stage worldStage;
    private Group gridsGroup;
    private Group entityGroup;
    private World worldPhysics;
    private RayHandler rayHandler;
    private Viewport view;
    public final OrthographicCamera viewCam;
    private Box2DDebugRenderer debugRenderer;
    private HashSet<Grid> grids;
    private HashSet<EntityBody> entities;

    private ArrayList<Vector2> removalQueue;
    private ArrayList<Vector2> addalQueue;

    private ArrayList<EntityBody> entityRemovalQueue;



    private Image background;

    //debug
    FPSLogger fapsLogger;


    public static WorldManager ins() {
        if(instance == null)
            instance = new WorldManager();
        return instance;
    }

    private WorldManager(){
        grids = new HashSet<Grid>();
        entities = new HashSet<EntityBody>();
        removalQueue = new ArrayList<Vector2>();
        entityRemovalQueue = new ArrayList<EntityBody>();
        addalQueue = new ArrayList<Vector2>();
        fapsLogger = new FPSLogger();
        viewCam = CameraManager.ins().mainCamera;
        view = new FitViewport(Gdx.graphics.getHeight(), Gdx.graphics.getHeight()*ResourceManager.ins().calculateAspectRatio(), viewCam);
        view.apply();
        BuildWorldt();
        if(debug) {
//            worldStage.setDebugAll(true);
//            worldStage.setDebugInvisible(false);
            worldStage.setDebugUnderMouse(true);
        }
        worldPhysics.setContactListener(new WorldContactListner());
        rayHandler = new RayHandler(worldPhysics);
        rayHandler.setAmbientLight(.0f);//.8f);
        rayHandler.setShadows(true);
        DirectionalLight dl = new DirectionalLight(rayHandler,100,
                new Color(Color.rgba8888(.20f, .20f, .20f, 1.0f)),-91);
        dl.setSoftnessLength(300f);
        Filter dlF = new Filter();
        dlF.maskBits = FilterIDs.GRID;
        dl.setContactFilter(dlF);
        debugRenderer = new Box2DDebugRenderer();

    }
    public Batch getBatch(){
        return worldStage.getBatch();
    }

    public boolean addVoxelScreenPosition(float x, float y, String block){
        boolean hitFlag = false;
        for(Grid g : grids){
            if(g.addVoxelScreenPos(Voxel.Build(block),new Vector2(x,y))){
                hitFlag = true;
                rethinkShape(g);
            }
        }
        return hitFlag;
    }

    public void removeVoxelScreenPosition(float x, float y){
//        boolean hitFlag = false;
        removalQueue.add(screenToWorldCoords(new Vector2(x,y)));
//        return hitFlag;
    }

    public boolean addVoxelWorldPosition(float x, float y, String block){
        boolean hitFlag = false;
        for(Grid g : grids){
            if(g.addVoxelWorldPos(Voxel.Build(block),new Vector2(x,y))){
                hitFlag = true;
                rethinkShape(g);
            }
        }
        return hitFlag;
    }

    public void removeVoxelWorldPosition(float x, float y){
//        boolean hitFlag = false;
        removalQueue.add(new Vector2(x,y));
//        return hitFlag;
    }



    public void BuildWorldt(){
        if(worldStage == null) {
            worldStage = new Stage(view);
            gridsGroup = new Group();
            entityGroup = new Group();
            background = new Image(ResourceManager.ins().nebula);
            background.scaleBy(1.5f,1.5f);
            background.moveBy(-background.getWidth()/2, -background.getHeight()/2);
            worldStage.addActor(background);
            worldStage.addActor(gridsGroup);
            worldStage.addActor(entityGroup);
        }
        if(worldPhysics == null){
            worldPhysics = new World(new Vector2(0,0), true);
        }
        //todo generate stuff with a procedural algorithm
    }

    public void update(float delta){
        for(Grid g : grids) {
            g.updateRendering();
        }
        for(EntityBody e : entities) {
            e.updateRendering();
            if(e instanceof AI)
                ((AI) e).Think(worldPhysics);
        }
        worldStage.act(delta);
        doPhysicsStep(delta);
        removeQueued();
        rayHandler.update();
        rayHandler.setCombinedMatrix(CameraManager.ins().mainCamera);//worldStage.getCamera());
    }

    public void render(){
        background.setScale(4f*CameraManager.ins().mainCamera.zoom);
        background.setPosition(
                CameraManager.ins().mainCamera.position.x-background.getWidth()*background.getScaleX()/2,
                CameraManager.ins().mainCamera.position.y-background.getHeight()*background.getScaleY()/2);

        worldStage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
        worldStage.getViewport().apply(true);

        CameraManager.ins().Update(0f);

        worldStage.draw();
//        worldStage.getBatch().begin();
//        for(EntityBody e : entities) {
//            e.view.draw(worldStage.getBatch(),1f);
//        }
//        worldStage.getBatch().end();
        rayHandler.render();
        if(debug) {
            debugRenderer.render(worldPhysics, viewCam.combined);
            fapsLogger.log();
        }
    }
    public void resize(int width, int height){
        worldStage.getViewport().update(width,height, true);
    }
    private void removeQueued(){
        for (Vector2 wp : removalQueue){
            HashSet<Grid> gridsClone = (HashSet<Grid>) grids.clone();
            for(Grid g : gridsClone){
                if(g.removeVoxelWorldPos(new Vector2(wp.x,wp.y))){
                    rethinkShape(g);
                }
            }
        }
        removalQueue.clear();

        for(EntityBody e : entityRemovalQueue){
            entities.remove(e);
            entityGroup.removeActor(e.view);
            //worldStage.getActors().removeValue(e.view,true);
            if(e.myBody != null){
                e.dispose();
                worldPhysics.destroyBody(e.myBody);
            }
        }
        entityRemovalQueue.clear();


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
        if(rayHandler != null)
            rayHandler.dispose();
    }

    public Vector2 screenToWorldCoords(Vector2 pos){
        return worldStage.screenToStageCoordinates(pos);
    }

    /**
     * Raycast from point to point, returning the position of the first hit
     * @param from
     * @param to
     * @return
     */
    public Vector2 raycast(Vector2 from, Vector2 to){
        final Vector2 hitPos = new Vector2();
        RayCastCallback callback = new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if(fixture.getBody().getUserData() instanceof Grid){
                    hitPos.x = point.x;
                    hitPos.y = point.y;
                }
                return 0;
            }
        };
        worldPhysics.rayCast(callback, from, to);
        return hitPos;
    }

    public void addGridToWorld(Grid d){
        if(!grids.contains(d)) {
            gridsGroup.addActor(d.voxels);
            //todo make funciton in Grid to parse through voxels and create fixture
            //d.recalculateShape();
            d.myBody = worldPhysics.createBody(d.bodyDef);
            d.myBody.setUserData(d);
//            System.out.println(d.myBody);
//            System.out.println(d.physicsShapes);
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
            entityGroup.addActor(e.view);
            e.myBody = worldPhysics.createBody(e.bodyDef);
            e.myBody.setUserData(e);
            e.CreateFixture();
            e.CreateSight(e.viewDistance);
            entities.add(e);
        }
    }
    public void removeEntityFromWorld(EntityBody e){
        entityRemovalQueue.add(e);
    }

    public void rethinkShape(Grid d){
//        d.recalculateShape();
        if(!grids.contains(d))
            grids.add(d);
    }


    public void GenerateAsteroid(int posX, int posY, int size){
        int start = VoxelCollection.maxSize/2-size/2;
        int end = VoxelCollection.maxSize/2+size/2;
        VoxelCollection astVox = new VoxelCollection(Voxel.Build(IDs.ROCK_TEX), new Vector2(posX,posY));
        boolean[][] putBlocks = PCGManager.ins().generateBetterAsteroid(size);
        for (int i = start; i < end; i++){
            for (int j = start; j < end; j++){
                if(putBlocks[i-start][j-start])
                    astVox.hardAddVoxelIndex(Voxel.Build(IDs.ROCK_TEX),i,j);
            }
        }
        DynamicGrid astGrid = new DynamicGrid(astVox);
        addGridToWorld(astGrid);
//        astGrid.myBody.setTransform(posX,posY,0);
        astGrid.voxels.checkAllConnected();
        astGrid.recalculateShape();
    }

    public void ApplyLightToBody(Body b){
        Filter f = new Filter();
        f.maskBits = FilterIDs.GRID;
        PointLight pl = new PointLight(rayHandler,60, new Color(Color.rgba8888(.40f, .40f, .20f, 1.0f)),
                ResourceManager.voxelPixelSize*40, 0,0);
        pl.setContactFilter(f);
        pl.attachToBody(b,8f,8f);
        pl.setSoftnessLength(100f);
    }

}
