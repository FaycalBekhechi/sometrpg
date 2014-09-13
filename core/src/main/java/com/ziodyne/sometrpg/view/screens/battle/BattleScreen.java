package com.ziodyne.sometrpg.view.screens.battle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
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
import com.ziodyne.sometrpg.view.entities.HealthBar;
import com.ziodyne.sometrpg.view.entities.RenderedCombatant;
import com.ziodyne.sometrpg.view.screens.GameScreen;
import com.ziodyne.sometrpg.view.systems.CameraPanSystem;

public abstract class BattleScreen extends GameScreen {
  protected final Engine engine = new Engine();
  protected final SpriteBatch spriteBatch = new SpriteBatch();
  protected final EntityFactory entityFactory = new EntityFactory(engine, new AssetManagerRepository(getAssetManager()));
  protected final OrthographicCamera camera;
  protected final Director director;
  protected final Viewport viewport;
  protected final EventBus eventBus;
  protected TiledMap map;
  protected SomeTRPGBattle battle;
  protected Map<Long, RenderedCombatant> renderedCombatantIndex = new HashMap<>();
  protected Entity unitSelector;
  protected Stage menuStage;
  protected Group unitActionMenu = new Group();
  protected GridPoint2 selectedTile;
  protected float gridSquareSize = 32;
  private Entity currentMovementOverlay;
  private Entity currentAttackOverlay;
  private Set<Entity> currentPathGuides;
  private CameraPanSystem cameraPanner;

  private Optional<Set<HealthBar>> healthBars = Optional.empty();

  public BattleScreen(Director director, OrthographicCamera camera, float gridSquareSize, EventBus eventBus) {
    this.gridSquareSize = gridSquareSize;
    this.director = director;
    this.camera = camera;
    this.eventBus = eventBus;
    this.viewport = new FitViewport(1600, 900, camera);
    this.menuStage = new Stage(viewport, spriteBatch);
    camera.position.set(viewport.getViewportWidth()/4, viewport.getViewportHeight()/4, 0);

    menuStage.addActor(unitActionMenu);
  }

  public void toggleHealthBars() {
    if (healthBars.isPresent()) {
      hideHealthBars();
    } else {
      showHealthBars();
    }
  }

  public void showHealthBars() {
    Set<HealthBar> bars = renderedCombatantIndex.values().stream()
      .map(entityFactory::createHealthBar)
      .collect(Collectors.toSet());

    healthBars = Optional.of(bars);
  }

  public void hideHealthBars() {
    healthBars.get().stream()
      .forEach(HealthBar::dispose);

    healthBars = Optional.empty();
  }

  /**
   * FIX ME FIX ME FIX ME
   *
   * This exists because we want this class to provide some sort of controller interface for the
   * flow states to use to trigger high-level actions in the view (like disabling panning while a menu is open).
   *
   * FIX ME FIX ME FIX ME
   */
  protected void battleInitialized(SomeTRPGBattle battle) {
    this.battle = battle;
    Rectangle mapViewBounds = new Rectangle(
            0, 0,
            battle.getWidth() * gridSquareSize,
            battle.getHeight() * gridSquareSize
    );
    this.cameraPanner = new CameraPanSystem(camera, mapViewBounds);
    engine.addSystem(cameraPanner);
  }

  public void enableCameraControl() {
    cameraPanner.enable();
  }

  public void disableCameraControl() {
    cameraPanner.disable();
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

  public RenderedCombatant getRenderedCombatant(Combatant combatant) {
    return renderedCombatantIndex.get(combatant.getId());
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

  protected void registerUnitEntity(RenderedCombatant renderedCombatant) {
    Combatant combatant = renderedCombatant.getCombatant();
    renderedCombatantIndex.put(combatant.getId(), renderedCombatant);
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height);
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    engine.update(delta);

    // Anchor the unit selection menu to the selected tile.
    if (selectedTile != null) {
      Vector3 menuPos = new Vector3(selectedTile.x, selectedTile.y, 0);
      camera.project(menuPos);

      unitActionMenu.setX(menuPos.x);
      unitActionMenu.setY(menuPos.y);
    }
  }
}
