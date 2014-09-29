package com.ziodyne.sometrpg.view.input;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.events.CloseTray;
import com.ziodyne.sometrpg.events.OpenTray;
import com.ziodyne.sometrpg.events.ToggleTray;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.navigation.Path;
import com.ziodyne.sometrpg.logic.navigation.Pathfinder;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.util.Logged;
import com.ziodyne.sometrpg.view.navigation.PathUtils;
import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;

import java.util.Optional;

public class BattleMapController extends InputAdapter implements Toggleable, Logged {
  private static final float[] ZOOM_LEVELS = {0.5f , 0.7f};

  private final OrthographicCamera camera;
  private final TweenManager tweenManager;
  private final BattleScreen battleScreen;
  private final BattleContext context;
  private final Pathfinder<GridPoint2> pathfinder;
  private final EventBus eventBus;
  private final float gridSize;
  private boolean ignoreNextTouchUp = false;
  private boolean enabled = true;
  private int currentZoomLevel = 0;

  public interface Factory {
    public BattleMapController create(OrthographicCamera camera, BattleScreen battleScreen, BattleContext context,
                                      Pathfinder<GridPoint2> pathfinder, EventBus eventBus, float gridSize);
  }

  @AssistedInject
  BattleMapController(@Assisted OrthographicCamera camera, @Assisted BattleScreen battleScreen,
                      @Assisted BattleContext context, @Assisted Pathfinder<GridPoint2> pathfinder,
                      @Assisted float gridSize, @Assisted EventBus eventBus, TweenManager tweenManager) {
    this.context = context;
    this.camera = camera;
    this.tweenManager = tweenManager;
    this.battleScreen = battleScreen;
    this.gridSize = gridSize;
    this.pathfinder = pathfinder;
    this.eventBus = eventBus;
  }

  @Override
  public void enable() {
    enabled = true;
  }

  @Override
  public void disable() {
    enabled = false;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public boolean keyDown(int keycode) {
    if (keycode == Input.Keys.UP || keycode == Input.Keys.DOWN) {
      // Choose the new zoom level, up key increases the zoom, down key decreases
      if (keycode == Input.Keys.UP) {
        currentZoomLevel = Math.max(currentZoomLevel - 1, 0);
        camera.zoom = ZOOM_LEVELS[currentZoomLevel];
      } else {
        currentZoomLevel = Math.min(currentZoomLevel + 1, ZOOM_LEVELS.length - 1);
        camera.zoom = ZOOM_LEVELS[currentZoomLevel];
      }
      // Re-center the position on the camera
      camera.position.set(camera.viewportWidth*camera.zoom/2, camera.viewportHeight*camera.zoom/2, 0);

      return true;
    } else if (keycode == Input.Keys.NUM_1) {
      eventBus.post(new ToggleTray());
    }

    if (keycode == Input.Keys.H) {
      battleScreen.toggleHealthBars();
    }

    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (!isEnabled()) {
      return false;
    }

    Vector3 clickCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
    camera.unproject(clickCoordinates);
    int x = (int)Math.floor(clickCoordinates.x/gridSize);
    int y = (int)Math.floor(clickCoordinates.y/gridSize);
    GridPoint2 selectedPoint = new GridPoint2(x, y);

    Optional<Path<GridPoint2>> path = pathfinder.computePath(selectedPoint, new GridPoint2(0, 1));
    if (path.isPresent()) {
      logDebug(PathUtils.segmentPath(path.get()).toString());
    }

    if (button == Input.Buttons.RIGHT) {
      Optional<Combatant> combatantResult = battleScreen.getCombatant(selectedPoint);
      if (combatantResult.isPresent()) {
        Combatant combatant = combatantResult.get();
        combatant.applyDamage(Integer.MAX_VALUE);
        return true;
      }
    }

    // Click outisde the map bounds
    if (!battleScreen.isValidSquare(selectedPoint)) {
      return false;
    }

    Optional<Combatant> combatantOptional = battleScreen.getCombatant(selectedPoint);
    if (combatantOptional.isPresent()) {
      Combatant combatant = combatantOptional.get();
      context.selectedCombatant = combatantOptional.get();
      context.selectedSquare = selectedPoint;

      BattleEvent event;
      if (battleScreen.isUnitTurn(combatant)) {
        // Shift+Click drops you right in the movement select state
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) {
          event = BattleEvent.QUICK_MOVE;
        } else {
          event = BattleEvent.FRIENDLY_UNIT_SELECTED;
        }
      } else {
        event = BattleEvent.ENEMY_UNIT_SELECTED;
      }

      context.safeTrigger(event);
    }

    ignoreNextTouchUp = false;

    return true;
  }
}
