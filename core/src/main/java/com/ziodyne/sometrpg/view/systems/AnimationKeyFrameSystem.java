package com.ziodyne.sometrpg.view.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ziodyne.sometrpg.view.components.SpriteComponent;
import com.ziodyne.sometrpg.view.components.SpriteAnimation;
import com.ziodyne.sometrpg.view.rendering.Sprite;

/**
 * This class is an entity system that updates {@link SpriteAnimation}s each frame to give their sprites that frame's
 * texture reagion for the animation.
 */
public class AnimationKeyFrameSystem extends EntitySystem {
  @Mapper
  private ComponentMapper<SpriteAnimation> animationMapper;

  @Mapper
  private ComponentMapper<SpriteComponent> spriteMapper;

  public AnimationKeyFrameSystem() {

    super(Aspect.getAspectForAll(SpriteAnimation.class, SpriteComponent.class));
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entityImmutableBag) {
    for (int i = 0; i < entityImmutableBag.size(); i++) {
      Entity entity = entityImmutableBag.get(i);
      SpriteAnimation animation = animationMapper.get(entity);
      SpriteComponent spriteComponent = spriteMapper.get(entity);

      TextureRegion region = animation.getCurrentFrame();

      Sprite sprite = spriteComponent.getSprite();
      TextureRegion lastRegion = sprite.getRegion();

      if (lastRegion.getRegionWidth() != region.getRegionWidth()) {
        float xOffset = (region.getRegionWidth() - 32f) / 64f;
        sprite.setOffsetX(-xOffset);
      } else if (lastRegion.getRegionHeight() != region.getRegionHeight()) {
        float yOffset = (region.getRegionHeight() - 32f) / 64f;
        sprite.setOffsetY(-yOffset);
      }

      sprite.setRegion(region);
      animation.tick();
      entity.changedInWorld();
    }
  }

  @Override
  protected boolean checkProcessing() {

    return true;
  }
}
