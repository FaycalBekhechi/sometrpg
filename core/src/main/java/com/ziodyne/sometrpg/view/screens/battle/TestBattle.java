package com.ziodyne.sometrpg.view.screens.battle;

import au.com.ds.ef.EasyFlow;
import au.com.ds.ef.StatefulContext;
import au.com.ds.ef.call.ExecutionErrorHandler;
import au.com.ds.ef.err.ExecutionError;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;
import com.artemis.Entity;
import com.artemis.managers.TagManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
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
import com.google.inject.Inject;
import com.ziodyne.sometrpg.logging.GdxLogger;
import com.ziodyne.sometrpg.logging.Logger;
import com.ziodyne.sometrpg.logic.loader.models.AnimationSpec;
import com.ziodyne.sometrpg.logic.loader.models.SpriteSheet;
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
import com.ziodyne.sometrpg.logic.navigation.AStarPathfinder;
import com.ziodyne.sometrpg.logic.navigation.BattleMapPathfindingStrategy;
import com.ziodyne.sometrpg.logic.navigation.Pathfinder;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.AnimationType;
import com.ziodyne.sometrpg.view.Director;
import com.ziodyne.sometrpg.view.TiledMapUtils;
import com.ziodyne.sometrpg.view.assets.AssetBundleLoader;
import com.ziodyne.sometrpg.view.assets.BattleLoader;
import com.ziodyne.sometrpg.view.assets.GameSpec;
import com.ziodyne.sometrpg.view.assets.GameSpecLoader;
import com.ziodyne.sometrpg.view.assets.MapLoader;
import com.ziodyne.sometrpg.view.assets.SpriteSheetAssetLoader;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.Sprite;
import com.ziodyne.sometrpg.view.entities.UnitEntityAnimation;
import com.ziodyne.sometrpg.view.input.BattleMapController;
import com.ziodyne.sometrpg.view.screens.battle.state.*;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.AttackConfirmationListener;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.AttackTargetSelectionListener;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.PlayerTurnListener;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.SelectingMoveLocation;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.UnitActionSelectListener;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.UnitAttackingListener;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.UnitMoving;
import com.ziodyne.sometrpg.view.screens.debug.ModelTestUtils;
import com.ziodyne.sometrpg.view.systems.AnimationKeyFrameSystem;
import com.ziodyne.sometrpg.view.systems.BattleAnimationSwitchSystem;
import com.ziodyne.sometrpg.view.systems.BattleUnitDeathSystem;
import com.ziodyne.sometrpg.view.systems.DeathFadeSystem;
import com.ziodyne.sometrpg.view.systems.MapHoverSelectorUpdateSystem;
import com.ziodyne.sometrpg.view.systems.MapMovementOverlayRenderer;
import com.ziodyne.sometrpg.view.systems.MapOverlayRenderSystem;
import com.ziodyne.sometrpg.view.systems.SpriteRenderSystem;
import com.ziodyne.sometrpg.view.systems.StageRenderSystem;
import com.ziodyne.sometrpg.view.systems.StageUpdateSystem;
import com.ziodyne.sometrpg.view.systems.TiledMapRenderSystem;
import com.ziodyne.sometrpg.view.systems.TimedProcessRunnerSystem;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
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
  private TweenAccessor<Position> positionTweenAccessor;
  private TweenAccessor<Sprite> spriteTweenAccessor;
  private TweenAccessor<Camera> cameraTweenAccessor;
  private SpriteRenderSystem.Factory spriteRendererFactory;
  private BattleMapController.Factory mapControllerFactory;
  private PlayerTurnListener.Factory turnListenerFactory;
  private AssetBundleLoader bundleLoader;
  private boolean initialized;
  private Pathfinder<GridPoint2> pathfinder;

  @Inject
  TestBattle(Director director, TweenManager tweenManager, TweenAccessor<Camera> cameraTweenAccessor,
             SpriteRenderSystem.Factory spriteRendererFactory, BattleMapController.Factory mapControllerFactory,
             TweenAccessor<Sprite> spriteTweenAccessor, AssetBundleLoader.Factory bundleLoaderFactory,
             PlayerTurnListener.Factory turnListenerFactory, TweenAccessor<Position> positionTweenAccessor) {
    super(director, new OrthographicCamera(), 32f);

    this.tweenManager = tweenManager;
    this.turnListenerFactory = turnListenerFactory;
    this.cameraTweenAccessor = cameraTweenAccessor;
    this.positionTweenAccessor = positionTweenAccessor;
    this.spriteRendererFactory = spriteRendererFactory;
    this.spriteTweenAccessor = spriteTweenAccessor;
    this.mapControllerFactory = mapControllerFactory;
    this.bundleLoader = bundleLoaderFactory.create(assetManager, "data/test.bundle");

    assetManager.setLoader(TiledMap.class, new TmxMapLoader());

    FileHandleResolver resolver = new InternalFileHandleResolver();
    assetManager.setLoader(BattleMap.class, new MapLoader(resolver));
    assetManager.setLoader(SomeTRPGBattle.class, new BattleLoader(resolver));
    assetManager.setLoader(GameSpec.class, new GameSpecLoader(resolver));
    assetManager.setLoader(SpriteSheet.class, new SpriteSheetAssetLoader(resolver));

    try {
      bundleLoader.load();
    } catch (IOException e) {
      logger.error("Failed to load bundle file.", e);
    }
  }

  private void initalizeBattle() {
    logger.log("Initializing battle.");

    camera.setToOrtho(false, 1600, 900);

    Tween.registerAccessor(Camera.class, cameraTweenAccessor);
    Tween.registerAccessor(Sprite.class, spriteTweenAccessor);
    Tween.registerAccessor(Position.class, positionTweenAccessor);

    TiledMap tiledMap = assetManager.get("maps/test/test.tmx");
    TiledMapTileLayer tileLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
    mapBoundingRect = new Rectangle(0, 0, (tileLayer.getWidth()-1) * gridSquareSize, (tileLayer.getHeight()-1) * gridSquareSize);

    SpriteRenderSystem spriteRenderSystem = spriteRendererFactory.create(camera);

    mapRenderSystem = new TiledMapRenderSystem(camera);
    mapSelectorUpdateSystem = new MapHoverSelectorUpdateSystem(world, camera, mapBoundingRect, 32);

    battle = initBattle(tiledMap);
    initUnitEntities();

    world.setSystem(new BattleUnitDeathSystem());
    world.setSystem(new AnimationKeyFrameSystem());
    world.setSystem(new DeathFadeSystem(tweenManager));
    world.setSystem(mapSelectorUpdateSystem);
    world.setSystem(new StageUpdateSystem());
    world.setSystem(new BattleAnimationSwitchSystem());
    world.setSystem(new TimedProcessRunnerSystem());

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
      turnListenerFactory.create(camera, this, pathfinder),
      new UnitActionSelectListener(skin, camera, menuStage),
      new SelectingMoveLocation(this),
      new UnitMoving(this, pathfinder, map, tweenManager),
      new AttackTargetSelectionListener(this),
      new AttackConfirmationListener(skin, menuStage, camera),
      new UnitAttackingListener(this, world)
    );

    for (FlowListener<BattleContext> listener : listeners) {
      listener.bind(flow);
    }

    flow.whenError(new ExecutionErrorHandler<StatefulContext>() {
      @Override
      public void call(ExecutionError error, StatefulContext context) {
        logger.error(error.getMessage(), error.getCause());
      }
    });

    flow.start(new BattleContext(battle));

    logger.log("Battle intialized.");
  }

  private void initializeMapObjects(TiledMap tiledMap) {
    for (MapLayer layer : tiledMap.getLayers()) {
      if (!(layer instanceof TiledMapTileLayer)) {
        for (MapObject object : layer.getObjects()) {
          TextureRegion region = TiledMapUtils.getTextureRegion(object.getProperties(), tiledMap);
          if (region != null) {
            Entity entity = entityFactory.createMapObject((RectangleMapObject)object, region, 1f);
            world.addEntity(entity);
          }
        }
      }
    }
  }

  private void initUnitEntities() {
    Texture attackTexture = assetManager.get("data/mc_attackback.png");
    SpriteSheet attackSheet = assetManager.get("data/mc_attackback.json");

    Texture idleTexture = assetManager.get("data/idle_sheet.png");
    SpriteSheet idleSheet = assetManager.get("data/idle_sheet.json");

    Texture dodgeTexture = assetManager.get("data/mc_dodgefront.png");
    SpriteSheet dodgeSheet = assetManager.get("data/mc_dodgefront.json");

    Texture runTexture = assetManager.get("data/mc_run.png");
    SpriteSheet runSheet = assetManager.get("data/mc_run.json");

    List<AnimationSpec> specs = new ArrayList<>();
    specs.addAll(idleSheet.getAnimationSpecs().values());

    int total = 0;
    for (int i = 0; i < map.getWidth(); i++) {
      for (int j = 0; j < map.getHeight(); j++) {
        Tile tile = map.getTile(i, j);
        Combatant combatant = tile.getCombatant();
        if (combatant != null) {
          Character character = combatant.getCharacter();
          if (character.getName().endsWith("_enemy")) {
            registerEnemy(combatant);
          } else {

            AnimationSpec idleSpec = specs.get(total % specs.size());
            AnimationSpec attackSpec = new ArrayList<>(attackSheet.getAnimationSpecs().values()).get(0);
            AnimationSpec dodgeSpec = new ArrayList<>(dodgeSheet.getAnimationSpecs().values()).get(0);

            total++;
            Set<UnitEntityAnimation> anims = new HashSet<>();
            anims.add(new UnitEntityAnimation(idleTexture, AnimationType.IDLE, idleSpec, idleSheet.getGridSize()));
            anims.add(new UnitEntityAnimation(attackTexture, AnimationType.ATTACK, attackSpec, attackSheet.getGridSize()));
            anims.add(new UnitEntityAnimation(dodgeTexture, AnimationType.DODGE, dodgeSpec, dodgeSheet.getGridSize()));


            Map<String, AnimationSpec> runSpecs = runSheet.getAnimationSpecs();
            AnimationSpec south = runSpecs.get("run_south");
            AnimationSpec north = runSpecs.get("run_north");
            AnimationSpec east = runSpecs.get("run_east");
            AnimationSpec west = runSpecs.get("run_west");

            int size = runSheet.getGridSize();
            anims.addAll(Lists.newArrayList(
              new UnitEntityAnimation(runTexture, AnimationType.RUN_SOUTH, south, size),
              new UnitEntityAnimation(runTexture, AnimationType.RUN_NORTH, north, size),
              new UnitEntityAnimation(runTexture, AnimationType.RUN_WEST, west, size),
              new UnitEntityAnimation(runTexture, AnimationType.RUN_EAST, east, size)
            ));

            Entity unitEntity = entityFactory.createAnimatedUnit(map, combatant, anims);
            registerUnitEntity(character, unitEntity);
          }
        }
      }
    }
  }

  private void registerEnemy(Combatant combatant) {
    String name = combatant.getCharacter().getName();
    Texture enemyTex = assetManager.get("data/enemies_fuckit.png");
    SpriteSheet enemySheet = assetManager.get("data/enemies_idle.json");
    Map<String, AnimationSpec> specsByName = enemySheet.getAnimationSpecs();
    Set<UnitEntityAnimation> anims = new HashSet<>();
    int gridSize = enemySheet.getGridSize();

    AnimationSpec idleSpec = specsByName.get(StringUtils.substringBefore(name, "_") + "_idle");
    anims.add(new UnitEntityAnimation(enemyTex, AnimationType.IDLE, idleSpec, gridSize));
    Entity entity = entityFactory.createAnimatedUnit(map, combatant, anims);

    registerUnitEntity(combatant.getCharacter(), entity);
  }

  private SomeTRPGBattle initBattle(TiledMap map) {

    TiledMapTileLayer tileLayer = (TiledMapTileLayer)map.getLayers().get(0);

    Combatant player = new Combatant(new Character(ModelTestUtils.homogeneousStats(40), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(20), "Test3x"));
    Combatant halb = new Combatant(new Character(ModelTestUtils.homogeneousStats(40), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(20), "Test"));
    Combatant swordboard = new Combatant(new Character(ModelTestUtils.homogeneousStats(40), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(20), "Test"));
    Combatant strike = new Combatant(new Character(ModelTestUtils.homogeneousStats(40), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(20), "Test"));
    Combatant fire = new Combatant(new Character(ModelTestUtils.homogeneousStats(40), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(20), "Test"));
    Combatant mace = new Combatant(new Character(ModelTestUtils.homogeneousStats(40), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(20), "Test"));

    Combatant swordbow = new Combatant(new Character(ModelTestUtils.homogeneousStats(40), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(20), "swordbow_enemy"));
    Combatant guts = new Combatant(new Character(ModelTestUtils.homogeneousStats(40), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(20), "guts_enemy"));
    Combatant speedy = new Combatant(new Character(ModelTestUtils.homogeneousStats(40), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(20), "speedy_enemy"));

    Army playerArmy = new Army("Greil Mercenaries", ArmyType.PLAYER);
    playerArmy.addCombatant(player);

    Army enemyArmy = new Army("Fail Brigade", ArmyType.ENEMY);
    enemyArmy.addCombatant(halb);
    enemyArmy.addCombatant(swordboard);
    enemyArmy.addCombatant(strike);
    enemyArmy.addCombatant(fire);
    enemyArmy.addCombatant(mace);
    enemyArmy.addCombatant(swordbow);
    enemyArmy.addCombatant(guts);
    enemyArmy.addCombatant(speedy);

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
    battleMap.addUnit(halb, 10, 10);
    battleMap.addUnit(strike, 5, 5);
    battleMap.addUnit(mace, 2, 6);
    battleMap.addUnit(fire, 7, 9);
    battleMap.addUnit(swordbow, 0, 0);
    battleMap.addUnit(guts, 3, 6);
    battleMap.addUnit(speedy, 6, 6);
    this.map = battleMap;

    this.pathfinder = new AStarPathfinder<>(new BattleMapPathfindingStrategy(battleMap));

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
