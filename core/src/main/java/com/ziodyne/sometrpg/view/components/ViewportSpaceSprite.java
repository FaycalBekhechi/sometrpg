package com.ziodyne.sometrpg.view.components;

import com.badlogic.ashley.core.Component;
import com.ziodyne.sometrpg.view.rendering.Sprite;

public class ViewportSpaceSprite extends Component {
  private final Sprite sprite;

  public ViewportSpaceSprite(Sprite sprite) {

    this.sprite = sprite;
  }

 public Sprite getSprite() {

    return sprite;
  }
}
