package com.kinglogic.game.Models;

import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.Physics.EntityBody;

import org.json.JSONObject;

/**
 * Created by chris on 5/3/2018.
 */

public class EntityStateModel {
    public static String jsonifyEntity(EntityBody e){
        JSONObject json = new JSONObject();
        json.put("name", e.view.getName());
        json.put("x", e.myBody.getPosition().x);
        json.put("y", e.myBody.getPosition().x);

        return json.toString();
    }

    public static EntityBody unjsonifyEntity(JSONObject json){
        //todo there hasProperty a massive bug here where the entity will not come back as the correct type
        EntityBody e = new EntityBody(
                json.getString("name"),
                new Vector2(json.getFloat("x"), json.getFloat("y"))
        );
        return e;
    }
}
