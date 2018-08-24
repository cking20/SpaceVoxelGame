package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kinglogic.game.Actors.Entities.Entity;
import com.kinglogic.game.Actors.ParallaxBG;
import com.kinglogic.game.Actors.Voxel.Blocks.Voxel;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Constants;
import com.kinglogic.game.Interfaces.AI;
import com.kinglogic.game.Models.WorldState;
import com.kinglogic.game.Physics.DynamicGrid;
import com.kinglogic.game.Physics.EntityBody;
import com.kinglogic.game.Physics.FilterIDs;
import com.kinglogic.game.Physics.Grid;
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
//    private HashSet<Grid> grids;
//    private HashSet<EntityBody> entities;

    private ArrayList<Vector2> removalQueue;
    private ArrayList<Vector2>playerRemovalQueue;
    private ArrayList<Vector2> addalQueue;

    private ArrayList<EntityBody> entityRemovalQueue;
    private ArrayList<Grid> gridRemovalQueue;
    private ArrayList<Grid> gridRecalculationQueue;

    //private String worldName;
    public WorldState currentLevel;

    private boolean queuedWorldSave = false;
    private String toLoad = "infinity";
    private boolean queuedWorldLoad = false;

//    private Image background;
    private ParallaxBG background;
    //debug
    FPSLogger fapsLogger;


    public static WorldManager ins() {
        if(instance == null)
            instance = new WorldManager();
        return instance;
    }

    private WorldManager(){
        //todo load this dont just create it
//        worldName = "testWorld";

//        grids = new HashSet<Grid>();
//        entities = new HashSet<EntityBody>();
        removalQueue = new ArrayList<Vector2>();
        playerRemovalQueue = new ArrayList<Vector2>();
        entityRemovalQueue = new ArrayList<EntityBody>();
        gridRemovalQueue = new ArrayList<Grid>();
        gridRecalculationQueue = new ArrayList<Grid>();
        addalQueue = new ArrayList<Vector2>();
        fapsLogger = new FPSLogger();
        viewCam = CameraManager.ins().mainCamera;
        view = new FitViewport(Gdx.graphics.getHeight(), Gdx.graphics.getHeight()*ResourceManager.ins().calculateAspectRatio(), viewCam);
        view.apply();
        BuildWorldt();
        if(debug) {
            worldStage.setDebugAll(true);
            worldStage.setDebugInvisible(false);
            worldStage.setDebugUnderMouse(true);
            debugRenderer = new Box2DDebugRenderer();
        }
        worldPhysics.setContactListener(new WorldContactListner());

        rayHandler = new RayHandler(worldPhysics);
        rayHandler.setAmbientLight(.8f);//.8f);
        rayHandler.setShadows(true);
//        DirectionalLight dl = new DirectionalLight(rayHandler,100,new Color(Color.rgba8888(.20f, .20f, .20f, 1.0f)),-91)
        PointLight dl = new PointLight(rayHandler,100, new Color(Color.rgba8888(.20f, .20f, .20f, 1.0f)),200f,0,0);

        dl.setSoftnessLength(16f);//300f);
        Filter dlF = new Filter();
        dlF.maskBits = FilterIDs.GRID;
        dl.setContactFilter(dlF);



    }
    public Batch getBatch(){
        return worldStage.getBatch();
    }


    public boolean addVoxelScreenPosition(float x, float y, String block){
        boolean hitFlag = false;
        HashSet<Grid> clone = (HashSet<Grid>) currentLevel.grids.clone();
        for(Grid g : clone){
            if(g.addVoxelScreenPos(Voxel.Build(block),new Vector2(x,y))){
                hitFlag = true;
                rethinkShape(g);
            }
        }
        return hitFlag;
    }

    public void QueueLoad(String sector){
        toLoad = sector;
        queuedWorldLoad = true;
    }
    public void QueueSave(){
        queuedWorldSave = true;
    }

    public void clearLevel(){
        gridRemovalQueue.addAll(currentLevel.grids);
        entityRemovalQueue.addAll(currentLevel.entities);
        entityRemovalQueue.remove(GameManager.ins().getThePlayer());
        gridRemovalQueue.remove(GameManager.ins().getThePlayer().lastControlled);
    }

    public void removeVoxelScreenPosition(float x, float y){
//        boolean hitFlag = false;
        removalQueue.add(screenToWorldCoords(new Vector2(x,y)));
//        return hitFlag;
    }
    public void playerRemoveVoxelScreenPosition(float x, float y){
//        boolean hitFlag = false;
        playerRemovalQueue.add(screenToWorldCoords(new Vector2(x,y)));
//        return hitFlag;
    }

    public boolean addVoxelWorldPosition(float x, float y, String block){
        boolean hitFlag = false;
        for(Grid g : currentLevel.grids){
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

    public Grid getGridAtWorldPos(Vector2 pos){
        HashSet<Grid> gridsClone = (HashSet<Grid>) currentLevel.grids.clone();
        for(Grid g : gridsClone){
            if(g.isWorldPositionInGrid(new Vector2(pos.x,pos.y))){
                return g;
            }
        }
        return null;
    }

    public ArrayList<Grid> getGridsAtWorldPos(Vector2 pos){
        HashSet<Grid> gridsClone = (HashSet<Grid>) currentLevel.grids.clone();
        ArrayList<Grid> grids = new ArrayList<Grid>();
        for(Grid g : gridsClone){
            if(g.isWorldPositionInGrid(new Vector2(pos.x,pos.y))){
                grids.add(g);
            }
        }
        return grids;
    }



    public void BuildWorldt(){
        if(worldStage == null) {
            worldStage = new Stage(view);
            gridsGroup = new Group();
            entityGroup = new Group();
//            background = new Image(ResourceManager.ins().nebula);
            background = new ParallaxBG();
            background.setSpeed(1,1);
//            background.scaleBy(1.5f,1.5f);
//            background.moveBy(-background.getWidth()/2, -background.getHeight()/2);
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
        synchronized (currentLevel) {
            for (Grid g : currentLevel.grids) {
                g.updateRendering();
            }
            for (EntityBody e : currentLevel.entities) {
                e.updateRendering();
                if (e instanceof AI)
                    ((AI) e).Think(worldPhysics);
            }
            removeQueued();
            worldStage.act(delta);
            doPhysicsStep(delta);
            rayHandler.update();
            rayHandler.setCombinedMatrix(CameraManager.ins().mainCamera);//worldStage.getCamera());
        }
        currentLevel.entities.remove(GameManager.ins().getThePlayer());
        checkWorldGeneration();
        currentLevel.entities.add(GameManager.ins().getThePlayer());
        if(queuedWorldSave) {
            queuedWorldSave = false;
            currentLevel.entities.remove(GameManager.ins().getThePlayer());
            PersistenceManager.ins().SaveCurrentWorldState();
            currentLevel.entities.add(GameManager.ins().getThePlayer());
        }
        if(queuedWorldLoad){
            queuedWorldLoad = false;
            System.out.println("loading"+toLoad);
            gridRemovalQueue.addAll(currentLevel.grids);
            entityRemovalQueue.addAll(currentLevel.entities);
            removeQueued();
            currentLevel.entities.remove(GameManager.ins().getThePlayer());
//            SectorState loading = PersistenceManager.ins().LoadLevel(toLoad);
//            currentLevel = loading;
            PersistenceManager.ins().LoadWorld(toLoad);
            System.out.println("loaded"+currentLevel.grids.size()+" grids");
            synchronized (currentLevel.grids) {
                HashSet<Grid> gclone = (HashSet<Grid>) currentLevel.grids.clone();
                for (Grid g : gclone)
                    LoadGridToWorld(g);
            }
            synchronized (currentLevel.entities) {
                HashSet<EntityBody> eclone = (HashSet<EntityBody>) currentLevel.entities.clone();
                for (EntityBody e : eclone)
                    LoadEntityToWorld(e);
            }
            currentLevel.entities.add(GameManager.ins().getThePlayer());
        }
        VerifyWorldState();

    }

    public void render(){
        background.setSpeed(GameManager.ins().getThePlayer().myBody.getLinearVelocity().x/100f,-GameManager.ins().getThePlayer().myBody.getLinearVelocity().y/100f);
//        background.setScale(4f*CameraManager.ins().mainCamera.zoom);
//        background.setPosition(
//                CameraManager.ins().mainCamera.position.x-background.getWidth()*background.getScaleX()/2,
//                CameraManager.ins().mainCamera.position.y-background.getHeight()*background.getScaleY()/2);

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
        }
        fapsLogger.log();
    }
    public void resize(int width, int height){
        worldStage.getViewport().update(width,height, true);
    }
    private void removeQueued(){
        for (Vector2 wp : removalQueue){
            HashSet<Grid> gridsClone = (HashSet<Grid>) currentLevel.grids.clone();
            for(Grid g : gridsClone){
                if(g.removeVoxelWorldPos(new Vector2(wp.x,wp.y))){
                    rethinkShape(g);
                }
            }
        }
        removalQueue.clear();
        for (Vector2 wp : playerRemovalQueue){
            HashSet<Grid> gridsClone = (HashSet<Grid>) currentLevel.grids.clone();
            for(Grid g : gridsClone){
                if(g.removeVoxelWorldPos(new Vector2(wp.x,wp.y))){
                    rethinkShape(g);
                    ControllerManager.ins().PlayerDestroyedBlock();
                }
            }
        }
        playerRemovalQueue.clear();

        for(EntityBody e : entityRemovalQueue){
            //System.out.println("REMOVING " + e);
            if(e == null) continue;
            if(e.myBody == null) continue;
            if(e.equals(GameManager.ins().getThePlayer())) continue;

            currentLevel.entities.remove(e);
            entityGroup.removeActor(e.view);
            //worldStage.getActors().removeValue(e.view,true);
            if(e.myBody != null){
                e.dispose();
                worldPhysics.destroyBody(e.myBody);
                e.myBody = null;
            }
        }
        entityRemovalQueue.clear();

        for(Grid g: gridRecalculationQueue){
            if(g != null && g.myBody != null){
                g.recalculateShape();
            }
        }
        gridRecalculationQueue.clear();

        for(Grid g : gridRemovalQueue){
            //System.out.println("REMOVING " + e);
            if(g == null){
                currentLevel.grids.remove(g);
                continue;
            }
            if(g.myBody == null){
                currentLevel.grids.remove(g);
                gridsGroup.removeActor(g.voxels);
                continue;
            }
            currentLevel.grids.remove(g);
            gridsGroup.removeActor(g.voxels);
            //worldStage.getActors().removeValue(e.view,true);
            if(g.myBody != null){
                g.dispose();
                worldPhysics.destroyBody(g.myBody);
                g.myBody = null;
            }
        }
        gridRemovalQueue.clear();
        //System.out.println("Done removing");


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
    public Vector2 worldToScreenCoords(Vector2 pos){
        return worldStage.stageToScreenCoordinates(pos);
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
        System.out.println("add called on "+ d);
        if(!currentLevel.grids.contains(d)) {
            System.out.println("adding grid...");
            gridsGroup.addActor(d.voxels);
            //todo make funciton in Grid to parse through voxels and create fixture
            //d.recalculateShape();
            d.myBody = worldPhysics.createBody(d.bodyDef);
            d.myBody.setUserData(d);
//            System.out.println(d.myBody);
//            System.out.println(d.physicsShapes);
            d.recalculateShape();
            //d.fixture.add(d.myBody.createFixture(d.fixtureDef));
            currentLevel.grids.add(d);
        }
    }

    public void LoadGridToWorld(Grid d){
            gridsGroup.addActor(d.voxels);
            //todo make funciton in Grid to parse through voxels and create fixture
            //d.recalculateShape();
            d.myBody = worldPhysics.createBody(d.bodyDef);
            d.myBody.setUserData(d);
//            System.out.println(d.myBody);
//            System.out.println(d.physicsShapes);
            d.recalculateShape();
            //d.fixture.add(d.myBody.createFixture(d.fixtureDef));
            currentLevel.grids.add(d);

    }

    public void removeGridFromWorld(Grid g){
        System.out.println("remove grid called");
        if(!currentLevel.grids.remove(g)){
            //the grid wasnt removed
            System.err.println(g+ "not removed");
        }
        gridsGroup.removeActor(g.voxels);
//        worldStage.getActors().removeValue(g.voxels,true);
        if(g.myBody != null){
            g.dispose();
            worldPhysics.destroyBody(g.myBody);
            g.myBody = null;
        }
    }

    public void recalculateGrid(Grid g){
        if(!gridRecalculationQueue.contains(g))
            gridRecalculationQueue.add(g);
    }

    public void addEntityToWorld(EntityBody e){
        if(!currentLevel.entities.contains(e)) {
            entityGroup.addActor(e.view);
            e.myBody = worldPhysics.createBody(e.bodyDef);
            e.myBody.setUserData(e);
            e.CreateFixture();
            e.CreateSight(e.viewDistance);
            currentLevel.entities.add(e);
        }
    }

    /**
     * This wont add it as an entity to be saved/loaded when the world is reset
     * @param view
     * @param def
     * @return
     */
    public Body addBodyToWorld(Entity view, BodyDef def){
        entityGroup.addActor(view);
        Body b = worldPhysics.createBody(def);
        return b;
    }

    public World getWorldPhysics(){
        return worldPhysics;
    }
    public void SetOnTop(Actor top, Actor bottom){
        entityGroup.removeActor(bottom);
        entityGroup.addActorBefore(top,bottom);
    }

    public void LoadEntityToWorld(EntityBody e){
            entityGroup.addActor(e.view);
            e.myBody = worldPhysics.createBody(e.bodyDef);
            e.myBody.setUserData(e);
            e.CreateFixture();
            e.CreateSight(e.viewDistance);
            //currentLevel.entities.add(e);
    }

    public void removeEntityFromWorld(EntityBody e){
        entityRemovalQueue.add(e);
    }

    public void rethinkShape(Grid d){
//        d.recalculateShape();
        if(!currentLevel.grids.contains(d))
            currentLevel.grids.add(d);
    }

    /**
     * Removes any grids/entities that have no body
     */
    public void VerifyWorldState(){
        for (Grid g : currentLevel.grids)
            if(g.myBody == null)
                gridRemovalQueue.add(g);
    }

    public void GenerateAsteroid(int posX, int posY, int size, float density){
        int start = VoxelCollection.maxSize/2-size/2;
        int end = VoxelCollection.maxSize/2+size/2;
        VoxelCollection astVox = new VoxelCollection(Voxel.Build(IDs.ROCK_TEX), new Vector2(posX,posY));
        boolean[][] putBlocks = PCGManager.ins().generateBetterAsteroid(size, density);
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

    private void checkWorldGeneration(){
        //todo map the player position to an index and save/load appropriate secotrs
        int newX, oldX = currentLevel.getCenterX();
        int newY, oldY = currentLevel.getCenterY();
        newX = oldX;
        newY = oldY;
        if(GameManager.ins().getThePlayer().myBody.getPosition().x > Constants.SECTOR_SIZE/2)
            newX++;
        else if(GameManager.ins().getThePlayer().myBody.getPosition().x < -Constants.SECTOR_SIZE/2)
            newX--;
        if(GameManager.ins().getThePlayer().myBody.getPosition().y > Constants.SECTOR_SIZE/2)
            newY++;
        else if(GameManager.ins().getThePlayer().myBody.getPosition().y < -Constants.SECTOR_SIZE/2)
            newY--;
        if(newX > Constants.NUM_SECTORS/2)
            newX = -Constants.NUM_SECTORS/2;
        else if(newX < -Constants.NUM_SECTORS/2)
            newX = Constants.NUM_SECTORS/2;
        if(newY > Constants.NUM_SECTORS/2)
            newY = -Constants.NUM_SECTORS/2;
        else if(newY < -Constants.NUM_SECTORS/2)
            newY = Constants.NUM_SECTORS/2;
        if(newX != oldX || newY != oldY)
            currentLevel.ShiftTo(newX,newY);







        return;
    }

    public void addLightToBody(Body b, float strength, int distance, Vector2 position, Vector2 offset){
        Filter f = new Filter();
        f.maskBits = FilterIDs.GRID;
        PointLight pl = new PointLight(rayHandler,60, new Color(Color.rgba8888(strength, strength, strength, 1.0f)),
                ResourceManager.VOXEL_PIXEL_SIZE *distance, position.x,position.y);
        pl.setContactFilter(f);
        pl.attachToBody(b,offset.x,offset.y);
        pl.setSoftnessLength(100f);
    }

    public String getWorldName(){
        return currentLevel.name;
    }

}
