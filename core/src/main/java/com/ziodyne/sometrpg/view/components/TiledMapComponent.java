package com.ziodyne.sometrpg.view.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.ziodyne.sometrpg.view.tiledmap.ObjectAwareTmxRenderer;

/**
 * An Artemis component that holds a Map instance loaded from Tiled.
 */
public class TiledMapComponent extends Component {
  private final TiledMap map;
  private final OrthogonalTiledMapRenderer renderer;

  public TiledMapComponent(TiledMap map, float unitScale, SpriteBatch batch) {
    this.map = map;
    this.renderer = new ObjectAwareTmxRenderer(map, unitScale, batch);
  }

  public TiledMap getMap() {
    return map;
  }

  public OrthogonalTiledMapRenderer getRenderer() {
    return renderer;
  }
}
