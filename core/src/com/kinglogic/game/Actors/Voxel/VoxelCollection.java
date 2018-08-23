package com.kinglogic.game.Actors.Voxel;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.kinglogic.game.Actors.Voxel.Blocks.Voxel;
import com.kinglogic.game.Constants;
import com.kinglogic.game.Managers.GUIManager;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Managers.WorldManager;
import com.kinglogic.game.Physics.DynamicGrid;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Created by chris on 4/1/2018.
 */

/**
 * Holds Data pertaining to grid of squares
 */
public class VoxelCollection extends Group {
    public static int maxSize = Constants.MAX_GRID_SIZE;
    Voxel[][] grid;

    /**
     * Init at a world position with a root
     * @param v root voxel
     * @param position in world units
     */
    public VoxelCollection(Voxel v, Vector2 position){
        //the size must be odd to get a center position
        if(maxSize%2 == 0)
            maxSize--;

        grid = new Voxel[maxSize][maxSize];

        grid[maxSize/2][maxSize/2] = v;
        super.addActor(v);
        v.setPosition((maxSize/2)*ResourceManager.VOXEL_PIXEL_SIZE,(maxSize/2)*ResourceManager.VOXEL_PIXEL_SIZE);
        //todo use v.origin(changes the last bit to +pix/2)
        setPosition(
                position.x-((maxSize * ResourceManager.VOXEL_PIXEL_SIZE)/2),
                position.y-((maxSize * ResourceManager.VOXEL_PIXEL_SIZE)/2));
    }

    public VoxelCollection(){
        grid = new Voxel[maxSize][maxSize];
    }

    /**
     * Init with a pre existing grid at a position
     * @param v pre existing grid
     * @param position in world units
     */
    public VoxelCollection(Voxel[][] v, Vector2 position, float rotation){
        //the size must be odd to get a center position
        if(maxSize%2 == 0)
            maxSize--;
        grid = v;
        if(grid == null){
            grid = new Voxel[maxSize][maxSize];
        }else {
            for (int i = 0; i < v.length; i++) {
                for (int j = 0; j < v.length; j++) {
                    if (grid[i][j] != null)
                        super.addActor(grid[i][j]);
                }
            }
        }
        //v.setPosition((maxSize/2)*ResourceManager.VOXEL_PIXEL_SIZE,(maxSize/2)*ResourceManager.VOXEL_PIXEL_SIZE);
        //todo use v.origin(changes the last bit to +pix/2)
        setPosition(
                position.x,
                position.y);
        setRotation(rotation);
    }

    /**
     * Add a voxel to this collection in screen units
     * @param v to add
     * @param screenPosition position to add at
     * @return false iff the voxel wasnt added
     */
    public boolean addVoxelScreenPos(Voxel v, Vector2 screenPosition){
        v.setColor(GUIManager.ins().selectedColor);
//        System.out.println("Screen pos:" + screenPosition);
        Vector2 worldPosition = WorldManager.ins().screenToWorldCoords(screenPosition.cpy());
//        System.out.println("World pos:" + worldPosition);
        Vector2 position = mapWorldPointToIndexies(worldPosition);
//        System.out.println("index pos:" + position);
        int x = (int)position.x;
        int y = (int)position.y;
        return addVoxelIndex(v,x,y);
    }
    public boolean addVoxelWorldPos(Voxel v, Vector2 worldPosition){
//        System.out.println("Screen pos:" + screenPosition);
//        Vector2 worldPosition = WorldManager.ins().screenToWorldCoords(screenPosition);
//        System.out.println("World pos:" + worldPosition);
        Vector2 position = mapWorldPointToIndexies(worldPosition.cpy());
//        System.out.println("index pos:" + position);
        int x = (int)position.x;
        int y = (int)position.y;
        return addVoxelIndex(v,x,y);
    }

    /**
     * Add a voxel to this collection in index units
     * @param v voxel to add
     * @param x column (0,0 hasProperty the bottom left)
     * @param y row (0,0 hasProperty the bottom left)
     * @return false iff the voxel wasnt added
     */
    public boolean addVoxelIndex(Voxel v, int x, int y){
        if(!validIndex(x,y))return false;
        if(verifyVoxelPlacement(x,y)) {
            grid[x][y] = v;
            super.addActor(v);
            v.setPosition((x * ResourceManager.VOXEL_PIXEL_SIZE), (y * ResourceManager.VOXEL_PIXEL_SIZE));
            return true;
        } else return false;
    }

    public boolean hardAddVoxelIndex(Voxel v, int x, int y){
        if(!validIndex(x,y))return false;
        if(grid[x][y] == null) {
            grid[x][y] = v;
            super.addActor(v);
            v.setPosition((x * ResourceManager.VOXEL_PIXEL_SIZE), (y * ResourceManager.VOXEL_PIXEL_SIZE));
            return true;
        }
        return false;
    }

    /**
     * Attempts to remove a block at screen position
     * @param screenPosition
     * @return true iff a block has been removed
     */
    public boolean removeVoxelScreenPos(Vector2 screenPosition){
//        System.out.println("remove screenpos @"+screenPosition);
        Vector2 position = mapWorldPointToIndexies(WorldManager.ins().screenToWorldCoords(screenPosition.cpy()));
//        System.out.println("remove indexpos @"+position);
        int x = (int)position.x;
        int y = (int)position.y;
        return removeVoxelIndex(x,y);
    }
    public boolean removeVoxelWorldPos(Vector2 worldPosition){
//        System.out.println("remove screenpos @"+screenPosition);
        Vector2 position = mapWorldPointToIndexies(worldPosition.cpy());
//        System.out.println("remove indexpos @"+position);
        int x = (int)position.x;
        int y = (int)position.y;
        return removeVoxelIndex(x,y);
    }

    /**
     * Attempts to remove a block at screen position
     * @param x column (0,0 hasProperty the bottom left)
     * @param y row (0,0 hasProperty the bottom left)
     * @return false iff the voxel wasnt added
     * @return true iff a block has been removed
     */
    public boolean removeVoxelIndex(int x, int y){
        if(!validIndex(x,y))return false;
        if(!validIndex(x+1,y))return false;
        if(!validIndex(x,y+1))return false;
        if(!validIndex(x-1,y))return false;
        if(!validIndex(x,y-1))return false;
        if(grid[x][y] != null) {
            Voxel v = grid[x][y];
            super.removeActor(v);
            grid[x][y] = null;
            Voxel[][] toRemove;
            VoxelUtils.Index ref = getCenterIndex();//getFirstIndex();
            if(grid[ref.x][ref.y] == null)
                ref = getFirstIndex();
            if(grid[x][y+1] != null)
                if(!connects(new VoxelUtils.Index(x,y+1),ref)){
//                    System.out.println("should cascade up");
                    toRemove = removeConnectedTo(x,y+1);
                    WorldManager.ins().addGridToWorld(new DynamicGrid(new VoxelCollection(toRemove, new Vector2(getX(),getY()),this.getRotation())));
                }
            if(grid[x][y-1] != null)
                if(!connects(new VoxelUtils.Index(x,y-1),ref)){
//                    System.out.println("should cascade down");
                    toRemove = removeConnectedTo(x,y-1);
                    WorldManager.ins().addGridToWorld(new DynamicGrid(new VoxelCollection(toRemove, new Vector2(getX(),getY()),this.getRotation())));
                }
            if(grid[x-1][y] != null)
                if(!connects(new VoxelUtils.Index(x-1,y),ref)){
//                    System.out.println("should cascade left");
                    toRemove = removeConnectedTo(x+1,y);
                    WorldManager.ins().addGridToWorld(new DynamicGrid(new VoxelCollection(toRemove, new Vector2(getX(),getY()),this.getRotation())));
                }
            if(grid[x+1][y] != null)
                if(!connects(new VoxelUtils.Index(x+1,y),ref)){
//                    System.out.println("should cascade right");
                    toRemove = removeConnectedTo(x-1,y);
                    if(toRemove != null)
                        WorldManager.ins().addGridToWorld(new DynamicGrid(new VoxelCollection(toRemove, new Vector2(getX(),getY()),this.getRotation())));
                }
            return true;
        } else{
//            System.out.println("grid @ click pos == null");
            return false;
        }

    }

    /**
     * WARNING THIS SHOULD ONLY EVER BE USED ON WORLD STARTUP!!!!!!
     */
    public void checkAllConnected(){
        for (int x = 0; x < grid.length; x++) {
            for (int y = 0; y < grid.length; y++) {
                if(validIndex(x,y)){
                    if(grid[x][y] != null) {
                        Voxel v = grid[x][y];
                        Voxel[][] toRemove;
                        VoxelUtils.Index ref = getCenterIndex();//getFirstIndex();
                        if(grid[ref.x][ref.y] == null)
                            ref = getFirstIndex();
                        if (grid[x][y] != null)
                            if (!connects(new VoxelUtils.Index(x, y), ref)) {
                                //                    System.out.println("should cascade up");
                                toRemove = removeConnectedTo(x, y);
                                WorldManager.ins().addGridToWorld(new DynamicGrid(new VoxelCollection(toRemove, new Vector2(getX(), getY()), this.getRotation())));
                            }
                        if (grid[x][y + 1] != null)
                            if (!connects(new VoxelUtils.Index(x, y + 1), ref)) {
    //                    System.out.println("should cascade up");
                                toRemove = removeConnectedTo(x, y + 1);
                                WorldManager.ins().addGridToWorld(new DynamicGrid(new VoxelCollection(toRemove, new Vector2(getX(), getY()), this.getRotation())));
                            }
                        if (grid[x][y - 1] != null)
                            if (!connects(new VoxelUtils.Index(x, y - 1), ref)) {
    //                    System.out.println("should cascade down");
                                toRemove = removeConnectedTo(x, y - 1);
                                WorldManager.ins().addGridToWorld(new DynamicGrid(new VoxelCollection(toRemove, new Vector2(getX(), getY()), this.getRotation())));
                            }
                        if (grid[x - 1][y] != null)
                            if (!connects(new VoxelUtils.Index(x - 1, y), ref)) {
    //                    System.out.println("should cascade left");
                                toRemove = removeConnectedTo(x + 1, y);
                                WorldManager.ins().addGridToWorld(new DynamicGrid(new VoxelCollection(toRemove, new Vector2(getX(), getY()), this.getRotation())));
                            }
                        if (grid[x + 1][y] != null)
                            if (!connects(new VoxelUtils.Index(x + 1, y), ref)) {
    //                    System.out.println("should cascade right");
                                toRemove = removeConnectedTo(x - 1, y);
                                if (toRemove != null)
                                    WorldManager.ins().addGridToWorld(new DynamicGrid(new VoxelCollection(toRemove, new Vector2(getX(), getY()), this.getRotation())));
                            }
                    }
                }
            }
        }
    }




    /**
     * Transforms a position in world units to an index into this grid
     * @param worldPos in world units
     * @return the x,y indexes
     */
    public Vector2 mapWorldPointToIndexies(Vector2 worldPos){
        worldPos = this.stageToLocalCoordinates(worldPos.cpy());
        worldPos.x = (int)(worldPos.x/ResourceManager.VOXEL_PIXEL_SIZE);
        worldPos.y = (int)(worldPos.y/ResourceManager.VOXEL_PIXEL_SIZE);
        return worldPos;
    }

    /**
     * Transforms an index to a world position
     * @param i column
     * @param j row
     * @return
     */
    public Vector2 mapIndexesToWorldPos(int i, int j){
        //todo test this
        Vector2 worldPos = new Vector2(getX(), getY());
        worldPos.x += i * ResourceManager.VOXEL_PIXEL_SIZE;
        worldPos.y += j * ResourceManager.VOXEL_PIXEL_SIZE;
        return worldPos;
    }

    /**
     * @return the actual grid(not a copy)
     */
    public Voxel[][] getGrid(){
        return grid;
    }

    public boolean isWorldPosInGrid(Vector2 worldPos){
        Vector2 pos = mapWorldPointToIndexies(worldPos);
        return validIndex((int)pos.x, (int)pos.y);
    }

    public Voxel getVoxelAtWorldPosition(Vector2 pos){
        Vector2 p = mapWorldPointToIndexies(pos.cpy());
        int i = (int)p.x;
        int j = (int)p.y;
        if(validIndex(i,j)){
            return grid[i][j];
        }
        return null;
    }

    /**
     * @param x index
     * @param y index
     * @return true iff there will not be an array index out of bounds exception
     */
    public boolean validIndex(int x, int y){
        if(x < 0 || y < 0) return false;
        if(x >= maxSize || y >= maxSize) return false;
        return true;
    }

    /**
     * Get all not null neighbors of the cell @ x,y
     * @param x index
     * @param y index
     * @return a list of the neighbors
     */
    private ArrayList<Voxel> getNeighbors(int x,int y){
        ArrayList<Voxel> neighbors = new ArrayList<Voxel>();
        if(validIndex(x+1,y))neighbors.add(grid[x+1][y]);
        if(validIndex(x-1,y))neighbors.add(grid[x-1][y]);
        if(validIndex(x,y+1))neighbors.add(grid[x][y+1]);
        if(validIndex(x,y+1))neighbors.add(grid[x][y-1]);
        return neighbors;
    }

    /**
     * @return the first not null Voxel in the grid
     */
    public Voxel getFirst(){
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                if(grid[i][j] != null)
                    return grid[i][j];
            }
        }
        return null;
    }

    /**
     * @return the indexes of the first not null Voxel in the grid
     */
    private VoxelUtils.Index getFirstIndex(){
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                if(grid[i][j] != null)
                    return new VoxelUtils.Index(i,j);
            }
        }
        return null;
    }

    /**
     * @return the index of the centermost cell(cell @ index could be null)
     */
    private VoxelUtils.Index getCenterIndex(){
        return new VoxelUtils.Index(maxSize/2,maxSize/2);
    }

    /**
     * Determins if there exists a path of not null tiles between two indexes
     * @param from start index
     * @param to end index(target)
     * @return true iff there hasProperty a not null path between from and to
     */
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
//            System.out.println("checking at"+c.x+", "+c.y);
            Voxel current = grid[c.x][c.y];
            if(current == goal){
                return true;
            }
            if(validIndex(c.x+1,c.y)){
                if(grid[c.x+1][c.y] != null && !visited[c.x+1][c.y]){
                    visited[c.x+1][c.y] = true;
                    VoxelUtils.Index next = new VoxelUtils.Index(c.x + 1, c.y);
                    if (!queue.contains(next))
                        queue.add(next);
                }
            }
            if(validIndex(c.x-1,c.y)){
                if(grid[c.x-1][c.y] != null && !visited[c.x-1][c.y]){
                    visited[c.x-1][c.y] = true;
                    VoxelUtils.Index next = new VoxelUtils.Index(c.x - 1, c.y);
                    if (!queue.contains(next))
                        queue.add(next);
                }
            }
            if(validIndex(c.x,c.y+1)){
                if(grid[c.x][c.y+1] != null && !visited[c.x][c.y+1]){
                    visited[c.x][c.y+1] = true;
                    VoxelUtils.Index next = new VoxelUtils.Index(c.x, c.y + 1);
                    if (!queue.contains(next))
                        queue.add(next);
                }
            }
            if(validIndex(c.x,c.y-1)){
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

    /**
     * Remove all Voxels that are connected to the cell @ index x,y
     * @param x index
     * @param y index
     * @return all the cells that have been removed
     */
    private Voxel[][] removeConnectedTo(int x, int y){
        if(!validIndex(x,y))return null;
        Voxel[][] delta = getVoxelsConnectedToPos(x,y);
        if(delta != null){
            for(int i = 0; i < grid.length; i++){
                for(int j = 0; j < grid[0].length; j++){
                    if(!validIndex(i,j))continue;
                    if(delta[i][j] != null) {
                        super.removeActor(grid[i][j]);
                        grid[i][j] = null;
                    }
                }
            }
        }
        //count the number in each
        int baseCount = 0;
        int removedCount = 0;
        if(delta != null){
            for (int i = 0; i < maxSize; i++) {
                for (int j = 0; j < maxSize; j++) {
                    if(delta[i][j] != null)
                        removedCount++;
                    if(grid[i][j] != null)
                        baseCount++;
                }
            }
        }
        if(removedCount > baseCount){
            Voxel[][] temp = delta;
            delta = grid;
            grid = temp;
            for(int i = 0; i < grid.length; i++){
                for(int j = 0; j < grid[0].length; j++){
                    if(grid[i][j] != null) {
                        super.addActor(grid[i][j]);
                    }
                }
            }

        }

        return delta;
    }

    /**
     * Find all Voxels that are connected to the cell @ index x,y
     * @param x index
     * @param y index
     * @return all the cells connected to the cell at index x,y
     */
    private Voxel[][] getVoxelsConnectedToPos(int x, int y){
        //todo bfs add
        Voxel[][] connected = new Voxel[maxSize][maxSize];
        boolean[][] visited = new boolean[maxSize][maxSize];
        if(!validIndex(x,y) || grid[x][y] == null){
//            System.out.println("returning null on getVoxelsConnectedToPos");
            return null;
        }
        LinkedList<VoxelUtils.Index> queue = new LinkedList<VoxelUtils.Index>();
        VoxelUtils.Index from = new VoxelUtils.Index(x,y);
        queue.add(from);//.Push(myMap[(int)startPos.x][(int)startPos.y]);
        connected[x][y] = grid[x][y];
        //evaluate
        while (queue.size() > 0){
            VoxelUtils.Index c = queue.remove();
//            System.out.println("checking at"+c.x+", "+c.y);
            Voxel current = grid[c.x][c.y];

            if(validIndex(c.x+1,c.y)){
                if(grid[c.x+1][c.y] != null && !visited[c.x+1][c.y]){
                    visited[c.x+1][c.y] = true;
                    VoxelUtils.Index next = new VoxelUtils.Index(c.x + 1, c.y);
                    if (!queue.contains(next)) {
                        queue.add(next);
                        connected[c.x+1][c.y] = grid[c.x+1][c.y];
                    }
                }
            }
            if(validIndex(c.x-1,c.y)){
                if(grid[c.x-1][c.y] != null && !visited[c.x-1][c.y]){
                    visited[c.x-1][c.y] = true;
                    VoxelUtils.Index next = new VoxelUtils.Index(c.x - 1, c.y);
                    if (!queue.contains(next)){
                        queue.add(next);
                        connected[c.x-1][c.y] = grid[c.x-1][c.y];
                    }
                }
            }
            if(validIndex(c.x,c.y+1)){
                if(grid[c.x][c.y+1] != null && !visited[c.x][c.y+1]){
                    visited[c.x][c.y+1] = true;
                    VoxelUtils.Index next = new VoxelUtils.Index(c.x, c.y + 1);
                    if (!queue.contains(next)){
                        queue.add(next);
                        connected[c.x][c.y+1] = grid[c.x][c.y+1];
                    }
                }
            }
            if(validIndex(c.x,c.y-1)){
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

    /**
     * Check to see if a placement at x,y would be connected to a not null tile
     * @param x index
     * @param y index
     * @return true iff a neighbor of position x,y hasProperty not null
     */
    public boolean verifyVoxelPlacement(int x, int y){
        boolean isGood = false;
        //check neighbors if there true
        if(validIndex(x+1,y) && x+1 < grid.length){
            if(grid[x+1][y] != null)
                isGood = true;
        } else return false;
        if(validIndex(x-1,y) && x-1 > 0){
            if(grid[x-1][y] != null)
                isGood = true;
        } else return false;
        if(validIndex(x,y+1) && y+1 < grid.length){
            if(grid[x][y+1] != null)
                isGood = true;
        } else return false;
        if(validIndex(x,y-1) && y-1 > 0){
            if(grid[x][y-1] != null)
                isGood = true;
        } else return false;

        //check self if there false
        if(validIndex(x,y) && grid[x][y] != null)
            isGood = false;

        return isGood;
    }

}
