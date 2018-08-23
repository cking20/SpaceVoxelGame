package com.kinglogic.game.Models;

import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.Physics.DynamicGrid;
import com.kinglogic.game.Physics.Grid;
import com.kinglogic.game.Physics.StaticGrid;

import org.json.*;

/**
 * Created by chris on 5/2/2018.
 */

public class GridStateModel {
    public static String jsonifyGrid(Grid g){
        JSONObject grid = new JSONObject();
        if(g instanceof DynamicGrid)
            grid.put("type", "DYNAMIC");
        else
            grid.put("type", "STATIC");
        grid.put("name", g.name);
        grid.put("x", g.myBody.getPosition().x);
        grid.put("y", g.myBody.getPosition().y);
        grid.put("angle", g.myBody.getAngle());
        grid.put("voxels", VoxelCollectionModel.jsonifyVoxelCollection(g.voxels));


        return grid.toString();
    }

    public static Grid unjsonifyDynamicGrid(JSONObject o){
        Grid toBuild;
        if(o.getString("type").equals("DYNAMIC"))
            toBuild = new DynamicGrid(VoxelCollectionModel.unjsonifyVoxelCollection(o.getJSONObject("voxels")));
        else
            toBuild = new StaticGrid(VoxelCollectionModel.unjsonifyVoxelCollection(o.getJSONObject("voxels")));
        toBuild.name = o.getString("name");
        toBuild.bodyDef.position.x = o.getFloat("x");
        toBuild.bodyDef.position.y = o.getFloat("y");
        toBuild.bodyDef.angle = o.getFloat("angle");
        return toBuild;
    }
}
