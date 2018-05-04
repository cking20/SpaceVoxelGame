package com.kinglogic.game.Models;

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

        return json.toString();
    }

    public static WorldState unjsonifyWorldState(JSONObject json){
        WorldState state = new WorldState(json.getString("name"), json.getInt("x"), json.getInt("y"));
        return state;
    }

}
