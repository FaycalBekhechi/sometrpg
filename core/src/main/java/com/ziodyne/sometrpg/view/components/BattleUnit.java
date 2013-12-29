package com.ziodyne.sometrpg.view.components;

import com.artemis.Component;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

public class BattleUnit extends Component {
  public final Combatant combatant;

  public BattleUnit(Combatant combatant) {
    this.combatant = combatant;
  }
}
