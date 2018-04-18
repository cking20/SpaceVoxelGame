package com.kinglogic.game.Managers;

import com.badlogic.gdx.math.MathUtils;

import java.util.Random;

/**
 * Created by chris on 4/17/2018.
 */

public class ProceduralContentManager {
    private static ProceduralContentManager instance;
    private int seed;
    private Random randomizer;

    public static ProceduralContentManager ins() {
        if(instance == null)
            instance = new ProceduralContentManager();
        return instance;
    }

    private ProceduralContentManager(){
        seed = MathUtils.random(Integer.MAX_VALUE);
        randomizer = new Random(seed);
    }

}
