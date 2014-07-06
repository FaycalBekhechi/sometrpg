package com.ziodyne.sometrpg.view.components;

import com.artemis.Component;
import com.ziodyne.sometrpg.view.graphics.SpriteLayer;
import com.ziodyne.sometrpg.view.rendering.Sprite;

public class SpriteComponent extends Component {
  private final Sprite sprite;

  // The lower the z-index, the earlier it will be drawn.
  private int zIndex;

  public SpriteComponent(Sprite sprite, int zIndex) {

    this.sprite = sprite;
    this.zIndex = zIndex;
  }

  public SpriteComponent(Sprite sprite, SpriteLayer layer) {

    this.sprite = sprite;
    this.zIndex = layer.getZIndex();
  }

  public int getzIndex() {

    return zIndex;
  }

  public Sprite getSprite() {

    return sprite;
  }
}
