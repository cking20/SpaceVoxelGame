package com.kinglogic.game.Actors.Voxel;

import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.Managers.ResourceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by chris on 4/1/2018.
 */

public class VoxelUtils {

    private static HashMap<Vector2,FromEdge> map(Byte value, int x, int y){
        HashMap<Vector2, FromEdge> edges = new HashMap();
        FromEdge temp;
        switch (value){
            case 0:
                break;
            case 1:
                temp = GetTop(x,y);
                edges.put(temp.from, temp);
                break;
            case 2:
                temp = GetLeft(x,y);
                edges.put(temp.from, temp);
                break;
            case 3:
                temp = GetTop(x,y);
                edges.put(temp.from, temp);
                temp = GetLeft(x,y);
                edges.put(temp.from, temp);
                break;
            case 4:
                temp = GetBottom(x,y);
                edges.put(temp.from, temp);
                break;
            case 5:
                temp = GetTop(x,y);
                edges.put(temp.from, temp);
                temp = GetBottom(x,y);
                edges.put(temp.from, temp);
                break;
            case 6:
                temp = GetLeft(x,y);
                edges.put(temp.from, temp);
                temp = GetBottom(x,y);
                edges.put(temp.from, temp);
                break;
            case 7:
                temp = GetTop(x,y);
                edges.put(temp.from, temp);
                temp = GetLeft(x,y);
                edges.put(temp.from, temp);
                temp = GetBottom(x,y);
                edges.put(temp.from, temp);
                break;
            case 8:
                temp = GetRight(x,y);
                edges.put(temp.from, temp);
                break;
            case 9:
                temp = GetRight(x,y);
                edges.put(temp.from, temp);
                temp = GetTop(x,y);
                edges.put(temp.from, temp);
                break;
            case 10:
                temp = GetLeft(x,y);
                edges.put(temp.from, temp);
                temp = GetRight(x,y);
                edges.put(temp.from, temp);
                break;
            case 11:
                temp = GetRight(x,y);
                edges.put(temp.from, temp);
                temp = GetTop(x,y);
                edges.put(temp.from, temp);
                temp = GetLeft(x,y);
                edges.put(temp.from, temp);
                break;
            case 12:
                temp = GetBottom(x,y);
                edges.put(temp.from, temp);
                temp = GetRight(x,y);
                edges.put(temp.from, temp);
                break;
            case 13:
                temp = GetBottom(x,y);
                edges.put(temp.from, temp);
                temp = GetRight(x,y);
                edges.put(temp.from, temp);
                temp = GetTop(x,y);
                edges.put(temp.from, temp);
                break;
            case 14:
                temp = GetLeft(x,y);
                edges.put(temp.from, temp);
                temp = GetBottom(x,y);
                edges.put(temp.from, temp);
                temp = GetRight(x,y);
                edges.put(temp.from, temp);
                break;
            case 15:
                temp = GetTop(x,y);
                edges.put(temp.from, temp);
                temp = GetLeft(x,y);
                edges.put(temp.from, temp);
                temp = GetBottom(x,y);
                edges.put(temp.from, temp);
                temp = GetRight(x,y);
                edges.put(temp.from, temp);
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
        HashMap<Vector2,FromEdge> edges = new HashMap();
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
                    edges.putAll(map(maping, i, j));
                }
            }

        }
        if(firstX >= 0){
            List<Vector2> verts = new ArrayList<Vector2>();
            Vector2 firstPos = new Vector2(firstX*ResourceManager.voxelPixelSize, firstY*ResourceManager.voxelPixelSize + ResourceManager.voxelPixelSize);
            System.out.println("adding first position = "+firstPos);
            FromEdge current = edges.get(firstPos);
            if(edges.size() == 0)
                return null;
            while (!current.to.equals(firstPos)){
                System.out.println("connecting "+current.from+" to"+current.to);
                verts.add(current.from);
                current = edges.get(current.to);
                System.out.println("current is now from "+current.from+" to"+current.to);
            }
            //todo maybe missing onehere?
            verts.add(current.from);
            //when parsing one should push THE VERT NOT EDGE to the poly list and when an edge is pulled that links to the start, its done
            //optimization - account for strait lines: if 3 verts in a row have the same X or Y, delete middle

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
