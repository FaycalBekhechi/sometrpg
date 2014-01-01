package com.ziodyne.sometrpg.view.assets;

import com.badlogic.gdx.maps.tiled.TiledMap;

public class TiledMapAsset extends Asset<TiledMap> {
  TiledMapAsset() {
    super("tiledMap", TiledMap.class);
  }
}
