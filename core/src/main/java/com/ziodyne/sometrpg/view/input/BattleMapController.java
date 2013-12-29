package com.ziodyne.sometrpg.view.input;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector3;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.view.screens.BattleScreen;
import com.ziodyne.sometrpg.view.screens.TestBattle;
import com.ziodyne.sometrpg.view.tween.CameraAccessor;

public class BattleMapController extends InputAdapter {
  private static final int DRAG_TOLERANCE = 4;

  private final OrthographicCamera camera;
  private final TweenManager tweenManager;
  private final BattleScreen battleScreen;
  private boolean ignoreNextTouchUp = false;

  public interface Factory {
    public BattleMapController create(OrthographicCamera camera, BattleScreen battleScreen);
  }

  @AssistedInject
  BattleMapController(@Assisted OrthographicCamera camera, @Assisted BattleScreen battleScreen, TweenManager tweenManager) {
    this.camera = camera;
    this.tweenManager = tweenManager;
    this.battleScreen = battleScreen;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    Vector3 clickCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
    camera.unproject(clickCoordinates);
    int x = (int)Math.floor(clickCoordinates.x);
    int y = (int)Math.floor(clickCoordinates.y);
    GridPoint2 selectedPoint = new GridPoint2(x, y);

    // Click outisde the map bounds
    if (!battleScreen.isValidSquare(selectedPoint)) {
      return false;
    }

    // Click on an unoccupied square
    if (!battleScreen.isOccupied(selectedPoint)) {
      battleScreen.setSelectedSquare(null);
      return doCameraPan(button);
    }

    battleScreen.setSelectedSquare(selectedPoint);
    return true;
  }

  private boolean doCameraPan(int button) {
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
