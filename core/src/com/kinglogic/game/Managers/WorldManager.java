package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
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

    }
    public void BuildWorldt(){
        if(worldStage == null)
            worldStage = new Stage(view);
        if(worldPhysics == null)
            worldPhysics = new World(new Vector2(0,0), true);
        //todo generate stuff with a procedural algorithm
    }

    public void update(float delta){
        worldStage.act(delta);
        doPhysicsStep(delta);
        //todo update actors
    }
    public void render(){
        worldStage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
        worldStage.draw();
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
        d.myBody = worldPhysics.createBody(d.bodyDef);

        //todo make funciton in Grid to parse through voxels and create fixture
        //must go counter clockwise
        /*something like
        * b2Vec2 vertices[5];
  vertices[0].Set(-1,  2);
  vertices[1].Set(-1,  0);
  vertices[2].Set( 0, -3);
  vertices[3].Set( 1,  0);
  vertices[4].Set( 1,  1);

  b2PolygonShape polygonShape;
  polygonShape.Set(vertices, 5); //pass array to the shape

  myFixtureDef.shape = &polygonShape; //change the shape of the fixture
  myBodyDef.position.Set(0, 20); //in the middle
  b2Body* dynamicBody2 = m_world->CreateBody(&myBodyDef);
  dynamicBody2->CreateFixture(&myFixtureDef); //add a fixture to the body
        *
        * */
    }

}
