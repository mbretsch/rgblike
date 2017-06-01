package net.mbretsch.rgblike.character.ai;

import java.util.Random;

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

public class BalancedRole implements NPCRole {

	private FOV fov;
	private double[][] fovMap;
	private NPC npc;
	private Random rand;

	public BalancedRole(NPC npc) {
		this.npc = npc;
		fov = new FOV();
		rand = new Random();
	}

	@Override
	public void calcTurn() {
		if (npc.isActive()) {
			Player p = Player.instance();
			if (this.isBalanced()) {
				this.move();
			} else {
				fovMap = fov.calculateFOV(DunGen.resistanceMap, npc.getPos().x, npc.getPos().y, 12);
				if (fovMap[p.getPos().x][p.getPos().y] > 0) {
					IColoredString<Color> str = new IColoredString.Impl<Color>(npc.getName(), npc.getCharColor());
					str.append(" attacks you with ", SColor.WHITE);
					int n = rand.nextInt((int) (Math.log(npc.getMaxAttack()) / Math.log(2)) + 1);
					int dmg = npc.getMaxAttack();
					while (n > 0) {
						n--;
						dmg /= 2;
					}
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
	}

	private void move() {
		int tries = 10;
		Coord mv, newPos;
		Player p = Player.instance();
		while (tries >= 0) {
			tries--;
			if (rand.nextBoolean()) {
				if (rand.nextBoolean()) {
					mv = Coord.get(0, -1);
				} else {
					mv = Coord.get(0, 1);
				}
			} else {
				if (rand.nextBoolean()) {
					mv = Coord.get(-1, 0);
				} else {
					mv = Coord.get(1, 0);
				}
			}
			newPos = npc.getPos().translate(mv.x, mv.y);
			boolean canMove = true;
			if (DunGen.bareDungeon[newPos.x][newPos.y] == '#') {
				continue;
			}
			if (p.getPos().x == newPos.x && p.getPos().y == newPos.y) {
				continue;
			}
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

	private boolean isBalanced() {
		int mean = (npc.getRed() + npc.getGreen() + npc.getBlue()) / 3;
		if (npc.getRed() - npc.getMaxAttack() > mean || npc.getRed() + npc.getMaxAttack() < mean) {
			return false;
		}
		if (npc.getGreen() - npc.getMaxAttack() > mean || npc.getGreen() + npc.getMaxAttack() < mean) {
			return false;
		}
		if (npc.getBlue() - npc.getMaxAttack() > mean || npc.getBlue() + npc.getMaxAttack() < mean) {
			return false;
		}
		return true;
	}

}
