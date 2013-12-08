package com.ziodyne.sometrpg.view.screens;

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
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.TerrainType;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.battle.Army;
import com.ziodyne.sometrpg.logic.models.battle.ArmyType;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.conditions.Rout;
import com.ziodyne.sometrpg.view.Director;
import com.ziodyne.sometrpg.view.assets.BattleLoader;
import com.ziodyne.sometrpg.view.assets.MapLoader;
import com.ziodyne.sometrpg.view.input.BattleMapController;
import com.ziodyne.sometrpg.view.input.CameraMoveController;
import com.ziodyne.sometrpg.view.input.GameExitController;
import com.ziodyne.sometrpg.view.screens.debug.ModelTestUtils;
import com.ziodyne.sometrpg.view.systems.MapHoverSelectorUpdateSystem;
import com.ziodyne.sometrpg.view.systems.SpriteRenderSystem;
import com.ziodyne.sometrpg.view.systems.TiledMapRenderSystem;

public class TestBattle extends BattleScreen {
  private final TweenManager tweenManager;
  private SpriteRenderSystem spriteRenderSystem;
  private TiledMapRenderSystem mapRenderSystem;
  private MapHoverSelectorUpdateSystem mapSelectorUpdateSystem;
  private Rectangle mapBoundingRect;

  @Inject
  TestBattle(Director director, TweenManager tweenManager, TweenAccessor<Camera> cameraTweenAccessor, GameExitController gameExitController,
             CameraMoveController.Factory cameraMoveControllerFactory, SpriteRenderSystem.Factory spriteRendererFactory,
             BattleMapController.Factory mapControllerFactory) {
    super(director, new OrthographicCamera(), "maps/test/test.tmx");
    this.tweenManager = tweenManager;
    camera.setToOrtho(false, 30, 20);

    Tween.registerAccessor(Camera.class, cameraTweenAccessor);

    TiledMap tiledMap = new TmxMapLoader().load("maps/test/test.tmx");
    TiledMapTileLayer tileLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
    mapBoundingRect = new Rectangle(0, 0, tileLayer.getWidth()-1, tileLayer.getHeight()-1);

    spriteRenderSystem = spriteRendererFactory.create(camera);
    mapRenderSystem = new TiledMapRenderSystem(camera);
    mapSelectorUpdateSystem = new MapHoverSelectorUpdateSystem(world, camera, mapBoundingRect);

    battle = initBattle(tileLayer);
    initUnitEntities();

    world.setSystem(spriteRenderSystem, true);
    world.setSystem(mapRenderSystem, true);

    world.setManager(new TagManager());

    world.initialize();


    Entity tileSelectorOverlay = entityFactory.createMapSelector();
    world.addEntity(tileSelectorOverlay);
    world.getManager(TagManager.class).register("map_hover_selector", tileSelectorOverlay);


    world.addEntity(entityFactory.createTiledMap(tiledMap, spriteBatch));

    assetManager.setLoader(BattleMap.class, new MapLoader(new InternalFileHandleResolver()));
    assetManager.setLoader(Battle.class, new BattleLoader(new InternalFileHandleResolver()));

    InputMultiplexer multiplexer = new InputMultiplexer();
    multiplexer.addProcessor(mapControllerFactory.create(camera, this));
    multiplexer.addProcessor(cameraMoveControllerFactory.create(camera));
    multiplexer.addProcessor(gameExitController);
    Gdx.input.setInputProcessor(multiplexer);

    //assetManager.load("battles/test.json", Battle.class);
  }


  private void initUnitEntities() {
    BattleMap map = battle.getMap();
    for (int i = 0; i < map.getWidth(); i++) {
      for (int j = 0; j < map.getHeight(); j++) {
        Tile tile = map.getTile(i, j);
        Unit unit = tile.getOccupyingUnit();
        if (unit != null) {
          Entity unitEntity = entityFactory.createUnit(unit, unit.getName().equals("Test3x") ? "single3x.png" : "single.png", i, j);
          registerUnitEntity(unit, unitEntity);
        }
      }
    }
  }

  private Battle initBattle(TiledMapTileLayer map) {
    Battle battle = new Battle();

    Combatant player = new Combatant(ModelTestUtils.homogeneousStats(40), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(20), "Test3x");
    Combatant enemy = new Combatant(ModelTestUtils.homogeneousStats(40), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(20), "Test");

    Army playerArmy = new Army(Sets.newHashSet(player), "Greil Mercenaries", ArmyType.PLAYER);
    Army enemyArmy = new Army(Sets.newHashSet(enemy), "Dawn Brigade", ArmyType.ENEMY);
    battle.setArmies(Lists.newArrayList(playerArmy, enemyArmy));

    battle.setCondition(new Rout());

    Tile[][] tiles = new Tile[map.getWidth()][map.getHeight()];
    for (int i = 0; i < map.getWidth(); i++) {
      for (int j = 0; j < map.getHeight(); j++) {
        // TODO: Read the tile from the tile layer and get the attribute
        tiles[i][j] = new Tile(TerrainType.GRASS);
      }
    }

    BattleMap battleMap = new BattleMap(tiles.length, tiles);
    battleMap.addUnit(player, 0, 0);
    battleMap.addUnit(enemy, tiles.length-1, tiles.length-1);
    battle.setMap(battleMap);

    return battle;
  }

  protected void update(float delta) {
    tweenManager.update(delta);
    mapSelectorUpdateSystem.process();
    mapRenderSystem.process();
    spriteRenderSystem.process();
  }
}
