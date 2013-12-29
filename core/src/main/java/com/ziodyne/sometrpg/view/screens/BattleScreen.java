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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.battle.SomeTRPGBattle;
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
  protected SomeTRPGBattle battle;
  protected Map<Unit, Entity> entityIndex = new HashMap<Unit, Entity>();
  protected Entity unitSelector;
  protected Stage menuStage;
  protected Group unitActionMenu = new Group();
  protected GridPoint2 selectedTile;
  protected Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
  protected float gridSquareSize = 32;

  public BattleScreen(Director director, OrthographicCamera camera, String tiledMapPath, float gridSquareSize) {
    this.gridSquareSize = gridSquareSize;
    this.director = director;
    this.camera = camera;
    this.menuStage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, spriteBatch);

    assetManager.setLoader(TiledMap.class, new TmxMapLoader());
    assetManager.load(tiledMapPath, TiledMap.class);

    menuStage.addActor(unitActionMenu);
  }

  public SomeTRPGBattle getBattle() {
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
      selectedTile = null;
      hideActionMenu();
    } else if (battle.tileExists(selectedSquare)) {
      unitSelector = entityFactory.createUnitSelector(selectedSquare);
      world.addEntity(unitSelector);
      selectedTile = selectedSquare;
      showActionMenu();
    }
  }

  private void showActionMenu() {
    TextButton testButton = new TextButton("Attack", skin);
    Vector3 screenSpacePoint = new Vector3(selectedTile.x + gridSquareSize, selectedTile.y, 0);
    camera.unproject(screenSpacePoint);


    // Offset gridSquareSize px to the right of the unit.
    testButton.setX(screenSpacePoint.x);
    testButton.setY(screenSpacePoint.y);
    unitActionMenu.addActor(testButton);
  }

  private void hideActionMenu() {
    unitActionMenu.clear();
  }

  public boolean isValidSquare(GridPoint2 square) {
    return battle.tileExists(square);
  }

  public boolean isOccupied(GridPoint2 square) {
    Tile tile = battle.getTile(square);
    return tile != null && tile.isOccupied();
  }

  protected void registerUnitEntity(Unit unit, Entity entity) {
    entityIndex.put(unit, entity);
    world.addEntity(entity);
  }

  @Override
  public void resize(int width, int height) {
    menuStage.setViewport(width, height, true);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0.2f, 1);
    Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    assetManager.update();
    camera.update();
    world.setDelta(delta);
    world.process();

    // Anchor the unit selection menu to the selected tile.
    if (selectedTile != null) {
      Vector3 menuPos = new Vector3(selectedTile.x, selectedTile.y, 0);
      camera.project(menuPos);

      unitActionMenu.setX(menuPos.x);
      unitActionMenu.setY(menuPos.y);
    }

    update(delta);

    menuStage.act(delta);
    menuStage.draw();
  }

  protected abstract void update(float delta);
}
