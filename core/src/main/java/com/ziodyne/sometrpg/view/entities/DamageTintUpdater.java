package com.ziodyne.sometrpg.view.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ziodyne.sometrpg.view.components.ShaderUpdater;

public class DamageTintUpdater implements ShaderUpdater {
  private float amount = 0;

  @Override
  public void update(ShaderProgram program, float deltaTime) {
    program.setUniformf("u_tintColor", new Color(amount, 0, 0, 1f));
  }

  public void setAmount(float amount) {
    this.amount = amount;
  }

  public float getAmount() {
    return amount;
  }
}
