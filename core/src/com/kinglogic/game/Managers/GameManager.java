package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kinglogic.game.TestInputProcessor;


/**
 * Created by chris on 4/1/2018.
 */

public class GameManager {
    private static GameManager instance;


    public static GameManager ins() {
        if(instance == null)
            instance = new GameManager();
        return instance;
    }

    private GameManager(){
        CameraManager.ins();
        ResourceManager.ins();
        WorldManager.ins();
        GUIManager.ins();
        SoundManager.ins();
        ControllerManager.ins();
    }

    public void Update(float delta){

    }

    public void dispose(){
        ResourceManager.ins().dispose();
        WorldManager.ins().dispose();
        GUIManager.ins().dispose();
        SoundManager.ins().dispose();
        ControllerManager.ins().dispose();
    }

}
