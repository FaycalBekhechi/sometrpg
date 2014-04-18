package com.ziodyne.sometrpg.view.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ziodyne.sometrpg.view.components.Sprite;
import com.ziodyne.sometrpg.view.components.SpriteAnimation;

/**
 * This class is an entity system that updates {@link SpriteAnimation}s each frame to give their sprites that frame's
 * texture reagion for the animation.
 */
public class AnimationKeyFrameSystem extends EntitySystem {
  @Mapper
  private ComponentMapper<SpriteAnimation> animationMapper;

  @Mapper
  private ComponentMapper<Sprite> spriteMapper;

  public AnimationKeyFrameSystem() {

    super(Aspect.getAspectForAll(SpriteAnimation.class, Sprite.class));
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entityImmutableBag) {
    for (int i = 0; i < entityImmutableBag.size(); i++) {
      Entity entity = entityImmutableBag.get(i);
      SpriteAnimation animation = animationMapper.get(entity);
      Sprite sprite = spriteMapper.get(entity);


      TextureRegion region = animation.getCurrentFrame();
      sprite.setRegion(animation.getCurrentFrame());
      animation.tick();
      entity.changedInWorld();
    }
  }

  @Override
  protected boolean checkProcessing() {

    return true;
  }
}
