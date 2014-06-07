package com.ziodyne.sometrpg.view.systems;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziodyne.sometrpg.logic.util.MathUtils;
import com.ziodyne.sometrpg.view.components.Position;

import static com.ziodyne.sometrpg.logic.util.MathUtils.nearestMultipleOf;

public class MapHoverSelectorUpdateSystem extends VoidEntitySystem {
  private final World world;
  private final Rectangle activeRegion;
  private final float gridSize;
  private final Viewport viewport;

  public MapHoverSelectorUpdateSystem(World world, Viewport viewport, Rectangle activeRegion, float gridSize) {
    this.world = world;
    this.activeRegion = activeRegion;
    this.gridSize = gridSize;
    this.viewport = viewport;
  }

  @Override
  protected void processSystem() {
    TagManager tagManager = world.getManager(TagManager.class);
    Entity mapSelector = tagManager.getEntity("map_hover_selector");

    if (mapSelector != null) {
      Vector2 mousePosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
      viewport.unproject(mousePosition);

      float unprojectedX = mousePosition.x;
      float unprojectedY = mousePosition.y;

      Position pos = world.getMapper(Position.class).get(mapSelector);
      pos.setX(unprojectedX);
      pos.setY(unprojectedY);

      snapToGrid(pos);

      if (!activeRegion.contains(pos.getX(), pos.getY())) {
        mapSelector.disable();
      } else {
        mapSelector.enable();
      }
    }
  }

  private void snapToGrid(Position position) {
    position.setX(nearestMultipleOf(gridSize, position.getX()));
    position.setY(nearestMultipleOf(gridSize, position.getY()));
  }
}
