package com.kinglogic.game.Physics;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.kinglogic.game.Actors.Voxel.VoxelCollection;

/**
 * Created by chris on 4/1/2018.
 */

public class StaticGrid extends Grid{
    public StaticGrid(VoxelCollection v) {
        super(v);
        bodyDef.type = BodyDef.BodyType.StaticBody;
    }
}
