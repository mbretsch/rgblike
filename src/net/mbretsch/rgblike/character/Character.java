package net.mbretsch.rgblike.character;

import java.util.Random;

import net.mbretsch.rgblike.reference.Reference;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidmath.Coord;

public class Character {

	protected String name;
	protected Coord pos;

	protected int red, green, blue;
	protected boolean isDead = false;

	public Character(String name, Coord pos) {
		this.name = name;
		this.pos = pos;
	}

	public int getRed() {
		return this.red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return this.green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return this.blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public Coord getPos() {
		return pos;
	}

	public void setPos(Coord pos) {
		this.pos = pos;
	}

	public String getName() {
		return this.name;
	}

	public boolean isDead() {
		return this.isDead;
	}

	public SColor getCharColor() {
		int max = Math.max(this.red, Math.max(this.green, this.blue));
		double r = this.red;
		double g = this.green;
		double b = this.blue;
		r /= max;
		g /= max;
		b /= max;
		return new SColor(new Double(r * 255).intValue(), new Double(g * 255).intValue(),
				new Double(b * 255).intValue());
	}

	public void calcBalance() {
		Random rand = new Random();
		int median = (this.red + this.green + this.blue) / 3;
		int netloss = 0;
		int netgain = 0;
		double balancingFactor = ((double)median) / Reference.BALANCING_FACTOR;
		double changeRed = (this.red - median) * balancingFactor;
		if (changeRed > 0) {
			this.red += Math.floor(changeRed);
			netgain += Math.floor(changeRed);
		} else {
			this.red += Math.ceil(changeRed);
			netloss += Math.ceil(changeRed);
		}
		double changeGreen = (this.green - median) * balancingFactor;
		if (changeGreen > 0) {
			this.green += Math.floor(changeGreen);
			netgain += Math.floor(changeGreen);
		} else {
			this.green += Math.ceil(changeGreen);
			netloss += Math.ceil(changeGreen);
		}
		double changeBlue = (this.blue - median) * balancingFactor;
		if (changeBlue > 0) {
			this.blue += Math.floor(changeBlue);
			netgain += Math.floor(changeBlue);
		} else {
			this.blue += Math.ceil(changeBlue);
			netloss += Math.ceil(changeBlue);
		}
		if (this.red < 0 || this.green < 0 || this.blue < 0) {
			this.isDead = true;
		}
		int dif = netgain - Math.abs(netloss);
		while (dif > 0) {
			if (changeRed < 0) {
				if (rand.nextBoolean()) {
					this.red -= dif;
					return;
				}
			}
			if (changeGreen < 0) {
				if (rand.nextBoolean()) {
					this.green -= dif;
					return;
				}
			}
			if (changeBlue < 0) {
				if (rand.nextBoolean()) {
					this.blue -= dif;
					return;
				}
			}
		}
		while (dif < 0) {
			if (changeRed > 0) {
				if (rand.nextBoolean()) {
					this.red += Math.abs(dif);
					return;
				}
			}
			if (changeGreen > 0) {
				if (rand.nextBoolean()) {
					this.green += Math.abs(dif);
					return;
				}
			}
			if (changeBlue > 0) {
				if (rand.nextBoolean()) {
					this.blue += Math.abs(dif);
					return;
				}
			}
		}
	}

}
