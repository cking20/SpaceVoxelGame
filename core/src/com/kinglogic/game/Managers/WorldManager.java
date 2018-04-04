package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kinglogic.game.Constants;
import com.kinglogic.game.Physics.DynamicGrid;
import com.kinglogic.game.Physics.Grid;

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


    public static WorldManager ins() {
        if(instance == null)
            instance = new WorldManager();
        return instance;
    }

    private WorldManager(){
        grids = new HashSet<Grid>();
        viewCam = new OrthographicCamera();
        view = new FitViewport(Gdx.graphics.getHeight(), Gdx.graphics.getHeight()*ResourceManager.ins().calculateAspectRatio(), viewCam);
        view.apply();
        BuildWorldt();
        debugRenderer = new Box2DDebugRenderer();

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
            g.myBody.setTransform(g.myBody.getPosition().x%(Gdx.graphics.getWidth()),(g.myBody.getPosition().y%(Gdx.graphics.getHeight())), g.myBody.getAngle());
        }
        worldStage.act(delta);
        doPhysicsStep(delta);
        //todo update actors
    }
    public void render(){
        worldStage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
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
    }

    public Vector2 screenToWorldCoords(Vector2 pos){
        return worldStage.screenToStageCoordinates(pos);
    }

    public void addGridToWorld(Grid d){
        worldStage.addActor(d.voxels);
        //todo make funciton in Grid to parse through voxels and create fixture
        d.recalculateShape();
        d.myBody = worldPhysics.createBody(d.bodyDef);
        System.out.println(d.myBody);
        System.out.println(d.fixtureDef);
        d.fixture = d.myBody.createFixture(d.fixtureDef);
        grids.add(d);
    }
    public void rethinkShape(Grid d){
        d.recalculateShape();
        d.myBody.destroyFixture(d.fixture);
        d.fixture = d.myBody.createFixture(d.fixtureDef);
        grids.add(d);
    }

}
