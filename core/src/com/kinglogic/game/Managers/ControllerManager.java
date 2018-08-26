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
import com.kinglogic.game.Player.PlayerBody;
import com.kinglogic.game.TestInputProcessor;

import java.util.ArrayList;

/**
 * Created by chris on 4/20/2018.
 */

public class ControllerManager {
    private static ControllerManager instance;
    private ControllerListener playerControllerAdapter;
    private Vector2 mousePos;
    private TestInputProcessor tip;

//    public  String playersCurrentBlock = IDs.getIDList().get(0);
//    private int currentBlockIndex = 0;

//    public Color playersCurrentColor = IDs.getColorList().get(0);
//    private int currentColorIndex = 0;
    public int numBlocks = 10000;

    private Vector2 rightStickVec = new Vector2(0,0);
    private Vector2 leftStickVec = new Vector2(0,0);


    public static ControllerManager ins(){
        if(instance == null)
            instance = new ControllerManager();
        return instance;
    }

    public int getNumControllers(){
        return Controllers.getControllers().size;
    }
    private ControllerManager(){
        tip = new TestInputProcessor();
        Gdx.input.setInputProcessor(tip);
        mousePos = new Vector2(0,0);
        playerControllerAdapter = new ControllerListener(){
            @Override
            public void connected(Controller controller) {
                System.out.println("connecting "+ controller);
                GameManager.ins().addPlayer();
            }

            @Override
            public void disconnected(Controller controller) {
                System.out.println("removing "+ controller);

                int playerNum = Controllers.getControllers().indexOf(controller, true);
                GameManager.ins().removePlayer(playerNum);
            }

            @Override
            public boolean buttonDown(Controller controller, int buttonCode) {
                return false;
            }

            @Override
            public boolean buttonUp(Controller controller, int buttonCode) {
                int playerNumber = Controllers.getControllers().indexOf(controller, true);
                if(playerNumber < 0){
//                    controllers.add(controller);
                    playerNumber = 0;
                }
                if(Constants.DEBUG)
                    System.out.println("button "+buttonCode + " up");
                switch (buttonCode){
                    case 0:// A
                        Jump(playerNumber);
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
                        if(!GameManager.ins().getPlayer(playerNumber).buildMode)
                            FireMain(playerNumber);
                        break;
                    case 6:// SHARE
                        GameManager.ins().getPlayer(playerNumber).buildMode = !GameManager.ins().getPlayer(playerNumber).buildMode;
                        break;
                    case 7:// MENU
                        CreateNewGrid(playerNumber);
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
                int playerIndex = Controllers.getControllers().indexOf(controller, true);
                PlayerBody player = GameManager.ins().getPlayer(playerIndex);
                System.out.println(povCode);
                if(value == PovDirection.north) {
                    NextColor(player);
//                    GUIManager.ins().selectedColor = playersCurrentColor;
//                    System.out.println("up");
                }if(value == PovDirection.south) {
                    PreviousColor(player);
//                    GUIManager.ins().selectedColor = playersCurrentColor;
//                    System.out.println("down");
                }if(value == PovDirection.west) {
                    PreviousBlock(player);
//                    GUIManager.ins().selectedBlockName = playersCurrentBlock;
                }   //System.out.println("left");
                if(value == PovDirection.east) {
                    NextBlock(player);
//                    GUIManager.ins().selectedBlockName = playersCurrentBlock;
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
        for (int i = 0; i < Controllers.getControllers().size; i++) {
//            //axis
            if(!GameManager.ins().getPlayer(i).buildMode){
                switch (Controllers.getControllers().get(i).getPov(0)){
                    case east:
                        GameManager.ins().getPlayer(i).GoRight();
                        GameManager.ins().getPlayer(i).TurnRight();
                        break;
                    case west:
                        GameManager.ins().getPlayer(i).GoLeft();
                        GameManager.ins().getPlayer(i).TurnLeft();
                        break;
                    case south:
                        GameManager.ins().getPlayer(i).GoBackward();
                        break;
                }

            }


            rightStickVec = new Vector2(Controllers.getControllers().get(i).getAxis(2),Controllers.getControllers().get(i).getAxis(3));
            leftStickVec  = new Vector2(Controllers.getControllers().get(i).getAxis(0),Controllers.getControllers().get(i).getAxis(1));

            if(rightStickVec.len() > .5f){
                GameManager.ins().getPlayer(i).LookToward(rightStickVec.nor());
            }

            if (Controllers.getControllers().get(i).getAxis(4) > .5) {
//                System.out.println("axis 4 > .5");
                GameManager.ins().getPlayer(i).RotateLeft();
            } else if (Controllers.getControllers().get(i).getAxis(4) < -.5) {
//                System.out.println("axis 4  < -.5");
                GameManager.ins().getPlayer(i).RotateRight();
            }
            if (Controllers.getControllers().get(i).getAxis(0) < -.5) {
                //GameManager.ins().getThePlayer().GoForeward();
            } else if (Controllers.getControllers().get(i).getAxis(0) > .5) {
                if(!Controllers.getControllers().get(i).getButton(4)) {
                    GameManager.ins().getPlayer(i).GoBackward();
                }
            }
            if (Controllers.getControllers().get(i).getAxis(1) < -.5) {
                if(!Controllers.getControllers().get(i).getButton(4)) {
                    GameManager.ins().getPlayer(i).GoLeft();
                    GameManager.ins().getPlayer(i).TurnLeft();
                }
            } else if (Controllers.getControllers().get(i).getAxis(1) > .5) {
                if(!Controllers.getControllers().get(i).getButton(4)) {
                    GameManager.ins().getPlayer(i).GoRight();
                    GameManager.ins().getPlayer(i).TurnRight();
                }
            }

            float dX = Controllers.getControllers().get(i).getAxis(3);
            float dY = -Controllers.getControllers().get(i).getAxis(2);
            if(dX > .05 || dX < -.05){
                GameManager.ins().getPlayer(i).buildPosition.x+=dX*5;
            }
            if(dY > .05 || dY < -.05) {
                GameManager.ins().getPlayer(i).buildPosition.y += dY * 5;
            }

            if (Controllers.getControllers().get(i).getButton(0)) {
                Jump(i);
            }
            if (Controllers.getControllers().get(i).getButton(9)) {
                //ZOOM IN
                CameraManager.ins().ZoomIn();
            }
            if (Controllers.getControllers().get(i).getButton(8)) {
                //ZOOM OUT
                CameraManager.ins().ZoomOut();
            }
            //todo optimization store a local copy of getPlayers
            if (Controllers.getControllers().get(i).getButton(4)) {
                if(GameManager.ins().getPlayer(i).buildMode)
                    if(numBlocks > 0)
                        if(WorldManager.ins().addVoxelScreenPosition(GameManager.ins().getPlayers()[i].buildPosition.x, Gdx.graphics.getHeight()-GameManager.ins().getPlayers()[i].buildPosition.y, GameManager.ins().getPlayers()[i].playersCurrentBlock, GameManager.ins().getPlayers()[i].playersCurrentColor))
                            numBlocks--;
    //                WorldManager.ins().addVoxelScreenPosition(GUIManager.ins().targetPosition.x, Gdx.graphics.getHeight()-GUIManager.ins().targetPosition.y, GUIManager.ins().selectedBlockName);
            }
            if (Controllers.getControllers().get(i).getButton(5)) {
                if(GameManager.ins().getPlayer(i).buildMode)
                    WorldManager.ins().playerRemoveVoxelScreenPosition((int)GameManager.ins().getPlayers()[i].buildPosition.x,(int)Gdx.graphics.getHeight()-GameManager.ins().getPlayers()[i].buildPosition.y);
            }

        }
        if(Controllers.getControllers().size == 0){//no controllers
            if(Gdx.input.isKeyPressed(Input.Keys.UP)){
                GameManager.ins().getPlayer().GoForeward();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
                GameManager.ins().getPlayer().GoBackward();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
                GameManager.ins().getPlayer().GoLeft();
                GameManager.ins().getPlayer().TurnLeft();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
                GameManager.ins().getPlayer().GoRight();
                GameManager.ins().getPlayer().TurnRight();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.PAGE_UP)){
                GameManager.ins().getPlayer().RotateLeft();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.PAGE_DOWN)){
                GameManager.ins().getPlayer().RotateRight();
            }
            if(Gdx.input.isKeyPressed(Input.Keys.MINUS)){
                CameraManager.ins().mainCamera.zoom-=.05;
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.EQUALS)){
                CameraManager.ins().mainCamera.zoom+=.05;
            }
            if(Gdx.input.isKeyPressed(Input.Keys.NUM_0)){
                GameManager.ins().getPlayer().FireMain();
            }
            mousePos.x = Gdx.input.getX();
            mousePos.y = Gdx.graphics.getHeight()-Gdx.input.getY();
            GameManager.ins().getPlayer().buildPosition = mousePos;
        }

    }
    public void PlayerDestroyedBlock(){
        numBlocks++;
    }

    public void NextBlock(PlayerBody playerBody){
        playerBody.currentBlockIndex++;
        playerBody.currentBlockIndex = playerBody.currentBlockIndex % IDs.ins().getNumBlockIds();
//        System.out.println(currentBlockIndex);
        playerBody.playersCurrentBlock = IDs.ins().getID(playerBody.currentBlockIndex);
    }
    public void PreviousBlock(PlayerBody playerBody){
        playerBody.currentBlockIndex--;
        if(playerBody.currentBlockIndex < 0)
            playerBody.currentBlockIndex = IDs.ins().getNumBlockIds()-1;
//        System.out.println(currentBlockIndex);
        playerBody.playersCurrentBlock = IDs.ins().getID(playerBody.currentBlockIndex);
    }
    public void NextColor(PlayerBody playerBody){
        playerBody.currentColorIndex++;
        playerBody.currentColorIndex = playerBody.currentColorIndex % IDs.ins().getNumColorIds();
        playerBody.playersCurrentColor = IDs.ins().getColor(playerBody.currentColorIndex);

    }
    public void PreviousColor(PlayerBody playerBody){
        playerBody.currentColorIndex--;
        if(playerBody.currentColorIndex < 0)
            playerBody.currentColorIndex = IDs.ins().getNumColorIds()-1;
        playerBody.playersCurrentColor = IDs.ins().getColor(playerBody.currentColorIndex);

    }
    public void TogglePlayerControl(int id){
        if(GameManager.ins().getPlayer(id).isControlling())
            GameManager.ins().getPlayer(id).Exit();
        else
            GameManager.ins().getPlayer(id).Enter(GameManager.ins().getPlayer(id).lastControlled);
    }
    public void Jump(int player){
        GameManager.ins().getPlayer(player).GoForeward();
    }
    public void FireMain(int player){
        if(numBlocks > 0) {
            GameManager.ins().getPlayer(player).FireMain();
            numBlocks--;
        }
    }
    public void CreateNewGrid(int player){
        if(numBlocks > 0) {
            numBlocks--;
            VoxelCollection vc = new VoxelCollection(Voxel.Build(IDs.ROCK_TEX), GameManager.ins().getPlayer(player).myBody.getPosition());
            WorldManager.ins().addGridToWorld(new StaticGrid(vc));
        }
    }


    public void dispose(){

    }
}
