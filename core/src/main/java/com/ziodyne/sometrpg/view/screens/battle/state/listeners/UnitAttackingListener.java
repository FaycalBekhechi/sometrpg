package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.google.common.eventbus.EventBus;
import com.ziodyne.sometrpg.events.UnitHit;
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
import com.ziodyne.sometrpg.view.tween.RenderedCombatantAccessor;

import java.util.Optional;
import java.util.Set;

/**
 * A handle for the state where an attack is being executed.
 */
public class UnitAttackingListener extends FlowListener<BattleContext> implements Logged {
  private final TweenManager tweenManager;
  private final BattleScreen screen;
  private final Engine engine;
  private final EventBus eventBus;

  public UnitAttackingListener(TweenManager tweenManager, BattleScreen screen, Engine engine, EventBus eventBus) {

    super(BattleState.UNIT_ATTACKING);
    this.tweenManager = tweenManager;
    this.screen = screen;
    this.engine = engine;
    this.eventBus = eventBus;
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
        AnimationType combatIdleType = getCombatIdleAnim(attacker, defender);
        defendingEntity.setAnimationType(combatIdleType);
        doHitFlash(defendingEntity);
        eventBus.post(new UnitHit());
      }

      Runnable resetAnimations = () -> {
        EncounterResult encounterResult = encounter.execute();

        Optional<Encounter> counterOptional = encounterResult.getCounter();
        if (counterOptional.isPresent()) {
          Encounter counterEncounter = counterOptional.get();
          AnimationType counterattackAnimType = getAttackAnimation(defender, attacker);
          defendingEntity.setAnimationType(counterattackAnimType);

          if (counterEncounter.defenderWillDodge()) {
            AnimationType dodgeType = getDodgeAnimation(defender, attacker);
            attackingEntity.setAnimationType(dodgeType);
          } else {
            AnimationType combatIdleType = getCombatIdleAnim(defender, attacker);
            attackingEntity.setAnimationType(combatIdleType);
            doHitFlash(attackingEntity);
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

  private void doHitFlash(RenderedCombatant combatant) {
    Timeline.createSequence()
            .push(Tween.to(combatant, RenderedCombatantAccessor.DAMAGE_TINT, 0.2f)
                    .ease(TweenEquations.easeOutCubic)
                    .target(0.9f))
            .push(Tween.to(combatant, RenderedCombatantAccessor.DAMAGE_TINT, 0.3f)
                    .ease(TweenEquations.easeOutCubic)
                    .target(0f))
            .start(tweenManager);
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

    throw new IllegalArgumentException("Cannot resolve dodge animation direction between " +
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

  private AnimationType getCombatIdleAnim(Combatant attacker, Combatant defender) {
    switch (getAttackAnimation(attacker, defender)) {
      case ATTACK_WEST:
        return AnimationType.COMBAT_IDLE_EAST;
      case ATTACK_EAST:
        return AnimationType.COMBAT_IDLE_WEST;
      case ATTACK_SOUTH:
        return AnimationType.COMBAT_IDLE_NORTH;
      case ATTACK_NORTH:
        return AnimationType.COMBAT_IDLE_SOUTH;
    }

    throw new IllegalArgumentException("Cannoy resolve combat idle direction between " +
            screen.getCombatantPosition(attacker) + " and " + screen.getCombatantPosition(defender));
  }

  private RenderedCombatant getEntityForCombatant(Combatant combatant) {
    return screen.getRenderedCombatant(combatant);
  }
}
