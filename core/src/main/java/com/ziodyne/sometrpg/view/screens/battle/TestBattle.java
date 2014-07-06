package com.ziodyne.sometrpg.view.screens.battle;

import au.com.ds.ef.EasyFlow;
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
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.ziodyne.sometrpg.logging.GdxLogger;
import com.ziodyne.sometrpg.logging.Logger;
import com.ziodyne.sometrpg.logic.loader.AssetUtils;
import com.ziodyne.sometrpg.logic.loader.loaders.CharactersLoader;
import com.ziodyne.sometrpg.logic.loader.TiledBattleBuilder;
import com.ziodyne.sometrpg.logic.loader.models.AnimationSpec;
import com.ziodyne.sometrpg.logic.loader.models.Characters;
import com.ziodyne.sometrpg.logic.loader.models.SpriteSheet;
import com.ziodyne.sometrpg.logic.models.SaveGameCharacterDatabase;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.SomeTRPGBattle;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.navigation.AStarPathfinder;
import com.ziodyne.sometrpg.logic.navigation.BattleMapPathfindingStrategy;
import com.ziodyne.sometrpg.logic.navigation.Pathfinder;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.util.CollectionUtils;
import com.ziodyne.sometrpg.view.AnimationType;
import com.ziodyne.sometrpg.view.Director;
import com.ziodyne.sometrpg.view.TiledMapUtils;
import com.ziodyne.sometrpg.view.assets.AssetBundleLoader;
import com.ziodyne.sometrpg.view.assets.AssetManagerRepository;
import com.ziodyne.sometrpg.view.assets.AssetRepository;
import com.ziodyne.sometrpg.view.assets.loaders.ArmiesLoader;
import com.ziodyne.sometrpg.logic.loader.models.GameSpec;
import com.ziodyne.sometrpg.view.assets.loaders.CharacterSpritesLoader;
import com.ziodyne.sometrpg.logic.loader.loaders.GameSpecLoader;
import com.ziodyne.sometrpg.view.assets.loaders.MapLoader;
import com.ziodyne.sometrpg.view.assets.loaders.SpriteSheetAssetLoader;
import com.ziodyne.sometrpg.logic.loader.models.Armies;
import com.ziodyne.sometrpg.view.assets.models.CharacterSpriteBook;
import com.ziodyne.sometrpg.view.assets.models.CharacterSprites;
import com.ziodyne.sometrpg.view.assets.models.SpriteReference;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.SpriteComponent;
import com.ziodyne.sometrpg.view.entities.EntityFactory;
import com.ziodyne.sometrpg.view.entities.UnitEntityAnimation;
import com.ziodyne.sometrpg.view.graphics.SpriteLayer;
import com.ziodyne.sometrpg.view.input.BattleMapController;
import com.ziodyne.sometrpg.view.screens.battle.eventhandlers.UnitMoveHandler;
import com.ziodyne.sometrpg.view.screens.battle.state.*;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.AttackConfirmationListener;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.AttackTargetSelectionListener;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.PlayerTurnListener;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.SelectingMoveLocation;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.UnitActionSelectListener;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.UnitAttackingListener;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.UnitMoving;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.ViewingUnitInfo;
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
import com.ziodyne.sometrpg.view.systems.ViewportSpaceSpriteRenderSystem;
import com.ziodyne.sometrpg.view.systems.VoidSpriteRenderSystem;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Nullable;

import static com.google.common.collect.Lists.newArrayList;

public class TestBattle extends BattleScreen {
  private final Logger logger = new GdxLogger(TestBattle.class);
  private TweenManager tweenManager;
  private TiledMapRenderSystem mapRenderSystem;
  private MapHoverSelectorUpdateSystem mapSelectorUpdateSystem;
  private Rectangle mapBoundingRect;
  private BattleMap map;
  private TweenAccessor<Position> positionTweenAccessor;
  private TweenAccessor<SpriteComponent> spriteTweenAccessor;
  private TweenAccessor<Camera> cameraTweenAccessor;
  private SpriteRenderSystem.Factory spriteRendererFactory;
  private BattleMapController.Factory mapControllerFactory;
  private VoidSpriteRenderSystem.Factory voidRenderSystemFactory;
  private PlayerTurnListener.Factory turnListenerFactory;
  private AssetBundleLoader bundleLoader;
  private boolean initialized;
  private Pathfinder<GridPoint2> pathfinder;

  @Inject
  TestBattle(Director director, TweenManager tweenManager, TweenAccessor<Camera> cameraTweenAccessor,
             SpriteRenderSystem.Factory spriteRendererFactory, BattleMapController.Factory mapControllerFactory,
             TweenAccessor<SpriteComponent> spriteTweenAccessor, AssetBundleLoader.Factory bundleLoaderFactory,
             PlayerTurnListener.Factory turnListenerFactory, TweenAccessor<Position> positionTweenAccessor,
             VoidSpriteRenderSystem.Factory voidRenderSystemFactory, EventBus eventBus) {
    super(director, new OrthographicCamera(), 32f, eventBus);

    camera.zoom = 0.5f;
    this.tweenManager = tweenManager;
    this.turnListenerFactory = turnListenerFactory;
    this.cameraTweenAccessor = cameraTweenAccessor;
    this.positionTweenAccessor = positionTweenAccessor;
    this.spriteRendererFactory = spriteRendererFactory;
    this.spriteTweenAccessor = spriteTweenAccessor;
    this.mapControllerFactory = mapControllerFactory;
    this.voidRenderSystemFactory = voidRenderSystemFactory;
    this.bundleLoader = bundleLoaderFactory.create(assetManager, "data/test.bundle");

    assetManager.setLoader(TiledMap.class, new TmxMapLoader());

    FileHandleResolver resolver = new InternalFileHandleResolver();
    assetManager.setLoader(BattleMap.class, new MapLoader(resolver));
    assetManager.setLoader(GameSpec.class, new GameSpecLoader(resolver));
    assetManager.setLoader(SpriteSheet.class, new SpriteSheetAssetLoader(resolver));
    assetManager.setLoader(CharacterSprites.class, new CharacterSpritesLoader(resolver));
    assetManager.setLoader(Armies.class, new ArmiesLoader(resolver));
    assetManager.setLoader(Characters.class, new CharactersLoader(resolver));

    try {
      bundleLoader.load();
    } catch (IOException e) {
      logger.error("Failed to load bundle file.", e);
    }
  }

  private void initalizeBattle() {
    logger.log("Initializing battle.");

    Tween.registerAccessor(Camera.class, cameraTweenAccessor);
    Tween.registerAccessor(SpriteComponent.class, spriteTweenAccessor);
    Tween.registerAccessor(Position.class, positionTweenAccessor);

    TiledMap tiledMap = assetManager.get("maps/chapter1.tmx");
    TiledMapTileLayer tileLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
    mapBoundingRect = new Rectangle(0, 0, (tileLayer.getWidth()-1) * gridSquareSize, (tileLayer.getHeight()-1) * gridSquareSize);

    SpriteRenderSystem spriteRenderSystem = spriteRendererFactory.create(camera);

    mapRenderSystem = new TiledMapRenderSystem(camera);
    mapSelectorUpdateSystem = new MapHoverSelectorUpdateSystem(world, viewport, mapBoundingRect, 32);

    GameSpec gameSpec = assetManager.get("data/game.json");
    Collection<Character> characters = AssetUtils.reifyCharacterSpecs(gameSpec.getCharacters());
    Armies armies = assetManager.get("data/armies.json");

    long start = System.currentTimeMillis();
    battle = new TiledBattleBuilder(tiledMap, new SaveGameCharacterDatabase(characters, armies.getArmies())).build(eventBus);
    long end = System.currentTimeMillis();

    logger.debug("Map init took: " + (end - start) + "ms");

    populateWorld(entityFactory, battle.getMap(), new AssetManagerRepository(assetManager));

    pathfinder = new AStarPathfinder<>(new BattleMapPathfindingStrategy(battle.getMap()));

    world.setSystem(new BattleUnitDeathSystem());
    world.setSystem(new AnimationKeyFrameSystem());
    world.setSystem(new DeathFadeSystem(tweenManager));
    world.setSystem(mapSelectorUpdateSystem);
    world.setSystem(new StageUpdateSystem());
    world.setSystem(new BattleAnimationSwitchSystem());
    world.setSystem(new TimedProcessRunnerSystem());

    /**
     * Render Order:
     *   - Void
     *   - Map Tiles
     *   - Map Movement Ranges
     *   - Grid Overlay
     *   - Background Sprites
     *   - Foreground Sprites
     *   - Menu/HUD
     */
    world.setSystem(voidRenderSystemFactory.create(camera));
    world.setSystem(mapRenderSystem);
    world.setSystem(new MapMovementOverlayRenderer(camera, gridSquareSize));
    world.setSystem(new MapOverlayRenderSystem(camera));
    world.setSystem(spriteRenderSystem);
    world.setSystem(new StageRenderSystem());
    world.setSystem(new ViewportSpaceSpriteRenderSystem(viewport));

    world.setManager(new TagManager());

    world.initialize();


    Entity tileSelectorOverlay = entityFactory.createMapSelector();
    world.addEntity(tileSelectorOverlay);
    world.getManager(TagManager.class).register("map_hover_selector", tileSelectorOverlay);


    world.addEntity(entityFactory.createTiledMap(tiledMap, spriteBatch));
    initializeMapObjects(tiledMap);

    BattleMap map = battle.getMap();
    Entity mapGridOverlay = entityFactory.createMapGridOverlay(map.getHeight()+1, map.getWidth()+1, 32, new GridPoint2());
    world.addEntity(mapGridOverlay);

    Entity stage = entityFactory.createStage(menuStage);
    world.addEntity(stage);

    world.addEntity(entityFactory.createVoid(viewport));

    final InputMultiplexer multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(menuStage);


    Gdx.input.setInputProcessor(multiplexer);

    initialized = true;

    UnitMover unitMover = new UnitMover(this, tweenManager, gridSquareSize);

    EasyFlow<BattleContext> flow = BattleFlow.FLOW;
    List<? extends FlowListener<BattleContext>> listeners = Arrays.asList(
      turnListenerFactory.create(camera, this, pathfinder, gridSquareSize),
      new UnitActionSelectListener(skin, viewport, menuStage, gridSquareSize),
      new ViewingUnitInfo(world, entityFactory),
      new SelectingMoveLocation(this, gridSquareSize),
      new UnitMoving(this, pathfinder, map, gridSquareSize, tweenManager, unitMover),
      new AttackTargetSelectionListener(this, gridSquareSize),
      new AttackConfirmationListener(skin, menuStage, camera, gridSquareSize),
      new UnitAttackingListener(this, world)
    );

    for (FlowListener<BattleContext> listener : listeners) {
      listener.bind(flow);
    }

    flow.whenError((error, context) -> logger.error(error.getMessage(), error.getCause()));

    flow.start(new BattleContext(battle));

    eventBus.register(new UnitMoveHandler(unitMover));
  }

  private void initializeMapObjects(TiledMap tiledMap) {

    List<MapLayer> layers = newArrayList(tiledMap.getLayers());

    // Increment the z-index for each layer counting up from
    int firstZIndex = SpriteLayer.FOREGROUND.getZIndex();
    IntStream.range(firstZIndex, firstZIndex + layers.size())
      .forEach((i) -> populateMapObjects(tiledMap, layers.get(i - firstZIndex), i));
  }

  private void populateMapObjects(TiledMap map, MapLayer layer, int zIndex) {

    for (MapObject object : layer.getObjects()) {
      TextureRegion region = TiledMapUtils.getTextureRegion(object.getProperties(), map);
      if (region != null) {
        Entity entity = entityFactory.createMapObject((RectangleMapObject)object, region,zIndex);
        world.addEntity(entity);
      }
    }
  }

  private void populateWorld(EntityFactory entityFactory, BattleMap battleMap, AssetRepository assets) {

    CharacterSprites sprites = assets.get("data/character_sprites.json");

    Map<String, CharacterSpriteBook> booksById = CollectionUtils.indexBy(sprites.getSprites(),
      CharacterSpriteBook::getCharacterId);

    for (int i = 0; i < battleMap.getWidth(); i++) {
      for (int j = 0; j < battleMap.getHeight(); j++) {
        Tile tile = battleMap.getTile(i, j);
        Combatant combatant = tile.getCombatant();
        if (combatant != null) {

          Character character = combatant.getCharacter();
          CharacterSpriteBook spriteBook = booksById.get(character.getId());

          Set<UnitEntityAnimation> animations = Sets.newHashSet(
            buildAnimation(spriteBook.getIdle(), AnimationType.IDLE, assets),
            buildAnimation(spriteBook.getDodge(), AnimationType.DODGE, assets),
            buildAnimation(spriteBook.getAttack(), AnimationType.ATTACK, assets),
            buildAnimation(spriteBook.getRunNorth(), AnimationType.RUN_NORTH, assets),
            buildAnimation(spriteBook.getRunSouth(), AnimationType.RUN_SOUTH, assets),
            buildAnimation(spriteBook.getRunEast(), AnimationType.RUN_EAST, assets),
            buildAnimation(spriteBook.getRunWest(), AnimationType.RUN_WEST, assets)
          ).stream().filter(Objects::nonNull).collect(Collectors.toSet());

          Entity entity = entityFactory.createAnimatedUnit(battleMap, combatant, animations);
          registerUnitEntity(character, entity);
        }
      }
    }
  }

  @Nullable
  private UnitEntityAnimation buildAnimation(SpriteReference reference, AnimationType type, AssetRepository asset) {

    if (reference == null ) {
      logger.error("Combatant missing animation: " + type);
      return null;
    }

    SpriteSheet sheet = asset.get(reference.getSheetPath());
    Texture texture = asset.get(StringUtils.replace(reference.getSheetPath(), ".json", ".png"));

    AnimationSpec spec = sheet.getAnimationSpecs().get(reference.getName());
    if (spec == null) {
      throw new IllegalArgumentException("Could not find animation spec by name: " + reference.getName());
    }

    return new UnitEntityAnimation(texture, type, spec, sheet.getGridSize());
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
