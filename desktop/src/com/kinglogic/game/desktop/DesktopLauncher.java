package com.kinglogic.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kinglogic.game.Constants;
import com.kinglogic.game.GameAdapter;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Constants.START_WIDTH;
		config.height = Constants.START_HEIGHT;
		new LwjglApplication(new GameAdapter(), config);
	}
}
