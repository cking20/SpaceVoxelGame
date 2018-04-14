package com.kinglogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Actors.Voxel.Voxel;
import com.kinglogic.game.Managers.IDs;
import com.kinglogic.game.Managers.WorldManager;
import com.kinglogic.game.Physics.DynamicGrid;
import com.kinglogic.game.Physics.EntityBody;
import com.kinglogic.game.Physics.Grid;

/**
 * Created by chris on 4/1/2018.
 */

public class TestInputProcessor implements InputProcessor {
    public String blockName = IDs.METAL_TEX;
    public Grid dyn;
    public Grid stc;
    public EntityBody player;
    public TestInputProcessor(){
        player = new EntityBody("player", new Vector2(450,300));
        WorldManager.ins().addEntityToWorld(player);
        stc = new Grid( new VoxelCollection(new Voxel(blockName),new Vector2(400,300)));
        WorldManager.ins().addGridToWorld(stc);
        WorldManager.ins().GenerateAsteroid(100,100, 10);
        VoxelCollection vc = new VoxelCollection(new Voxel(blockName),new Vector2(200,300));
        vc.setRotation(45);
        dyn = new DynamicGrid(vc);

        WorldManager.ins().addGridToWorld(dyn);
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
