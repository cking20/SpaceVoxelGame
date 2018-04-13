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
        //System.out.println(tip.dyn.fixture);
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
//            tip.dyn.myBody.applyForceToCenter(tip.dyn.myBody.getTransform().getOrientation().rotate90(1).scl(1000f*tip.dyn.myBody.getMass()),true);
            tip.player.GoForeward();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
//            tip.dyn.myBody.applyForceToCenter(tip.dyn.myBody.getTransform().getOrientation().rotate90(1).scl(1000f*tip.dyn.myBody.getMass()),true);
            tip.player.GoBackward();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
//            tip.dyn.myBody.applyForceToCenter(tip.dyn.myBody.getTransform().getOrientation().rotate90(1).scl(1000f*tip.dyn.myBody.getMass()),true);
            tip.player.GoLeft();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
//            tip.dyn.myBody.applyForceToCenter(tip.dyn.myBody.getTransform().getOrientation().rotate90(1).scl(1000f*tip.dyn.myBody.getMass()),true);
            tip.player.GoRight();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.PAGE_UP)){
            tip.player.RotateLeft();
//            tip.dyn.myBody.applyTorque(1000f*tip.dyn.myBody.getMass(),true);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN)){
            tip.player.RotateRight();
//            tip.dyn.myBody.applyTorque(-1000f*tip.dyn.myBody.getMass(),true);
        }
    }

    public void dispose(){
        ResourceManager.ins().dispose();
        WorldManager.ins().dispose();
    }

}
