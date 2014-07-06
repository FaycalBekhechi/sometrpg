package com.ziodyne.sometrpg.view.entities;

import com.badlogic.gdx.graphics.Texture;
import com.ziodyne.sometrpg.logic.loader.models.AnimationSpec;
import com.ziodyne.sometrpg.view.AnimationType;

/**
 * A bundle class for a single unit entity's animation.
 */
public class UnitEntityAnimation {
  private Texture texture;
  private AnimationType type;
  private AnimationSpec spec;
  private int gridSize;

  public UnitEntityAnimation() { }
  public UnitEntityAnimation(Texture texture, AnimationType type, AnimationSpec spec, int gridSize) {

    this.texture = texture;
    this.type = type;
    this.spec = spec;
    this.gridSize = gridSize;
  }

  public int getGridSize() {

    return gridSize;
  }

  public Texture getTexture() {

    return texture;
  }

  public AnimationType getType() {

    return type;
  }

  public AnimationSpec getSpec() {

    return spec;
  }
}
