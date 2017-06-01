package net.mbretsch.rgblike.character;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import net.mbretsch.rgblike.character.ai.BalancedRole;
import net.mbretsch.rgblike.character.ai.ColorStickerRole;
import net.mbretsch.rgblike.character.ai.HardHitterRole;
import net.mbretsch.rgblike.character.ai.NPCRole;
import net.mbretsch.rgblike.character.ai.RandomRole;
import net.mbretsch.rgblike.character.ai.SuicideRole;
import net.mbretsch.rgblike.reference.Reference;
import squidpony.squidai.DijkstraMap;
import squidpony.squidmath.Coord;

public class NPC extends Character {

	public static DijkstraMap npcPathfinder;

	private boolean isBoss;
	private static String[] alist, blist, clist, dlist;
	private char type;
	private boolean isActive = false;
	private int maxAttack;
	private NPCRole playerRole;
	private static Random rand = new Random();

	public NPC(String name, Coord pos) {
		super(name, pos);
	}

	public char getChar() {
		if (isBoss) {
			return java.lang.Character.toUpperCase(this.type);
		}
		return this.type;
	}

	public static NPC createNPC(char type, boolean boss) {
		NPC npc = new NPC(NPC.generateName(type), Coord.get(0, 0));
		npc.type = type;
		switch (type) {
		case 'a': {
			npc.setRed(rand.nextInt(Reference.ENEMIES_BASE_STAGE_1 / 2) + Reference.ENEMIES_BASE_STAGE_1 - 15);
			npc.setGreen(rand.nextInt(Reference.ENEMIES_BASE_STAGE_1 / 2) + Reference.ENEMIES_BASE_STAGE_1 - 15);
			npc.setBlue(rand.nextInt(Reference.ENEMIES_BASE_STAGE_1 / 2) + Reference.ENEMIES_BASE_STAGE_1 - 15);
			npc.maxAttack = Reference.ENEMIES_BASE_ATTACK_STAGE_1;
			npc.playerRole = generateRole(npc);
			if (boss) {
				npc.isBoss = boss;
				npc.name = npc.name.toUpperCase();
				npc.setRed(Reference.ENEMIES_BASE_STAGE_1 * 4);
				npc.setGreen(Reference.ENEMIES_BASE_STAGE_1 * 4);
				npc.setBlue(Reference.ENEMIES_BASE_STAGE_1 * 4);
				npc.maxAttack = Reference.ENEMIES_BASE_ATTACK_STAGE_1 * 4;
				npc.playerRole = generateRole(npc);
			}
			break;
		}
		case 'b': {
			npc.setRed(rand.nextInt(Reference.ENEMIES_BASE_STAGE_2 / 2) + Reference.ENEMIES_BASE_STAGE_2 - 15);
			npc.setGreen(rand.nextInt(Reference.ENEMIES_BASE_STAGE_2 / 2) + Reference.ENEMIES_BASE_STAGE_2 - 15);
			npc.setBlue(rand.nextInt(Reference.ENEMIES_BASE_STAGE_2 / 2) + Reference.ENEMIES_BASE_STAGE_2 - 15);
			npc.maxAttack = Reference.ENEMIES_BASE_ATTACK_STAGE_2;
			npc.playerRole = generateRole(npc);
			if (boss) {
				npc.isBoss = boss;
				npc.name = npc.name.toUpperCase();
				npc.setRed(Reference.ENEMIES_BASE_STAGE_2 * 4);
				npc.setGreen(Reference.ENEMIES_BASE_STAGE_2 * 4);
				npc.setBlue(Reference.ENEMIES_BASE_STAGE_2 * 4);
				npc.maxAttack = Reference.ENEMIES_BASE_ATTACK_STAGE_2 * 4;
				npc.playerRole = generateRole(npc);
			}
			break;

		}
		case 'c': {
			npc.setRed(rand.nextInt(Reference.ENEMIES_BASE_STAGE_3 / 2) + Reference.ENEMIES_BASE_STAGE_3 - 100);
			npc.setGreen(rand.nextInt(Reference.ENEMIES_BASE_STAGE_3 / 2) + Reference.ENEMIES_BASE_STAGE_3 - 100);
			npc.setBlue(rand.nextInt(Reference.ENEMIES_BASE_STAGE_3 / 2) + Reference.ENEMIES_BASE_STAGE_3 - 100);
			npc.maxAttack = Reference.ENEMIES_BASE_ATTACK_STAGE_3;
			npc.playerRole = generateRole(npc);
			if (boss) {
				npc.isBoss = boss;
				npc.name = npc.name.toUpperCase();
				npc.setRed(Reference.ENEMIES_BASE_STAGE_3 * 4);
				npc.setGreen(Reference.ENEMIES_BASE_STAGE_3 * 4);
				npc.setBlue(Reference.ENEMIES_BASE_STAGE_3 * 4);
				npc.maxAttack = Reference.ENEMIES_BASE_ATTACK_STAGE_3 * 4;
				npc.playerRole = generateRole(npc);
			}
			break;
		}
		case 'd':
		default: {
			npc.setRed(rand.nextInt(Reference.ENEMIES_BASE_STAGE_4 / 2) + Reference.ENEMIES_BASE_STAGE_4 - 200);
			npc.setGreen(rand.nextInt(Reference.ENEMIES_BASE_STAGE_4 / 2) + Reference.ENEMIES_BASE_STAGE_4 - 200);
			npc.setBlue(rand.nextInt(Reference.ENEMIES_BASE_STAGE_4 / 2) + Reference.ENEMIES_BASE_STAGE_4 - 200);
			npc.maxAttack = Reference.ENEMIES_BASE_ATTACK_STAGE_4;
			npc.playerRole = generateRole(npc);
			if (boss) {
				npc.isBoss = boss;
				npc.name = npc.name.toUpperCase();
				npc.setRed(Reference.ENEMIES_BASE_STAGE_4 * 4);
				npc.setGreen(Reference.ENEMIES_BASE_STAGE_4 * 4);
				npc.setBlue(Reference.ENEMIES_BASE_STAGE_4 * 4);
				npc.maxAttack = Reference.ENEMIES_BASE_ATTACK_STAGE_4 * 4;
				npc.playerRole = new BalancedRole(npc);
			}
			break;
		}
		}
		return npc;
	}

	private static NPCRole generateRole(NPC npc) {
		double sel = rand.nextDouble();
		if (sel <= 0.1) {
			return new HardHitterRole(npc);
		} else if (sel <= 0.25) {
			return new SuicideRole(npc);
		} else if (sel <= 0.4) {
			return new ColorStickerRole(npc);
		} else if (sel <= 0.65) {
			return new BalancedRole(npc);
		}
		return new RandomRole(npc);
	}

	private static String generateName(char type) {
		Random rand = new Random();
		switch (type) {
		case 'a': {
			if (alist == null) {
				alist = getList("namesa.txt");
			}
			return alist[rand.nextInt(alist.length)];
		}
		case 'b': {
			if (blist == null) {
				blist = getList("namesb.txt");
			}
			return blist[rand.nextInt(blist.length)];
		}
		case 'c': {
			if (clist == null) {
				clist = getList("namesc.txt");
			}
			return clist[rand.nextInt(clist.length)];
		}
		case 'd': {
			if (dlist == null) {
				dlist = getList("namesd.txt");
			}
			return dlist[rand.nextInt(dlist.length)];
		}
		}
		return "";
	}

	private static String[] getList(String filename) {
		InputStream is = NPC.class.getClassLoader().getResourceAsStream(filename);

		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
			int numLines = 0;
			while (bufferedReader.readLine() != null) {
				numLines++;
			}
			is.close();
			is = NPC.class.getClassLoader().getResourceAsStream(filename);
			bufferedReader = new BufferedReader(new InputStreamReader(is));
			String[] list = new String[numLines];
			String n;
			int c = 0;
			while ((n = bufferedReader.readLine()) != null) {
				list[c] = n.toLowerCase();
				c++;
			}
			return list;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	public boolean isActive() {
		return this.isActive;
	}

	public void setActive(boolean active) {
		this.isActive = active;
	}

	public boolean isBoss() {
		return this.isBoss;
	}

	public int getMaxAttack() {
		return maxAttack;
	}

	public void calcNPCTurn() {
		this.playerRole.calcTurn();
	}

}
