package net.mbretsch.rgblike.item;

public interface ItemModifier {

	public int combatMod(int value);

	public int combatSelfMod(int value);

	public int wearMod(int value);

	public int dropMod(int value);

}
