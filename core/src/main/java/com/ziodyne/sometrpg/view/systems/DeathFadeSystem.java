package com.ziodyne.sometrpg.view.systems;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.ziodyne.sometrpg.view.components.DeathFade;
import com.ziodyne.sometrpg.view.components.Sprite;
import com.ziodyne.sometrpg.view.tween.SpriteComponentAccessor;

/**
 * Fades {@link Sprite} entities with the {@link DeathFade} component to invisible then removes them from the world.
 */
public class DeathFadeSystem extends EntityProcessingSystem {
  @Mapper
  private ComponentMapper<Sprite> spriteComponentMapper;

  private final TweenManager tweenManager;

  public DeathFadeSystem(TweenManager tweenManager) {
    super(Aspect.getAspectForAll(DeathFade.class, Sprite.class));
    this.tweenManager = tweenManager;
  }

  @Override
  protected void process(final Entity entity) {
    Sprite spriteComponent = spriteComponentMapper.get(entity);
    Tween.to(spriteComponent, SpriteComponentAccessor.ALPHA, 0.5f)
            .target(0f)
            .ease(TweenEquations.easeOutCubic)
            .setCallback(new TweenCallback() {
              @Override
              public void onEvent(int type, BaseTween<?> source) {
                entity.deleteFromWorld();
              }
            })
            .start(tweenManager);
  }
}
