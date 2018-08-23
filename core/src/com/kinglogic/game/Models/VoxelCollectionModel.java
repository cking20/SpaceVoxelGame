package com.kinglogic.game.Models;

import com.badlogic.gdx.graphics.Color;
import com.kinglogic.game.Actors.Voxel.Blocks.Voxel;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;
import com.kinglogic.game.ChemestryFramework.Properties;

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
            JSONObject props = j.getJSONObject("properties");

            Voxel v = Voxel.Build(name);
            //todo should set the properites with all the vals
            v.properties = new Properties(
                    (byte) props.getInt("l"),
                    (byte) props.getInt("h"),
                    (byte) props.getInt("p"),
                    (byte) props.getInt("s"),
                           props.getInt("d"),
                    (byte) props.getInt("f"));
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
        json.put("r", v.getColor().r);
        json.put("g", v.getColor().g);
        json.put("b", v.getColor().b);

//        json.put("properties", v.properties.getProps());
        JSONObject props = new JSONObject();
        props.put("l", v.properties.getLevel());
        props.put("h", v.properties.getHealth());
        props.put("p", v.properties.getProperties());
        props.put("s", v.properties.getStatus());
        props.put("d", v.properties.getDurration());
        props.put("f", v.properties.getFluids());
        json.put("properties", props);

        return json;
    }
}
