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

public class MapHoverSelectorUpdateSystem extends VoidEntitySystem {
  private final World world;
  private final Camera camera;
  private final Rectangle activeRegion;

  public MapHoverSelectorUpdateSystem(World world, Camera camera, Rectangle activeRegion) {
    this.world = world;
    this.camera = camera;
    this.activeRegion = activeRegion;
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

      float mapWidth = 20*32;
      double tileX = Math.floor((unprojectedX/mapWidth)*32);
      double tileY = Math.floor((unprojectedY/mapWidth)*32);
      Vector3 mapHoverPOsition = new Vector3((float)tileX*32, (float)tileY*32, 0);
      camera.project(mapHoverPOsition);


      Position pos = world.getMapper(Position.class).get(mapSelector);
      pos.setX((float) tileX * 32);
      pos.setY((float) tileY * 32);

      if (!activeRegion.contains(pos.getX(), pos.getY())) {
        mapSelector.disable();
      } else {
        mapSelector.enable();
      }
    }
  }
}
