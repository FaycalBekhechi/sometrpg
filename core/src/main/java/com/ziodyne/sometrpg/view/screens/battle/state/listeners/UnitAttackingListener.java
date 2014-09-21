package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.ziodyne.sometrpg.logic.models.battle.combat.BattleResult;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatResult;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.logic.models.battle.combat.CounterAttack;
import com.ziodyne.sometrpg.logic.models.battle.combat.Encounter;
import com.ziodyne.sometrpg.logic.models.battle.combat.EncounterResult;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.util.Logged;
import com.ziodyne.sometrpg.view.AnimationType;
import com.ziodyne.sometrpg.view.components.TimedProcess;
import com.ziodyne.sometrpg.view.entities.RenderedCombatant;
import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

import java.util.Optional;
import java.util.Set;

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

      Encounter encounter = context.battle.startCombat(attacker, context.attackToExecute, defender);

      RenderedCombatant attackingEntity = getEntityForCombatant(attacker);
      RenderedCombatant defendingEntity = getEntityForCombatant(defender);

      AnimationType attackAnimType = getAttackAnimation(attacker, defender);
      attackingEntity.setAnimationType(attackAnimType);

      if (encounter.defenderWillDodge()) {
        AnimationType dodgeType = getDodgeAnimation(attacker, defender);
        defendingEntity.setAnimationType(dodgeType);
      } else {
        defendingEntity.setAnimationType(AnimationType.BE_HIT);
      }

      Runnable resetAnimations = () -> {
        EncounterResult encounterResult = encounter.execute();

        Optional<Encounter> counterOptional = encounterResult.getCounter();
        if (counterOptional.isPresent()) {
          Encounter counterEncounter = counterOptional.get();
          if (counterEncounter.defenderWillDodge()) {
            AnimationType dodgeType = getDodgeAnimation(defender, attacker);
            attackingEntity.setAnimationType(dodgeType);
          } else {
            attackingEntity.setAnimationType(AnimationType.BE_HIT);
          }
          Entity counterAnimReset = new Entity();
          counterAnimReset.add(new TimedProcess(() -> {
            counterEncounter.execute();
            attackingEntity.setAnimationType(AnimationType.IDLE);
            defendingEntity.setAnimationType(AnimationType.IDLE);
            context.safeTrigger(BattleEvent.UNIT_ATTACKED);
          }, 1200));
          engine.addEntity(counterAnimReset);

        } else {
          attackingEntity.setAnimationType(AnimationType.IDLE);
          defendingEntity.setAnimationType(AnimationType.IDLE);
          context.safeTrigger(BattleEvent.UNIT_ATTACKED);
        }
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
