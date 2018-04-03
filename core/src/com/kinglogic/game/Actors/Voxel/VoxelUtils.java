package com.kinglogic.game.Actors.Voxel;

import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;
import java.util.List;

/**
 * Created by chris on 4/1/2018.
 */

public class VoxelUtils {

    private HashSet<FromEdge> Map(Byte value, int originX, int oriiginY){
        HashSet<FromEdge> edges = new HashSet<FromEdge>();
        switch (value){
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
            case 14:
                break;
            case 15:
                break;

        }
        return edges;
    }

    public static List<Vector2> MarchingSquares(Voxel[][] state){
        //todo implement this
        //save the first
        //for each generate map value and add the verts to the list, then parse the list and reorder it so its a loop counter clockwise loop
        //when parsing one should push THE VERT NOT EDGE to the poly list and when an edge is pulled that links to the start, its done
        //optimization - account for strait lines: if 3 verts in a row have the same X or Y, delete middle
        return null;
    }

    private class FromEdge{
        public Vector2 from;
        public Vector2 to;

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
