package com.ziodyne.sometrpg.logic.loader.models;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpriteSheet {
  @JsonProperty("grid_size")
  private int gridSize;

  @JsonProperty("animations")
  private Map<String, AnimationSpec> animationSpecs;

  public int getGridSize() {

    return gridSize;
  }

  public Map<String, AnimationSpec> getAnimationSpecs() {

    return animationSpecs;
  }
}
