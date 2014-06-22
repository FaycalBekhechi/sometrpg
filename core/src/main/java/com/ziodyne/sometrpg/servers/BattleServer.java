package com.ziodyne.sometrpg.servers;

import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.models.battle.combat.Attack;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

public interface BattleServer {

  public void moveCombatant(Combatant combatant, Tile destination);

  public void attack(Combatant attacker, Attack attack, Combatant defender);
}
