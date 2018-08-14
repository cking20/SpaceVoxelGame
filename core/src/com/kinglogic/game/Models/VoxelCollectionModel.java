package com.kinglogic.game.Models;

import com.badlogic.gdx.graphics.Color;
import com.kinglogic.game.Actors.Voxel.Blocks.Voxel;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * Created by chris on 5/2/2018.
 */

public class VoxelCollectionModel {
    public static JSONObject jsonifyVoxelCollection(VoxelCollection v){
        JSONObject json = new JSONObject();
        JSONArray jsonvoxels = new JSONArray();
        JSONObject voxel;
        Voxel[][] grid = v.getGrid();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if(grid[i][j] != null){
                    voxel = jsonifyVoxel(grid[i][j]);
                    voxel.put("x",i);
                    voxel.put("y",j);
                    jsonvoxels.put(voxel);
                }
            }
        }
        json.put("voxels", jsonvoxels);
        return json;
    }

    public static VoxelCollection unjsonifyVoxelCollection(JSONObject o){
        VoxelCollection ret = new VoxelCollection();
        JSONArray voxels = o.getJSONArray("voxels");
        for (int i = 0; i < voxels.length(); i++) {
            JSONObject j = (JSONObject) voxels.get(i);
            int x = j.getInt("x");
            int y = j.getInt("y");
            String name = j.getString("name");
            int props = j.getInt("properties");
            Voxel v = new Voxel(name);
            v.properties.setProps((byte) props);
            Color c = new Color();
            c.r = j.getFloat("r");
            c.g = j.getFloat("g");
            c.b = j.getFloat("b");
            c.a = 1;
            v.setColor(c);

            ret.hardAddVoxelIndex(v,x,y);
        }
        return ret;

    }

    public static JSONObject jsonifyVoxel(Voxel v){
        JSONObject json = new JSONObject();
        json.put("name", v.getName());
        json.put("properties", v.properties.getProps());
        json.put("r", v.getColor().r);
        json.put("g", v.getColor().g);
        json.put("b", v.getColor().b);
        return json;
    }
}
