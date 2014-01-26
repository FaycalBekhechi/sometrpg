package com.ziodyne.sometrpg.view.screens.battle;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.battle.SomeTRPGBattle;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.Director;
import com.ziodyne.sometrpg.view.assets.AssetManagerRepository;
import com.ziodyne.sometrpg.view.entities.EntityFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class BattleScreen extends ScreenAdapter {
  protected final World world = new World();
  protected final SpriteBatch spriteBatch = new SpriteBatch();
  protected final AssetManager assetManager = new AssetManager();
  protected final EntityFactory entityFactory = new EntityFactory(world, new AssetManagerRepository(assetManager));
  protected final OrthographicCamera camera;
  protected final Director director;
  protected TiledMap map;
  protected SomeTRPGBattle battle;
  protected Map<Character, Entity> entityIndex = new HashMap<Character, Entity>();
  protected Entity unitSelector;
  protected Stage menuStage;
  protected Group unitActionMenu = new Group();
  protected GridPoint2 selectedTile;
  protected Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
  protected float gridSquareSize = 32;
  private Entity currentMovementOverlay;

  public BattleScreen(Director director, OrthographicCamera camera, float gridSquareSize) {
    this.gridSquareSize = gridSquareSize;
    this.director = director;
    this.camera = camera;
    this.menuStage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, spriteBatch);

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

  public Entity getUnitEntity(Character character) {
    return entityIndex.get(character);
  }

  private void setMovementOverlay(Entity overlay) {
    if (currentMovementOverlay != null) {
      currentMovementOverlay.deleteFromWorld();
    }

    world.addEntity(overlay);
    currentMovementOverlay = overlay;
  }

  public void hideMoveRange() {
    if (currentMovementOverlay != null) {
      currentMovementOverlay.deleteFromWorld();
      currentMovementOverlay = null;
    }
  }

  public void showMoveRange(Combatant combatant) {

    Set<GridPoint2> locations = Sets.newHashSet();
    for (Tile tile : battle.getMovableTiles(combatant)) {
      locations.add(tile.getPosition());
    }

    Entity movementOverlay = entityFactory.createMapMovementOverlay(locations);
    setMovementOverlay(movementOverlay);
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

  public Optional<Combatant> getCombatant(GridPoint2 square) {
    Tile tile = battle.getTile(square);
    if (tile == null) {
      return Optional.absent();
    }

    return Optional.fromNullable(tile.getCombatant());
  }

  protected void registerUnitEntity(Character character, Entity entity) {
    entityIndex.put(character, entity);
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

  }

  protected abstract void update(float delta);
}
