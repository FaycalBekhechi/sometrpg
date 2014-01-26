package com.ziodyne.sometrpg.view.screens.battle.state;

import au.com.ds.ef.StatefulContext;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.input.BattleMapController;

public class BattleContext extends StatefulContext {
  public Combatant selectedCombatant;
  public GridPoint2 selectedSquare;
  public BattleMapController mapController;
}
