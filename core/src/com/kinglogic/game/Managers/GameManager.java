package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kinglogic.game.TestInputProcessor;


/**
 * Created by chris on 4/1/2018.
 */

public class GameManager {
    private static GameManager instance;
    private TestInputProcessor tip;
    private Vector2 mousePos;

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
        tip = new TestInputProcessor();
        Gdx.input.setInputProcessor(tip);
        mousePos = new Vector2(0,0);

    }

    public void Update(float delta){
//        Gdx.input.setCursorPosition((int) targetScreenPos.x,(int) targetScreenPos.y);

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







        if(Controllers.getControllers().size > 0) {
            if (Controllers.getControllers().get(0).getAxis(4) > .5) {
                tip.player.RotateLeft();
            } else if (Controllers.getControllers().get(0).getAxis(4) < -.5) {
                tip.player.RotateRight();
            }
            if (Controllers.getControllers().get(0).getAxis(0) < -.5) {
                tip.player.GoForeward();
            } else if (Controllers.getControllers().get(0).getAxis(0) > .5) {
                tip.player.GoBackward();
            }
            if (Controllers.getControllers().get(0).getAxis(1) < -.5) {
                tip.player.GoLeft();
            } else if (Controllers.getControllers().get(0).getAxis(1) > .5) {
                tip.player.GoRight();
            }
            //cursor
            if (Controllers.getControllers().get(0).getAxis(3) < -.5) {
                mousePos.x += delta;
//            GUIManager.ins().targetPosition = mousePos;
            } else if (Controllers.getControllers().get(0).getAxis(3) > .5) {
                mousePos.x -= delta;
//            GUIManager.ins().targetPosition = mousePos;
            }
            if (Controllers.getControllers().get(0).getAxis(2) < -.5) {
                mousePos.y += delta;
//            GUIManager.ins().targetPosition = mousePos;
            } else if (Controllers.getControllers().get(0).getAxis(2) > .5) {
                mousePos.y -= delta;
            }
        }
        GUIManager.ins().targetPosition = mousePos;
    }

    public void dispose(){
        ResourceManager.ins().dispose();
        WorldManager.ins().dispose();
        GUIManager.ins().dispose();
    }

}
