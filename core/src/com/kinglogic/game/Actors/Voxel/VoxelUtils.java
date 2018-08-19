package com.kinglogic.game.Actors.Voxel;

import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.Actors.Voxel.Blocks.Voxel;
import com.kinglogic.game.Managers.ResourceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by chris on 4/1/2018.
 */

public class VoxelUtils {

    /**
     * Helper function of marching squares. Maps the values to a set of edges
     * @param value the neighbor code
     * @param x index
     * @param y index
     * @return
     */
    private static HashMap<Vector2,LinkedList<FromEdge>> map(Byte value, int x, int y){
        HashMap<Vector2, LinkedList<FromEdge>> edges = new HashMap();
        LinkedList<FromEdge> list = new LinkedList<FromEdge>();
        FromEdge temp;
        switch (value){
            case 0:
                break;
            case 1:
                temp = GetTop(x,y);
                list.push(temp);
                edges.put(list.getLast().from, list);
                break;
            case 2:
                temp = GetLeft(x,y);
                list.push(temp);
                edges.put(list.getLast().from, list);
                break;
            case 3:
                temp = GetTop(x,y);
                list.push(temp);
                temp = GetLeft(x,y);
                list.push(temp);
                edges.put(list.getLast().from, list);
                break;
            case 4:
                temp = GetBottom(x,y);
                list.push(temp);
                edges.put(list.getLast().from, list);
                break;
            case 5:
                temp = GetTop(x,y);
                list.push(temp);
                edges.put(list.getLast().from, list);

                list = new LinkedList<FromEdge>();
                temp = GetBottom(x,y);
                list.push(temp);
                edges.put(list.getLast().from, list);
                break;
            case 6:
                temp = GetLeft(x,y);
                list.push(temp);
                temp = GetBottom(x,y);
                list.push(temp);
                edges.put(list.getLast().from, list);
                break;
            case 7:
                temp = GetTop(x,y);
                list.push(temp);
                temp = GetLeft(x,y);
                list.push(temp);
                temp = GetBottom(x,y);
                list.push(temp);
                edges.put(list.getLast().from, list);
                break;
            case 8:
                temp = GetRight(x,y);
                list.push(temp);
                edges.put(list.getLast().from, list);
                break;
            case 9:
                temp = GetRight(x,y);
                list.push(temp);
                temp = GetTop(x,y);
                list.push(temp);
                edges.put(list.getLast().from, list);
                break;
            case 10:
                temp = GetLeft(x,y);
                list.push(temp);
                edges.put(list.getLast().from, list);

                list = new LinkedList<FromEdge>();
                temp = GetRight(x,y);
                list.push(temp);
                edges.put(list.getLast().from, list);
                break;
            case 11:
                temp = GetRight(x,y);
                list.push(temp);
                temp = GetTop(x,y);
                list.push(temp);
                temp = GetLeft(x,y);
                list.push(temp);
                edges.put(list.getLast().from, list);
                break;
            case 12:
                temp = GetBottom(x,y);
                list.push(temp);
                temp = GetRight(x,y);
                list.push(temp);
                edges.put(list.getLast().from, list);
                break;
            case 13:
                temp = GetBottom(x,y);
                list.push(temp);
                temp = GetRight(x,y);
                list.push(temp);
                temp = GetTop(x,y);
                list.push(temp);
                edges.put(list.getLast().from, list);
                break;
            case 14:
                temp = GetLeft(x,y);
                list.push(temp);
                temp = GetBottom(x,y);
                list.push(temp);
                temp = GetRight(x,y);
                list.push(temp);
                edges.put(list.getLast().from, list);
                break;
            case 15:
                temp = GetLeft(x,y);
                list.push(temp);
                temp = GetBottom(x,y);
                list.push(temp);
                temp = GetRight(x,y);
                list.push(temp);
                temp = GetTop(x,y);
                list.push(temp);
                edges.put(list.getLast().from, list);
                break;

        }
        return edges;
    }
    /*              bottom left      new Vector2(i*ResourceManager.voxelPixelSize, j*ResourceManager.voxelPixelSize)
                    bottom right     new Vector2(i*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, j*ResourceManager.voxelPixelSize)
                    top right        new Vector2(i*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, j*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize)
                    top left         new Vector2(i*ResourceManager.voxelPixelSize, j*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize)     */
    private static FromEdge GetTop(int i,int j){
        return new FromEdge(
                new Vector2(i*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, j*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize),
                new Vector2(i*ResourceManager.voxelPixelSize, j*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize)
                );
    }
    private static FromEdge GetLeft(int i,int j){
        return new FromEdge(
                new Vector2(i*ResourceManager.voxelPixelSize, j*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize),
                new Vector2(i*ResourceManager.voxelPixelSize, j*ResourceManager.voxelPixelSize)
        );
    }
    private static FromEdge GetRight(int i,int j){
        return new FromEdge(
                new Vector2(i*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, j*ResourceManager.voxelPixelSize),
                new Vector2(i*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, j*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize)
        );
    }
    private static FromEdge GetBottom(int i,int j){
        return new FromEdge(
                new Vector2(i*ResourceManager.voxelPixelSize, j*ResourceManager.voxelPixelSize),
                new Vector2(i*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, j*ResourceManager.voxelPixelSize)
        );

    }

    /**
     * Implementation of the marching squares algorithm. Creates a perimeter of vertexes
     *      based on the state of the grid. Intended to be used for ChainShape generation
     * @param state of a grid
     * @return a list of connected vertices
     */
    public static List<Vector2[]> MarchingSquares(Voxel[][] state){
        //save the first
        //for each generate map value and add the verts to the list, then parse the list and reorder it so its a loop counter clockwise loop
        int firstX = -1,firstY = -1;
        HashMap<Vector2,LinkedList<FromEdge>> edgeslist = new HashMap();
        for(int i = 0; i < state.length; i++){
            for(int j = 0; j < state[0].length; j++){
                if(state[i][j] != null && state[i][j].properties.hasProperty(VoxelProperties.COLLIDABLE)) {
                    //save the firstSpot
                    if(firstX < 0){
                        firstX = i;
                        firstY = j;
                    }

                    byte maping = 0;
                    //right 8
                    if (i + 1 < state.length)
                        if (state[i + 1][j] == null || !state[i + 1][j].properties.hasProperty(VoxelProperties.COLLIDABLE))
                            maping ^= 8;
                    //bottom 4
                    if (j - 1 > 0)
                        if (state[i][j - 1] == null || !state[i][j - 1].properties.hasProperty(VoxelProperties.COLLIDABLE))
                            maping ^= 4;
                    //left 2
                    if (i - 1 > 0)
                        if (state[i - 1][j] == null || !state[i - 1][j].properties.hasProperty(VoxelProperties.COLLIDABLE))
                            maping ^= 2;
                    //top 1
                    if (j + 1 < state.length)
                        if (state[i][j + 1] == null || !state[i][j + 1].properties.hasProperty(VoxelProperties.COLLIDABLE))
                            maping ^= 1;

                    edgeslist.putAll(map(maping, i, j));
                }
            }

        }
        if(firstX >= 0){
            List<Vector2[]> chainsList = new LinkedList<Vector2[]>();


            Vector2 firstPos = new Vector2(firstX*ResourceManager.voxelPixelSize, firstY*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize);
//            System.out.println("adding first position = "+firstPos);

            if(edgeslist.size() == 0){
//                System.out.println("edgesList of size 0");
                return null;
            }
            while (edgeslist.size() > 0) {
                firstPos = edgeslist.keySet().iterator().next();
                LinkedList<FromEdge> currentList = edgeslist.remove(firstPos);
                if (currentList == null) {
                    //meaning we hit an edge case such that the first position isnt in the top left, its in the top right
                    firstPos = new Vector2(firstX * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, firstY * ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize);
                    currentList = edgeslist.remove(firstPos);
                    if (currentList == null){//then its really northing there
//                        System.out.println("null currents list");
                        break;
                    }
                }
                List<Vector2> verts = new ArrayList<Vector2>();
                FromEdge currentVert = currentList.removeLast();
                while (!currentVert.to.equals(firstPos)) {
                    verts.add(currentVert.from);
                    //System.out.println("connecting "+currentVert.from+" to"+currentVert.to);
                    if (currentList.size() > 0) {
                        currentVert = currentList.removeLast();
                    } else {
                        currentList = edgeslist.remove(currentVert.to);
                        if (currentList == null)
                            break;
                        currentVert = currentList.removeLast();
                    }
                }
                verts.add(currentVert.from);
                verts.add(currentVert.to);
                Vector2[] chain = new Vector2[verts.size()];
                chain = verts.toArray(chain);
                chainsList.add(chain);

                //todo when parsing one should push THE VERT NOT EDGE to the poly list and when an edge hasProperty pulled that links to the start, its done
                //todo optimization - account for strait lines: if 3 verts in a row have the same X or Y, delete middle
                //todo optimization - istead of removing the links and putting them in a new list, ATTATCH THEM}
            }
            System.out.println(chainsList.size()+ " chains found");
            return chainsList;
        }
        //nothing was found
        return null;
    }

    /**
     * Generates a list of rectangles(pos 0,1,2,3 repeating)
     * todo optimize the number of rectangles being created
     * @param state of a grid
     * @return list of rectangles whos verts are at i*4+0, i*4+1, i*4+2, i*4+3
     */
    public static List<Vector2[]> LeastRects(Voxel[][] state){
        //todo make this only use the largest possible rectangles not a rect per block
        List<Vector2> verts = new ArrayList<Vector2>();
        boolean foundOneFlag = false;
        for(int i = 0; i < state.length; i++){
            for(int j = 0; j < state[0].length; j++){
                if(state[i][j] != null && state[i][j].properties.hasProperty(VoxelProperties.COLLIDABLE)){
                    foundOneFlag = true;
                    verts.add(new Vector2(i*ResourceManager.voxelPixelSize, j*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize));
                    verts.add(new Vector2(i*ResourceManager.voxelPixelSize, j*ResourceManager.voxelPixelSize));
                    verts.add(new Vector2(i*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, j*ResourceManager.voxelPixelSize));
                    verts.add(new Vector2(i*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, j*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize));
                }
            }
        }
        if(foundOneFlag){
            Vector2[] vs  = new Vector2[verts.size()];
            vs = verts.toArray(vs);
            List<Vector2[]> ret = new LinkedList<Vector2[]>();
            ret.add(vs);
            return ret;
        }
        else return null;
    }

    public static List<Vector2[]> LeastRectsByCol(Voxel[][] state){
        int startPos = 0;
        int endPos = 0;
        boolean drawing = false;
        List<Vector2> verts = new ArrayList<Vector2>();
        boolean foundOneFlag = false;
        for(int i = 0; i < state.length; i++){
            for(int j = 0; j < state[0].length; j++){
                if(state[i][j] != null && state[i][j].properties.hasProperty(VoxelProperties.COLLIDABLE)){
                    foundOneFlag = true;
                    if(!drawing){
                        drawing = !drawing;
                        startPos = j;
                    }
                }else{
                    if(drawing){
                        drawing = !drawing;
                        endPos = j-1;
                        verts.add(new Vector2(i*ResourceManager.voxelPixelSize, endPos*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize));
                        verts.add(new Vector2(i*ResourceManager.voxelPixelSize, startPos*ResourceManager.voxelPixelSize));
                        verts.add(new Vector2(i*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, startPos*ResourceManager.voxelPixelSize));
                        verts.add(new Vector2(i*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, endPos*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize));
                    }
                }
            }
            if(drawing){
                drawing = !drawing;
                endPos = state[0].length-1;
                verts.add(new Vector2(i*ResourceManager.voxelPixelSize, endPos*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize));
                verts.add(new Vector2(i*ResourceManager.voxelPixelSize, startPos*ResourceManager.voxelPixelSize));
                verts.add(new Vector2(i*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, startPos*ResourceManager.voxelPixelSize));
                verts.add(new Vector2(i*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, endPos*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize));
            }
        }
        if(foundOneFlag){
            Vector2[] vs  = new Vector2[verts.size()];
            vs = verts.toArray(vs);
            List<Vector2[]> ret = new LinkedList<Vector2[]>();
            ret.add(vs);
            return ret;
        }
        else return null;
    }

    /**
     * Helper class of the MarchingSquares function
     */
    private static class FromEdge{
        public Vector2 from;
        public Vector2 to;

        public FromEdge(Vector2 f, Vector2 t){
            from = f; to = t;
        }

        public boolean connectsTo(Vector2 from){
            return this.from.equals(from);
        }

        //really specific case where i only care about quickly finding the from nodes
        @Override
        public int hashCode () {
            return from.hashCode();
        }
        @Override
        public boolean equals(Object o){
            return from.equals(o);
        }
    }

    /**
     * Helper class for indexing into a grid
     */
    public static class Index{
        public Integer x;
        public Integer y;
        public Index(int i, int j){
            x = i; y = j;
        }
    }

}
