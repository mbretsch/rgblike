package net.mbretsch.rgblike.character.ai;

import java.util.List;

import com.badlogic.gdx.graphics.Color;

import net.mbretsch.rgblike.character.NPC;
import net.mbretsch.rgblike.character.Player;
import net.mbretsch.rgblike.combat.Attack.GColor;
import net.mbretsch.rgblike.dungeon.DunGen;
import net.mbretsch.rgblike.game.RGBlike;
import squidpony.panel.IColoredString;
import squidpony.squidgrid.gui.gdx.SColor;
import squidpony.squidmath.Coord;

public class HardHitterRole implements NPCRole {

	private NPC npc;

	public HardHitterRole(NPC npc) {
		this.npc = npc;
	}

	@Override
	public void calcTurn() {
		if (npc.isActive()) {
			Player p = Player.instance();
			List<Coord> pathToPlayer = NPC.npcPathfinder.findPath(100, null, null, npc.getPos(), p.getPos());
			if (pathToPlayer.size() > 1) {
				this.move();
			} else {
				IColoredString<Color> str = new IColoredString.Impl<Color>(npc.getName(), npc.getCharColor());
				str.append(" attacks you with ", SColor.WHITE);
				int dmg = npc.getMaxAttack() * 2;
				if (npc.getRed() > npc.getGreen() && npc.getRed() > npc.getBlue()) {
					npc.setRed(npc.getRed() - dmg);
					p.takeRedDmg(dmg);
					str.append("red ", GColor.RED.col());
					str.append("Power: " + dmg, SColor.WHITE);
				} else if (npc.getGreen() > npc.getRed() && npc.getGreen() > npc.getBlue()) {
					npc.setGreen(npc.getGreen() - dmg);
					p.takeGreenDmg(dmg);
					str.append("green ", GColor.GREEN.col());
					str.append("Power: " + dmg, SColor.WHITE);
				} else {
					npc.setBlue(npc.getBlue() - dmg);
					p.takeBlueDmg(dmg);
					str.append("blue ", GColor.BLUE.col());
					str.append("Power: " + dmg, SColor.WHITE);
				}
				RGBlike.logMessages.appendMessage(str);

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
