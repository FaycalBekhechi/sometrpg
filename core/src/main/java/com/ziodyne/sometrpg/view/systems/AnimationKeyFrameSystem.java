package com.ziodyne.sometrpg.view.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ziodyne.sometrpg.view.components.SpriteComponent;
import com.ziodyne.sometrpg.view.components.SpriteAnimation;
import com.ziodyne.sometrpg.view.rendering.Sprite;

/**
 * This class is an entity system that updates {@link SpriteAnimation}s each frame to give their sprites that frame's
 * texture reagion for the animation.
 */
public class AnimationKeyFrameSystem extends IteratingSystem {

  public AnimationKeyFrameSystem() {
    super(Family.getFamilyFor(SpriteAnimation.class, SpriteComponent.class));
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {
    SpriteAnimation animation = entity.getComponent(SpriteAnimation.class);
    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);

    TextureRegion region = animation.getCurrentFrame();

    Sprite sprite = spriteComponent.getSprite();
    TextureRegion lastRegion = sprite.getRegion();

    sprite.setRegion(region);
    animation.tick();
  }
}
