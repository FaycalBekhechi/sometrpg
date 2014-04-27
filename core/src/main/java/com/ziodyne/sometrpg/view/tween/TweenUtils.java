package com.ziodyne.sometrpg.view.tween;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import com.ziodyne.sometrpg.logic.navigation.Path;
import com.ziodyne.sometrpg.logic.util.GridPoint2;

public class TweenUtils {
  private TweenUtils() { }

  public static Timeline moveAlongPath(Path<GridPoint2> path, float stepDuration, Object target, int tweenType) {
    Timeline timeline = Timeline.createSequence();

    for (GridPoint2 point : path.getPoints()) {
      timeline = timeline.push(
        Tween.to(target, tweenType, stepDuration)
             .target(point.x, point.y)
      );
    }

    return timeline;
  }
}
