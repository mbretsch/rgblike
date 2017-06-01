package net.mbretsch.rgblike.item;

public class LensMod implements ItemModifier {

	private double value;

	public LensMod(double value) {
		this.value = Math.floor(value * 10) / 10;

	}

	@Override
	public int combatMod(int value) {
		return (int) Math.round(value * this.value);
	}

	@Override
	public int wearMod(int value) {
		return value;
	}

	@Override
	public int dropMod(int value) {
		return value;
	}

	@Override
	public String toString() {
		return String.format("x%.1f", value);
	}

	@Override
	public int combatSelfMod(int value) {
		return value;
	}

}
