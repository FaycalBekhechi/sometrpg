package com.ziodyne.sometrpg.view.screens.battle.state;

import au.com.ds.ef.StatefulContext;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
import com.ziodyne.sometrpg.logic.models.battle.combat.Attack;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.input.BattleMapController;

public class BattleContext extends StatefulContext {
  public final Battle battle;

  public BattleContext(Battle battle) {
    this.battle = battle;
  }

  public Combatant selectedCombatant;
  public Combatant defendingCombatant;
  public Attack attackToExecute;
  public GridPoint2 selectedSquare;
  public BattleMapController mapController;
  public GridPoint2 movementDestination;
}
