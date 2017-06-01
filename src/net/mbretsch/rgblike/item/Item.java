package net.mbretsch.rgblike.item;

import java.util.List;

import net.mbretsch.rgblike.character.Player;
import net.mbretsch.rgblike.combat.Attack.GColor;
import squidpony.squidmath.Coord;

public class Item {

	private GColor color;
	private List<ItemModifier> modifiers;
	private Coord posOnGround;

	public Item(List<ItemModifier> modifiers, Coord pos, GColor color) {
		this.modifiers = modifiers;
		this.posOnGround = pos;
		this.color = color;
	}

	public String getItemString() {
		String itemString = "";
		for (int i = 0; i < modifiers.size() - 1; i++) {
			itemString += modifiers.get(i).toString() + ",";
		}
		itemString += modifiers.get(modifiers.size() - 1).toString();
		return itemString;
	}

	public int calcItemAdjustmentAttack(int attack) {
		int newValue = attack;
		for (int i = 0; i < modifiers.size(); i++) {
			newValue = modifiers.get(i).combatMod(newValue);
		}
		return newValue;
	}

	public int calcItemAdjustmentAttackSelf(int dmg) {
		int newValue = dmg;
		for (int i = 0; i < modifiers.size(); i++) {
			newValue = modifiers.get(i).combatSelfMod(newValue);
		}
		return newValue;
	}

	public void calcItemAdjustmentWear() {
		for (int i = 0; i < modifiers.size(); i++) {
			if (color == GColor.RED) {
				Player.instance().setRed(modifiers.get(i).wearMod(Player.instance().getRed()));
			}
			if (color == GColor.GREEN) {
				Player.instance().setGreen(modifiers.get(i).wearMod(Player.instance().getGreen()));
			}
			if (color == GColor.BLUE) {
				Player.instance().setBlue(modifiers.get(i).wearMod(Player.instance().getBlue()));
			}
		}
	}

	public void calcItemAdjustmentDrop() {
		for (int i = 0; i < modifiers.size(); i++) {
			if (color == GColor.RED) {
				Player.instance().setRed(modifiers.get(i).dropMod(Player.instance().getRed()));
			}
			if (color == GColor.GREEN) {
				Player.instance().setGreen(modifiers.get(i).dropMod(Player.instance().getGreen()));
			}
			if (color == GColor.BLUE) {
				Player.instance().setBlue(modifiers.get(i).dropMod(Player.instance().getBlue()));
			}
		}
	}

	public Coord getPos() {
		return this.posOnGround;
	}

	public GColor getGColor() {
		return this.color;
	}

}
