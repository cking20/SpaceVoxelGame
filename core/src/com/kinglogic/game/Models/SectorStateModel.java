package com.kinglogic.game.Models;

import com.kinglogic.game.Managers.PCGManager;

import org.json.JSONObject;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by chris on 5/2/2018.
 */

public class SectorStateModel {
    public static JSONObject jsonifySectorState(SectorState sectorState){
        JSONObject json = new JSONObject();
        json.put("type", sectorState.type);
        json.put("x", sectorState.x);
        json.put("y", sectorState.y);
        json.put("cleared", sectorState.cleared);

        return json;
    }

    public static SectorState unjsonifySectorState(JSONObject json){
        SectorState state = new SectorState(json.getInt("x"), json.getInt("y"), PCGManager.SECTOR_ARCHETYPE.valueOf( json.getString("type")));
        state.cleared = json.getBoolean("cleared");
        return state;
    }
}
