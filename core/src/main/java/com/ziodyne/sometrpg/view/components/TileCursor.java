package com.ziodyne.sometrpg.view.components;

import com.badlogic.ashley.core.Component;

public class TileCursor extends Component {
  private boolean active = true;

  public boolean isActive() {

    return active;
  }

  public void setActive(boolean active) {

    this.active = active;
  }
}
