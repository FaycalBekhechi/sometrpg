package com.ziodyne.sometrpg.view;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;

public class TiledMapUtils {
  private TiledMapUtils() { }

  public static TextureRegion getTextureRegion(MapProperties properties, TiledMap map) {
    if (!properties.containsKey("gid")) {
      return null;
    }

    Integer gid = properties.get("gid", Integer.class);
    TiledMapTile tile = map.getTileSets().getTile(gid);

    if (tile == null) {
      return null;
    }

    return tile.getTextureRegion();
  }
}
