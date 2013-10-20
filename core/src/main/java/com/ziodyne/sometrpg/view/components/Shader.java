package com.ziodyne.sometrpg.view.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public abstract class Shader extends Component {
  private final ShaderProgram shader;

  protected Shader(ShaderProgram shader) {
    this.shader = shader;
  }

  public abstract void update(float delta);

  public ShaderProgram getShader() {
    return shader;
  }
}
