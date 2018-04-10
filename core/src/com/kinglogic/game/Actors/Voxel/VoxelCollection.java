package com.kinglogic.game.Actors.Voxel;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Managers.WorldManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by chris on 4/1/2018.
 */

public class VoxelCollection extends Group {
    public static int maxSize = 10;//241;
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
        if(!validPosition(x,y))return false;
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
        if(!validPosition(x,y))return false;
        if(grid[x][y] != null) {
            Voxel v = grid[x][y];
            super.removeActor(v);
            grid[x][y] = null;
            //todo Splitting algorithm

            VoxelUtils.Index ref = getFirstIndex();
            if(grid[x][y+1] != null)
                if(!connects(new VoxelUtils.Index(x,y+1),ref)){
                    System.out.println("should cascade up");
                    removeConnectedTo(x,y+1);
                }
            if(grid[x][y-1] != null)
                if(!connects(new VoxelUtils.Index(x,y-1),ref)){
                    System.out.println("should cascade down");
                    removeConnectedTo(x,y-1);
                }
            if(grid[x-1][y] != null)
                if(!connects(new VoxelUtils.Index(x-1,y),ref)){
                    System.out.println("should cascade left");
                    removeConnectedTo(x+1,y);
                }
            if(grid[x+1][y] != null)
                if(!connects(new VoxelUtils.Index(x+1,y),ref)){
                    System.out.println("should cascade right");
                    removeConnectedTo(x-1,y);
                }
            ////////////////////////


            return true;
        } else{
            System.out.println("grid @ click pos == null");
            return false;
        }

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

    private boolean validPosition(int x, int y){
        if(x < 0 || y < 0) return false;
        if(x >= maxSize || y >= maxSize) return false;
        return true;
    }

    private ArrayList<Voxel> getNeighbors(int x,int y){
        ArrayList<Voxel> neighbors = new ArrayList<Voxel>();
        if(validPosition(x+1,y))neighbors.add(grid[x+1][y]);
        if(validPosition(x-1,y))neighbors.add(grid[x-1][y]);
        if(validPosition(x,y+1))neighbors.add(grid[x][y+1]);
        if(validPosition(x,y+1))neighbors.add(grid[x][y-1]);
        return neighbors;
    }
    private Voxel getFirst(){
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                if(grid[i][j] != null)
                    return grid[i][j];
            }
        }
        return null;
    }
    private VoxelUtils.Index getFirstIndex(){
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                if(grid[i][j] != null)
                    return new VoxelUtils.Index(i,j);
            }
        }
        return null;
    }
    private boolean connects(VoxelUtils.Index from, VoxelUtils.Index to){
        //todo improve this
        boolean[][] visited = new boolean[maxSize][maxSize];
        if (from == null || to == null) return false;
        IndexCompare compare = new IndexCompare(to);
        PriorityQueue<VoxelUtils.Index> queue = new PriorityQueue<VoxelUtils.Index>(11,compare);
        queue.add(from);//.Push(myMap[(int)startPos.x][(int)startPos.y]);
        Voxel goal = grid[to.x][to.y];
        //evaluate
        while (queue.size() > 0){
            VoxelUtils.Index c = queue.remove();
            System.out.println("checking at"+c.x+", "+c.y);
            Voxel current = grid[c.x][c.y];
            if(current == goal){
                return true;
            }
            if(validPosition(c.x+1,c.y)){
                if(grid[c.x+1][c.y] != null && !visited[c.x+1][c.y]){
                    visited[c.x+1][c.y] = true;
                    VoxelUtils.Index next = new VoxelUtils.Index(c.x + 1, c.y);
                    if (!queue.contains(next))
                        queue.add(next);
                }
            }
            if(validPosition(c.x-1,c.y)){
                if(grid[c.x-1][c.y] != null && !visited[c.x-1][c.y]){
                    visited[c.x-1][c.y] = true;
                    VoxelUtils.Index next = new VoxelUtils.Index(c.x - 1, c.y);
                    if (!queue.contains(next))
                        queue.add(next);
                }
            }
            if(validPosition(c.x,c.y+1)){
                if(grid[c.x][c.y+1] != null && !visited[c.x][c.y+1]){
                    visited[c.x][c.y+1] = true;
                    VoxelUtils.Index next = new VoxelUtils.Index(c.x, c.y + 1);
                    if (!queue.contains(next))
                        queue.add(next);
                }
            }
            if(validPosition(c.x,c.y-1)){
                if(grid[c.x][c.y-1] != null && !visited[c.x][c.y-1]){
                    visited[c.x][c.y-1] = true;
                    VoxelUtils.Index next = new VoxelUtils.Index(c.x, c.y - 1);
                    if (!queue.contains(next))
                        queue.add(next);
                }
            }
        }
        return false;
    }
    private void removeConnectedTo(int x, int y){
        if(!validPosition(x,y))return;
        Voxel[][] delta = getVoxelsConnectedToPos(x,y);
        if(delta != null){
            for(int i = 0; i < grid.length; i++){
                for(int j = 0; j < grid[0].length; j++){
                    if(delta[i][j] != null) {
                        super.removeActor(grid[i][j]);
                        grid[i][j] = null;
                    }
                }
            }
        }
    }
    private Voxel[][] getVoxelsConnectedToPos(int x, int y){
        //todo bfs add
        Voxel[][] connected = new Voxel[maxSize][maxSize];
        boolean[][] visited = new boolean[maxSize][maxSize];
        if(!validPosition(x,y) || grid[x][y] == null){
            System.out.println("returning null on getVoxelsConnectedToPos");
            return null;
        }
        LinkedList<VoxelUtils.Index> queue = new LinkedList<VoxelUtils.Index>();
        VoxelUtils.Index from = new VoxelUtils.Index(x,y);
        queue.add(from);//.Push(myMap[(int)startPos.x][(int)startPos.y]);
        connected[x][y] = grid[x][y];
        //evaluate
        while (queue.size() > 0){
            VoxelUtils.Index c = queue.remove();
            System.out.println("checking at"+c.x+", "+c.y);
            Voxel current = grid[c.x][c.y];

            if(validPosition(c.x+1,c.y)){
                if(grid[c.x+1][c.y] != null && !visited[c.x+1][c.y]){
                    visited[c.x+1][c.y] = true;
                    VoxelUtils.Index next = new VoxelUtils.Index(c.x + 1, c.y);
                    if (!queue.contains(next)) {
                        queue.add(next);
                        connected[c.x+1][c.y] = grid[c.x+1][c.y];
                    }
                }
            }
            if(validPosition(c.x-1,c.y)){
                if(grid[c.x-1][c.y] != null && !visited[c.x-1][c.y]){
                    visited[c.x-1][c.y] = true;
                    VoxelUtils.Index next = new VoxelUtils.Index(c.x - 1, c.y);
                    if (!queue.contains(next)){
                        queue.add(next);
                        connected[c.x-1][c.y] = grid[c.x-1][c.y];
                    }
                }
            }
            if(validPosition(c.x,c.y+1)){
                if(grid[c.x][c.y+1] != null && !visited[c.x][c.y+1]){
                    visited[c.x][c.y+1] = true;
                    VoxelUtils.Index next = new VoxelUtils.Index(c.x, c.y + 1);
                    if (!queue.contains(next)){
                        queue.add(next);
                        connected[c.x][c.y+1] = grid[c.x][c.y+1];
                    }
                }
            }
            if(validPosition(c.x,c.y-1)){
                if(grid[c.x][c.y-1] != null && !visited[c.x][c.y-1]){
                    visited[c.x][c.y-1] = true;
                    VoxelUtils.Index next = new VoxelUtils.Index(c.x, c.y - 1);
                    if (!queue.contains(next)){
                        queue.add(next);
                        connected[c.x][c.y-1] = grid[c.x][c.y-1];
                    }
                }
            }
        }
        return connected;
    }

    public boolean verifyVoxelPlacement(int x, int y){
        boolean isGood = false;
        //check neighbors if there true
        if(validPosition(x+1,y) && x+1 < grid.length){
            if(grid[x+1][y] != null)
                isGood = true;
        } else return false;
        if(validPosition(x-1,y) && x-1 > 0){
            if(grid[x-1][y] != null)
                isGood = true;
        } else return false;
        if(validPosition(x,y+1) && y+1 < grid.length){
            if(grid[x][y+1] != null)
                isGood = true;
        } else return false;
        if(validPosition(x,y-1) && y-1 > 0){
            if(grid[x][y-1] != null)
                isGood = true;
        } else return false;

        //check self if there false
        if(validPosition(x,y) && grid[x][y] != null)
            isGood = false;

        return isGood;
    }


}
