package com.kinglogic.game.Models;

import org.json.JSONObject;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by chris on 5/2/2018.
 */

public class SectorStateModel {
    public static String jsonifySectorState(SectorState sectorState){
        JSONObject json = new JSONObject();
        json.put("name", sectorState.name);
        json.put("x", sectorState.x);
        json.put("y", sectorState.y);

        return json.toString();
    }

    public static SectorState unjsonifySectorState(JSONObject json){
        SectorState state = new SectorState(json.getString("name"), json.getInt("x"), json.getInt("y"), false);
        return state;
    }
}
