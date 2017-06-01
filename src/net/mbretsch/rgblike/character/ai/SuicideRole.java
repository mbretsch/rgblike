package net.mbretsch.rgblike.character.ai;

import com.badlogic.gdx.graphics.Color;

import net.mbretsch.rgblike.character.NPC;
import net.mbretsch.rgblike.character.Player;
import net.mbretsch.rgblike.combat.Attack.GColor;
import net.mbretsch.rgblike.dungeon.DunGen;
import net.mbretsch.rgblike.game.RGBlike;
import squidpony.panel.IColoredString;
import squidpony.squidgrid.FOV;
import squidpony.squidgrid.gui.gdx.SColor;

public class SuicideRole implements NPCRole {

	private FOV fov;
	private double[][] fovMap;
	private NPC npc;

	public SuicideRole(NPC npc) {
		this.npc = npc;
		fov = new FOV();
	}

	@Override
	public void calcTurn() {
		if (npc.isActive()) {
			fovMap = fov.calculateFOV(DunGen.resistanceMap, npc.getPos().x, npc.getPos().y, 12);
			Player p = Player.instance();
			if (fovMap[p.getPos().x][p.getPos().y] > 0) {
				IColoredString<Color> str = new IColoredString.Impl<Color>(npc.getName(), npc.getCharColor());
				str.append(" attacks you with ", SColor.WHITE);
				if (npc.getRed() > npc.getGreen() && npc.getRed() > npc.getBlue()) {
					npc.setRed(npc.getRed() - npc.getMaxAttack());
					p.takeRedDmg(npc.getMaxAttack());
					str.append("red ", GColor.RED.col());
					str.append("Power: " + npc.getMaxAttack(), SColor.WHITE);
				} else if (npc.getGreen() > npc.getRed() && npc.getGreen() > npc.getBlue()) {
					npc.setGreen(npc.getGreen() - npc.getMaxAttack());
					p.takeGreenDmg(npc.getMaxAttack());
					str.append("green ", GColor.GREEN.col());
					str.append("Power: " + npc.getMaxAttack(), SColor.WHITE);
				} else {
					npc.setBlue(npc.getBlue() - npc.getMaxAttack());
					p.takeBlueDmg(npc.getMaxAttack());
					str.append("blue ", GColor.BLUE.col());
					str.append("Power: " + npc.getMaxAttack(), SColor.WHITE);
				}
				RGBlike.logMessages.appendMessage(str);

			}
		}
	}

}
