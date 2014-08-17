package com.ziodyne.sometrpg.view.components;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 * A component for a shader that does not vary.
 */
public class StaticShader extends Shader {

  public StaticShader(ShaderProgram shader) {

    super(shader);
  }

  @Override
  public void update(float delta) {

  }
}
