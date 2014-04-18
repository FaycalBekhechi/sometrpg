package com.ziodyne.sometrpg.view.components;

import com.artemis.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.apache.commons.lang3.Validate;

/**
 * This class is an Artemis {@link com.artemis.Component} that holds an {@link com.badlogic.gdx.graphics.g2d.Animation}.
 */
public class SpriteAnimation extends Component {

  private final Animation animation;
  private float currentTime = 0L;

  public SpriteAnimation(Animation animation) {

    Validate.notNull(animation);
    this.animation = animation;
  }

  public TextureRegion getKeyFrame(float time) {
    return animation.getKeyFrame(time);
  }

  public TextureRegion getCurrentFrame() {
    return getKeyFrame(getCurrentTime());
  }

  public float getCurrentTime() {

    return currentTime;
  }

  public void tick() {
    float time = Gdx.graphics.getDeltaTime();
    currentTime += time;
  }
}
