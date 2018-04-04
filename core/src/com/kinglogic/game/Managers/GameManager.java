package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.kinglogic.game.TestInputProcessor;

/**
 * Created by chris on 4/1/2018.
 */

public class GameManager {
    private static GameManager instance;
    private TestInputProcessor tip;

    public static GameManager ins() {
        if(instance == null)
            instance = new GameManager();
        return instance;
    }

    private GameManager(){
        ResourceManager.ins();
        WorldManager.ins();
        tip = new TestInputProcessor();
        Gdx.input.setInputProcessor(tip);

    }

    public void Update(float delta){
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            tip.dyn.myBody.applyForceToCenter(tip.dyn.myBody.getTransform().getOrientation().rotate90(1).scl(1000f),true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.PAGE_UP)){
            tip.dyn.myBody.applyTorque(1000f,true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN)){
            tip.dyn.myBody.applyTorque(-1000f,true);
        }
    }

    public void dispose(){
        ResourceManager.ins().dispose();
        WorldManager.ins().dispose();
    }

}
