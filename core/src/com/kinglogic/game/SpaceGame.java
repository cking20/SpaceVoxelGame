package com.kinglogic.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.kinglogic.game.Managers.GameManager;
import com.kinglogic.game.Screens.GameScreen;

/**
 * Created by chris on 4/1/2018.
 */

public class SpaceGame extends Game {
    private GameScreen mainGsmeScreen;
    @Override
    public void create() {
        Gdx.graphics.setTitle("Space Handy");
        GameManager.ins();
        mainGsmeScreen = new GameScreen(this);
        setScreen(mainGsmeScreen);

    }

    @Override
    public void render () {
        super.render();
    }


    @Override
    public void dispose () {

    }
}
