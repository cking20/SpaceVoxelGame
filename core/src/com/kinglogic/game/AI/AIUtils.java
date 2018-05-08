package com.kinglogic.game.AI;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.kinglogic.game.Actors.Entities.Entity;
import com.kinglogic.game.Managers.ResourceManager;
import com.kinglogic.game.Physics.EntityBody;

/**
 * Created by chris on 4/23/2018.
 */

public class AIUtils {
    public static void Swarm(EntityBody me, EntityBody leader){
        Vector2 currVelocity =leader.myBody.getLinearVelocity();
        Vector2 future = leader.myBody.getPosition();
        Vector2 distance = me.myBody.getPosition();
        distance = distance.sub(leader.myBody.getPosition());
        if(distance.len() > ResourceManager.voxelPixelSize){
            future = future.add(currVelocity);
            Seek(me, future);
        }

    }
    public static void Persue(EntityBody me, EntityBody target){
        //Assuming the max velocity is 180ish
        //make prediction with a time heuristic(will be finniky)
        Vector2 currVelocity =target.myBody.getLinearVelocity();
        Vector2 future = target.myBody.getPosition();
//        Vector2 distance = me.myBody.getPosition();
//        distance = distance.sub(target.myBody.getPosition());
        future = future.add(currVelocity);
        Seek(me, future);

    }

    public static void Seek(EntityBody me, Vector2 target){
        //steering is target - current
        float angleToTarget = me.myBody.getTransform().getOrientation().angle(target.sub(me.myBody.getPosition()));
        if(angleToTarget > 90) {
            me.RotateLeft();
            me.RotateLeft();
            me.RotateLeft();
        }
        else {
            me.RotateRight();
            me.RotateRight();
            me.RotateRight();
        }
        if(angleToTarget < 30+90f && angleToTarget > -30+90f)
            me.GoForeward();
    }
    public static void Flee(EntityBody me, Vector2 target){
        //steering is target - current
        float angleToTarget = me.myBody.getTransform().getOrientation().angle(target.sub(me.myBody.getPosition()));

        if(angleToTarget > 90) {
            me.RotateRight();
            me.RotateRight();
            me.RotateRight();
        }
        else {

            me.RotateLeft();
            me.RotateLeft();
            me.RotateLeft();
        }
        if(angleToTarget > 30+90f || angleToTarget < -30+90f)
            me.GoForeward();
    }

/*
    public static void Evade(RigidBody me, RigidBody target){
        Vector2 currVelocity = new Vector2(target.velocity.x, target.velocity.y);
        Vector2 future = new Vector2(target.position.x, target.position.y);
        Vector2 distance = new Vector2(me.position);
        distance = distance.sub(target.position);
        future = future.add(currVelocity.scl(distance.len()/180));
        Flee(me, future);

    }

    public static void Flee(RigidBody me, Vector2 target){
        target = target.scl(-1);
        me.AddForce(target.sub(me.position));
    }*/
}
