package net.mbretsch.rgblike.character;

import net.mbretsch.rgblike.item.Item;
import net.mbretsch.rgblike.reference.Reference;
import squidpony.squidmath.Coord;

public class Player extends Character {

	private static Player instance;

	private Item redItem;
	private Item greenItem;
	private Item blueItem;

	public Player(String name, Coord pos) {
		super(name, pos);
	}

	public static Player instance() {
		if (instance == null) {
			instance = new Player("Player", Coord.get(0, 0));
			instance.red = 255;
			instance.green = 255;
			instance.blue = 255;
		}
		return instance;
	}

	public int getRedBarWidth() {
		int maxBarWidth = Reference.BAR_WIDTH * Reference.CELL_WIDTH;
		int max = Math.max(this.red, Math.max(this.green, this.blue));
		if (this.red <= 0) {
			return 0;
		}
		return (max == this.red ? maxBarWidth : maxBarWidth * this.red / max);
	}

	public int getGreenBarWidth() {
		int maxBarWidth = Reference.BAR_WIDTH * Reference.CELL_WIDTH;
		int max = Math.max(this.green, Math.max(this.red, this.blue));
		if (this.green <= 0) {
			return 0;
		}
		return (max == this.green ? maxBarWidth : maxBarWidth * this.green / max);
	}

	public int getBlueBarWidth() {
		int maxBarWidth = Reference.BAR_WIDTH * Reference.CELL_WIDTH;
		int max = Math.max(this.blue, Math.max(this.green, this.red));
		if (this.blue <= 0) {
			return 0;
		}
		return (max == this.blue ? maxBarWidth : maxBarWidth * this.blue / max);
	}

	public Item getRedItem() {
		return redItem;
	}

	public void setRedItem(Item redItem) {
		this.redItem = redItem;
	}

	public Item getGreenItem() {
		return greenItem;
	}

	public void setGreenItem(Item greenItem) {
		this.greenItem = greenItem;
	}

	public Item getBlueItem() {
		return blueItem;
	}

	public void setBlueItem(Item blueItem) {
		this.blueItem = blueItem;
	}

	public void takeRedDmg(int ammount) {
		this.red += ammount;
	}

	public void takeGreenDmg(int ammount) {
		this.green += ammount;
	}

	public void takeBlueDmg(int ammount) {
		this.blue += ammount;
	}

}
