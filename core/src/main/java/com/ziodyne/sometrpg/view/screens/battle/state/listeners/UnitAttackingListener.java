package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.artemis.Entity;
import com.artemis.World;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatResult;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.view.AnimationType;
import com.ziodyne.sometrpg.view.components.BattleUnit;
import com.ziodyne.sometrpg.view.components.TimedProcess;
import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

import java.util.Set;

/**
 * A handle for the state where an attack is being executed.
 */
public class UnitAttackingListener extends FlowListener<BattleContext> {
  private final BattleScreen screen;
  private final World world;

  public UnitAttackingListener(BattleScreen screen, World world) {

    super(BattleState.UNIT_ATTACKING);
    this.screen = screen;
    this.world = world;
  }

  @Override
  public void onLeave(BattleContext context) {

  }

  @Override
  public void onEnter(BattleContext context) {
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
      attackingBattleUnit.setAnimType(AnimationType.ATTACK);

      final BattleUnit defendingBattleUnit = defendingEntity.getComponent(BattleUnit.class);
      if (result.wasEvaded()) {
        defendingBattleUnit.setAnimType(AnimationType.DODGE);
      } else {
        defendingBattleUnit.setAnimType(AnimationType.BE_HIT);
      }

      Runnable resetAnimations = new Runnable() {
        @Override
        public void run() {
          attackingBattleUnit.setAnimType(AnimationType.IDLE);
          defendingBattleUnit.setAnimType(AnimationType.IDLE);
        }
      };

      Entity process = world.createEntity();
      process.addComponent(new TimedProcess(resetAnimations, 300));

      world.addEntity(process);

      // Attacking is instant for now
      context.safeTrigger(BattleEvent.UNIT_ATTACKED);
    }
  }

  private Entity getEntityForCombatant(Combatant combatant) {
    com.ziodyne.sometrpg.logic.models.Character character = combatant.getCharacter();
    return screen.getUnitEntity(character);
  }
}
