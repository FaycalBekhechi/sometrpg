package com.ziodyne.sometrpg.view.assets;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ziodyne.sometrpg.view.assets.Asset;

public class ShaderAsset extends Asset<ShaderProgram> {
  public ShaderAsset() {

    super("shader", ShaderProgram.class);
  }
}
