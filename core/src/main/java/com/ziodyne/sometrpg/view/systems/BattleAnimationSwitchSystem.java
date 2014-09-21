package com.ziodyne.sometrpg.view.systems;

import java.util.Optional;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.util.Logged;
import com.ziodyne.sometrpg.view.components.BattleUnit;
import com.ziodyne.sometrpg.view.components.SpriteAnimation;
import com.ziodyne.sometrpg.view.components.SpriteComponent;
import com.ziodyne.sometrpg.view.rendering.Sprite;

/**
 * This system is responsible for changing the animation on units when
 * their animation state changes.
 */
public class BattleAnimationSwitchSystem extends IteratingSystem implements Logged {

  public BattleAnimationSwitchSystem() {

    super(Family.getFamilyFor(SpriteAnimation.class, BattleUnit.class, SpriteComponent.class));
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

    SpriteComponent spriteCmp = entity.getComponent(SpriteComponent.class);
    Sprite sprite = spriteCmp.getSprite();

    Optional<Vector2> offset = unit.getCurrentOffset();
    if (offset.isPresent()) {
      Vector2 vec = offset.get();
      sprite.setOffsetX(vec.x);
      sprite.setOffsetY(vec.y);
    } else {
      sprite.setOffsetY(0);
      sprite.setOffsetX(0);
    }
  }
}
