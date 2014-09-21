package com.ziodyne.sometrpg.view.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.util.function.Consumer;

public class Shader extends Component {
  private final ShaderProgram shader;
  private final ShaderUpdater updater;

  public Shader(ShaderProgram shader, ShaderUpdater updater) {
    this.shader = shader;
    this.updater = updater;
  }

  public Shader(ShaderProgram shader) {
    this.shader = shader;
    this.updater = (prog, time) -> {};
  }

  public void update(float delta) {
    shader.begin();
    updater.update(shader, delta);
    shader.end();
  }

  public ShaderProgram getShader() {
    return shader;
  }
}
