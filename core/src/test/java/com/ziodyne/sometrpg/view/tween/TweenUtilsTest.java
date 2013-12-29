package com.ziodyne.sometrpg.view.tween;

import aurelienribon.tweenengine.Timeline;
import com.badlogic.gdx.math.GridPoint2;
import com.google.common.collect.Lists;
import com.ziodyne.sometrpg.view.components.Position;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TweenUtilsTest {
  @Test
  public void testSequencePosition() throws Exception {
    Position positionComponent = new Position();

    List<GridPoint2> points = Lists.newArrayList(
            new GridPoint2(0, 0),
            new GridPoint2(0, 1),
            new GridPoint2(0, 2)
    );

    Timeline timeline = TweenUtils.sequencePosition(positionComponent, points, 0.1f);

    Assert.assertEquals("the timeline should have children equal to the number of given points",
            points.size(), timeline.getChildren().size());
  }
}
