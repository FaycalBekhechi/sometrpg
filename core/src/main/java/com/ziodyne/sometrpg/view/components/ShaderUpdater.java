package com.ziodyne.sometrpg.view.components;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

@FunctionalInterface
public interface ShaderUpdater {
  public void update(ShaderProgram program, float deltaTime);
}
