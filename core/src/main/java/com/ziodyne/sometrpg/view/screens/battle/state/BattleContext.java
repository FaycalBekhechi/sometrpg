package com.ziodyne.sometrpg.view.screens.battle.state;

import au.com.ds.ef.StatefulContext;
import com.google.common.base.Optional;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
import com.ziodyne.sometrpg.logic.models.battle.SomeTRPGBattle;
import com.ziodyne.sometrpg.logic.models.battle.combat.Attack;
import com.ziodyne.sometrpg.logic.models.battle.combat.BattleAction;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.SomeTRPG;
import com.ziodyne.sometrpg.view.input.BattleMapController;
import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;

public class BattleContext extends StatefulContext {
  public final SomeTRPGBattle battle;
  public final BattleScreen battleView;

  public BattleContext(SomeTRPGBattle battle, BattleScreen battleView) {
    this.battle = battle;
    this.battleView = battleView;
  }

  public Boolean won;
  public Combatant selectedCombatant;
  public Combatant defendingCombatant;
  public Attack attackToExecute;
  public GridPoint2 selectedSquare;
  public BattleMapController mapController;
  public GridPoint2 movementDestination;

  public Optional<BattleAction> getAction() {
    BattleAction action = null;
    if (attackToExecute != null &&
        selectedCombatant != null &&
        defendingCombatant != null) {
      action = new BattleAction(selectedCombatant, defendingCombatant, attackToExecute);
    }

    return Optional.fromNullable(action);
  }
}
