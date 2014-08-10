package com.ziodyne.sometrpg.view.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.TileCursor;

import static com.ziodyne.sometrpg.logic.util.MathUtils.nearestMultipleOf;

public class MapHoverSelectorUpdateSystem extends IteratingSystem {
  private final Rectangle activeRegion;
  private final float gridSize;
  private final Viewport viewport;

  public MapHoverSelectorUpdateSystem(Viewport viewport, Rectangle activeRegion, float gridSize) {
    super(Family.getFamilyFor(TileCursor.class));
    this.activeRegion = activeRegion;
    this.gridSize = gridSize;
    this.viewport = viewport;
  }

  @Override
  public void processEntity(Entity mapSelector, float deltaTime) {

    Vector2 mousePosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
    viewport.unproject(mousePosition);

    float unprojectedX = mousePosition.x;
    float unprojectedY = mousePosition.y;

    TileCursor cursorComponent = mapSelector.getComponent(TileCursor.class);
    if (cursorComponent.isActive() && mapSelector.hasComponent(Position.class)){
      Position pos = mapSelector.getComponent(Position.class);
      pos.setX(unprojectedX);
      pos.setY(unprojectedY);

      snapToGrid(pos);

      cursorComponent.setActive(!activeRegion.contains(pos.getX(), pos.getY()));
    }
  }

  private void snapToGrid(Position position) {
    position.setX(nearestMultipleOf(gridSize, position.getX()));
    position.setY(nearestMultipleOf(gridSize, position.getY()));
  }
}
