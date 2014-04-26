package com.ziodyne.sometrpg.view.components;

import java.util.Map;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.view.MapAnimation;

public class BattleUnit extends Component {
  public final Combatant combatant;

  private MapAnimation animType = MapAnimation.IDLE;

  private final Map<MapAnimation, Animation> anims;

  public BattleUnit(Combatant combatant, Map<MapAnimation, Animation> anims) {

    this.combatant = combatant;
    this.anims = anims;
  }

  public Animation getCurrentAnimation() {
    return anims.get(animType);
  }

  public void setAnimType(MapAnimation animType) {

    this.animType = animType;
  }

  public MapAnimation getAnimType() {

    return animType;
  }
}
