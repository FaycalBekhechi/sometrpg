package com.ziodyne.sometrpg.logic.models.battle.combat;

import com.badlogic.gdx.math.GridPoint2;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;

public class MapCombatResolver implements CombatResolver {
  private final BattleMap map;

  public MapCombatResolver(BattleMap map) {
    this.map = map;
  }

  @Override
  public boolean isValid(BattleAction action) {
    GridPoint2 attackerPos = map.getCombatantPosition(action.getAttacker());
    if (attackerPos == null) {
      return false;
    }

    GridPoint2 defenderPos = map.getCombatantPosition(action.getDefender());
    if (defenderPos == null) {
      return false;
    }

    return false;
  }

  @Override
  public CombatSummary preview(BattleAction action) throws InvalidBattleActionException {
    validate(action);
    return CombatUtils.previewBattle(action);
  }

  @Override
  public void execute(BattleAction action) throws InvalidBattleActionException {
    validate(action);

    int damage = computeDamageSubtotal(action.getAttack(), action.getAttacker(), action.getDefender());
    action.getDefender().applyDamage(damage);
  }

  static int computeDamageSubtotal(Attack attack, Combatant attacker, Combatant defender) {
    int hitChance = attack.computeHitChance(attacker, defender);
    if (Math.random() <= (hitChance/100)) {
      int damage = attack.computeDamage(attacker, defender);
      int critChance = attack.computeCritChance(attacker, defender);
      if (Math.random() <= (critChance/100)) {
        damage *= 2;
      }

      return damage;
    }

    return 0;
  }

  private void validate(BattleAction action) throws InvalidBattleActionException {
    if (!isValid(action)) {
      throw new InvalidBattleActionException();
    }
  }
}
