package com.ziodyne.sometrpg.logic.models.battle.combat;

import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.Stat;

public class WeaponAttack implements Attack {
  @Override
  public int getRange() {
    return 1;
  }

  @Override
  public int computeHitChance(Combatant attacker, Combatant defender) {
    Character attackingCharacter = attacker.getCharacter();
    Character defendingCharacter = defender.getCharacter();

    return attackingCharacter.getStat(Stat.SKILL)*3 - defendingCharacter.getStat(Stat.SPEED);
  }

  @Override
  public int computeCritChance(Combatant attacker, Combatant defender) {
    Character attackingCharacter = attacker.getCharacter();
    Character defendingCharacter = defender.getCharacter();

    return attackingCharacter.getStat(Stat.SKILL) - defendingCharacter.getStat(Stat.SPEED);
  }

  @Override
  public int computeDamage(Combatant attacker, Combatant defender) {
    Character attackingCharacter = attacker.getCharacter();
    Character defendingCharacter = defender.getCharacter();

    return attackingCharacter.getStat(Stat.STRENGTH) - defendingCharacter.getStat(Stat.DEFENCE);
  }
}
