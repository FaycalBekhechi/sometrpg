package com.ziodyne.sometrpg.logic.loader.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AnimationSpec {
  private String name;

  @JsonProperty("start")
  private int[] startCoords;

  @JsonProperty("end")
  private int[] endCoords;

  @JsonProperty("frame_length")
  private long frameDurationMs;

  public String getName() {

    return name;
  }

  public int[] getStartCoords() {

    return startCoords;
  }

  public int[] getEndCoords() {

    return endCoords;
  }

  public long getFrameDurationMs() {

    return frameDurationMs;
  }
}
