package com.ziodyne.sometrpg.view.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.apache.commons.lang3.Validate;

/**
 * This class is an Artemis {@link Component} that holds an {@link com.badlogic.gdx.graphics.g2d.Animation}.
 */
public class SpriteAnimation extends Component {

  private Animation animation;
  private float currentTime = 0L;

  public SpriteAnimation(Animation animation) {

    Validate.notNull(animation);
    setAnimation(animation);
  }

  public Animation getAnimation() {

    return animation;
  }

  public boolean isLoop() {
    Animation.PlayMode mode = animation.getPlayMode();
    return mode != Animation.PlayMode.NORMAL && mode != Animation.PlayMode.REVERSED;
  }

  public void setAnimation(Animation anim) {
    this.animation = anim;
    this.currentTime = 0L;
  }

  public boolean isFinished() {
    return animation.isAnimationFinished(currentTime);
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
