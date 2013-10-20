package com.ziodyne.sometrpg.view.systems;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.managers.TagManager;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.ziodyne.sometrpg.view.components.Position;

public class MapSelectorUpdateSystem extends VoidEntitySystem {
  private final World world;
  private final Camera camera;
  private final Rectangle activeRegion;

  public MapSelectorUpdateSystem(World world, Camera camera, Rectangle activeRegion) {
    this.world = world;
    this.camera = camera;
    this.activeRegion = activeRegion;
  }

  @Override
  protected void processSystem() {
    TagManager tagManager = world.getManager(TagManager.class);
    Entity mapSelector = tagManager.getEntity("map_selector");

    if (mapSelector != null) {
      Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
      camera.unproject(mousePosition);

      double unprojectedX = mousePosition.x;
      double unprojectedY = mousePosition.y;

      Position pos = world.getMapper(Position.class).get(mapSelector);
      pos.setX((float) Math.floor(unprojectedX));
      pos.setY((float) Math.floor(unprojectedY));

      if (!activeRegion.contains(pos.getX(), pos.getY())) {
        mapSelector.disable();
      } else {
        mapSelector.enable();
      }
    }
  }
}
