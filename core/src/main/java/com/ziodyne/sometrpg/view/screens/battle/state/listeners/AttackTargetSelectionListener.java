package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.badlogic.gdx.Gdx;
import com.google.common.base.Optional;
import com.ziodyne.sometrpg.logic.models.battle.combat.Attack;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.WeaponAttack;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.input.GridSelectionController;
import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

import java.util.Set;

/**
 * Handler for the state where a user is selecting a target for their attack.
 */
public class AttackTargetSelectionListener extends FlowListener<BattleContext> {
  private final BattleScreen screen;
  private GridSelectionController gridSelectionController;

  public AttackTargetSelectionListener(BattleScreen battleScreen) {
    super(BattleState.SELECTING_ATTACK_TARGET);
    this.screen = battleScreen;
  }

  @Override
  public void onLeave(BattleContext context) {
    screen.hideAttackRange();
    context.mapController.enable();
    Gdx.input.setInputProcessor(null);
  }

  @Override
  public void onEnter(final BattleContext context) {
    context.mapController.disable();
    Attack attack = new WeaponAttack();
    context.attackToExecute = attack;

    Set<GridPoint2> attackSquares = screen.showAttackRange(context.selectedCombatant, attack);

    gridSelectionController = new GridSelectionController(screen.getCamera(), attackSquares, new GridSelectionController.SelectionHandler() {
      @Override
      public void handleSelection(GridPoint2 selectedPoint) {
        Optional<Combatant> defender = screen.getCombatant(selectedPoint);
        if (defender.isPresent()) {
          context.defendingCombatant = defender.get();
          context.safeTrigger(BattleEvent.TARGET_SELECTED);
        }
      }

      @Override
      public void handleCancelation() {
        context.safeTrigger(BattleEvent.ATTACK_CANCEL);
      }
    });
    Gdx.input.setInputProcessor(gridSelectionController);
  }
}
