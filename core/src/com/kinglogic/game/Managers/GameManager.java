package com.kinglogic.game.Managers;

import com.badlogic.gdx.Gdx;
import com.kinglogic.game.TestInputProcessor;

/**
 * Created by chris on 4/1/2018.
 */

public class GameManager {
    private static GameManager instance;
    private TestInputProcessor tip;

    public static GameManager ins() {
        if(instance == null)
            instance = new GameManager();
        return instance;
    }

    private GameManager(){
        ResourceManager.ins();
        WorldManager.ins();
        tip = new TestInputProcessor();
        Gdx.input.setInputProcessor(tip);

    }

    public void dispose(){
        ResourceManager.ins().dispose();
        WorldManager.ins().dispose();
    }

}
