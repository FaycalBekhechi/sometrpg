package com.ziodyne.sometrpg.view.tween;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.math.GridPoint2;
import com.ziodyne.sometrpg.view.components.Position;

import java.util.List;

public class TweenUtils {
  private TweenUtils() { }

  public static Timeline sequencePosition(Position position, List<GridPoint2> points, float stepDuration, TweenEquation easingEquation) {
    Timeline seq = Timeline.createSequence();

    for (GridPoint2 point : points) {
      seq.push(
        Tween.to(position, 0, stepDuration)
             .ease(easingEquation)
             .target(point.x, point.y)
      );
    }

    return seq;
  }

  public static Timeline sequencePosition(Position position, List<GridPoint2> points, float stepDuration) {
    return sequencePosition(position, points, stepDuration, TweenEquations.easeNone);
  }
}
