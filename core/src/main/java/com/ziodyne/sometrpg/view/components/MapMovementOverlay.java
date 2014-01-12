package com.ziodyne.sometrpg.view.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.GridPoint2;

import java.util.Set;

/**
 * This represents a set of tiles to highlight as movable when
 * a unit is selected.
 */
public class MapMovementOverlay extends Component {
  public final Set<GridPoint2> points;
  public final Color color = new Color(0, 0, 1, 0.5f);

  public MapMovementOverlay(Set<GridPoint2> points) {
    this.points = points;
  }
}
