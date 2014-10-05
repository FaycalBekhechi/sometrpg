package com.ziodyne.sometrpg.view.components;

import java.util.Map;
import java.util.Optional;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.view.AnimationType;

public class BattleUnit extends Component {
  public final Combatant combatant;

  private AnimationType animType = AnimationType.IDLE;

  private final Map<AnimationType, Animation> anims;
  private final Map<AnimationType, Vector2> offsets;

  public BattleUnit(Combatant combatant, Map<AnimationType, Animation> anims, Map<AnimationType, Vector2> offsets) {

    this.combatant = combatant;
    this.anims = anims;
    this.offsets = offsets;
  }

  public Animation getAnimation(AnimationType type) {
    return anims.get(type);
  }

  public Optional<Vector2> getCurrentOffset() {
    return Optional.ofNullable(offsets.get(animType));
  }

  public Animation getCurrentAnimation() {
    return getAnimation(animType);
  }

  public void setAnimType(AnimationType animType) {

    this.animType = animType;
  }

  public AnimationType getAnimType() {

    return animType;
  }
}
