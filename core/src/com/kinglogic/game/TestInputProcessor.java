package com.kinglogic.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.Actors.Voxel.Voxel;
import com.kinglogic.game.Managers.WorldManager;
import com.kinglogic.game.Physics.Grid;

/**
 * Created by chris on 4/1/2018.
 */

public class TestInputProcessor implements InputProcessor {
    Grid dyn;
    public TestInputProcessor(){
        dyn = new Grid( new VoxelCollection(new Voxel("metal"),new Vector2(400,300)));
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

        }
        else if(character == '2'){

        }
        else if(character == '3'){

        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            // Some stuff
            System.out.println("click @ ("+screenX+"),("+(Gdx.graphics.getHeight()-screenY)+")");
            dyn.voxels.addVoxelScreenPos(new Voxel("metal"), new Vector2(screenX,screenY));
            return true;
        }
        if (button == Input.Buttons.RIGHT) {
            // Some stuff
            System.out.println("click @ ("+screenX+"),("+(Gdx.graphics.getHeight()-screenY)+")");
            dyn.voxels.removeVoxelScreenPos(new Vector2(screenX,screenY));
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
