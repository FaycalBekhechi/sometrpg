package com.ziodyne.sometrpg.view.components;

import com.badlogic.ashley.core.Component;

public class MapGridOverlay extends Component {
  public final int rows;
  public final int columns;
  public final float gridSquareSize;
  public final float opacity;

  public MapGridOverlay(int rows, int columns, float gridSquareSize, float opacity) {
    this.rows = rows;
    this.columns = columns;
    this.gridSquareSize = gridSquareSize;
    this.opacity = opacity;
  }
}
