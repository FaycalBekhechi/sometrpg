package com.ziodyne.sometrpg.view.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.ziodyne.sometrpg.view.components.BattleUnit;
import com.ziodyne.sometrpg.view.components.SpriteAnimation;

/**
 * This system is responsible for changing the animation on units when
 * their animation state changes.
 */
public class BattleAnimationSwitchSystem extends EntitySystem {
  @Mapper
  private ComponentMapper<SpriteAnimation> spriteAnimMapper;

  @Mapper
  private ComponentMapper<BattleUnit> unitMapper;

  public BattleAnimationSwitchSystem() {

    super(Aspect.getAspectForAll(SpriteAnimation.class, BattleUnit.class));
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entityImmutableBag) {
    for (int i = 0; i < entityImmutableBag.size(); i++) {
      Entity entity = entityImmutableBag.get(i);

      SpriteAnimation spriteAnimation = spriteAnimMapper.get(entity);
      BattleUnit unit = unitMapper.get(entity);


      // Only change the running animation if it's actually different
      // than the one already goin'.
      // TODO: Use a more sophisticated equality check.
      Animation animation = unit.getCurrentAnimation();
      if (!animation.equals(spriteAnimation.getAnimation())) {
        spriteAnimation.setAnimation(animation);
      }
    }
  }

  @Override
  protected boolean checkProcessing() {

    return true;
  }
}
