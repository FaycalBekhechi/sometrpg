package com.ziodyne.sometrpg.view.input;

import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector3;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.view.screens.BattleScreen;

public class BattleMapController implements InputProcessor {
  private final OrthographicCamera camera;
  private final TweenManager tweenManager;
  private final BattleScreen battleScreen;

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
  public boolean keyDown(int keycode) {
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    return false;
  }

  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return false;
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
      return false;
    }

    battleScreen.setSelectedSquare(selectedPoint);
    return true;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    return false;
  }

  @Override
  public boolean scrolled(int amount) {
    return false;
  }
}
