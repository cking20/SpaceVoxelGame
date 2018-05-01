package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.kinglogic.game.TestInputProcessor;

/**
 * Created by chris on 4/20/2018.
 */

public class ControllerManager {
    private static ControllerManager instance;
    private ControllerListener playerControllerAdapter;
    private Vector2 mousePos;
    private TestInputProcessor tip;

    private String playersCurrentBlock = IDs.METAL_TEX;
    private int currentBlockIndex = 0;


    public static ControllerManager ins(){
        if(instance == null)
            instance = new ControllerManager();
        return instance;
    }
    private ControllerManager(){
        tip = new TestInputProcessor();
        Gdx.input.setInputProcessor(tip);
        mousePos = new Vector2(0,0);
        playerControllerAdapter = new ControllerListener(){
            @Override
            public void connected(Controller controller) {

            }

            @Override
            public void disconnected(Controller controller) {

            }

            @Override
            public boolean buttonDown(Controller controller, int buttonCode) {
                return false;
            }

            @Override
            public boolean buttonUp(Controller controller, int buttonCode) {
                if (buttonCode == 0) {
                    if(tip.player.isControlling())
                        tip.player.Exit();
                    else
                        tip.player.Enter(tip.dyn);
                }
                if (buttonCode == 2) {
                    //FIRE MAIN
                    tip.player.FireMain();
                }
//                if (buttonCode == 9) {
//                    //ZOOM IN
//                    CameraManager.ins().mainCamera.zoom-=.01;
//                }
//                if (buttonCode == 8) {
//                    //ZOOM OUT
//                    CameraManager.ins().mainCamera.zoom+=.01;
//                }
//                if (buttonCode == 4) {
//                    WorldManager.ins().addVoxelScreenPosition(GUIManager.ins().targetPosition.x, Gdx.graphics.getHeight()-GUIManager.ins().targetPosition.y, tip.blockName);
//                }
//                if (buttonCode == 5) {
//                    WorldManager.ins().removeVoxelScreenPosition((int)GUIManager.ins().targetPosition.x,(int)Gdx.graphics.getHeight()-GUIManager.ins().targetPosition.y);
//                }
                return false;
            }

            @Override
            public boolean axisMoved(Controller controller, int axisCode, float value) {
                //axis
//                if (axisCode == 4 && value > .5) {
//                    tip.player.RotateLeft();
//                } else if (axisCode == 4 && value < -.5) {
//                    tip.player.RotateRight();
//                }
//                if (axisCode == 0 && value < -.5) {
//                    tip.player.GoForeward();
//                } else if (axisCode == 0 && value > .5) {
//                    tip.player.GoBackward();
//                }
//                if (axisCode == 1 && value < -.5) {
//                    tip.player.GoLeft();
//                    tip.player.TurnLeft();
//                } else if (axisCode == 1 && value > .5) {
//                    tip.player.GoRight();
//                    tip.player.TurnRight();
//                }
//
//                if(axisCode == 3 &&(value > .05 || value < -.05)){
//                    tip.player.buildPosition.x+=value*5;
//                }
//                if(axisCode == 2 &&(value > .05 || value < -.05)) {
//                    tip.player.buildPosition.y -= value * 5;
//                }
//                GUIManager.ins().targetPosition = tip.player.buildPosition;
                return false;
            }

            @Override
            public boolean povMoved(Controller controller, int povCode, PovDirection value) {
                if(value == PovDirection.north)
                    System.out.println("up");
                if(value == PovDirection.south)
                    System.out.println("down");
                if(value == PovDirection.west) {
                    PreviousBlock();
                    GUIManager.ins().selectedBlockName = playersCurrentBlock;
                }   //System.out.println("left");
                if(value == PovDirection.east) {
                    NextBlock();
                    GUIManager.ins().selectedBlockName = playersCurrentBlock;
                }    //System.out.println("right");
                return false;
            }

            @Override
            public boolean xSliderMoved(Controller controller, int sliderCode, boolean value) {
                return false;
            }

            @Override
            public boolean ySliderMoved(Controller controller, int sliderCode, boolean value) {
                return false;
            }

            @Override
            public boolean accelerometerMoved(Controller controller, int accelerometerCode, Vector3 value) {
                return false;
            }
        };

        Controllers.addListener(playerControllerAdapter);
    }
    public void Update(float delta){

        if(Controllers.getControllers().size > 0) {
//            //axis
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
                tip.player.TurnLeft();
            } else if (Controllers.getControllers().get(0).getAxis(1) > .5) {
                tip.player.GoRight();
                tip.player.TurnRight();
            }
            float dX = Controllers.getControllers().get(0).getAxis(3);
            float dY = -Controllers.getControllers().get(0).getAxis(2);
            if(dX > .05 || dX < -.05)
                tip.player.buildPosition.x+=dX*5;
            if(dY > .05 || dY < -.05)
                tip.player.buildPosition.y+=dY*5;
            GUIManager.ins().targetPosition = tip.player.buildPosition;

            //buttons
//            if (Controllers.getControllers().get(0).getButton(0)) {
//                if(tip.player.isControlling())
//                    tip.player.Exit();
//                else
//                    tip.player.Enter(tip.dyn);
//            }
//            if (Controllers.getControllers().get(0).getButton(2)) {
//                //FIRE MAIN
//                tip.player.FireMain();
//            }
            if (Controllers.getControllers().get(0).getButton(9)) {
                //ZOOM IN
                CameraManager.ins().mainCamera.zoom-=.05;
            }
            if (Controllers.getControllers().get(0).getButton(8)) {
                //ZOOM OUT
                CameraManager.ins().mainCamera.zoom+=.05;
            }
            if (Controllers.getControllers().get(0).getButton(4)) {
                WorldManager.ins().addVoxelScreenPosition(GUIManager.ins().targetPosition.x, Gdx.graphics.getHeight()-GUIManager.ins().targetPosition.y, GUIManager.ins().selectedBlockName);
            }
            if (Controllers.getControllers().get(0).getButton(5)) {
                WorldManager.ins().removeVoxelScreenPosition((int)GUIManager.ins().targetPosition.x,(int)Gdx.graphics.getHeight()-GUIManager.ins().targetPosition.y);
            }

        } else {//no controllers
            if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                tip.player.GoForeward();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                tip.player.GoBackward();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                tip.player.GoLeft();
                tip.player.TurnLeft();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                tip.player.GoRight();
                tip.player.TurnRight();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.PAGE_UP)){
                tip.player.RotateLeft();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN)){
                tip.player.RotateRight();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.MINUS)){
                CameraManager.ins().mainCamera.zoom-=.05;
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.EQUALS)){
                CameraManager.ins().mainCamera.zoom+=.05;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.NUM_0)){
                tip.player.FireMain();
            }
            mousePos.x = Gdx.input.getX();
            mousePos.y = Gdx.graphics.getHeight()-Gdx.input.getY();
            GUIManager.ins().targetPosition = mousePos;
        }

    }

    private void NextBlock(){
        currentBlockIndex++;
        currentBlockIndex = currentBlockIndex % IDs.ins().getNumBlockIds();
        System.out.println(currentBlockIndex);
        playersCurrentBlock = IDs.ins().getID(currentBlockIndex);
    }
    private void PreviousBlock(){
        currentBlockIndex--;
        if(currentBlockIndex < 0)
            currentBlockIndex = IDs.ins().getNumBlockIds()-1;
        System.out.println(currentBlockIndex);
        playersCurrentBlock = IDs.ins().getID(currentBlockIndex);
    }


    public void dispose(){

    }
}
