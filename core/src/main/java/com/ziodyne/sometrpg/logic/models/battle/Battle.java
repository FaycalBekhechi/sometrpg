package com.ziodyne.sometrpg.logic.models.battle;

import com.ziodyne.sometrpg.logic.models.battle.combat.Attack;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

import java.util.Set;

public interface Battle {
  public Army getCurrentTurnArmy();
  public Set<Combatant> getPlayerUnits();
  public Set<Combatant> getEnemyUnits();
  public Set<Combatant> getNeutralUnits();
  public void moveCombatant(Combatant combatant, Tile destination);
  public void executeAttack(Combatant attacker, Attack attack, Combatant defender);

  public boolean isWon();
  public boolean isLost();
}
