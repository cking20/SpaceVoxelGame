package com.kinglogic.game.AI;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by chris on 4/23/2018.
 */

public class DestructoEnemy extends Enemy {
    public DestructoEnemy(String name, Vector2 position) {
        super(name, position);
        this.speed = 200f;
    }

    @Override
    public void Think(World perception) {
        super.Think(perception);
    }

    @Override
    public String Speak(){
        return "im a destructo Baddy!";
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
