package com.ziodyne.sometrpg.view.components;

import com.artemis.Component;
import com.ziodyne.sometrpg.view.graphics.SpriteLayer;
import com.ziodyne.sometrpg.view.rendering.Sprite;

public class SpriteComponent extends Component {
  private final SpriteLayer layer;
  private final Sprite sprite;

  public SpriteComponent(Sprite sprite, SpriteLayer layer) {

    this.sprite = sprite;
    this.layer = layer;
  }

  public SpriteLayer getLayer() {

    return layer;
  }

  public Sprite getSprite() {

    return sprite;
  }
}
