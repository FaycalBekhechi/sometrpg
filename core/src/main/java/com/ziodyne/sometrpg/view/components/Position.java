package com.ziodyne.sometrpg.view.components;

import com.badlogic.ashley.core.Component;

public class Position extends Component {
  private float x;
  private float y;
  private Position parent;

  public Position(float x, float y) {
      this.x = x;
      this.y = y;
  }

  public Position(float x, float y, Position position) {
    this(x, y);
    this.parent = position;
  }

  public Position() {
    this(0f, 0f);
  }

  public float getX() {
    if (parent == null) {
      return x;
    } else {
      return parent.getX() + x;
    }
  }

  public void setX(float x) {
    this.x = x;
  }

  public float getY() {
    if (parent == null) {
      return y;
    } else {
      return parent.getY() + y;
    }
  }

  public void setY(float y) {
    this.y = y;
  }
}
