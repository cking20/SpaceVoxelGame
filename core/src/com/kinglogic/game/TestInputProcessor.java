package com.kinglogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.kinglogic.game.AI.BaseAIBody;
import com.kinglogic.game.AI.DestructoEnemy;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Actors.Voxel.Voxel;
import com.kinglogic.game.Managers.CameraManager;
import com.kinglogic.game.Managers.IDs;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Managers.WorldManager;
import com.kinglogic.game.Physics.DynamicGrid;
import com.kinglogic.game.Physics.EntityBody;
import com.kinglogic.game.Physics.Grid;
import com.kinglogic.game.Physics.Projectile;
import com.kinglogic.game.Player.PlayerBody;

/**
 * Created by chris on 4/1/2018.
 */

public class TestInputProcessor implements InputProcessor {
    public String blockName = IDs.METAL_TEX;
    public Grid dyn;
    public Grid stc;
    public PlayerBody player;
    public BaseAIBody enemy;
    public TestInputProcessor(){
        player = new PlayerBody("player", new Vector2(800,500));
        WorldManager.ins().addEntityToWorld(player);
        WorldManager.ins().ApplyLightToBody(player.myBody);
        CameraManager.ins().Track(player.view);
        for (int i = 0; i < 40; i++) {
            enemy = new DestructoEnemy("Yellowparasite", new Vector2(400,300));
            WorldManager.ins().addEntityToWorld(enemy);
        }
        stc = new Grid( new VoxelCollection(Voxel.Build(blockName),new Vector2(400,300)));
        WorldManager.ins().addGridToWorld(stc);
        for (int i = 0; i < 10; i++) {
            WorldManager.ins().GenerateAsteroid((i*ResourceManager.voxelPixelSize*50)-(5*ResourceManager.voxelPixelSize*50),100, 50);
        }
        VoxelCollection vc = new VoxelCollection(Voxel.Build(blockName),new Vector2(800,550));
        dyn = new DynamicGrid(vc);

        WorldManager.ins().addGridToWorld(dyn);
        Controllers.addListener(new ControllerAdapter(){
//            public void connected(Controller controller);
//            public void disconnected(Controller controller);
            public boolean buttonDown (Controller controller, int buttonCode){
                System.out.println(buttonCode);
                return true;
            }
            public boolean buttonUp (Controller controller, int buttonCode){
//                System.out.println(buttonCode);
                return true;
            }
            public boolean axisMoved (Controller controller, int axisCode, float value){
//                System.out.println("code: "+axisCode+" value:"+value);
                /*
                if(axisCode == 4){
                    if(value > 0){
                        player.RotateLeft();
                    }else {
                        player.RotateRight();
                    }
                }*/
                return true;
            }
//            public boolean povMoved (Controller controller, int povCode, PovDirection value);
//            public boolean xSliderMoved (Controller controller, int sliderCode, boolean value);
//            public boolean ySliderMoved (Controller controller, int sliderCode, boolean value);
//            public boolean accelerometerMoved (Controller controller, int accelerometerCode, Vector3 value);
        });
    }
    @Override
    public boolean keyDown(int keycode) {

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if(character == '1'){
            blockName = IDs.METAL_TEX;
        }
        else if(character == '2'){
            blockName = IDs.TECH_TEX;

        }
        else if(character == '3'){
            blockName = IDs.ROCK_TEX;
        }
        else if(character == '4'){
            blockName = IDs.BASE_TEX;
        }
        else if(character == ' '){
            if(player.isControlling())
                player.Exit();
            else
                player.Enter(dyn);
        }


        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            WorldManager.ins().addVoxelScreenPosition(screenX,screenY, blockName);
            return true;
        }
        if (button == Input.Buttons.RIGHT) {
            WorldManager.ins().removeVoxelScreenPosition(screenX,screenY);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
