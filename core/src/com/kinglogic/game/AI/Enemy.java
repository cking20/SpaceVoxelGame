package com.kinglogic.game.AI;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Created by chris on 4/23/2018.
 */

public class Enemy extends BaseAIBody {
    public Enemy(String name, Vector2 position) {
        super(name, position);
        this.speed = 750f;
    }
    @Override
    public void Think(World perception) {
        super.Think(perception);
        Array<Body> pr = new Array<Body>();
        perception.getBodies(pr);
        if(targeting != null) {
            if (targeting instanceof Enemy) {
                AIUtils.Swarm(this, targeting);
            } else
                AIUtils.Seek(this, targeting.myBody.getPosition());
        }else {
            if(perceptions.size() > 0)
                targeting = perceptions.get(0);
        }
    }

    @Override
    public String Speak(){
        return "im a baddy!";
    }
}
