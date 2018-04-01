package com.kinglogic.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kinglogic.game.Managers.GameManager;

public class GameAdapter extends ApplicationAdapter {
	SpaceGame game;

	
	@Override
	public void create () {
		game = new SpaceGame();
		game.create();
	}

	@Override
	public void render () {
		game.render();
	}
	
	@Override
	public void dispose () {
		game.dispose();
		GameManager.ins().dispose();
	}
}
