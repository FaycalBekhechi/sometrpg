package com.ziodyne.sometrpg.view.assets;

import com.ziodyne.sometrpg.logic.loader.models.GameSpec;

public class GameSpecAsset extends Asset<GameSpec> {
  public GameSpecAsset() {
    super("gameSpec", GameSpec.class);
  }
}
