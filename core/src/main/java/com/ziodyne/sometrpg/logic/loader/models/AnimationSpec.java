package com.ziodyne.sometrpg.logic.loader.models;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

public class AnimationSpec {
  @JsonProperty("offset")
  private int[] offset = new int[]{};

  @JsonProperty("start")
  private int[] startCoords;

  @JsonProperty("end")
  private int[] endCoords;

  @JsonProperty("frame_length")
  private long frameDurationMs;

  @JsonProperty("mode")
  private PlayMode playMode = PlayMode.LOOP;

  @JsonProperty("flip_y")
  private boolean flipY = false;

  public int[] getOffset() {

    return offset;
  }

  public boolean isYFlipped() {

    return flipY;
  }

  public PlayMode getPlayMode() {

    return playMode;
  }

  public int[] getStartCoords() {

    return startCoords;
  }

  public int[] getEndCoords() {

    return endCoords;
  }

  public int getNumFrames() {

    return (int)Math.sqrt(Math.pow(startCoords[0] - endCoords[0], 2) +
                          Math.pow(startCoords[1] - endCoords[1], 2));
  }

  public List<Vector2> getFrameCoords() {

    List<Vector2> result = new ArrayList<>();

    Range<Integer> xRange = Range.closed(startCoords[0], endCoords[0]);
    Range<Integer> yRange = Range.closed(startCoords[1], endCoords[1]);

    for (int x : ContiguousSet.create(xRange, DiscreteDomain.integers())) {
      for (int y : ContiguousSet.create(yRange, DiscreteDomain.integers())) {
        result.add(new Vector2(x, y));
      }
    }

    return result;
  }

  public long getFrameDurationMs() {

    return frameDurationMs;
  }
}
