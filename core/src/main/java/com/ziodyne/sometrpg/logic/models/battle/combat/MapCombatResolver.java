package com.ziodyne.sometrpg.logic.models.battle.combat;

import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.util.GridPoint2;

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

    if (isFriendlyFire(action)) {
      return false;
    }

    return true;
  }

  @Override
  public CombatSummary preview(BattleAction action) throws InvalidBattleActionException {
    validate(action);
    return CombatUtils.previewBattle(action);
  }

  @Override
  public CombatResult execute(BattleAction action) throws InvalidBattleActionException {
    validate(action);

    Attack attack = action.getAttack();
    Combatant attacker = action.getAttacker();
    Combatant defender = action.getDefender();

    boolean evaded = !doesAttackHit(attack, attacker, defender);
    int damage = 0;
    if (!evaded) {
      damage = computeDamageSubtotal(attack, attacker, defender);
      action.getDefender().applyDamage(damage);
    }

    return new CombatResult(damage, evaded);
  }

  private static boolean isFriendlyFire(BattleAction action) {
    Combatant attacker = action.getAttacker();
    Combatant defender = action.getDefender();
    return attacker.getArmy().equals(defender.getArmy());
  }

  static boolean doesAttackHit(Attack attack, Combatant attacker, Combatant defender) {

    int hitChance = attack.computeHitChance(attacker, defender);
    return Math.random() <= (hitChance/100f);
  }

  static int computeDamageSubtotal(Attack attack, Combatant attacker, Combatant defender) {
    int damage = attack.computeDamage(attacker, defender);
    int critChance = attack.computeCritChance(attacker, defender);
    if (Math.random() <= (critChance/100f)) {
      damage *= 2;
    }

    return damage;
  }

  private void validate(BattleAction action) throws InvalidBattleActionException {
    if (!isValid(action)) {
      throw new InvalidBattleActionException();
    }
  }
}
