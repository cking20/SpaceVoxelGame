package com.kinglogic.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kinglogic.game.ChemestryFramework.ChemistryManager;
import com.kinglogic.game.Managers.CameraManager;
import com.kinglogic.game.Managers.ControllerManager;
import com.kinglogic.game.Managers.GUIManager;
import com.kinglogic.game.Managers.GameManager;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Managers.WorldManager;
import com.kinglogic.game.SpaceGame;

/**
 * Created by chris on 4/1/2018.
 */


public class GameScreen implements Screen {
    SpaceGame theGame;
    private float accumulator = 0f;

    public GameScreen(SpaceGame game){
        theGame = game;
    }

    @Override
    public void show() {
//        aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
//        gui.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);

    }

    @Override
    public void render(float delta) {
//        aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
//        gui.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
        //Clear the screen from the last frame
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Set the entire screen to this color
        Gdx.gl.glClearColor(.078f, .047f, .101f, 1);
        //render the actors

        //perform the actions of the actors
        GameManager.ins().Update(delta);
        ControllerManager.ins().Update(delta);
        ChemistryManager.ins().Update(delta);
        GUIManager.ins().update(delta);
        WorldManager.ins().update(delta);
        ResourceManager.ins().Update(delta);
        CameraManager.ins().Update(delta);


        WorldManager.ins().render();
        GUIManager.ins().render();

    }

    @Override
    public void resize(int width, int height) {
        WorldManager.ins().resize(width,height);
        GUIManager.ins().resize(width,height);

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
        GameManager.ins().dispose();
    }
}
