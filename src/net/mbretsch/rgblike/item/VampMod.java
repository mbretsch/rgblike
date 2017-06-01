package net.mbretsch.rgblike.item;

public class VampMod implements ItemModifier {

	@Override
	public int combatMod(int value) {
		return value * -1;
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
		return "V";
	}

	@Override
	public int combatSelfMod(int value) {
		return value * -1;
	}

}
