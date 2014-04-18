package com.ziodyne.sometrpg.logic.loader.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnimationSpec {
  @JsonProperty("start")
  private int[] startCoords;

  @JsonProperty("end")
  private int[] endCoords;

  @JsonProperty("frame_length")
  private long frameDurationMs;

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

  public long getFrameDurationMs() {

    return frameDurationMs;
  }
}
