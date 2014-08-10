package com.ziodyne.sometrpg.view.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.ziodyne.sometrpg.view.components.BattleUnit;
import com.ziodyne.sometrpg.view.components.SpriteAnimation;

/**
 * This system is responsible for changing the animation on units when
 * their animation state changes.
 */
public class BattleAnimationSwitchSystem extends IteratingSystem {

  public BattleAnimationSwitchSystem() {

    super(Family.getFamilyFor(SpriteAnimation.class, BattleUnit.class));
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {

    SpriteAnimation spriteAnimation = entity.getComponent(SpriteAnimation.class);
    BattleUnit unit = entity.getComponent(BattleUnit.class);


    // Only change the running animation if it's actually different
    // than the one already goin'.
    // TODO: Use a more sophisticated equality check.
    Animation animation = unit.getCurrentAnimation();
    if (animation != null) {
      if (!spriteAnimation.getAnimation().equals(animation)) {
        spriteAnimation.setAnimation(animation);
      }
    }
  }
}
