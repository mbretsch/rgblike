package net.mbretsch.rgblike.game;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import net.mbretsch.rgblike.character.NPC;
import net.mbretsch.rgblike.character.Player;
import net.mbretsch.rgblike.combat.Attack;
import net.mbretsch.rgblike.dungeon.DunGen;
import net.mbretsch.rgblike.item.Item;
import net.mbretsch.rgblike.item.ItemGenerator;
import net.mbretsch.rgblike.main.Game;
import net.mbretsch.rgblike.reference.Reference;
import net.mbretsch.rgblike.squidlib.SquidMessageBoxWithBackground;
import squidpony.panel.IColoredString;
import squidpony.squidai.DijkstraMap;
import squidpony.squidgrid.FOV;
import squidpony.squidgrid.gui.gdx.DefaultResources;
import squidpony.squidgrid.gui.gdx.SquidInput;
import squidpony.squidgrid.gui.gdx.SquidLayers;
import squidpony.squidgrid.gui.gdx.SquidMessageBox;
import squidpony.squidgrid.gui.gdx.SquidPanel;
import squidpony.squidgrid.gui.gdx.TextCellFactory;
import squidpony.squidmath.Coord;

public class RGBlike extends ApplicationAdapter {

	private SpriteBatch spriteBatch;
	private SquidLayers squidLayers;

	private Stage stage;

	private FOV fov;
	private double[][] fovMap;

	private List<NPC> visibleNpcs;

	private List<Item> itemsOnGround;
	private boolean isItemPickup = false;
	private Item pickupItem;

	private List<List<Coord>> attackPaths;
	private int attackPathIndex = 0;
	private DijkstraMap playerToMonster;
	private boolean isAttacking = false;
	private Attack attack;
	
	private boolean gameWon = false;
	private boolean showStartScreen = true;

	private SquidInput input;

	public static ShapeRenderer shapeRenderer;

	public static SquidMessageBox logMessages;
	private SquidMessageBox barsBox;
	private SquidPanel attackBox;
	private SquidPanel deathBox;
	private SquidMessageBoxWithBackground itemBox;
	private SquidMessageBoxWithBackground wonBox;
	private SquidMessageBoxWithBackground startBox;

	@Override
	public void create() {

		spriteBatch = new SpriteBatch();

		stage = new Stage(new StretchViewport((Reference.GRID_WIDTH) * Reference.CELL_WIDTH,
				(Reference.GRID_HEIGHT + Reference.HUD_HEIGHT) * Reference.CELL_HEIGHT), spriteBatch);

		squidLayers = new SquidLayers(Reference.GRID_WIDTH, Reference.GRID_HEIGHT + Reference.HUD_HEIGHT,
				Reference.CELL_WIDTH, Reference.CELL_HEIGHT, DefaultResources.getStretchableFont());
		squidLayers.setAnimationDuration(0.03f);
		squidLayers.setPosition(0, 0);

		DunGen.genDungeon();
		fov = new FOV();

		calcFov();

		playerToMonster = new DijkstraMap(DunGen.bareDungeon, DijkstraMap.Measurement.EUCLIDEAN);

		calcVisibleNPC();

		itemsOnGround = new ArrayList<Item>();

		input = new SquidInput(new SquidInput.KeyHandler() {

			@Override
			public void handle(char key, boolean alt, boolean ctrl, boolean shift) {
				if (showStartScreen) {
					if (key == SquidInput.ESCAPE) {
						Game.application.exit();
					}
					if (key == SquidInput.ENTER) {
						startBox.setVisible(false);
						showStartScreen = false;
					} else {
						return;
					}
				}
				
				if (Player.instance().isDead()) {
					if (key == SquidInput.ESCAPE) {
						Game.application.exit();
					}
				} else {
					switch (key) {
					case SquidInput.UP_ARROW: {
						if (isItemPickup || gameWon) {
							break;
						}
						if (!isAttacking) {
							move(0, -1);
						} else {
							attack.incStr();
						}
						break;
					}
					case SquidInput.DOWN_ARROW: {
						if (isItemPickup || gameWon) {
							break;
						}
						if (!isAttacking) {
							move(0, 1);
						} else {
							attack.decStr();
						}
						break;
					}
					case SquidInput.LEFT_ARROW: {
						if (isItemPickup || gameWon) {
							break;
						}
						if (!isAttacking) {
							move(-1, 0);
						} else {
							attack.prevCol();
						}
						break;
					}
					case SquidInput.RIGHT_ARROW: {
						if (isItemPickup || gameWon) {
							break;
						}
						if (!isAttacking) {
							move(1, 0);
						} else {
							attack.nextCol();
						}
						break;
					}
					case 'a':
					case 'A': {
						if (isItemPickup || gameWon) {
							break;
						}
						if (visibleNpcs.size() > 0) {
							attackPaths = new ArrayList<List<Coord>>();
							for (NPC n : visibleNpcs) {
								attackPaths.add(playerToMonster.findPath(100, null, null, Player.instance().getPos(),
										n.getPos()));
							}
							isAttacking = true;
							if (attackPathIndex < visibleNpcs.size() - 1) {
								attackPathIndex++;
							} else {
								attackPathIndex = 0;
							}
							if (attack == null) {
								attack = new Attack(visibleNpcs.get(attackPathIndex));
							} else {
								attack.setEnemy(visibleNpcs.get(attackPathIndex));
							}
							attackBox.setVisible(true);
							Coord boxPos = getAttackBoxPos();
							attackBox.setOffsets(Reference.CELL_WIDTH * boxPos.x,
									(Reference.GRID_HEIGHT + Reference.HUD_HEIGHT - boxPos.y) * Reference.CELL_HEIGHT);

						}
						break;
					}
					case ' ': {
						if (isItemPickup || gameWon) {
							break;
						}
						calcNextTurn();
						break;
					}
					case SquidInput.ENTER: {
						if (isAttacking) {
							logMessages.appendMessage(attack.confirm());
						}
					}
					case 'q':
					case 'Q': {
						isAttacking = false;
						attackPaths = null;
						attackPathIndex = 0;
						attackBox.setVisible(false);
						attack = null;
						break;
					}
					case 'p':
					case 'P': {
						if (!isItemPickup) {
							break;
						}
						itemBox.setVisible(false);
						isItemPickup = false;
						pickupItem = null;
						break;
					}
					case 't':
					case 'T': {
						if (!isItemPickup) {
							break;
						}
						itemBox.setVisible(false);
						isItemPickup = false;
						pickItemUp(pickupItem);
						pickupItem = null;
						break;
					}
					case SquidInput.ESCAPE: {
						Game.application.exit();
					}
					}
				}
			}
		});

		Gdx.input.setInputProcessor(new InputMultiplexer(stage, input));

		shapeRenderer = new ShapeRenderer();
		TextCellFactory tcf = DefaultResources.getStretchableFont().setSmoothingMultiplier(2f)
				.width(Reference.CELL_WIDTH).height(Reference.CELL_HEIGHT).initBySize();
		logMessages = new SquidMessageBox(50, 7, tcf) {

			@Override
			public void appendMessage(IColoredString<Color> message) {
				IColoredString.Impl<Color> truncated = new IColoredString.Impl<>();
				truncated.append(message);
				truncated.setLength(gridWidth - 2);
				messages.add(truncated);
				messageIndex = messages.size() - 1;
			}

		};
		logMessages.setOffsets(30 * Reference.CELL_WIDTH, 0);

		barsBox = new SquidMessageBox(30, 7, tcf);

		attackBox = new SquidMessageBoxWithBackground(46, 5, tcf);

		attackBox.setVisible(false);
		attackBox.setOffsets(Reference.CELL_WIDTH * 25, Reference.CELL_HEIGHT * (Reference.HUD_HEIGHT + 5));

		deathBox = new SquidMessageBoxWithBackground(25, 3, tcf);
		deathBox.setVisible(false);
		deathBox.setOffsets((Reference.GRID_WIDTH / 2 - 12) * Reference.CELL_WIDTH,
				(Reference.HUD_HEIGHT + Reference.GRID_HEIGHT / 2 - 1) * Reference.CELL_HEIGHT);

		itemBox = new SquidMessageBoxWithBackground(20, 4, tcf);
		itemBox.setVisible(false);
		
		wonBox = new SquidMessageBoxWithBackground(38, 4, tcf);
		wonBox.setVisible(false);
		wonBox.setOffsets((Reference.GRID_WIDTH / 2 - 19) * Reference.CELL_WIDTH,
				(Reference.HUD_HEIGHT + Reference.GRID_HEIGHT / 2 - 1) * Reference.CELL_HEIGHT);
		
		startBox = new SquidMessageBoxWithBackground(75, 15, tcf);
		startBox.setOffsets(Reference.CELL_WIDTH * 3, (Reference.HUD_HEIGHT +5) * Reference.CELL_HEIGHT);
		startBox.appendWrappingMessage("RGBlike was programmed during 7DRL 2017. It is a simple Rougelike with a unique game mechanic. The main goal is to keep your 3 colors balanced and use them to get enemies out of balance. Once one of your colors reaches 0, you have lost.");
		startBox.appendWrappingMessage("The game uses Java + Squidlib, is tested in Windows, but should work under Linux.");
		startBox.appendWrappingMessage("Attacks are possible on every enemy in your FOV (use <A> to attack). Select color and strength to attack with.");
		startBox.appendWrappingMessage("After an enemy died, he drops an item. If you walk into the item you can pick it up or pass. Items currently equiped will be destroyed. If you pass, the item is destroyed aswell.");
		startBox.appendWrappingMessage("The goal is to find and kill the boss. After his death a downward stair will appear on the map to get you to the next stage. Press <ENTER>");
		
		stage.addActor(squidLayers);
		squidLayers.addActor(attackBox);
		squidLayers.addActor(barsBox);
		squidLayers.addActor(logMessages);
		squidLayers.addActor(deathBox);
		squidLayers.addActor(itemBox);
		squidLayers.addActor(wonBox);
		squidLayers.addActor(startBox);

		squidLayers.addActor(new Actor() {

			@Override
			public void draw(Batch batch, float parentAlpha) {
				batch.end();
				shapeRenderer.begin(ShapeType.Filled);
				shapeRenderer.setColor(Color.RED);
				shapeRenderer.rect(Reference.CELL_WIDTH, ((Reference.HUD_HEIGHT - 3)) * 20 + 5,
						Player.instance().getRedBarWidth(), Reference.CELL_HEIGHT - 8);
				shapeRenderer.setColor(Color.GREEN);
				shapeRenderer.rect(Reference.CELL_WIDTH, ((Reference.HUD_HEIGHT - 5)) * 20 + 12,
						Player.instance().getGreenBarWidth(), Reference.CELL_HEIGHT - 8);
				shapeRenderer.setColor(Color.BLUE);
				shapeRenderer.rect(Reference.CELL_WIDTH, ((Reference.HUD_HEIGHT - 7)) * 20 + 19,
						Player.instance().getBlueBarWidth(), Reference.CELL_HEIGHT - 8);
				shapeRenderer.end();
				batch.begin();
			}

			@Override
			public Actor hit(float x, float y, boolean touchable) {
				return null;
			}

		});

	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(Reference.BG_COLOR.r / 255, Reference.BG_COLOR.g / 255, Reference.BG_COLOR.b / 255, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);

		this.drawMap();
		this.drawHud();

		if (input.hasNext()) {
			input.next();
		}

		stage.draw();
		stage.act();
	}

	private void drawMap() {

		for (int i = 0; i < Reference.GRID_WIDTH; i++) {
			for (int j = 0; j < Reference.GRID_HEIGHT; j++) {
				squidLayers.put(i, j, DunGen.dungeon[i][j], DunGen.colorInd[i][j], DunGen.bgColorInd[i][j], -100);
			}
		}

		squidLayers.put(Player.instance().getPos().x, Player.instance().getPos().y, '@',
				Player.instance().getCharColor());

		for (int i = 0; i < Reference.GRID_WIDTH; i++) {
			for (int j = 0; j < Reference.GRID_HEIGHT; j++) {
				if (fovMap[i][j] > 0) {
					squidLayers.highlight(i, j, new Double(fovMap[i][j] * 85).intValue());
				} else {
					squidLayers.highlight(i, j, -50);
				}
			}
		}

		for (Item item : itemsOnGround) {
			squidLayers.put(item.getPos().x, item.getPos().y, '*', item.getGColor().col());
		}

		for (NPC npc : visibleNpcs) {
			squidLayers.put(npc.getPos().x, npc.getPos().y, npc.getChar(), npc.getCharColor());
		}
		if (attackPaths != null && attackPaths.size() > 0) {
			for (Coord ap : attackPaths.get(attackPathIndex)) {
				squidLayers.highlight(ap.x, ap.y, -100);
			}
		}

		if (attack != null) {
			attackBox.put(1, 1, attack.getColoredString());
			attackBox.put(1, 2, "<A> next Enemy, <ARROW_KEYS> attack settings");
			attackBox.put(1, 3, "<Enter> to confirm, <Q> to cancel");
		}

		if (deathBox.isVisible()) {
			deathBox.put(2, 1, "You died. Press <ESC>");
		}

		if (isItemPickup) {
			IColoredString<Color> str = new IColoredString.Impl<Color>("Item: ", Color.WHITE);
			str.append("[" + pickupItem.getItemString() + "]", pickupItem.getGColor().col());
			itemBox.put(1, 1, str);
			itemBox.put(1, 2, "<T>ake or <P>ass");
		}
		
		if (gameWon) {
			wonBox.put(1,1, "Congratulations!!! You Won the Game!");
			wonBox.put(1,2, "Thanks for playing! Press <ESC>");
		}
	}

	private void drawHud() {

		squidLayers.putString(12, Reference.GRID_HEIGHT + 1, this.getAlignedColorString(Player.instance().getRed()));
		squidLayers.putString(12, Reference.GRID_HEIGHT + 3, this.getAlignedColorString(Player.instance().getGreen()));
		squidLayers.putString(12, Reference.GRID_HEIGHT + 5, this.getAlignedColorString(Player.instance().getBlue()));

		if (Player.instance().getRedItem() != null) {
			squidLayers.putString(18, Reference.GRID_HEIGHT + 1,
					String.format("%11s", "[" + Player.instance().getRedItem().getItemString() + "]"));
		} else {
			squidLayers.putString(18, Reference.GRID_HEIGHT + 1, String.format("%11s", "[none]"));
		}
		if (Player.instance().getGreenItem() != null) {
			squidLayers.putString(18, Reference.GRID_HEIGHT + 3,
					String.format("%11s", "[" + Player.instance().getGreenItem().getItemString() + "]"));
		} else {
			squidLayers.putString(18, Reference.GRID_HEIGHT + 3, String.format("%11s", "[none]"));
		}
		if (Player.instance().getBlueItem() != null) {
			squidLayers.putString(18, Reference.GRID_HEIGHT + 5,
					String.format("%11s", "[" + Player.instance().getBlueItem().getItemString() + "]"));
		} else {
			squidLayers.putString(18, Reference.GRID_HEIGHT + 5, String.format("%11s", "[none]"));
		}

	}

	private void move(int x, int y) {
		Coord newPos = Coord.get(Player.instance().getPos().x + x, Player.instance().getPos().y + y);
		if (DunGen.bareDungeon[newPos.x][newPos.y] != '#') {
			boolean npcInPlace = false;
			for (int i = 0; i < DunGen.npc.size(); i++) {
				if (DunGen.npc.get(i).getPos().equals(newPos)) {
					npcInPlace = true;
				}
			}
			if (!npcInPlace) {
				Player.instance().setPos(Player.instance().getPos().translate(x, y));
			}
		}

		for (int i = 0; i < itemsOnGround.size(); i++) {
			if (itemsOnGround.get(i).getPos().x == Player.instance().getPos().x
					&& itemsOnGround.get(i).getPos().y == Player.instance().getPos().y) {
				isItemPickup = true;
				itemBox.setVisible(true);
				Coord boxPos = getItemBoxPos();
				itemBox.setOffsets(Reference.CELL_WIDTH * boxPos.x,
						(Reference.GRID_HEIGHT + Reference.HUD_HEIGHT - boxPos.y) * Reference.CELL_HEIGHT);
				pickupItem = itemsOnGround.get(i);
				itemsOnGround.remove(i);
			}
		}

		if (DunGen.bareDungeon[Player.instance().getPos().x][Player.instance().getPos().y] == '>') {
			DunGen.genNextLevel();
			itemsOnGround.clear();
			pickupItem = null;
			
			isAttacking = false;
			attackPaths = null;
			attackPathIndex = 0;
			attackBox.setVisible(false);
			attack = null;
			
			playerToMonster = new DijkstraMap(DunGen.bareDungeon, DijkstraMap.Measurement.EUCLIDEAN);
		}

		calcFov();

		calcNextTurn();
		
	}

	private void calcVisibleNPC() {
		visibleNpcs = new ArrayList<NPC>();
		for (int i = 0; i < Reference.GRID_WIDTH; i++) {
			for (int j = 0; j < Reference.GRID_HEIGHT; j++) {
				if (fovMap[i][j] > 0) {
					for (int k = 0; k < DunGen.npc.size(); k++) {
						NPC m = DunGen.npc.get(k);
						if (m.getPos().x == i && m.getPos().y == j) {
							visibleNpcs.add(m);
							m.setActive(true);
						}
					}
				}
			}
		}
	}

	private void calcFov() {
		fovMap = fov.calculateFOV(DunGen.resistanceMap, Player.instance().getPos().x, Player.instance().getPos().y, 12);
	}

	private Coord getAttackBoxPos() {
		if (isAttacking) {
			List<Coord> path = attackPaths.get(attackPathIndex);
			Coord first = path.get(0);
			Coord last = path.get(path.size() - 1);
			int x = (first.x + last.x) / 2;
			x -= 23;
			if (x < 1) {
				x = 1;
			}
			if ((x + 46) > Reference.GRID_WIDTH) {
				x = Reference.GRID_WIDTH - 47;
			}
			int maxy = Math.max(first.y, last.y);
			int y = 0;
			if ((maxy + 7) > Reference.GRID_HEIGHT) {
				y = Math.min(first.y, last.y) - 1;
			} else {
				y = maxy + 7;
			}
			return Coord.get(x, y);
		}
		return Coord.get(0, 0);
	}

	private Coord getItemBoxPos() {
		if (isItemPickup) {
			int x = Player.instance().getPos().x;
			x -= 10;
			if (x < 1) {
				x = 1;
			}
			if ((x + 10) > Reference.GRID_WIDTH) {
				x = Reference.GRID_WIDTH - 20;
			}
			int y = Player.instance().getPos().y;
			if ((y + 5) > Reference.GRID_HEIGHT) {
				y = y - 5;
			} else {
				y += 5;
			}
			return Coord.get(x, y);

		}
		return Coord.get(0, 0);
	}

	private String getAlignedColorString(int value) {
		return String.format("%5d", value);
	}

	private void calcNextTurn() {
		Player.instance().calcBalance();
		if (Player.instance().isDead()) {
			deathBox.setVisible(true);
		}

		for (int i = 0; i < DunGen.npc.size(); i++) {
			NPC n = DunGen.npc.get(i);
			if (n.isActive()) {
				n.calcBalance();
				if (n.isDead()) {
					IColoredString<Color> str = new IColoredString.Impl<Color>(n.getName(), n.getCharColor());
					str.append(" has died", Color.WHITE);
					logMessages.appendMessage(str);
					DunGen.npc.remove(i--);
					itemsOnGround.add(ItemGenerator.generateItem(n));
					if (n.isBoss()) {
						if (DunGen.currentStage < 4) {
							DunGen.genExit();
						} else {
							gameWon = true;
							wonBox.setVisible(true);
						}
					}
				}
			}
			n.calcNPCTurn();
		}

		calcVisibleNPC();
		this.render();

	}

	private void pickItemUp(Item item) {
		Item oldItem;
		switch (item.getGColor()) {
		case RED: {
			oldItem = Player.instance().getRedItem();
			if (oldItem != null) {
				oldItem.calcItemAdjustmentDrop();
			}
			Player.instance().setRedItem(item);
			item.calcItemAdjustmentWear();
			break;
		}
		case GREEN: {
			oldItem = Player.instance().getGreenItem();
			if (oldItem != null) {
				oldItem.calcItemAdjustmentDrop();
			}
			Player.instance().setGreenItem(item);
			item.calcItemAdjustmentWear();
			break;
		}
		case BLUE: {
			oldItem = Player.instance().getBlueItem();
			if (oldItem != null) {
				oldItem.calcItemAdjustmentDrop();
			}
			Player.instance().setBlueItem(item);
			item.calcItemAdjustmentWear();
			break;
		}
		}
	}

}
