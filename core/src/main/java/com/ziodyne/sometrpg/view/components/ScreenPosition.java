package com.ziodyne.sometrpg.view.components;

import com.badlogic.ashley.core.Component;

/**
 * A component for an object that is positioned in screen space.
 */
public class ScreenPosition extends Component {
  private float x;
  private float y;

  // The lower the z-index, the lower 'layer' on which it will be rendered. Functions like CSS.
  private int zIndex = 0;

  public ScreenPosition(float x, float y) {

    this.x = x;
    this.y = y;
  }

  public ScreenPosition() {
    this(0f, 0f);
  }

  public int getzIndex() {

    return zIndex;
  }

  public void setzIndex(int zIndex) {

    this.zIndex = zIndex;
  }

  public float getX() {

    return x;
  }

  public void setX(float x) {

    this.x = x;
  }

  public float getY() {

    return y;
  }

  public void setY(float y) {

    this.y = y;
  }
}
