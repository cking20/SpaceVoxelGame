package com.kinglogic.game.Physics;

import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.ChemestryFramework.Properties;

/**
 * Created by chris on 8/6/2018.
 */

public class EntityProperties extends Properties{
    Vector2 gravity;

    public EntityProperties(){
        this.setProperty(true,Properties.AFFECTED_BY_GRAVITY);
        level = 1;
        health = 127;
        properties = 0;
        gravity = null;
    }
}
