package com.ziodyne.sometrpg.view.navigation;

import com.badlogic.gdx.math.GridPoint2;
import com.ziodyne.sometrpg.logic.navigation.Path;

import java.util.ArrayList;
import java.util.List;

import static com.ziodyne.sometrpg.view.navigation.PathSegment.Type;

public class PathUtils {
  private PathUtils() { }

  /**
   * Compute a {@link List} of {@link PathSegment} from a {@link Path} of {@link GridPoint2}
   * @param path the path to segment
   * @return A list of the path segments that make up the path
   */
  public static List<PathSegment> segmentPath(Path<GridPoint2> path) {
    List<GridPoint2> points = path.getPoints();
    if (points.isEmpty()) {
      return new ArrayList<PathSegment>();
    }

    List<PathSegment> result = new ArrayList<PathSegment>();
    int idx = 0;
    for (GridPoint2 point : points) {
      int prevIdx = idx-1;
      if (prevIdx < 0) {
        // The point is at the beginning
        result.add(new PathSegment(point, Type.START));
      } else {
        // The point is in the middle or end
        int nextIdx = idx+1;
        if (nextIdx >= points.size()) {
          // The point is at the end
          result.add(new PathSegment(point, Type.END));
        } else {
          // The point is in the middle
          GridPoint2 previousPoint = points.get(prevIdx);
          GridPoint2 next = points.get(nextIdx);
          Type previousDirection = computeDirection(previousPoint, point);
          Type nextDirection = computeDirection(point, next);
          PathSegment nextSegment;

          // If the direction between the previous point and this point differs
          // from the direction between this point and the next point, we're rounding
          // a corner
          if (previousDirection != nextDirection) {
            nextSegment = new PathSegment(point, computeCornerDirection(previousDirection, nextDirection));
          } else {
            nextSegment = new PathSegment(point, previousDirection);
          }

          result.add(nextSegment);
        }
      }
      idx++;
    }

    return result;
  }

  /**
   * Compute the type of curve between two {@link PathSegment.Type}s
   *
   * @param start Start type
   * @param end End type
   * @return The type of curve between these two directions
   */
  private static Type computeCornerDirection(Type start, Type end) {
    switch (start) {
      case N:
        switch (end) {
          case E:
            return Type.N2E;
          case W:
            return Type.N2W;
        }
        break;
      case S:
        switch (end) {
          case E:
            return Type.S2E;
          case W:
            return Type.S2W;
        }
        break;
      case E:
        switch (end) {
          case S:
            return Type.E2S;
          case N:
            return Type.E2N;
        }
        break;
      case W:
        switch (end) {
          case S:
            return Type.W2S;
          case N:
            return Type.W2N;
        }
        break;
      default:
        throw new IllegalArgumentException("Invalid path.");
    }

    return null;
  }

  /**
   * Determine the direction between two adjacent grid points.
   *
   * @param start The starting point
   * @param end The end point
   * @return A {@link Type} describing the direction.
   */
  private static Type computeDirection(GridPoint2 start, GridPoint2 end) {
    if (start.x > end.x) {
      return Type.E;
    }

    if (start.x < end.x) {
      return Type.W;
    }

    if (start.y < end.y) {
      return Type.N;
    }

    if (start.y > end.y) {
      return Type.S;
    }

    throw new IllegalArgumentException("Points must be adjacent!");
  }
}
