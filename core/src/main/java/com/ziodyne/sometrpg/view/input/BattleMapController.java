package com.ziodyne.sometrpg.view.input;

import java.util.Optional;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.navigation.Path;
import com.ziodyne.sometrpg.logic.navigation.Pathfinder;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.util.Logged;
import com.ziodyne.sometrpg.view.navigation.PathUtils;
import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.tween.CameraAccessor;

public class BattleMapController extends InputAdapter implements Toggleable, Logged {
  private static final int DRAG_TOLERANCE = 4;

  private final OrthographicCamera camera;
  private final TweenManager tweenManager;
  private final BattleScreen battleScreen;
  private final BattleContext context;
  private final Pathfinder<GridPoint2> pathfinder;
  private final float gridSize;
  private boolean ignoreNextTouchUp = false;
  private boolean enabled = true;

  public interface Factory {
    public BattleMapController create(OrthographicCamera camera, BattleScreen battleScreen, BattleContext context,
                                      Pathfinder<GridPoint2> pathfinder, float gridSize);
  }

  @AssistedInject
  BattleMapController(@Assisted OrthographicCamera camera, @Assisted BattleScreen battleScreen,
                      @Assisted BattleContext context, @Assisted Pathfinder<GridPoint2> pathfinder,
                      @Assisted float gridSize, TweenManager tweenManager) {
    this.context = context;
    this.camera = camera;
    this.tweenManager = tweenManager;
    this.battleScreen = battleScreen;
    this.gridSize = gridSize;
    this.pathfinder = pathfinder;
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

    // Click on an unoccupied square
    if (!battleScreen.isOccupied(selectedPoint)) {
      battleScreen.setSelectedSquare(null);
      return doCameraPan(button);
    }

    Optional<Combatant> combatantOptional = battleScreen.getCombatant(selectedPoint);
    if (combatantOptional.isPresent()) {
      Combatant combatant = combatantOptional.get();
      context.selectedCombatant = combatantOptional.get();
      context.selectedSquare = selectedPoint;

      BattleEvent event;
      if (battleScreen.isUnitTurn(combatant)) {
        event = BattleEvent.FRIENDLY_UNIT_SELECTED;
      } else {
        event = BattleEvent.ENEMY_UNIT_SELECTED;
      }

      context.safeTrigger(event);
    }

    ignoreNextTouchUp = false;

    return true;
  }

  private boolean doCameraPan(int button) {
    if (!isEnabled()) {
      return false;
    }

    if (button != Input.Buttons.LEFT) {
      return false;
    }

    if (ignoreNextTouchUp) {
      return false;
    }

    int screenX = Gdx.input.getX();
    int screenY = Gdx.input.getY();

    // Get the touch position in world space.
    Vector3 touchCoords3d = new Vector3(screenX, screenY, 0);
    camera.unproject(touchCoords3d);

    // Slide the camera to the new position.
    Tween.to(camera, CameraAccessor.POSITION, 0.5f)
            .target(touchCoords3d.x, touchCoords3d.y, 0)
            .start(tweenManager);

    return true;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    if (!isEnabled()) {
      return false;
    }

    int dx = Gdx.input.getDeltaX();
    int dy = Gdx.input.getDeltaY();

    if (Math.abs(dx) < DRAG_TOLERANCE && Math.abs(dy) < DRAG_TOLERANCE) {
      return false;
    }

    camera.translate(new Vector3(-Gdx.input.getDeltaX(), Gdx.input.getDeltaY(), 0));
    ignoreNextTouchUp = true;

    return true;
  }
}
