package com.kinglogic.game.Interfaces;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by chris on 4/24/2018.
 */

/**
 * Hoping to use this for gravity, thruster and shooty blocks
 */
public interface VoxelAction {
    public void Act(Body body);
}
