package net.mbretsch.rgblike.character.ai;

import java.util.List;

import com.badlogic.gdx.graphics.Color;

import net.mbretsch.rgblike.character.NPC;
import net.mbretsch.rgblike.character.Player;
import net.mbretsch.rgblike.combat.Attack.GColor;
import net.mbretsch.rgblike.dungeon.DunGen;
import net.mbretsch.rgblike.game.RGBlike;
import squidpony.panel.IColoredString;
import squidpony.squidgrid.FOV;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidmath.Coord;

public class ColorStickerRole implements NPCRole {

	private FOV fov;
	private double[][] fovMap;
	private NPC npc;
	private GColor col;

	public ColorStickerRole(NPC npc) {
		this.npc = npc;
		fov = new FOV();
		if (npc.getRed() > npc.getGreen() && npc.getRed() > npc.getBlue()) {
			col = GColor.RED;
		} else if (npc.getGreen() > npc.getRed() && npc.getGreen() > npc.getBlue()) {
			col = GColor.GREEN;
		} else {
			col = GColor.BLUE;
		}
	}

	@Override
	public void calcTurn() {
		if (npc.isActive()) {
			Player p = Player.instance();
			fovMap = fov.calculateFOV(DunGen.resistanceMap, npc.getPos().x, npc.getPos().y, 7);
			if (fovMap[p.getPos().x][p.getPos().y] > 0) {
				IColoredString<Color> str = new IColoredString.Impl<Color>(npc.getName(), npc.getCharColor());
				str.append(" attacks you with ", SColor.WHITE);
				switch (col) {
				case RED: {
					npc.setRed(npc.getRed() - npc.getMaxAttack());
					p.takeRedDmg(npc.getMaxAttack());
					str.append("red ", GColor.RED.col());
					str.append("Power: " + npc.getMaxAttack(), SColor.WHITE);
					break;
				}
				case GREEN: {
					npc.setGreen(npc.getGreen() - npc.getMaxAttack());
					p.takeGreenDmg(npc.getMaxAttack());
					str.append("green ", GColor.GREEN.col());
					str.append("Power: " + npc.getMaxAttack(), SColor.WHITE);
					break;
				}
				case BLUE: {
					npc.setBlue(npc.getBlue() - npc.getMaxAttack());
					p.takeBlueDmg(npc.getMaxAttack());
					str.append("blue ", GColor.BLUE.col());
					str.append("Power: " + npc.getMaxAttack(), SColor.WHITE);
					break;
				}
				}
				RGBlike.logMessages.appendMessage(str);
			} else {
				this.move();
			}
		}
	}

	private void move() {
		int tries = 10;
		Coord newPos;
		Player p = Player.instance();
		while (tries >= 0) {
			tries--;
			List<Coord> pathToPlayer = NPC.npcPathfinder.findPath(100, null, null, npc.getPos(), p.getPos());
			if (pathToPlayer.size() > 1) {
				newPos = pathToPlayer.get(0);
				boolean canMove = true;
				for (NPC n : DunGen.npc) {
					if (n.getPos().x == newPos.x && n.getPos().y == newPos.y) {
						canMove = false;
					}
				}
				if (canMove) {
					npc.setPos(newPos);
					break;
				}
			}
		}
	}

}
