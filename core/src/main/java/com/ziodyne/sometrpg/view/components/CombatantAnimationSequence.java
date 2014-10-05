package com.ziodyne.sometrpg.view.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.ziodyne.sometrpg.view.AnimationType;

import java.util.List;

public class CombatantAnimationSequence extends Component {
  private final List<AnimationType> animations;
  private final BattleUnit unit;
  private int currentAnimationIndex = -1;

  public CombatantAnimationSequence(BattleUnit unit, List<AnimationType> animations) {
    for (AnimationType animation : animations.subList(0, animations.size() -1)) {
      Animation anim = unit.getAnimation(animation);
      Animation.PlayMode mode  = anim.getPlayMode();
      if (mode != Animation.PlayMode.NORMAL && mode != Animation.PlayMode.REVERSED) {
        throw new IllegalArgumentException("Cannot use looped animation in a sequence.");
      }
    }
    this.animations = animations;
    this.unit = unit;
    unit.setAnimType(animations.get(0));
  }

  public void next() {
    if (currentAnimationIndex < animations.size() -1) {
      currentAnimationIndex++;
      unit.setAnimType(animations.get(currentAnimationIndex));
    }
  }
}
