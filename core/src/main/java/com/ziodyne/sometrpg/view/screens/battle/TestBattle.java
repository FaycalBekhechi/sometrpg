package com.ziodyne.sometrpg.view.screens.battle;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import au.com.ds.ef.EasyFlow;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.ziodyne.sometrpg.logic.loader.AssetUtils;
import com.ziodyne.sometrpg.logic.loader.TiledBattleBuilder;
import com.ziodyne.sometrpg.logic.loader.loaders.ArmiesLoader;
import com.ziodyne.sometrpg.logic.loader.loaders.CharactersLoader;
import com.ziodyne.sometrpg.logic.loader.loaders.GameSpecLoader;
import com.ziodyne.sometrpg.logic.loader.models.Armies;
import com.ziodyne.sometrpg.logic.loader.models.Characters;
import com.ziodyne.sometrpg.logic.loader.models.GameSpec;
import com.ziodyne.sometrpg.logic.loader.models.Roster;
import com.ziodyne.sometrpg.logic.loader.models.SpriteSheet;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.SaveGameCharacterDatabase;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.SomeTRPGBattle;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.MapCombatResolver;
import com.ziodyne.sometrpg.logic.navigation.AStarPathfinder;
import com.ziodyne.sometrpg.logic.navigation.BattleMapPathfindingStrategy;
import com.ziodyne.sometrpg.logic.navigation.Pathfinder;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.Director;
import com.ziodyne.sometrpg.view.TiledMapUtils;
import com.ziodyne.sometrpg.view.assets.AssetBundleLoader;
import com.ziodyne.sometrpg.view.assets.AssetManagerRepository;
import com.ziodyne.sometrpg.view.assets.AssetRepository;
import com.ziodyne.sometrpg.view.assets.loaders.ChapterLoader;
import com.ziodyne.sometrpg.view.assets.loaders.CharacterSpritesLoader;
import com.ziodyne.sometrpg.view.assets.loaders.MapLoader;
import com.ziodyne.sometrpg.view.assets.loaders.ShaderProgramLoader;
import com.ziodyne.sometrpg.view.assets.loaders.SpriteSheetAssetLoader;
import com.ziodyne.sometrpg.view.assets.loaders.TextureAtlasLoader;
import com.ziodyne.sometrpg.view.assets.models.Chapter;
import com.ziodyne.sometrpg.view.assets.models.CharacterSprites;
import com.ziodyne.sometrpg.view.audio.BattleMusicPlayer;
import com.ziodyne.sometrpg.view.audio.BattleSoundPlayer;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.SpriteComponent;
import com.ziodyne.sometrpg.view.controllers.BattleMenuController;
import com.ziodyne.sometrpg.view.entities.EntityFactory;
import com.ziodyne.sometrpg.view.entities.RenderedCombatant;
import com.ziodyne.sometrpg.view.entities.UnitEntityAnimation;
import com.ziodyne.sometrpg.view.graphics.SpriteLayer;
import com.ziodyne.sometrpg.view.input.BattleMapController;
import com.ziodyne.sometrpg.view.input.InputHandlerStack;
import com.ziodyne.sometrpg.view.rendering.TextRenderer;
import com.ziodyne.sometrpg.view.screens.battle.eventhandlers.UnitMoveHandler;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleFlow;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;
import com.ziodyne.sometrpg.view.screens.battle.state.listeners.*;
import com.ziodyne.sometrpg.view.systems.AnimationKeyFrameSystem;
import com.ziodyne.sometrpg.view.systems.AnimationSequenceSystem;
import com.ziodyne.sometrpg.view.systems.BattleAnimationSwitchSystem;
import com.ziodyne.sometrpg.view.systems.MapHoverSelectorUpdateSystem;
import com.ziodyne.sometrpg.view.systems.MapMovementOverlayRenderer;
import com.ziodyne.sometrpg.view.systems.MapOverlayRenderSystem;
import com.ziodyne.sometrpg.view.systems.ShapeRenderSystem;
import com.ziodyne.sometrpg.view.systems.SpriteRenderSystem;
import com.ziodyne.sometrpg.view.systems.StageRenderSystem;
import com.ziodyne.sometrpg.view.systems.StageUpdateSystem;
import com.ziodyne.sometrpg.view.systems.TextRenderSystem;
import com.ziodyne.sometrpg.view.systems.TiledMapRenderSystem;
import com.ziodyne.sometrpg.view.systems.TimedProcessRunnerSystem;
import com.ziodyne.sometrpg.view.systems.ViewportSpaceSpriteRenderSystem;
import com.ziodyne.sometrpg.view.systems.ViewportSpaceTextRenderSystem;
import com.ziodyne.sometrpg.view.systems.VoidSpriteRenderSystem;
import com.ziodyne.sometrpg.view.widgets.PortraitTray;

import static com.google.common.collect.Lists.newArrayList;

public class TestBattle extends BattleScreen {
  private TweenManager tweenManager;
  private TiledMapRenderSystem mapRenderSystem;
  private MapHoverSelectorUpdateSystem mapSelectorUpdateSystem;
  private Rectangle mapBoundingRect;
  private TweenAccessor<Position> positionTweenAccessor;
  private TweenAccessor<SpriteComponent> spriteTweenAccessor;
  private TweenAccessor<Camera> cameraTweenAccessor;
  private TweenAccessor<RenderedCombatant> renderedCombatantTweenAccessor;
  private SpriteRenderSystem.Factory spriteRendererFactory;
  private BattleMapController.Factory mapControllerFactory;
  private ViewportSpaceTextRenderSystem.Factory textRenderSystemFactory;
  private VoidSpriteRenderSystem.Factory voidRenderSystemFactory;
  private AssetBundleLoader bundleLoader;
  private TextRenderSystem textRenderSystem;
  private boolean initialized;
  private Pathfinder<GridPoint2> pathfinder;
  private final String chapterPath;
  private final BattleSoundPlayer battleSoundPlayer;
  private final BattleMusicPlayer battleMusicPlayer;

  public interface Factory {
    TestBattle create(String chapterPath);
  }

  @Inject
  TestBattle(Director director, TweenManager tweenManager, TweenAccessor<Camera> cameraTweenAccessor,
             SpriteRenderSystem.Factory spriteRendererFactory, BattleMapController.Factory mapControllerFactory,
             TweenAccessor<SpriteComponent> spriteTweenAccessor, AssetBundleLoader.Factory bundleLoaderFactory,
             TweenAccessor<Position> positionTweenAccessor, VoidSpriteRenderSystem.Factory voidRenderSystemFactory,
             EventBus eventBus, TextRenderSystem textRenderSystem, ViewportSpaceTextRenderSystem.Factory textRenderSystemFactory,
             TweenAccessor<RenderedCombatant> renderedCombatantTweenAccessor, @Assisted String chapterPath) {
    super(director, new OrthographicCamera(), 32f, eventBus);

    camera.zoom = 0.5f;
    this.chapterPath = chapterPath;
    this.renderedCombatantTweenAccessor = renderedCombatantTweenAccessor;
    this.tweenManager = tweenManager;
    this.textRenderSystemFactory = textRenderSystemFactory;
    this.mapControllerFactory = mapControllerFactory;
    this.cameraTweenAccessor = cameraTweenAccessor;
    this.positionTweenAccessor = positionTweenAccessor;
    this.spriteRendererFactory = spriteRendererFactory;
    this.spriteTweenAccessor = spriteTweenAccessor;
    this.mapControllerFactory = mapControllerFactory;
    this.voidRenderSystemFactory = voidRenderSystemFactory;

    AssetManager assetManager = getAssetManager();
    AssetRepository repo = new AssetManagerRepository(assetManager);
    this.battleSoundPlayer = new BattleSoundPlayer(eventBus, repo);
    this.battleMusicPlayer = new BattleMusicPlayer(repo);
    this.bundleLoader = bundleLoaderFactory.create(assetManager, "data/test.bundle");
    this.textRenderSystem = textRenderSystem;

    assetManager.setLoader(TiledMap.class, new TmxMapLoader());

    FileHandleResolver resolver = new InternalFileHandleResolver();
    assetManager.setLoader(BattleMap.class, new MapLoader(resolver));
    assetManager.setLoader(GameSpec.class, new GameSpecLoader(resolver));
    assetManager.setLoader(SpriteSheet.class, new SpriteSheetAssetLoader(resolver));
    assetManager.setLoader(CharacterSprites.class, new CharacterSpritesLoader(resolver));
    assetManager.setLoader(Armies.class, new ArmiesLoader(resolver));
    assetManager.setLoader(Characters.class, new CharactersLoader(resolver));
    assetManager.setLoader(TextureAtlas.class, new TextureAtlasLoader(resolver));
    assetManager.setLoader(Chapter.class, new ChapterLoader(resolver));
    assetManager.setLoader(Sound.class, new SoundLoader(resolver));
    assetManager.setLoader(ShaderProgram.class, new ShaderProgramLoader(resolver));
    assetManager.setLoader(BitmapFont.class, new BitmapFontLoader(resolver));

    try {
      bundleLoader.load();
    } catch (IOException e) {
      logError("Failed to load bundle file.", e);
    }
  }

  private void initalizeBattle() {
    logDebug("Initializing battle.");

    Tween.registerAccessor(Camera.class, cameraTweenAccessor);
    Tween.registerAccessor(SpriteComponent.class, spriteTweenAccessor);
    Tween.registerAccessor(RenderedCombatant.class, renderedCombatantTweenAccessor);
    Tween.registerAccessor(Position.class, positionTweenAccessor);

    AssetManager assetManager = getAssetManager();

    Chapter chapter = assetManager.get(chapterPath, Chapter.class);
    TiledMap tiledMap = chapter.getMap();
    TiledMapTileLayer tileLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
    mapBoundingRect = new Rectangle(0, 0, (tileLayer.getWidth()-1) * gridSquareSize, (tileLayer.getHeight()-1) * gridSquareSize);

    SpriteRenderSystem spriteRenderSystem = spriteRendererFactory.create(camera, engine);

    mapRenderSystem = new TiledMapRenderSystem(camera);
    mapSelectorUpdateSystem = new MapHoverSelectorUpdateSystem(viewport, mapBoundingRect, 32);

    GameSpec gameSpec = assetManager.get("data/game.json");
    Collection<Character> characters = AssetUtils.reifyCharacterSpecs(gameSpec.getCharacters());
    List<Roster> rosters = gameSpec.getRosters();

    long start = System.currentTimeMillis();
    SomeTRPGBattle battle = new TiledBattleBuilder(tiledMap, new SaveGameCharacterDatabase(characters, rosters), eventBus).build();
    long end = System.currentTimeMillis();

    logDebug("Map init took: " + (end - start) + "ms");

    populateWorld(entityFactory, battle.getMap(), new AssetManagerRepository(assetManager));

    pathfinder = new AStarPathfinder<>(new BattleMapPathfindingStrategy(battle.getMap()));

    engine.addSystem(new BattleAnimationSwitchSystem());
    engine.addSystem(new AnimationKeyFrameSystem());
    engine.addSystem(new AnimationSequenceSystem());
    engine.addSystem(mapSelectorUpdateSystem);
    engine.addSystem(new StageUpdateSystem());
    engine.addSystem(new TimedProcessRunnerSystem(engine));

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
    engine.addSystem(voidRenderSystemFactory.create(camera));
    engine.addSystem(mapRenderSystem);
    engine.addSystem(new MapMovementOverlayRenderer(camera, gridSquareSize));
    engine.addSystem(new MapOverlayRenderSystem(camera));
    engine.addSystem(spriteRenderSystem);
    engine.addSystem(new StageRenderSystem());
    engine.addSystem(new ShapeRenderSystem(camera));
    engine.addSystem(textRenderSystem);
    engine.addSystem(new ViewportSpaceSpriteRenderSystem(viewport));
    engine.addSystem(textRenderSystemFactory.create(viewport));

    Entity tileSelectorOverlay = entityFactory.createMapSelector();
    engine.addEntity(tileSelectorOverlay);

    engine.addEntity(entityFactory.createTiledMap(tiledMap, spriteBatch));
    initializeMapObjects(tiledMap);

    BattleMap map = battle.getMap();
    Entity mapGridOverlay = entityFactory.createMapGridOverlay(map.getHeight()+1, map.getWidth()+1, 32, new GridPoint2());
    engine.addEntity(mapGridOverlay);

    Entity stage = entityFactory.createStage(menuStage);
    engine.addEntity(stage);

    engine.addEntity(entityFactory.createVoid(viewport));

    initialized = true;

    UnitMover unitMover = new UnitMover(this, tweenManager, gridSquareSize);
    BattleMenuController menuController = new BattleMenuController(entityFactory, engine, viewport);

    InputHandlerStack handlerStack = new InputHandlerStack();
    handlerStack.enable();

    EasyFlow<BattleContext> flow = BattleFlow.FLOW;
    List<? extends FlowListener<BattleContext>> listeners = Arrays.asList(
      new PlayerTurnListener<>(camera, this, battle, pathfinder, gridSquareSize, mapControllerFactory, handlerStack,
              eventBus),
      new UnitActionSelectListener(menuController, handlerStack, this, gridSquareSize),
      new ViewingUnitInfo(BattleState.SHOWING_ENEMY_DETAILS, menuController, handlerStack),
      new ViewingUnitInfo(BattleState.SHOWING_FRIENDLY_DETAILS, menuController, handlerStack),
      new SelectingMoveLocation(this, handlerStack, gridSquareSize),
      new UnitMoving(this, pathfinder, map, gridSquareSize, tweenManager, unitMover),
      new AttackTargetSelectionListener(this, handlerStack, gridSquareSize),
      new AttackConfirmationListener(engine, entityFactory, camera, new MapCombatResolver(map), handlerStack,
              gridSquareSize),
      new UnitAttackingListener(tweenManager, this, engine, eventBus),
      new EnemyTurnListener(battle, engine, viewport, new TextRenderer(entityFactory, engine)),
      new EndScreenListener(engine, viewport, new TextRenderer(entityFactory, engine))
    );

    for (FlowListener<BattleContext> listener : listeners) {
      listener.bind(flow);
    }

    flow.whenError((error, context) -> logError(error.getMessage(), error.getCause()));

    flow.start(new BattleContext(battle, this));

    PortraitTray portraitTray = new PortraitTray(engine, entityFactory, battle, viewport, eventBus, tweenManager);
    portraitTray.render();

    eventBus.register(new UnitMoveHandler(unitMover));
    battleInitialized(battle);
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
        engine.addEntity(entity);
      }
    }
  }

  private void populateWorld(EntityFactory entityFactory, BattleMap battleMap, AssetRepository assets) {

    Chapter chapter = assets.get(chapterPath, Chapter.class);
    CharacterSprites sprites = chapter.getSprites();
    for (int i = 0; i < battleMap.getWidth(); i++) {
      for (int j = 0; j < battleMap.getHeight(); j++) {
        Tile tile = battleMap.getTile(i, j);
        Combatant combatant = tile.getCombatant();
        if (combatant != null) {

          Character character = combatant.getCharacter();
          Set<UnitEntityAnimation> animations = sprites.getAnimations(character.getId());
          RenderedCombatant renderedCombatant = entityFactory.createAnimatedUnit(battleMap, combatant, animations);

          registerUnitEntity(renderedCombatant);
        }
      }
    }
  }

  @Override
  public void render(float delta) {

    super.render(delta);

    if (initialized) {
      tweenManager.update(delta);
    } else {
      if (bundleLoader.update()) {
        initalizeBattle();
      }
    }
  }
}
