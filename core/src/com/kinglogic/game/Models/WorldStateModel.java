package com.kinglogic.game.Models;

import com.kinglogic.game.Managers.PCGManager;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by chris on 5/2/2018.
 */

public class WorldStateModel {

    public static String jsonifyWorldState(WorldState worldState){
        JSONObject json = new JSONObject();
        json.put("name", worldState.name);
        json.put("x", worldState.getCenterX());
        json.put("y", worldState.getCenterY());

        JSONArray worldData = new JSONArray();
        for (int i = 0; i < worldState.sectorsData.length; i++) {

            JSONArray col = new JSONArray();
            for (int j = 0; j < worldState.sectorsData[0].length; j++) {
               col.put(worldState.sectorsData[i][j]);
            }
            worldData.put(col);
        }
        json.put("worldData", worldData);

        return json.toString();
    }

    public static WorldState unjsonifyWorldState(JSONObject json){
        PCGManager.SECTOR_ARCHETYPE[][] worldData;

        JSONArray dat = json.getJSONArray("worldData");
        worldData = new PCGManager.SECTOR_ARCHETYPE[dat.length()][dat.getJSONArray(0).length()];
        for (int i = 0; i < dat.length(); i++) {
            JSONArray col = dat.getJSONArray(i);
            for (int j = 0; j < col.length(); j++) {
                worldData[i][j] = PCGManager.SECTOR_ARCHETYPE.valueOf(col.getString(j));
            }
        }
        WorldState state = new WorldState(json.getString("name"), worldData, json.getInt("x"), json.getInt("y"));
        return state;
    }

}
