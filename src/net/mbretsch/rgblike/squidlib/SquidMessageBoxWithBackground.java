package net.mbretsch.rgblike.squidlib;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import net.mbretsch.rgblike.game.RGBlike;
import squidpony.IColorCenter;
import squidpony.squidgrid.gui.gdx.SquidMessageBox;
import squidpony.squidgrid.gui.gdx.TextCellFactory;

public class SquidMessageBoxWithBackground extends SquidMessageBox {

	public SquidMessageBoxWithBackground(int gridWidth, int gridHeight, int cellWidth, int cellHeight) {
		super(gridWidth, gridHeight, cellWidth, cellHeight);
	}

	public SquidMessageBoxWithBackground(int gridWidth, int gridHeight, TextCellFactory factory,
			IColorCenter<Color> center) {
		super(gridWidth, gridHeight, factory, center);
	}

	public SquidMessageBoxWithBackground(int gridWidth, int gridHeight, TextCellFactory factory) {
		super(gridWidth, gridHeight, factory);
	}

	public SquidMessageBoxWithBackground(int gridWidth, int gridHeight) {
		super(gridWidth, gridHeight);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();
		RGBlike.shapeRenderer.begin(ShapeType.Filled);
		RGBlike.shapeRenderer.setColor(Color.BLACK);
		RGBlike.shapeRenderer.rect(xOffset, yOffset * 0.775f, this.getWidth(), this.getHeight() * 0.775f);
		RGBlike.shapeRenderer.end();
		batch.begin();
		super.draw(batch, parentAlpha);
	}

}
