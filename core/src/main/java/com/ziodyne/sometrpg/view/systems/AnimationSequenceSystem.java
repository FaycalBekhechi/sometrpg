package com.ziodyne.sometrpg.view.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.ziodyne.sometrpg.view.components.CombatantAnimationSequence;
import com.ziodyne.sometrpg.view.components.SpriteAnimation;

public class AnimationSequenceSystem extends IteratingSystem {

  public AnimationSequenceSystem() {
    super(Family.getFamilyFor(SpriteAnimation.class, CombatantAnimationSequence.class));
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {
    SpriteAnimation animation = entity.getComponent(SpriteAnimation.class);
    CombatantAnimationSequence sequence = entity.getComponent(CombatantAnimationSequence.class);

    if (animation.isFinished()) {
      sequence.next();
    }
  }
}
