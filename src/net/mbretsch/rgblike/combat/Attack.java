package net.mbretsch.rgblike.combat;

import com.badlogic.gdx.graphics.Color;

import net.mbretsch.rgblike.character.NPC;
import net.mbretsch.rgblike.character.Player;
import squidpony.panel.IColoredString;

public class Attack {

	public enum GColor {
		RED("red", Color.RED), GREEN("green", Color.GREEN), BLUE("blue", Color.BLUE);

		private String colString;
		private Color col;

		GColor(String colString, Color col) {
			this.colString = colString;
			this.col = col;
		}

		public String colorString() {
			return this.colString;
		}

		public Color col() {
			return this.col;
		}
	}

	private GColor selectedColor = GColor.RED;
	private int strength = 1;
	private NPC enemy;

	public Attack(NPC enemy) {
		this.enemy = enemy;
	}

	public IColoredString<Color> confirm() {
		Player player = Player.instance();
		int dmg = strength;
		if (selectedColor == GColor.RED) {
			if (player.getRedItem() != null) {
				strength = player.getRedItem().calcItemAdjustmentAttack(strength);
				dmg = player.getRedItem().calcItemAdjustmentAttackSelf(dmg);
			}
			player.setRed(player.getRed() - dmg);
			enemy.setRed(enemy.getRed() + strength);
		} else if (selectedColor == GColor.GREEN) {
			if (player.getGreenItem() != null) {
				strength = player.getGreenItem().calcItemAdjustmentAttack(strength);
				dmg = player.getGreenItem().calcItemAdjustmentAttackSelf(dmg);
			}
			player.setGreen(player.getGreen() - dmg);
			enemy.setGreen(enemy.getGreen() + strength);
		} else {
			if (player.getBlueItem() != null) {
				strength = player.getBlueItem().calcItemAdjustmentAttack(strength);
				dmg = player.getBlueItem().calcItemAdjustmentAttackSelf(dmg);
			}
			player.setBlue(player.getBlue() - dmg);
			enemy.setBlue(enemy.getBlue() + strength);
		}

		IColoredString<Color> str = new IColoredString.Impl<Color>("You attack ", Color.WHITE);
		str.append(enemy.getName(), enemy.getCharColor());
		str.append(" with ", Color.WHITE);
		str.append(selectedColor.colString, selectedColor.col());
		str.append(" Power: " + strength, Color.WHITE);
		return str;
	}

	public void setEnemy(NPC enemy) {
		this.enemy = enemy;
	}

	public IColoredString<Color> getColoredString() {
		IColoredString<Color> str = new IColoredString.Impl<Color>("You attack ", Color.WHITE);
		str.append(enemy.getName(), enemy.getCharColor());
		str.append(" with ", Color.WHITE);
		str.append(selectedColor.colString, selectedColor.col());
		str.append(" Power: " + strength, Color.WHITE);

		return str;
	}

	public void nextCol() {
		if (selectedColor == GColor.RED) {
			selectedColor = GColor.GREEN;
		} else if (selectedColor == GColor.GREEN) {
			selectedColor = GColor.BLUE;
		} else {
			selectedColor = GColor.RED;
		}
	}

	public void prevCol() {
		if (selectedColor == GColor.RED) {
			selectedColor = GColor.BLUE;
		} else if (selectedColor == GColor.GREEN) {
			selectedColor = GColor.RED;
		} else {
			selectedColor = GColor.GREEN;
		}
	}

	public void incStr() {
		strength *= 2;
	}

	public void decStr() {
		if (strength > 1) {
			strength /= 2;
		}
	}

}
