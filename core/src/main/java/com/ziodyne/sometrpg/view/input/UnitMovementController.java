package com.ziodyne.sometrpg.view.input;

import au.com.ds.ef.err.LogicViolationError;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;

import java.util.Set;

public class UnitMovementController extends InputAdapter {
  private final Set<GridPoint2> bounds;
  private final OrthographicCamera camera;
  private final BattleContext context;


  public UnitMovementController(OrthographicCamera camera, Set<GridPoint2> bounds, BattleContext context) {
    this.camera = camera;
    this.bounds = bounds;
    this.context = context;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {

    Vector3 clickCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
    camera.unproject(clickCoordinates);
    int x = (int)Math.floor(clickCoordinates.x);
    int y = (int)Math.floor(clickCoordinates.y);
    GridPoint2 selectedPoint = new GridPoint2(x, y);

    if (bounds.contains(selectedPoint)) {
      context.movementDestination = selectedPoint;
      try {
        context.trigger(BattleEvent.MOVE_LOC_SELECTED);
        return true;
      } catch (LogicViolationError e) {

      }
    }

    return false;
  }
}
