package com.ziodyne.sometrpg.view.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class ViewportPosition extends Component {
  private final Vector2 position;

  public ViewportPosition(Vector2 position) {

    this.position = position;
  }

  public Vector2 getPosition() {

    return position;
  }
}
