package com.ziodyne.sometrpg.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

/**
 * An Artemis component that holds a Map instance loaded from Tiled.
 */
public class TiledMapComponent extends Component {
  private final TiledMap map;
  private final OrthogonalTiledMapRenderer renderer;

  public TiledMapComponent(TiledMap map, float unitScale, SpriteBatch batch) {
    this.map = map;
    this.renderer = new OrthogonalTiledMapRenderer(map, unitScale, batch);
  }

  public TiledMap getMap() {
    return map;
  }

  public OrthogonalTiledMapRenderer getRenderer() {
    return renderer;
  }
}
