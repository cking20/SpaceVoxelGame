package com.kinglogic.game.Models;

import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.Physics.DynamicGrid;
import com.kinglogic.game.Physics.Grid;
import org.json.*;

/**
 * Created by chris on 5/2/2018.
 */

public class GridStateModel {
    public static String jsonifyGrid(Grid g){
        JSONObject grid = new JSONObject();
        grid.put("name", g.name);
        grid.put("x", g.myBody.getPosition().x);
        grid.put("y", g.myBody.getPosition().y);
        grid.put("angle", g.myBody.getAngle());
        grid.put("voxels", VoxelCollectionModel.jsonifyVoxelCollection(g.voxels));


        return grid.toString();
    }

    public static DynamicGrid unjsonifyDynamicGrid(JSONObject o){
        DynamicGrid toBuild = new DynamicGrid(
                VoxelCollectionModel.unjsonifyVoxelCollection(o.getJSONObject("voxels")));
        toBuild.name = o.getString("name");
        toBuild.bodyDef.position.x = o.getFloat("x");
        toBuild.bodyDef.position.y = o.getFloat("y");
        toBuild.bodyDef.angle = o.getFloat("angle");
//        toBuild.voxels = VoxelCollectionModel.unjsonifyVoxelCollection(o.getJSONObject("voxels"));

        return toBuild;
    }
}
