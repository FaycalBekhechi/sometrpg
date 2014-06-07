package com.ziodyne.sometrpg.view.systems;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.ziodyne.sometrpg.logic.util.MathUtils;
import com.ziodyne.sometrpg.view.components.Position;

import static com.ziodyne.sometrpg.logic.util.MathUtils.nearestMultipleOf;

public class MapHoverSelectorUpdateSystem extends VoidEntitySystem {
  private final World world;
  private final Camera camera;
  private final Rectangle activeRegion;
  private final float gridSize;

  public MapHoverSelectorUpdateSystem(World world, Camera camera, Rectangle activeRegion, float gridSize) {
    this.world = world;
    this.camera = camera;
    this.activeRegion = activeRegion;
    this.gridSize = gridSize;
  }

  @Override
  protected void processSystem() {
    TagManager tagManager = world.getManager(TagManager.class);
    Entity mapSelector = tagManager.getEntity("map_hover_selector");

    if (mapSelector != null) {
      Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
      camera.unproject(mousePosition);

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
