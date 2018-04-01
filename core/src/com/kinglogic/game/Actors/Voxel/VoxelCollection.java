package com.kinglogic.game.Actors.Voxel;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Managers.WorldManager;

/**
 * Created by chris on 4/1/2018.
 */

public class VoxelCollection extends Group {
    public static int maxSize = 31;
    Voxel[][] grid;

    public VoxelCollection(Voxel v, Vector2 position){
        //the size must be odd to get a center position
        if(maxSize%2 == 0)
            maxSize--;

        grid = new Voxel[maxSize][maxSize];

        grid[maxSize/2][maxSize/2] = v;
        super.addActor(v);
        v.setPosition((maxSize/2)*ResourceManager.voxelPixelSize,(maxSize/2)*ResourceManager.voxelPixelSize);
        //todo use v.origin(changes the last bit to +pix/2)
        setPosition(
                position.x-((maxSize * ResourceManager.voxelPixelSize)/2),
                position.y-((maxSize * ResourceManager.voxelPixelSize)/2));
    }

    public boolean addVoxelScreenPos(Voxel v, Vector2 screenPosition){
        System.out.println("Screen pos:" + screenPosition);
        Vector2 worldPosition = WorldManager.ins().screenToWorldCoords(screenPosition);
        System.out.println("World pos:" + worldPosition);
        Vector2 position = mapWorldPointToIndexies(worldPosition);
        System.out.println("index pos:" + position);
        int x = (int)position.x;
        int y = (int)position.y;
        if(verifyVoxelPlacement(x,y)) {
            grid[x][y] = v;
            super.addActor(v);
            v.setPosition((x * ResourceManager.voxelPixelSize), (y * ResourceManager.voxelPixelSize));
            return true;
        } else return false;
    }

    public boolean removeVoxelScreenPos(Vector2 screenPosition){
        Vector2 position = mapWorldPointToIndexies(WorldManager.ins().screenToWorldCoords(screenPosition));
        int x = (int)position.x;
        int y = (int)position.y;
        if(grid[x][y] != null) {
            Voxel v = grid[x][y];
            super.removeActor(v);
            grid[x][y] = null;
            return true;
        } else return false;
    }

    private Vector2 mapWorldPointToIndexies(Vector2 worldPos){
        worldPos = this.stageToLocalCoordinates(worldPos);
        worldPos.x = (int)(worldPos.x/ResourceManager.voxelPixelSize);
        worldPos.y = (int)(worldPos.y/ResourceManager.voxelPixelSize);
        return worldPos;
    }

    public Voxel[][] getGrid(){
        return grid;
    }

    public boolean verifyVoxelPlacement(int x, int y){
        boolean isGood = false;
        //check neighbors if there true
        if(x+1 < grid.length)
            if(grid[x+1][y] != null)
                isGood = true;
        if(x-1 > 0)
            if(grid[x-1][y] != null)
                isGood = true;
        if(y+1 < grid.length)
            if(grid[x][y+1] != null)
                isGood = true;
        if(y-1 > 0)
            if(grid[x][y-1] != null)
                isGood = true;

        //check self if there false
        if(grid[x][y] != null)
            isGood = false;

        return isGood;
    }


}
