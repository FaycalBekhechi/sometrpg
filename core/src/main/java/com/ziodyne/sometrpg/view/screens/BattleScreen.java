package com.ziodyne.sometrpg.view.screens;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.GridPoint2;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.view.Director;
import com.ziodyne.sometrpg.view.entities.EntityFactory;

import java.util.HashMap;
import java.util.Map;

public abstract class BattleScreen extends ScreenAdapter {
  protected final World world = new World();
  protected final EntityFactory entityFactory = new EntityFactory(world);
  protected final SpriteBatch spriteBatch = new SpriteBatch();
  protected final AssetManager assetManager = new AssetManager();
  protected final OrthographicCamera camera;
  protected final Director director;
  protected TiledMap map;
  protected Battle battle;
  protected Map<Unit, Entity> entityIndex = new HashMap<Unit, Entity>();
  protected Entity unitSelector;

  public BattleScreen(Director director, OrthographicCamera camera, String tiledMapPath) {
    this.director = director;
    this.camera = camera;
    assetManager.setLoader(TiledMap.class, new TmxMapLoader());

    assetManager.load(tiledMapPath, TiledMap.class);
  }


  public Battle getBattle() {
    return battle;
  }

  public OrthographicCamera getCamera() {
    return camera;
  }

  public TiledMap getMap() {
    return map;
  }

  public Entity getUnitEntity(Unit unit) {
    return entityIndex.get(unit);
  }

  public void setSelectedSquare(GridPoint2 selectedSquare) {
    if (selectedSquare == null) {
      if (unitSelector != null) {
        world.deleteEntity(unitSelector);
        unitSelector = null;
      }
    } else if (battle.getMap().tileExists(selectedSquare.x, selectedSquare.y)) {
      unitSelector = entityFactory.createUnitSelector(selectedSquare);
      world.addEntity(unitSelector);
    }
  }

  public boolean isValidSquare(GridPoint2 square) {
    return battle.getMap().tileExists(square.x, square.y);
  }

  public boolean isOccupied(GridPoint2 square) {
    Tile tile = battle.getMap().getTile(square.x, square.y);
    return tile != null && tile.isOccupied();
  }

  protected void registerUnitEntity(Unit unit, Entity entity) {
    entityIndex.put(unit, entity);
    world.addEntity(entity);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    assetManager.update();
    camera.update();
    world.setDelta(delta);
    world.process();
    update(delta);
  }

  protected abstract void update(float delta);
}
