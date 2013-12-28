package com.ziodyne.sometrpg.logic.models.battle;

import com.badlogic.gdx.math.GridPoint2;
import com.ziodyne.sometrpg.logic.models.battle.combat.Attack;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

import java.util.Set;

public interface Battle {
  public void moveCombatant(Combatant combatant, Tile destination);
  public void executeAttack(Combatant attacker, Attack attack, Combatant defender);
  public Set<Tile> getMovableTiles(Combatant combatant);
  public Army getCurrentTurnArmy();
  public int getTurnNumber();
  public boolean isWon();
  public boolean isLost();
  public Set<Combatant> getPlayerUnits();
  public Set<Combatant> getEnemyUnits();
  public Set<Combatant> getNeutralUnits();
  public Tile getTile(GridPoint2 point);
}
