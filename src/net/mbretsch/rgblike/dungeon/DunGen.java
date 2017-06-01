package net.mbretsch.rgblike.dungeon;

import java.util.ArrayList;
import java.util.List;

import net.mbretsch.rgblike.character.NPC;
import net.mbretsch.rgblike.character.Player;
import net.mbretsch.rgblike.main.Game;
import net.mbretsch.rgblike.reference.Reference;
import squidpony.squidai.DijkstraMap;
import squidpony.squidai.DijkstraMap.Measurement;
import squidpony.squidgrid.mapping.DungeonGenerator;
import squidpony.squidgrid.mapping.DungeonUtility;
import squidpony.squidgrid.mapping.styled.TilesetType;
import squidpony.squidmath.Coord;
import squidpony.squidmath.CoordPacker;
import squidpony.squidmath.RNG;

public class DunGen {

	private static DungeonGenerator dungeonGenerator;
	private static RNG rng;
	
	public static char[][] dungeon;
	public static char[][] bareDungeon;
	public static double[][] resistanceMap;
	public static int[][] colorInd;
	public static int[][] bgColorInd;
	public static int currentStage = 1;

	public static short[] placement;

	public static List<NPC> npc;

	public static void genDungeon() {

		rng = new RNG();
		dungeonGenerator = new DungeonGenerator(Reference.GRID_WIDTH, Reference.GRID_HEIGHT, rng);

		try {
			genStage1();
		} catch (Exception e) {
			e.printStackTrace();
			Game.application.exit();
		}

	}

	public static void genStage1() throws Exception {
		boolean bossGen = false;
		int tries = 10;
		while (!bossGen && tries > 0) {
			tries--;
			dungeon = dungeonGenerator.generate(TilesetType.CAVES_LIMIT_CONNECTIVITY);
			int count = Reference.GRID_HEIGHT * Reference.GRID_WIDTH;
			while (count > (Reference.GRID_HEIGHT * Reference.GRID_WIDTH * 2) / 3) {
				dungeon = dungeonGenerator.generate(TilesetType.CAVES_LIMIT_CONNECTIVITY);
				count = 0;
				for (int i = 0; i < dungeon.length; i++) {
					for (int j = 0; j < dungeon[i].length; j++) {
						count += (dungeon[i][j] == '#' ? 1 : 0);
					}
				}
			}
			bareDungeon = dungeonGenerator.getBareDungeon();
			placement = CoordPacker.pack(bareDungeon, '.');
			resistanceMap = DungeonUtility.generateResistances(dungeon);
			colorInd = DungeonUtility.generatePaletteIndices(dungeon);
			bgColorInd = DungeonUtility.generateBGPaletteIndices(dungeon);
			Player.instance().setPos(dungeonGenerator.utility.randomCell(placement));

			bossGen = genNPCs('a', Reference.ENEMIES_COUNT_STAGE_1);
		}
		if (tries == 0) {
			throw new Exception("Could not generate Stage 1!!!");
		}
	}

	public static void genStage2() throws Exception {
		boolean bossGen = false;
		int tries = 10;
		while (!bossGen && tries > 0) {
			tries--;
			dungeon = dungeonGenerator.generate(TilesetType.ROOMS_AND_CORRIDORS_A);
			int count = Reference.GRID_HEIGHT * Reference.GRID_WIDTH;
			while (count > (Reference.GRID_HEIGHT * Reference.GRID_WIDTH * 2) / 3) {
				dungeon = dungeonGenerator.generate(TilesetType.ROOMS_AND_CORRIDORS_A);
				count = 0;
				for (int i = 0; i < dungeon.length; i++) {
					for (int j = 0; j < dungeon[i].length; j++) {
						count += (dungeon[i][j] == '#' ? 1 : 0);
					}
				}
			}
			bareDungeon = dungeonGenerator.getBareDungeon();
			placement = CoordPacker.pack(bareDungeon, '.');
			resistanceMap = DungeonUtility.generateResistances(dungeon);
			colorInd = DungeonUtility.generatePaletteIndices(dungeon);
			bgColorInd = DungeonUtility.generateBGPaletteIndices(dungeon);
			Player.instance().setPos(dungeonGenerator.utility.randomCell(placement));

			bossGen = genNPCs('b', Reference.ENEMIES_COUNT_STAGE_2);
		}
		if (tries == 0) {
			throw new Exception("Could not generate Stage 2!!!");
		}
	}
	
	public static void genStage3() throws Exception {
		boolean bossGen = false;
		int tries = 10;
		while (!bossGen && tries > 0) {
			tries--;
			dungeon = dungeonGenerator.generate(TilesetType.ROUND_ROOMS_DIAGONAL_CORRIDORS);
			int count = Reference.GRID_HEIGHT * Reference.GRID_WIDTH;
			while (count > (Reference.GRID_HEIGHT * Reference.GRID_WIDTH * 2) / 3) {
				dungeon = dungeonGenerator.generate(TilesetType.ROUND_ROOMS_DIAGONAL_CORRIDORS);
				count = 0;
				for (int i = 0; i < dungeon.length; i++) {
					for (int j = 0; j < dungeon[i].length; j++) {
						count += (dungeon[i][j] == '#' ? 1 : 0);
					}
				}
			}
			bareDungeon = dungeonGenerator.getBareDungeon();
			placement = CoordPacker.pack(bareDungeon, '.');
			resistanceMap = DungeonUtility.generateResistances(dungeon);
			colorInd = DungeonUtility.generatePaletteIndices(dungeon);
			bgColorInd = DungeonUtility.generateBGPaletteIndices(dungeon);
			Player.instance().setPos(dungeonGenerator.utility.randomCell(placement));

			bossGen = genNPCs('c', Reference.ENEMIES_COUNT_STAGE_3);
		}
		if (tries == 0) {
			throw new Exception("Could not generate Stage 3!!!");
		}
	}

	public static void genStage4() throws Exception {
		boolean bossGen = false;
		int tries = 10;
		while (!bossGen && tries > 0) {
			tries--;
			dungeon = dungeonGenerator.generate(TilesetType.MAZE_A);
			int count = Reference.GRID_HEIGHT * Reference.GRID_WIDTH;
			while (count > (Reference.GRID_HEIGHT * Reference.GRID_WIDTH * 2) / 3) {
				dungeon = dungeonGenerator.generate(TilesetType.MAZE_A);
				count = 0;
				for (int i = 0; i < dungeon.length; i++) {
					for (int j = 0; j < dungeon[i].length; j++) {
						count += (dungeon[i][j] == '#' ? 1 : 0);
					}
				}
			}
			bareDungeon = dungeonGenerator.getBareDungeon();
			placement = CoordPacker.pack(bareDungeon, '.');
			resistanceMap = DungeonUtility.generateResistances(dungeon);
			colorInd = DungeonUtility.generatePaletteIndices(dungeon);
			bgColorInd = DungeonUtility.generateBGPaletteIndices(dungeon);
			Player.instance().setPos(dungeonGenerator.utility.randomCell(placement));

			bossGen = genNPCs('d', Reference.ENEMIES_COUNT_STAGE_4);
		}
		if (tries == 0) {
			throw new Exception("Could not generate Stage 3!!!");
		}
	}

	public static boolean genNPCs(char type, int count) {
		Player p = Player.instance();
		npc = new ArrayList<NPC>();
		int i = 0;
		while (i < count) {
			NPC newNpc = NPC.createNPC(type, false);
			newNpc.setPos(dungeonGenerator.utility.randomCell(placement));
			boolean canPlace = true;
			if (p.getPos().x == newNpc.getPos().x && p.getPos().y == newNpc.getPos().y) {
				canPlace = false;
			}
			for (NPC n : npc) {
				if (n.getPos().x == newNpc.getPos().x && n.getPos().y == newNpc.getPos().y) {
					canPlace = false;
				}
			}
			if (canPlace) {
				i++;
				npc.add(newNpc);
			}
		}
		int tries = 20;
		DijkstraMap bossDistance = new DijkstraMap(bareDungeon, Measurement.MANHATTAN);
		while (tries > 0) {
			tries--;
			NPC boss = NPC.createNPC(type, true);
			boss.setPos(dungeonGenerator.utility.randomCell(placement));
			boolean canPlace = true;
			for (NPC n : npc) {
				if (n.getPos().x == boss.getPos().x && n.getPos().y == boss.getPos().y) {
					canPlace = false;
				}
			}
			if (bossDistance.findPath(100, null, null, boss.getPos(), p.getPos()).size() < 30) {
				canPlace = false;
			}
			if (canPlace) {
				npc.add(boss);
				break;
			}
		}

		NPC.npcPathfinder = new DijkstraMap(bareDungeon, Measurement.MANHATTAN, rng);

		return (tries > 0 ? true : false);
	}

	public static void genExit() {
		Coord exit = dungeonGenerator.utility.randomCell(placement);
		bareDungeon[exit.x][exit.y] = '>';
		dungeon[exit.x][exit.y] = '>';
	}

	public static void genNextLevel() {
		try {
			if (currentStage == 1) {
				genStage2();
			}
			if (currentStage == 2) {
				genStage3();
			}
			if (currentStage == 3) {
				genStage4();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Game.application.exit();
		}
		currentStage++;
	}
}
