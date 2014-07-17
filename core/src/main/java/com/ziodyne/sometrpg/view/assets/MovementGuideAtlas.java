package com.ziodyne.sometrpg.view.assets;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ziodyne.sometrpg.view.navigation.PathSegment;

public class MovementGuideAtlas {

  private final TextureAtlas atlas;

  public MovementGuideAtlas(TextureAtlas atlas) {

    this.atlas = atlas;
  }

  public TextureRegion getLineRegion(PathSegment.Type segType) {

    switch (segType) {
      case N:
      case S:
        return getRegion("vertical_line");
      case E:
      case W:
        return getRegion("horizontal_line");
      case E2S:
      case N2W:
        return getRegion("northeast_corner");
      case W2S:
      case N2E:
        return getRegion("northwest_corner");
      case W2N:
      case S2E:
        return getRegion("southwest_corner");
      case E2N:
      case S2W:
        return getRegion("southeast_corner");
      default:
        return null;
    }
  }

  private TextureRegion getRegion(String name) {

    return atlas.findRegion(name);
  }
}
