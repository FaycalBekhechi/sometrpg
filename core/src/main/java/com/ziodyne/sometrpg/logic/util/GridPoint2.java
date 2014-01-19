package com.ziodyne.sometrpg.logic.util;


import com.google.common.base.Objects;

/**
 * This class exists solely because {@link GridPoint2} from LibGDX does not implement equals or hashCode...Come on.
 */
public class GridPoint2 {
	public int x;
	public int y;

	public GridPoint2() {
	}

	public GridPoint2(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public GridPoint2(GridPoint2 point) {
		this.x = point.x;
		this.y = point.y;
	}

	public GridPoint2 set(GridPoint2 point) {
		this.x = point.x;
		this.y = point.y;
		return this;
	}

	public GridPoint2 set(int x, int y) {
		this.x = x;
		this.y = y;
		return this;
	}

  @Override
  public int hashCode() {
    return Objects.hashCode(x, y);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof GridPoint2) {
      GridPoint2 gridPoint2 = (GridPoint2) obj;
      return Objects.equal(gridPoint2.x, this.x) &&
             Objects.equal(gridPoint2.y, this.y);
    } else {
      return false;
    }
  }
}
