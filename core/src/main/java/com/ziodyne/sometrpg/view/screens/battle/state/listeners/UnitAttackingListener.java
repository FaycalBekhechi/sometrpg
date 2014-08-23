package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import java.util.Set;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatResult;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.AnimationType;
import com.ziodyne.sometrpg.view.components.BattleUnit;
import com.ziodyne.sometrpg.view.components.TimedProcess;
import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

/**
 * A handle for the state where an attack is being executed.
 */
public class UnitAttackingListener extends FlowListener<BattleContext> {
  private final BattleScreen screen;
  private final Engine engine;

  public UnitAttackingListener(BattleScreen screen, Engine engine) {

    super(BattleState.UNIT_ATTACKING);
    this.screen = screen;
    this.engine = engine;
  }

  @Override
  public void onLeave(BattleContext context) {

  }

  @Override
  public void onEnter(final BattleContext context) {
    Combatant selectedCombatant = context.selectedCombatant;
    Set<CombatantAction> allowedActions = context.battle.getAvailableActions(selectedCombatant);
    if (allowedActions.isEmpty()) {
      context.safeTrigger(BattleEvent.ACTIONS_EXHAUSTED);
    } else {
      Combatant attacker = context.selectedCombatant;
      Combatant defender = context.defendingCombatant;
      CombatResult result = context.battle.executeAttack(attacker, context.attackToExecute, defender);

      Entity attackingEntity = getEntityForCombatant(attacker);
      Entity defendingEntity = getEntityForCombatant(defender);

      final BattleUnit attackingBattleUnit = attackingEntity.getComponent(BattleUnit.class);
      AnimationType attackAnimType = getAttackAnimation(attacker, defender);
      attackingBattleUnit.setAnimType(attackAnimType);

      final BattleUnit defendingBattleUnit = defendingEntity.getComponent(BattleUnit.class);
      if (result.wasEvaded()) {
        defendingBattleUnit.setAnimType(AnimationType.DODGE);
      } else {
        defendingBattleUnit.setAnimType(AnimationType.BE_HIT);
      }

      Runnable resetAnimations = () -> {
        attackingBattleUnit.setAnimType(AnimationType.IDLE);
        defendingBattleUnit.setAnimType(AnimationType.IDLE);
        context.safeTrigger(BattleEvent.UNIT_ATTACKED);
      };

      Entity process = new Entity();
      process.add(new TimedProcess(resetAnimations, 1300));

      engine.addEntity(process);
    }
  }

  private AnimationType getAttackAnimation(Combatant attacker, Combatant defender) {

    GridPoint2 attackerPos = screen.getCombatantPosition(attacker);
    GridPoint2 defenderPos = screen.getCombatantPosition(defender);

    if (attackerPos.x > defenderPos.x) {
      return AnimationType.ATTACK_WEST;
    }

    if (attackerPos.y > defenderPos.y) {
      return AnimationType.ATTACK_SOUTH;
    }

    if (attackerPos.x < defenderPos.x) {
      return AnimationType.ATTACK_EAST;
    }

    if (attackerPos.y < defenderPos.y) {
      return AnimationType.ATTACK_NORTH;
    }

    throw new IllegalArgumentException("Cannot resolve attack animation direction between " +
      attackerPos + " and " + defenderPos);
  }

  private Entity getEntityForCombatant(Combatant combatant) {
    com.ziodyne.sometrpg.logic.models.Character character = combatant.getCharacter();
    return screen.getUnitEntity(character);
  }
}
