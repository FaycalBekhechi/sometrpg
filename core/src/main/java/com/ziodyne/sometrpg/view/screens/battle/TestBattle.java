package com.ziodyne.sometrpg.view.screens.battle;

import au.com.ds.ef.EasyFlow;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;
import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.ziodyne.sometrpg.logging.GdxLogger;
import com.ziodyne.sometrpg.logging.Logger;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.SomeTRPGBattle;
import com.ziodyne.sometrpg.logic.models.battle.TerrainType;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.battle.Army;
import com.ziodyne.sometrpg.logic.models.battle.ArmyType;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.conditions.Rout;
import com.ziodyne.sometrpg.logic.models.battle.conditions.WinCondition;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.Director;
import com.ziodyne.sometrpg.view.TiledMapUtils;
import com.ziodyne.sometrpg.view.assets.AssetBundleLoader;
import com.ziodyne.sometrpg.view.assets.BattleLoader;
import com.ziodyne.sometrpg.view.assets.MapLoader;
import com.ziodyne.sometrpg.view.components.Sprite;
import com.ziodyne.sometrpg.view.input.BattleMapController;
import com.ziodyne.sometrpg.view.screens.battle.state.*;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.AttackTargetSelectionListener;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.PlayerTurnListener;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.SelectingMoveLocation;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.UnitActionSelectListener;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.UnitMoving;
import com.ziodyne.sometrpg.view.screens.debug.ModelTestUtils;
import com.ziodyne.sometrpg.view.systems.BattleUnitDeathSystem;
import com.ziodyne.sometrpg.view.systems.BattleUnitMovementSystem;
import com.ziodyne.sometrpg.view.systems.DeathFadeSystem;
import com.ziodyne.sometrpg.view.systems.MapHoverSelectorUpdateSystem;
import com.ziodyne.sometrpg.view.systems.MapMovementOverlayRenderer;
import com.ziodyne.sometrpg.view.systems.MapOverlayRenderSystem;
import com.ziodyne.sometrpg.view.systems.SpriteRenderSystem;
import com.ziodyne.sometrpg.view.systems.StageRenderSystem;
import com.ziodyne.sometrpg.view.systems.StageUpdateSystem;
import com.ziodyne.sometrpg.view.systems.TiledMapRenderSystem;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestBattle extends BattleScreen {
  private final Logger logger = new GdxLogger(TestBattle.class);
  private TweenManager tweenManager;
  private TiledMapRenderSystem mapRenderSystem;
  private MapHoverSelectorUpdateSystem mapSelectorUpdateSystem;
  private Rectangle mapBoundingRect;
  private BattleMap map;
  private TweenAccessor<Sprite> spriteTweenAccessor;
  private TweenAccessor<Camera> cameraTweenAccessor;
  private SpriteRenderSystem.Factory spriteRendererFactory;
  private BattleMapController.Factory mapControllerFactory;
  private PlayerTurnListener.Factory turnListenerFactory;
  private AssetBundleLoader bundleLoader;
  private boolean initialized;

  @Inject
  TestBattle(Director director, TweenManager tweenManager, TweenAccessor<Camera> cameraTweenAccessor,
             SpriteRenderSystem.Factory spriteRendererFactory, BattleMapController.Factory mapControllerFactory,
             TweenAccessor<Sprite> spriteTweenAccessor, AssetBundleLoader.Factory bundleLoaderFactory,
             PlayerTurnListener.Factory turnListenerFactory) {
    super(director, new OrthographicCamera(), 32f);

    this.tweenManager = tweenManager;
    this.turnListenerFactory = turnListenerFactory;
    this.cameraTweenAccessor = cameraTweenAccessor;
    this.spriteRendererFactory = spriteRendererFactory;
    this.spriteTweenAccessor = spriteTweenAccessor;
    this.mapControllerFactory = mapControllerFactory;
    this.bundleLoader = bundleLoaderFactory.create(assetManager, "data/test.bundle");

    assetManager.setLoader(TiledMap.class, new TmxMapLoader());
    assetManager.setLoader(BattleMap.class, new MapLoader(new InternalFileHandleResolver()));
    assetManager.setLoader(SomeTRPGBattle.class, new BattleLoader(new InternalFileHandleResolver()));

    try {
      bundleLoader.load();
    } catch (IOException e) {
      logger.error("Failed to load bundle file.", e);
    }
  }

  private void initalizeBattle() {
    logger.log("Initializing battle.");

    camera.setToOrtho(false, 32, 18);

    Tween.registerAccessor(Camera.class, cameraTweenAccessor);
    Tween.registerAccessor(Sprite.class, spriteTweenAccessor);

    TiledMap tiledMap = assetManager.get("maps/test/test.tmx");
    TiledMapTileLayer tileLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
    mapBoundingRect = new Rectangle(0, 0, tileLayer.getWidth()-1, tileLayer.getHeight()-1);

    SpriteRenderSystem spriteRenderSystem = spriteRendererFactory.create(camera);

    mapRenderSystem = new TiledMapRenderSystem(camera);
    mapSelectorUpdateSystem = new MapHoverSelectorUpdateSystem(world, camera, mapBoundingRect);

    battle = initBattle(tiledMap);
    initUnitEntities();

    world.setSystem(new BattleUnitDeathSystem());
    world.setSystem(new DeathFadeSystem(tweenManager));
    world.setSystem(new BattleUnitMovementSystem(map));
    world.setSystem(mapSelectorUpdateSystem);
    world.setSystem(new StageUpdateSystem());

    /**
     * Render Order:
     *   - Map Tiles
     *   - Map Movement Ranges
     *   - Grid Overlay
     *   - Background Sprites
     *   - Foreground Sprites
     *   - Menu/HUD
     */
    world.setSystem(mapRenderSystem);
    world.setSystem(new MapMovementOverlayRenderer(camera));
    world.setSystem(new MapOverlayRenderSystem(camera));
    world.setSystem(spriteRenderSystem);
    world.setSystem(new StageRenderSystem());

    world.setManager(new TagManager());

    world.initialize();


    Entity tileSelectorOverlay = entityFactory.createMapSelector();
    world.addEntity(tileSelectorOverlay);
    world.getManager(TagManager.class).register("map_hover_selector", tileSelectorOverlay);


    world.addEntity(entityFactory.createTiledMap(tiledMap, spriteBatch, gridSquareSize));
    initializeMapObjects(tiledMap);

    Entity mapGridOverlay = entityFactory.createMapGridOverlay(20, 20, 32, new GridPoint2());
    world.addEntity(mapGridOverlay);

    Entity stage = entityFactory.createStage(menuStage);
    world.addEntity(stage);


    final InputMultiplexer multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(menuStage);


    Gdx.input.setInputProcessor(multiplexer);

    initialized = true;

    EasyFlow<BattleContext> flow = BattleFlow.FLOW;
    List<? extends FlowListener<BattleContext>> listeners = Arrays.asList(
      turnListenerFactory.create(camera, this),
      new UnitActionSelectListener(skin, camera, menuStage),
      new SelectingMoveLocation(this),
      new UnitMoving(this),
      new AttackTargetSelectionListener()
    );

    for (FlowListener<BattleContext> listener : listeners) {
      listener.bind(flow);
    }

    flow.start(new BattleContext(battle));

    logger.log("Battle intialized.");
  }

  private void initializeMapObjects(TiledMap tiledMap) {
    for (MapLayer layer : tiledMap.getLayers()) {
      if (!(layer instanceof TiledMapTileLayer)) {
        for (MapObject object : layer.getObjects()) {
          TextureRegion region = TiledMapUtils.getTextureRegion(object.getProperties(), tiledMap);
          if (region != null) {
            Entity entity = entityFactory.createMapObject((RectangleMapObject)object, region, 1/gridSquareSize);
            world.addEntity(entity);
          }
        }
      }
    }
  }

  private void initUnitEntities() {
    Texture unitTexture = assetManager.get("single.png");
    for (int i = 0; i < map.getWidth(); i++) {
      for (int j = 0; j < map.getHeight(); j++) {
        Tile tile = map.getTile(i, j);
        Combatant combatant = tile.getCombatant();
        if (combatant != null) {
          Character character = combatant.getCharacter();
          Entity unitEntity = entityFactory.createUnit(map, combatant, unitTexture);
          registerUnitEntity(character, unitEntity);
        }
      }
    }
  }

  private SomeTRPGBattle initBattle(TiledMap map) {

    TiledMapTileLayer tileLayer = (TiledMapTileLayer)map.getLayers().get(0);

    Combatant player = new Combatant(new Character(ModelTestUtils.homogeneousStats(40), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(20), "Test3x"));
    Combatant enemy = new Combatant(new Character(ModelTestUtils.homogeneousStats(40), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(20), "Test"));

    Army playerArmy = new Army(Sets.newHashSet(player), "Greil Mercenaries", ArmyType.PLAYER);
    Army enemyArmy = new Army(Sets.newHashSet(enemy), "Dawn Brigade", ArmyType.ENEMY);

    Set<Tile> tiles = new HashSet<Tile>(tileLayer.getHeight());
    Map<GridPoint2, Tile> pointToTile = Maps.newHashMap();
    for (int i = 0; i < tileLayer.getWidth(); i++) {
      for (int j = 0; j < tileLayer.getHeight(); j++) {
        // TODO: Read the tile from the tile layer and get the attribute
        Tile tile = new Tile(TerrainType.GRASS, i, j);
        tiles.add(tile);
        pointToTile.put(new GridPoint2(i, j), tile);
      }
    }


    MapLayer blockingLayer = map.getLayers().get("Blocking");
    for (MapObject object : blockingLayer.getObjects()) {
      RectangleMapObject rect = (RectangleMapObject)object;
      Rectangle locationRect = rect.getRectangle();
      int x = Math.round(locationRect.x / gridSquareSize);
      int y = Math.round(locationRect.y / gridSquareSize);

      Tile tile = pointToTile.get(new GridPoint2(x, y));
      if (tile != null) {
        MapProperties props = object.getProperties();
        if (Boolean.valueOf((String)props.get("blocked"))) {
          tile.setPassable(false);
        }
      }
    }

    BattleMap battleMap = new BattleMap(tiles);
    battleMap.addUnit(player, 7, 8);
    battleMap.addUnit(enemy, tileLayer.getWidth()-1, tileLayer.getHeight()-1);
    this.map = battleMap;



    List<Army> armies = Lists.newArrayList(playerArmy, enemyArmy);
    WinCondition winCondition = new Rout();

    return new SomeTRPGBattle(battleMap, armies, winCondition);
  }

  protected void update(float delta) {
    if (initialized) {
      tweenManager.update(delta);
    } else {
      if (bundleLoader.update()) {
        initalizeBattle();
      }
    }
  }
}
