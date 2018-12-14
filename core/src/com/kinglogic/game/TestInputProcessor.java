package com.kinglogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.AI.BaseAIBody;
import com.kinglogic.game.AI.RobotFriend;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Actors.Voxel.Blocks.Voxel;
import com.kinglogic.game.Managers.ControllerManager;
import com.kinglogic.game.Managers.GUIManager;
import com.kinglogic.game.Managers.GameManager;
import com.kinglogic.game.Managers.IDs;
import com.kinglogic.game.Managers.WorldManager;
import com.kinglogic.game.Physics.DynamicGrid;
import com.kinglogic.game.Physics.Grid;

/**
 * Created by chris on 4/1/2018.
 */

public class TestInputProcessor implements InputProcessor {
    //public String blockName = IDs.getIDList().get(0);
    private boolean gravLock = false;
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
//                WorldManager.ins().GenerateAsteroid((i*ResourceManager.VOXEL_PIXEL_SIZE*50)-(5*ResourceManager.VOXEL_PIXEL_SIZE*50),(j*ResourceManager.VOXEL_PIXEL_SIZE*50)-(3*ResourceManager.VOXEL_PIXEL_SIZE*50), 50, densities[i][j]);
//            }
//        }


        ////
//        for (int i = 0; i < 10; i++) {
//            WorldManager.ins().GenerateAsteroid((i*ResourceManager.VOXEL_PIXEL_SIZE*50)-(5*ResourceManager.VOXEL_PIXEL_SIZE*50),100, 50, (float)i/20f+.1f);
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
            ControllerManager.ins().PreviousBlock(GameManager.ins().getPlayer());
//            GUIManager.ins().selectedBlockName = ControllerManager.ins().playersCurrentBlock;
//            blockName = IDs.METAL_TEX;
        }
        else if(character == '2'){
            ControllerManager.ins().NextBlock(GameManager.ins().getPlayer());
//            GUIManager.ins().selectedBlockName = ControllerManager.ins().playersCurrentBlock;
//            blockName = IDs.TECH_TEX;

        }
        else if(character == '3'){
            ControllerManager.ins().NextColor(GameManager.ins().getPlayer());
//            GUIManager.ins().selectedColor = ControllerManager.ins().playersCurrentColor;
//            blockName = IDs.ROCK_TEX;
        }
        else if(character == '4'){
            ControllerManager.ins().PreviousColor(GameManager.ins().getPlayer());
//            GUIManager.ins().selectedColor = ControllerManager.ins().playersCurrentColor;
//            blockName = IDs.BASE_TEX;
        }
        else if(character == 's'){
            WorldManager.ins().QueueSave();
        }
        else if(character == 't'){
            WorldManager.ins();
        }
        else if(character == 'f'){
            GameManager.ins().getPlayer().buildMode = !GameManager.ins().getPlayer().buildMode;
//            WorldManager.ins().addEntityToWorld(new RobotFriend("robot", GameManager.ins().getPlayer().myBody.getPosition()));
        }
        else if(character == 'g'){
            //GameManager.ins().getThePlayer().ToggleGravLock();
        }
        else if(character == 'l'){
            System.out.println("queued load");
            WorldManager.ins().QueueLoad("infinity");
        }
        else if(character == ' '){
            if(GameManager.ins().getPlayer().isControlling())
                GameManager.ins().getPlayer().Exit();
            else
                GameManager.ins().getPlayer().Enter(GameManager.ins().getPlayer().lastControlled);
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
        if(GameManager.ins().getPlayer().buildMode) {
            if (button == Input.Buttons.LEFT) {
                if (ControllerManager.ins().numBlocks > 0)
                    if (WorldManager.ins().addVoxelScreenPosition(GameManager.ins().getPlayer().buildPosition.x, Gdx.graphics.getHeight() - GameManager.ins().getPlayer().buildPosition.y, GameManager.ins().getPlayer().playersCurrentBlock, GameManager.ins().getPlayer().playersCurrentColor))
                        ControllerManager.ins().numBlocks--;
                return true;
            }
            if (button == Input.Buttons.RIGHT) {
                WorldManager.ins().playerRemoveVoxelScreenPosition((int) GameManager.ins().getPlayer().buildPosition.x, (int) Gdx.graphics.getHeight() - GameManager.ins().getPlayer().buildPosition.y);
                return true;
            }
        }else{
            if (button == Input.Buttons.LEFT) {
                GameManager.ins().getPlayer().FireMain();
                return true;
            }
            if (button == Input.Buttons.RIGHT) {
                GameManager.ins().getPlayer().FireAlt();
                return true;
            }
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
