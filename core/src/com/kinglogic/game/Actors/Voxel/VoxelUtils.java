package com.kinglogic.game.Actors.Voxel;

import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.Managers.ResourceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by chris on 4/1/2018.
 */

public class VoxelUtils {

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

    public static List<Vector2> MarchingSquares(Voxel[][] state){
        //todo implement this
        //save the first
        //for each generate map value and add the verts to the list, then parse the list and reorder it so its a loop counter clockwise loop
        int firstX = -1,firstY = -1;
        HashMap<Vector2,LinkedList<FromEdge>> edgeslist = new HashMap();
        for(int i = 0; i < state.length; i++){
            for(int j = 0; j < state[0].length; j++){
                if(state[i][j] != null) {
                    //save the firstSpot
                    if(firstX < 0){
                        firstX = i;
                        firstY = j;
                    }

                    byte maping = 0;
                    //right 8
                    if (i + 1 < state.length)
                        if (state[i + 1][j] == null)
                            maping ^= 8;
                    //bottom 4
                    if (j - 1 > 0)
                        if (state[i][j - 1] == null)
                            maping ^= 4;
                    //left 2
                    if (i - 1 > 0)
                        if (state[i - 1][j] == null)
                            maping ^= 2;
                    //top 1
                    if (j + 1 < state.length)
                        if (state[i][j + 1] == null)
                            maping ^= 1;

                    edgeslist.putAll(map(maping, i, j));
                }
            }

        }
        if(firstX >= 0){
            List<Vector2> verts = new ArrayList<Vector2>();
            Vector2 firstPos = new Vector2(firstX*ResourceManager.voxelPixelSize, firstY*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize);
            System.out.println("adding first position = "+firstPos);
            //todo parse the lists
            LinkedList<FromEdge> currentList = edgeslist.get(firstPos);
            if(edgeslist.size() == 0){
                System.out.println("edgesList of size 0");
                return null;
            }
            if(currentList == null) {
                System.out.println("null currents list");
                //meaning we hit an edge case such that the first position isnt in the top left, its in the top right
                firstPos = new Vector2(firstX*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize, firstY*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize);
                currentList = edgeslist.get(firstPos);
                if(currentList == null)//then its really northing there
                    return null;
            }
            FromEdge currentVert = currentList.removeLast();
            while(!currentVert.to.equals( firstPos)){
                verts.add(currentVert.from);
                System.out.println("connecting "+currentVert.from+" to"+currentVert.to);
                if(currentList.size() > 0) {
                    currentVert = currentList.removeLast();
                }else {
                    currentList = edgeslist.get(currentVert.to);
                    if(currentList == null)
                        break;
                    currentVert = currentList.removeLast();
                }
            }
            verts.add(currentVert.from);
            verts.add(currentVert.to);

            //todo when parsing one should push THE VERT NOT EDGE to the poly list and when an edge is pulled that links to the start, its done
            //todo optimization - account for strait lines: if 3 verts in a row have the same X or Y, delete middle
            //todo optimization - istead of removing the links and putting them in a new list, ATTATCH THEM

            return verts;
        }
        //nothing was found
        return null;
    }

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
}
