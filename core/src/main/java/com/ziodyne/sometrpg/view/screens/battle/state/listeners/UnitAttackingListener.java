package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import java.util.Optional;
import java.util.Set;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.ziodyne.sometrpg.logic.models.battle.combat.BattleResult;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatResult;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.util.Logged;
import com.ziodyne.sometrpg.view.AnimationType;
import com.ziodyne.sometrpg.view.components.BattleUnit;
import com.ziodyne.sometrpg.view.components.TimedProcess;
import com.ziodyne.sometrpg.view.entities.RenderedCombatant;
import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

/**
 * A handle for the state where an attack is being executed.
 */
public class UnitAttackingListener extends FlowListener<BattleContext> implements Logged {
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
      BattleResult result = context.battle.executeAttack(attacker, context.attackToExecute, defender);
      CombatResult initialAttack = result.getInitalAttack();

      RenderedCombatant attackingEntity = getEntityForCombatant(attacker);
      RenderedCombatant defendingEntity = getEntityForCombatant(defender);

      AnimationType attackAnimType = getAttackAnimation(attacker, defender);
      attackingEntity.setAnimationType(attackAnimType);

      if (initialAttack.wasEvaded()) {
        AnimationType dodgeType = getDodgeAnimation(attacker, defender);
        defendingEntity.setAnimationType(dodgeType);
      } else {
        defendingEntity.setAnimationType(AnimationType.BE_HIT);
      }

      Runnable resetAnimations = () -> {
        attackingEntity.setAnimationType(AnimationType.IDLE);
        defendingEntity.setAnimationType(AnimationType.IDLE);
        context.safeTrigger(BattleEvent.UNIT_ATTACKED);
      };

      Entity process = new Entity();
      process.add(new TimedProcess(resetAnimations, 1200));

      engine.addEntity(process);
    }
  }

  private AnimationType getDodgeAnimation(Combatant attacker, Combatant defender) {
    switch (getAttackAnimation(attacker, defender)) {
      case ATTACK_WEST:
        return AnimationType.DODGE_EAST;
      case ATTACK_EAST:
        return AnimationType.DODGE_WEST;
      case ATTACK_SOUTH:
        return AnimationType.DODGE_NORTH;
      case ATTACK_NORTH:
        return AnimationType.DODGE_SOUTH;
    }

    throw new IllegalArgumentException("Cannoy resolve dodge animation direction between " +
      screen.getCombatantPosition(attacker) + " and " + screen.getCombatantPosition(defender));
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

  private RenderedCombatant getEntityForCombatant(Combatant combatant) {
    return screen.getRenderedCombatant(combatant);
  }
}
