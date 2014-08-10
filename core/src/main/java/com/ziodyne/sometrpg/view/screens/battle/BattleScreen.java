package com.ziodyne.sometrpg.view.screens.battle;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.battle.Army;
import com.ziodyne.sometrpg.logic.models.battle.SomeTRPGBattle;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.models.battle.combat.Attack;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.WeaponAttack;
import com.ziodyne.sometrpg.logic.navigation.Path;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.Director;
import com.ziodyne.sometrpg.view.assets.AssetManagerRepository;
import com.ziodyne.sometrpg.view.entities.EntityFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BattleScreen extends ScreenAdapter {
  protected final Engine engine = new Engine();
  protected final SpriteBatch spriteBatch = new SpriteBatch();
  protected final AssetManager assetManager = new AssetManager();
  protected final EntityFactory entityFactory = new EntityFactory(engine, new AssetManagerRepository(assetManager));
  protected final OrthographicCamera camera;
  protected final Director director;
  protected final Viewport viewport;
  protected final EventBus eventBus;
  protected TiledMap map;
  protected SomeTRPGBattle battle;
  protected Map<Character, Entity> entityIndex = new HashMap<>();
  protected Entity unitSelector;
  protected Stage menuStage;
  protected Group unitActionMenu = new Group();
  protected GridPoint2 selectedTile;
  protected Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
  protected float gridSquareSize = 32;
  private Entity currentMovementOverlay;
  private Entity currentAttackOverlay;
  private Set<Entity> currentPathGuides;

  public BattleScreen(Director director, OrthographicCamera camera, float gridSquareSize, EventBus eventBus) {
    this.gridSquareSize = gridSquareSize;
    this.director = director;
    this.camera = camera;
    this.eventBus = eventBus;
    this.viewport = new FitViewport(1600, 900, camera);

    camera.translate(viewport.getWorldWidth()/2, viewport.getWorldHeight()/2);

    this.menuStage = new Stage(viewport, spriteBatch);

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

  private void setAttackOverlay(Entity overlay) {
    if (currentAttackOverlay != null) {
      engine.removeEntity(currentAttackOverlay);
    }

    engine.addEntity(overlay);
    currentAttackOverlay = overlay;
  }

  public void hideAttackRange() {
    if (currentAttackOverlay != null) {
      engine.removeEntity(currentAttackOverlay);
      currentAttackOverlay = null;
    }
  }

  private void setMovementOverlay(Entity overlay) {
    if (currentMovementOverlay != null) {
      engine.removeEntity(currentMovementOverlay);
    }

    engine.addEntity(overlay);
    currentMovementOverlay = overlay;
  }

  public void hideMoveRange() {
    if (currentMovementOverlay != null) {
      engine.removeEntity(currentMovementOverlay);
      currentMovementOverlay = null;
    }
  }

  public void moveCombatant(Combatant combatant, GridPoint2 dest, Path<GridPoint2> path) {
    Tile tile = battle.getTile(dest);
    battle.moveCombatant(combatant, tile, path);
  }

  public void attackCombatant(Combatant attacker, Combatant defender) {
    battle.executeAttack(attacker, new WeaponAttack(), defender);
  }

  public Set<GridPoint2> showAttackRange(Combatant combatant, Attack attack) {
    Set<GridPoint2> locations = battle.getAttackableTiles(combatant, attack).stream()
      .map(Tile::getPosition)
      .collect(Collectors.toSet());

    Entity attackOverlay = entityFactory.createMapAttackOverlay(locations);
    setAttackOverlay(attackOverlay);

    return locations;
  }

  public Set<GridPoint2> showMoveRange(Combatant combatant) {

    Set<GridPoint2> locations = battle.getMovableTiles(combatant).stream()
      .map(Tile::getPosition)
      .collect(Collectors.toSet());

    Entity movementOverlay = entityFactory.createMapMovementOverlay(locations);
    setMovementOverlay(movementOverlay);

    return locations;
  }

  public void showPathGuide(Path<GridPoint2> path) {
    hidePathGuide();
    currentPathGuides = entityFactory.createPathGuides(path);
    currentPathGuides.stream().forEach(engine::addEntity);
  }

  public void hidePathGuide() {
    if (currentPathGuides != null) {
      currentPathGuides.forEach(engine::removeEntity);
      currentPathGuides = null;
    }
  }

  public GridPoint2 getCombatantPosition(Combatant combatant) {
    return battle.getCombatantPosition(combatant);
  }

  public void setSelectedSquare(GridPoint2 selectedSquare) {
    if (selectedSquare == null) {
      if (unitSelector != null) {
        engine.removeEntity(unitSelector);
        unitSelector = null;
      }
      selectedTile = null;
    } else if (battle.tileExists(selectedSquare)) {
      unitSelector = entityFactory.createUnitSelector(selectedSquare);
      engine.addEntity(unitSelector);
      selectedTile = selectedSquare;
    }
  }

  public boolean isValidSquare(GridPoint2 square) {
    return battle.tileExists(square);
  }

  public boolean isOccupied(GridPoint2 square) {
    Tile tile = battle.getTile(square);
    return tile != null && tile.isOccupied();
  }

  public boolean isTurnComplete() {
    return battle.isTurnComplete();
  }

  public void endTurn() {
    battle.endTurn();
  }

  public boolean isUnitTurn(Combatant combatant) {

    Preconditions.checkNotNull(combatant);
    Set<Combatant> currentCombatants = battle.getCurrentTurnArmy().getCombatants();
    return currentCombatants.contains(combatant);
  }

  public Optional<Combatant> getCombatant(GridPoint2 square) {
    Tile tile = battle.getTile(square);
    if (tile == null) {
      return Optional.empty();
    }

    return Optional.ofNullable(tile.getCombatant());
  }

  protected void registerUnitEntity(Character character, Entity entity) {
    entityIndex.put(character, entity);
    engine.addEntity(entity);
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height);
  }

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    assetManager.update();
    engine.update(delta);

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
