package com.ziodyne.sometrpg.view.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.ziodyne.sometrpg.logic.util.GridPoint2;

import java.util.Set;

public class GridSelectionController extends InputAdapter {
  private final Set<GridPoint2> bounds;
  private final OrthographicCamera camera;
  private final SelectionHandler handler;
  private final float gridSize;

  public static interface SelectionHandler {
    public void handleHover(GridPoint2 hoveredPoint);
    public void handleSelection(GridPoint2 selectedPoint);
    public void handleCancelation();
  }



  public GridSelectionController(OrthographicCamera camera, Set<GridPoint2> bounds, float gridSize,
                                 SelectionHandler handler) {
    this.camera = camera;
    this.bounds = bounds;
    this.gridSize = gridSize;
    this.handler = handler;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {

    Vector3 clickCoordinates = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
    camera.unproject(clickCoordinates);
    int x = (int)Math.floor(clickCoordinates.x/gridSize);
    int y = (int)Math.floor(clickCoordinates.y/gridSize);
    GridPoint2 selectedPoint = new GridPoint2(x, y);

    if (bounds.contains(selectedPoint)) {
      handler.handleSelection(selectedPoint);
    }

    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {

    Vector3 hoverCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
    camera.unproject(hoverCoords);
    int x = (int)Math.floor(hoverCoords.x/gridSize);
    int y = (int)Math.floor(hoverCoords.y/gridSize);

    GridPoint2 hoveredPoint = new GridPoint2(x, y);
    if (bounds.contains(hoveredPoint)) {
      handler.handleHover(hoveredPoint);
    }

    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    if (Input.Keys.ESCAPE == keycode) {
      handler.handleCancelation();
    }

    return false;
  }
}
