package net.mbretsch.rgblike.main;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import net.mbretsch.rgblike.game.RGBlike;
import net.mbretsch.rgblike.reference.Reference;

public class Game {

	public static LwjglApplication application;

	public static void main(String[] args) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = Reference.GRID_WIDTH * Reference.CELL_WIDTH;
		config.height = Reference.GRID_HEIGHT * Reference.CELL_HEIGHT;

		config.title = Reference.GAME_TITLE;

		application = new LwjglApplication(new RGBlike(), config);
	}

}
