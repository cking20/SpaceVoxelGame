package com.kinglogic.game.Actors.Voxel;

import java.util.Comparator;

/**
 * Created by chris on 4/9/2018.
 */

public class IndexCompare implements Comparator<VoxelUtils.Index> {
    private VoxelUtils.Index reference;

    public IndexCompare(VoxelUtils.Index ref){
        reference = ref;
    }

    private IndexCompare(){
        reference = new VoxelUtils.Index(0,0);
    }
    @Override

    public int compare(VoxelUtils.Index t1, VoxelUtils.Index t2) {
        //a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
        int dif1 = (Math.abs(t1.x - reference.x) + Math.abs(t1.y - reference.y));
        int dif2 = (Math.abs(t2.x - reference.x) + Math.abs(t2.y - reference.y));
        return dif1-dif2;
    }
}
