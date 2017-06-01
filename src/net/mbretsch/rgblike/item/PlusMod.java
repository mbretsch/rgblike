package net.mbretsch.rgblike.item;

public class PlusMod implements ItemModifier {

	private int value;

	public PlusMod(int value) {
		this.value = value;
	}

	@Override
	public int combatMod(int value) {
		return value;
	}

	@Override
	public int wearMod(int value) {
		return value + this.value;
	}

	@Override
	public int dropMod(int value) {
		return value - this.value;
	}

	@Override
	public String toString() {
		return "+" + this.value;
	}

	@Override
	public int combatSelfMod(int value) {
		return value;
	}

}
