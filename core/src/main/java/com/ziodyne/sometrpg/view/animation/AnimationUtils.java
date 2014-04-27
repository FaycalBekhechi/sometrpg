package com.ziodyne.sometrpg.view.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ziodyne.sometrpg.logic.loader.models.AnimationSpec;

/**
 * Helper methods for doing animation.
 */
public class AnimationUtils {
  private AnimationUtils() { }

  /**
   * Create a LibGDX Animation from an Animation spec and a Texture
   * @param texture The {@link Texture} from which to draw frames
   * @param spec The {@link AnimationSpec} describing the animation.
   * @param frameSize The size of each frame on the sprite sheet
   * @return An {@link Animation} from the input
   */
  public static Animation createFromSpec(Texture texture, AnimationSpec spec, int frameSize) {

    Array<TextureRegion> textureRegions = new Array<>();
    for (Vector2 frameCoord : spec.getFrameCoords()) {
      int x = (int)frameCoord.x*frameSize;
      int y = (int)frameCoord.y*frameSize;

      TextureRegion region = new TextureRegion(texture, x, y, frameSize, frameSize);
      region.flip(spec.isYFlipped(), false);

      textureRegions.add(region);
    }

    float frameDuration = spec.getFrameDurationMs() / 1000f;
    int playMode = spec.getPlayMode().getGdxPlayMode();

    return new Animation(frameDuration, textureRegions, playMode);
  }
}
