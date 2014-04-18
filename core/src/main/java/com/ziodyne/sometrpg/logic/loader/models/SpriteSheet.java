package com.ziodyne.sometrpg.logic.loader.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpriteSheet {
  @JsonProperty("grid_size")
  private int gridSize;

  @JsonProperty("animations")
  private List<AnimationSpec> animationSpecs;

  public int getGridSize() {

    return gridSize;
  }

  public List<AnimationSpec> getAnimationSpecs() {

    return animationSpecs;
  }
}
