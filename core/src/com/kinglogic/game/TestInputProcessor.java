package com.kinglogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.kinglogic.game.AI.BaseAIBody;
import com.kinglogic.game.AI.DestructoEnemy;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Actors.Voxel.Voxel;
import com.kinglogic.game.Managers.CameraManager;
import com.kinglogic.game.Managers.ControllerManager;
import com.kinglogic.game.Managers.GUIManager;
import com.kinglogic.game.Managers.GameManager;
import com.kinglogic.game.Managers.IDs;
import com.kinglogic.game.Managers.PCGManager;
import com.kinglogic.game.Managers.PersistenceManager;
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
    //public String blockName = IDs.getIDList().get(0);
    public Grid dyn;
    public Grid stc;
    //public PlayerBody player;
    public BaseAIBody enemy;
    public TestInputProcessor(){
//        player = new PlayerBody("player", new Vector2(800,500));
//        WorldManager.ins().addEntityToWorld(player);
//        WorldManager.ins().ApplyLightToBody(player.myBody);
//        CameraManager.ins().Track(player.view);
//
//        for (int i = 0; i < 40; i++) {
//            enemy = new DestructoEnemy("Yellowparasite", new Vector2(400,300));
//            WorldManager.ins().addEntityToWorld(enemy);
//        }
//        stc = new Grid( new VoxelCollection(Voxel.Build(blockName),new Vector2(400,300)));
//        WorldManager.ins().addGridToWorld(stc);

        ////
//        boolean[][] seededMap = PCGManager.ins().generateBetterAsteroid(6, .8f);
//        float[][] densities = PCGManager.ins().genDensityMap(seededMap, .1f, .5f);
//        for (int i = 0; i < densities.length; i++) {
//            for (int j = 0; j < densities[0].length; j++) {
//                WorldManager.ins().GenerateAsteroid((i*ResourceManager.voxelPixelSize*50)-(5*ResourceManager.voxelPixelSize*50),(j*ResourceManager.voxelPixelSize*50)-(3*ResourceManager.voxelPixelSize*50), 50, densities[i][j]);
//            }
//        }


        ////
//        for (int i = 0; i < 10; i++) {
//            WorldManager.ins().GenerateAsteroid((i*ResourceManager.voxelPixelSize*50)-(5*ResourceManager.voxelPixelSize*50),100, 50, (float)i/20f+.1f);
//        }

//        VoxelCollection vc = new VoxelCollection(Voxel.Build(IDs.getIDList().get(0)),new Vector2(800,550));
//        dyn = new DynamicGrid(vc);
//        WorldManager.ins().addGridToWorld(dyn);
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
            ControllerManager.ins().PreviousBlock();
            GUIManager.ins().selectedBlockName = ControllerManager.ins().playersCurrentBlock;
//            blockName = IDs.METAL_TEX;
        }
        else if(character == '2'){
            ControllerManager.ins().NextBlock();
            GUIManager.ins().selectedBlockName = ControllerManager.ins().playersCurrentBlock;
//            blockName = IDs.TECH_TEX;

        }
        else if(character == '3'){
            ControllerManager.ins().NextColor();
            GUIManager.ins().selectedColor = ControllerManager.ins().playersCurrentColor;
//            blockName = IDs.ROCK_TEX;
        }
        else if(character == '4'){
            ControllerManager.ins().PreviousColor();
            GUIManager.ins().selectedColor = ControllerManager.ins().playersCurrentColor;
//            blockName = IDs.BASE_TEX;
        }
        else if(character == 's'){
            WorldManager.ins().QueueSave();
        }
        else if(character == 't'){
            WorldManager.ins();
        }
        else if(character == 'l'){
            System.out.println("queued load");
            WorldManager.ins().QueueLoad("infinity");
        }
        else if(character == ' '){
            if(GameManager.ins().getThePlayer().isControlling())
                GameManager.ins().getThePlayer().Exit();
            else
                GameManager.ins().getThePlayer().Enter(GameManager.ins().getThePlayer().lastControlled);
        }
        else if(character == '.'){
            VoxelCollection vc = new VoxelCollection(Voxel.Build(IDs.ROCK_TEX),new Vector2(800,550));
            dyn = new DynamicGrid(vc);
            WorldManager.ins().addGridToWorld(dyn);
        }


        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            WorldManager.ins().addVoxelScreenPosition(GUIManager.ins().targetPosition.x,Gdx.graphics.getHeight()-GUIManager.ins().targetPosition.y, GUIManager.ins().selectedBlockName);
            return true;
        }
        if (button == Input.Buttons.RIGHT) {
            WorldManager.ins().removeVoxelScreenPosition((int)GUIManager.ins().targetPosition.x,(int)Gdx.graphics.getHeight()-GUIManager.ins().targetPosition.y);
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
