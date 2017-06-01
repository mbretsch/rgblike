package net.mbretsch.rgblike.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.mbretsch.rgblike.character.NPC;
import net.mbretsch.rgblike.combat.Attack.GColor;

public class ItemGenerator {

	private static Random rand = null;

	public static Item generateItem(NPC npc) {

		if (rand == null) {
			rand = new Random();
		}

		GColor col = getColor(npc);
		List<ItemModifier> modifiers = new ArrayList<ItemModifier>();
		List<ItemModifier> basicModifiers = new ArrayList<ItemModifier>();
		List<ItemModifier> advancedModifiers = new ArrayList<ItemModifier>();
		basicModifiers.add(generatePlusMod(npc));
		basicModifiers.add(generateLensMod(npc));
		advancedModifiers.add(generateVampMod());
		int indBasicMod = rand.nextInt(basicModifiers.size());
		modifiers.add(basicModifiers.get(indBasicMod));
		basicModifiers.remove(indBasicMod);
		if (npc.isBoss()) {
			if (rand.nextBoolean()) {
				int indAdvMod = rand.nextInt(advancedModifiers.size());
				modifiers.add(advancedModifiers.get(indAdvMod));
				advancedModifiers.remove(indAdvMod);
			} else {
				indBasicMod = rand.nextInt(basicModifiers.size());
				modifiers.add(basicModifiers.get(indBasicMod));
				basicModifiers.remove(indBasicMod);
			}
		}
		return new Item(modifiers, npc.getPos(), col);
	}

	private static GColor getColor(NPC npc) {
		GColor gc = null;

		int max = (npc.getRed() < 0 ? 0 : npc.getRed()) + (npc.getGreen() < 0 ? 0 : npc.getGreen())
				+ (npc.getBlue() < 0 ? 0 : npc.getBlue()) + 1;
		while (gc == null) {
			if (rand.nextInt(max) <= npc.getRed()) {
				gc = GColor.RED;
				break;
			}
			if (rand.nextInt(max) <= npc.getGreen()) {
				gc = GColor.GREEN;
				break;
			}
			if (rand.nextInt(max) <= npc.getBlue()) {
				gc = GColor.BLUE;
				break;
			}

		}
		return gc;
	}

	private static ItemModifier generatePlusMod(NPC npc) {
		int plus = 25;
		plus += rand.nextInt(125);
		switch (npc.getChar()) {
		case 'b': {
			plus *= 4;
			break;
		}
		case 'c': {
			plus *= 6;
			break;
		}
		case 'd': {
			plus *= 8;
			break;
		}
		}
		if (npc.isBoss()) {
			plus *= 1.5;
		}
		return new PlusMod(plus);
	}

	private static ItemModifier generateLensMod(NPC npc) {
		double lens = 1;
		lens += Math.ceil(rand.nextDouble() / 2 * 10) / 10;
		switch (npc.getChar()) {
		case 'b': {
			lens += 0.5;
			break;
		}
		case 'c': {
			lens *= 2;
			break;
		}
		case 'd': {
			lens += 0.5;
			lens *= 2;
			break;
		}
		}
		return new LensMod(lens);
	}

	private static ItemModifier generateVampMod() {
		return new VampMod();
	}

}
