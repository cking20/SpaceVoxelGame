package com.kinglogic.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kinglogic.game.Managers.GameManager;
import com.kinglogic.game.Managers.WorldManager;
import com.kinglogic.game.SpaceGame;

/**
 * Created by chris on 4/1/2018.
 */


public class GameScreen implements Screen {
    SpaceGame theGame;
    Stage gui;
    private Viewport view;
    private OrthographicCamera viewCam;
    private float aspectRatio;

    public GameScreen(SpaceGame game){
        theGame = game;
        aspectRatio = (float) Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        viewCam = new OrthographicCamera();
        view = new FitViewport(Gdx.graphics.getHeight(), Gdx.graphics.getHeight()*aspectRatio, viewCam);
        view.apply();
        gui = new Stage(view);
    }

    @Override
    public void show() {
        aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        gui.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);

    }

    @Override
    public void render(float delta) {
        aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        gui.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
        //Clear the screen from the last frame
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Set the entire screen to this color
        Gdx.gl.glClearColor(.17f, .17f, .17f, 1);
        //render the actors
        WorldManager.ins().render();
        gui.draw();

        //perform the actions of the actors
        GameManager.ins().Update(delta);
        WorldManager.ins().update(delta);
        gui.act(delta);


    }

    @Override
    public void resize(int width, int height) {
        aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        gui.getViewport().update(width,height,true);
        WorldManager.ins().resize(width,height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        gui.dispose();
    }
}
