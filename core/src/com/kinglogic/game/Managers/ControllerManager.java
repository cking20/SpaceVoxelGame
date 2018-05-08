package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.kinglogic.game.Actors.Voxel.Voxel;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Physics.DynamicGrid;
import com.kinglogic.game.Physics.StaticGrid;
import com.kinglogic.game.TestInputProcessor;

/**
 * Created by chris on 4/20/2018.
 */

public class ControllerManager {
    private static ControllerManager instance;
    private ControllerListener playerControllerAdapter;
    private Vector2 mousePos;
    private TestInputProcessor tip;

    public  String playersCurrentBlock = IDs.getIDList().get(0);
    private int currentBlockIndex = 0;

    public Color playersCurrentColor = IDs.getColorList().get(0);
    private int currentColorIndex = 0;
    private boolean gravLock = false;
    public int numBlocks = 0;


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
                    if(GameManager.ins().getThePlayer().isControlling())
                        GameManager.ins().getThePlayer().Exit();
                    else
                        GameManager.ins().getThePlayer().Enter(GameManager.ins().getThePlayer().lastControlled);
                }
                if (buttonCode == 2) {
                    //FIRE MAIN
                    if(numBlocks > 0) {
                        GameManager.ins().getThePlayer().FireMain();
                        numBlocks--;
                    }
                }
                if (buttonCode == 6) {
                    //FIRE MAIN
                    GameManager.ins().getThePlayer().ToggleGravLock();
                }
                if (buttonCode == 7) {
                    if(numBlocks > 0) {
                        numBlocks--;
                        VoxelCollection vc = new VoxelCollection(Voxel.Build(IDs.ROCK_TEX), GameManager.ins().getThePlayer().myBody.getPosition());
                        WorldManager.ins().addGridToWorld(new StaticGrid(vc));
                    }
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
//                    GameManager.ins().getThePlayer().RotateLeft();
//                } else if (axisCode == 4 && value < -.5) {
//                    GameManager.ins().getThePlayer().RotateRight();
//                }
//                if (axisCode == 0 && value < -.5) {
//                    GameManager.ins().getThePlayer().GoForeward();
//                } else if (axisCode == 0 && value > .5) {
//                    GameManager.ins().getThePlayer().GoBackward();
//                }
//                if (axisCode == 1 && value < -.5) {
//                    GameManager.ins().getThePlayer().GoLeft();
//                    GameManager.ins().getThePlayer().TurnLeft();
//                } else if (axisCode == 1 && value > .5) {
//                    GameManager.ins().getThePlayer().GoRight();
//                    GameManager.ins().getThePlayer().TurnRight();
//                }
//
//                if(axisCode == 3 &&(value > .05 || value < -.05)){
//                    GameManager.ins().getThePlayer().buildPosition.x+=value*5;
//                }
//                if(axisCode == 2 &&(value > .05 || value < -.05)) {
//                    GameManager.ins().getThePlayer().buildPosition.y -= value * 5;
//                }
//                GUIManager.ins().targetPosition = GameManager.ins().getThePlayer().buildPosition;
                return false;
            }

            @Override
            public boolean povMoved(Controller controller, int povCode, PovDirection value) {
                if(value == PovDirection.north) {
                    NextColor();
                    GUIManager.ins().selectedColor = playersCurrentColor;
//                    System.out.println("up");
                }if(value == PovDirection.south) {
                    PreviousColor();
                    GUIManager.ins().selectedColor = playersCurrentColor;
//                    System.out.println("down");
                }if(value == PovDirection.west) {
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
                GameManager.ins().getThePlayer().RotateLeft();
            } else if (Controllers.getControllers().get(0).getAxis(4) < -.5) {
                GameManager.ins().getThePlayer().RotateRight();
            }
            if (Controllers.getControllers().get(0).getAxis(0) < -.5) {
                GameManager.ins().getThePlayer().GoForeward();
            } else if (Controllers.getControllers().get(0).getAxis(0) > .5) {
                GameManager.ins().getThePlayer().GoBackward();
            }
            if (Controllers.getControllers().get(0).getAxis(1) < -.5) {
                GameManager.ins().getThePlayer().GoLeft();
                GameManager.ins().getThePlayer().TurnLeft();
            } else if (Controllers.getControllers().get(0).getAxis(1) > .5) {
                GameManager.ins().getThePlayer().GoRight();
                GameManager.ins().getThePlayer().TurnRight();
            }
            float dX = Controllers.getControllers().get(0).getAxis(3);
            float dY = -Controllers.getControllers().get(0).getAxis(2);
            if(dX > .05 || dX < -.05)
                GameManager.ins().getThePlayer().buildPosition.x+=dX*5;
            if(dY > .05 || dY < -.05)
                GameManager.ins().getThePlayer().buildPosition.y+=dY*5;
            GUIManager.ins().targetPosition = GameManager.ins().getThePlayer().buildPosition;

            //buttons
//            if (Controllers.getControllers().get(0).getButton(0)) {
//                if(GameManager.ins().getThePlayer().isControlling())
//                    GameManager.ins().getThePlayer().Exit();
//                else
//                    GameManager.ins().getThePlayer().Enter(tip.dyn);
//            }
//            if (Controllers.getControllers().get(0).getButton(2)) {
//                //FIRE MAIN
//                GameManager.ins().getThePlayer().FireMain();
//            }
            if (Controllers.getControllers().get(0).getButton(9)) {
                //ZOOM IN
                CameraManager.ins().ZoomIn();
            }
            if (Controllers.getControllers().get(0).getButton(8)) {
                //ZOOM OUT
                CameraManager.ins().ZoomOut();
            }
            if (Controllers.getControllers().get(0).getButton(4)) {
                if(numBlocks > 0)
                    if(WorldManager.ins().addVoxelScreenPosition(GUIManager.ins().targetPosition.x, Gdx.graphics.getHeight()-GUIManager.ins().targetPosition.y, GUIManager.ins().selectedBlockName))
                        numBlocks--;
//                WorldManager.ins().addVoxelScreenPosition(GUIManager.ins().targetPosition.x, Gdx.graphics.getHeight()-GUIManager.ins().targetPosition.y, GUIManager.ins().selectedBlockName);
            }
            if (Controllers.getControllers().get(0).getButton(5)) {
                WorldManager.ins().playerRemoveVoxelScreenPosition((int)GUIManager.ins().targetPosition.x,(int)Gdx.graphics.getHeight()-GUIManager.ins().targetPosition.y);
            }

        } else {//no controllers
            if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                GameManager.ins().getThePlayer().GoForeward();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                GameManager.ins().getThePlayer().GoBackward();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                GameManager.ins().getThePlayer().GoLeft();
                GameManager.ins().getThePlayer().TurnLeft();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                GameManager.ins().getThePlayer().GoRight();
                GameManager.ins().getThePlayer().TurnRight();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.PAGE_UP)){
                GameManager.ins().getThePlayer().RotateLeft();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN)){
                GameManager.ins().getThePlayer().RotateRight();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.MINUS)){
                CameraManager.ins().mainCamera.zoom-=.05;
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.EQUALS)){
                CameraManager.ins().mainCamera.zoom+=.05;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.NUM_0)){
                GameManager.ins().getThePlayer().FireMain();
            }
            mousePos.x = Gdx.input.getX();
            mousePos.y = Gdx.graphics.getHeight()-Gdx.input.getY();
            GUIManager.ins().targetPosition = mousePos;
        }

    }
    public void PlayerDestroyedBlock(){
        numBlocks++;
    }

    public void NextBlock(){
        currentBlockIndex++;
        currentBlockIndex = currentBlockIndex % IDs.ins().getNumBlockIds();
        System.out.println(currentBlockIndex);
        playersCurrentBlock = IDs.ins().getID(currentBlockIndex);
    }
    public void PreviousBlock(){
        currentBlockIndex--;
        if(currentBlockIndex < 0)
            currentBlockIndex = IDs.ins().getNumBlockIds()-1;
        System.out.println(currentBlockIndex);
        playersCurrentBlock = IDs.ins().getID(currentBlockIndex);
    }
    public void NextColor(){
        currentColorIndex++;
        currentColorIndex = currentColorIndex % IDs.ins().getNumColorIds();
        playersCurrentColor = IDs.ins().getColor(currentColorIndex);

    }
    public void PreviousColor(){
        currentColorIndex--;
        if(currentColorIndex < 0)
            currentColorIndex = IDs.ins().getNumColorIds()-1;
        playersCurrentColor = IDs.ins().getColor(currentColorIndex);

    }


    public void dispose(){

    }
}
