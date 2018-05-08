package com.kinglogic.game.AI;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.kinglogic.game.Physics.Projectile;
import com.kinglogic.game.Player.PlayerBody;

/**
 * Created by chris on 5/8/2018.
 */

public class RobotFriend extends BaseAIBody {
    public RobotFriend(String name, Vector2 position) {
        super(name, position);
        this.speed = 200f;
    }

    @Override
    public void Think(World perception) {
        super.Think(perception);
        Array<Body> pr = new Array<Body>();
        perception.getBodies(pr);
        if(targeting != null) {
            if (targeting instanceof Enemy || targeting instanceof Projectile) {
                AIUtils.Flee(this, targeting.myBody.getPosition());
            } else if (targeting instanceof PlayerBody) {
                AIUtils.Persue(this, targeting);
            }
        }else {
            if(perceptions.size() > 0)
                targeting = perceptions.get(0);
        }
    }

    @Override
    public String Speak(){
        return "im a friend!";
    }
    @Override
    public void GoForeward() {
        if(this.controlling != null)
            controlling.GoForeward();
        else{
            myBody.applyForceToCenter(myBody.getTransform().getOrientation().rotate90(1).scl(speed*myBody.getMass()),true);
        }
    }
}
