package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.kinglogic.game.Actors.Voxel.Blocks.Voxel;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Constants;
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
    public int numBlocks = 10000;

    private Vector2 rightStickVec = new Vector2(0,0);
    private Vector2 leftStickVec = new Vector2(0,0);


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
                if(Constants.DEBUG)
//                    System.out.println("button "+buttonCode + " up");
                switch (buttonCode){
                    case 0:// A
                        Jump();
                        break;
                    case 1:// B
                        break;
                    case 2:// X
                        break;
                    case 3:// Y
                        break;
                    case 4:// L_BUMPER
                        break;
                    case 5:// R_BUMPER
                        FireMain();
                        break;
                    case 6:// SHARE
                        GameManager.ins().getThePlayer().ToggleGravLock();
                        break;
                    case 7:// MENU
                        CreateNewGrid();
                        break;
                    case 8:// L_STICK

                        break;
                    case 9:// R_STICK

                        break;
                    case 10://??
                        break;
                }
                return false;
            }

            @Override
            public boolean axisMoved(Controller controller, int axisCode, float value) {
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
            rightStickVec = new Vector2(Controllers.getControllers().get(0).getAxis(2),Controllers.getControllers().get(0).getAxis(3)).nor();
            leftStickVec  = new Vector2(Controllers.getControllers().get(0).getAxis(0),Controllers.getControllers().get(0).getAxis(1)).nor();

            if (Controllers.getControllers().get(0).getAxis(4) > .5) {
//                System.out.println("axis 4 > .5");
                GameManager.ins().getThePlayer().RotateLeft();
            } else if (Controllers.getControllers().get(0).getAxis(4) < -.5) {
//                System.out.println("axis 4  < -.5");
                GameManager.ins().getThePlayer().RotateRight();
            }
            if (Controllers.getControllers().get(0).getAxis(0) < -.5) {
                //GameManager.ins().getThePlayer().GoForeward();
            } else if (Controllers.getControllers().get(0).getAxis(0) > .5) {
                if(!Controllers.getControllers().get(0).getButton(4)) {
                    GameManager.ins().getThePlayer().GoBackward();
                }
            }
            if (Controllers.getControllers().get(0).getAxis(1) < -.5) {
                if(!Controllers.getControllers().get(0).getButton(4)) {
                    GameManager.ins().getThePlayer().GoLeft();
                    GameManager.ins().getThePlayer().TurnLeft();
                }
            } else if (Controllers.getControllers().get(0).getAxis(1) > .5) {
                if(!Controllers.getControllers().get(0).getButton(4)) {
                    GameManager.ins().getThePlayer().GoRight();
                    GameManager.ins().getThePlayer().TurnRight();
                }
            }
            float dX = Controllers.getControllers().get(0).getAxis(3);
            float dY = -Controllers.getControllers().get(0).getAxis(2);
            if(dX > .05 || dX < -.05)
                GameManager.ins().getThePlayer().buildPosition.x+=dX*5;
            if(dY > .05 || dY < -.05)
                GameManager.ins().getThePlayer().buildPosition.y+=dY*5;
            GUIManager.ins().targetPosition = GameManager.ins().getThePlayer().buildPosition;

            if (Controllers.getControllers().get(0).getButton(0)) {
                Jump();
            }
            if (Controllers.getControllers().get(0).getButton(9)) {
                //ZOOM IN
//                CameraManager.ins().ZoomIn();
            }
            if (Controllers.getControllers().get(0).getButton(8)) {
                //ZOOM OUT
//                CameraManager.ins().ZoomOut();
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
    public void TogglePlayerControl(){
        if(GameManager.ins().getThePlayer().isControlling())
            GameManager.ins().getThePlayer().Exit();
        else
            GameManager.ins().getThePlayer().Enter(GameManager.ins().getThePlayer().lastControlled);
    }
    public void Jump(){
        GameManager.ins().getThePlayer().GoForeward();
    }
    public void FireMain(){
        if(numBlocks > 0) {
            GameManager.ins().getThePlayer().FireMain(leftStickVec);
            numBlocks--;
        }
    }
    public void CreateNewGrid(){
        if(numBlocks > 0) {
            numBlocks--;
            VoxelCollection vc = new VoxelCollection(Voxel.Build(IDs.ROCK_TEX), GameManager.ins().getThePlayer().myBody.getPosition());
            WorldManager.ins().addGridToWorld(new StaticGrid(vc));
        }
    }


    public void dispose(){

    }
}
