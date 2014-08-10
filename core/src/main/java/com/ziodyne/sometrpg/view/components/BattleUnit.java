package com.ziodyne.sometrpg.view.components;

import java.util.Map;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.view.AnimationType;

public class BattleUnit extends Component {
  public final Combatant combatant;

  private AnimationType animType = AnimationType.IDLE;

  private final Map<AnimationType, Animation> anims;

  public BattleUnit(Combatant combatant, Map<AnimationType, Animation> anims) {

    this.combatant = combatant;
    this.anims = anims;
  }

  public Animation getCurrentAnimation() {
    return anims.get(animType);
  }

  public void setAnimType(AnimationType animType) {

    this.animType = animType;
  }

  public AnimationType getAnimType() {

    return animType;
  }
}
