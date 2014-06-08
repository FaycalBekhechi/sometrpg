package com.ziodyne.sometrpg.view.components;

import com.artemis.Component;
import com.ziodyne.sometrpg.view.rendering.Sprite;

/**
 * A tag component to allow a sprite to be drawn in 'the void' behind the world and circumvent normal sprite layer order.
 */
public class VoidSprite extends Component {

  private final Sprite sprite;

  public VoidSprite(Sprite sprite) {

    this.sprite = sprite;
  }

  public Sprite getSprite() {

    return sprite;
  }
}
