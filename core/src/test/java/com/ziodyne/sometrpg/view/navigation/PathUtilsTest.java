package com.ziodyne.sometrpg.view.navigation;

import com.badlogic.gdx.math.GridPoint2;
import com.ziodyne.sometrpg.logic.navigation.Path;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PathUtilsTest {
  @Test
  public void testSegmentPath() throws Exception {
    Path<GridPoint2> path = new Path.Builder<GridPoint2>()
            .addPoint(new GridPoint2(0,0))
            .addPoint(new GridPoint2(0,1))
            .addPoint(new GridPoint2(0,2))
            .addPoint(new GridPoint2(0,3))
            .addPoint(new GridPoint2(1,3))
            .addPoint(new GridPoint2(2,3))
            .build();

    List<PathSegment> segmentedPath = PathUtils.segmentPath(path);
    PathSegment first = segmentedPath.get(0);
    Assert.assertEquals("The type of the first segment should be START", PathSegment.Type.START, first.getType());

    PathSegment last = segmentedPath.get(segmentedPath.size() - 1);
    Assert.assertEquals("The type of the last segment should be END", PathSegment.Type.END, last.getType());

    PathSegment firstCurve = segmentedPath.get(3);
    Assert.assertEquals("The fourth path segment should be a north to west turn.", PathSegment.Type.N2W, firstCurve.getType());

    PathSegment postCurveStraight = segmentedPath.get(4);
    Assert.assertEquals("The fifth path segment after the turn should continue west.", PathSegment.Type.W, postCurveStraight.getType());
  }
}
