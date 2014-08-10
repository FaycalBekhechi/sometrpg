package com.ziodyne.sometrpg.view.systems;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.ziodyne.sometrpg.logging.GdxLogger;
import com.ziodyne.sometrpg.logging.Logger;
import com.ziodyne.sometrpg.view.components.DeathFade;
import com.ziodyne.sometrpg.view.components.SpriteComponent;
import com.ziodyne.sometrpg.view.tween.SpriteComponentAccessor;

/**
 * Fades {@link com.ziodyne.sometrpg.view.components.SpriteComponent} entities with the {@link DeathFade} component to invisible then removes them from the world.
 */
public class DeathFadeSystem extends IteratingSystem {
  private static final Logger LOG = new GdxLogger(DeathFadeSystem.class);

  private final TweenManager tweenManager;
  private final Engine engine;

  public DeathFadeSystem(TweenManager tweenManager, Engine engine) {
    super(Family.getFamilyFor(DeathFade.class, SpriteComponent.class));
    this.tweenManager = tweenManager;
    this.engine = engine;
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {

    SpriteComponent spriteComponentComponent = entity.getComponent(SpriteComponent.class);
    Tween.to(spriteComponentComponent, SpriteComponentAccessor.ALPHA, 0.5f)
      .target(0f)
      .ease(TweenEquations.easeOutCubic)
      .setCallback((type, source) -> {
        engine.removeEntity(entity);
      })
      .start(tweenManager);
  }

}
