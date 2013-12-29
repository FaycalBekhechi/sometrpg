package com.ziodyne.sometrpg.view.components;

import com.artemis.Component;
import com.badlogic.gdx.math.GridPoint2;
import com.ziodyne.sometrpg.logic.navigation.Path;

public class Navigating extends Component {
  public final Path<GridPoint2> path;

  public Navigating(Path<GridPoint2> path) {
    this.path = path;
  }
}
